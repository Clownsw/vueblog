package cn.smilex.vueblog.common.util;

import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.blog.SearchBlog;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.common.Triplet;
import cn.smilex.vueblog.common.entity.common.Tuple;
import cn.smilex.vueblog.common.exception.VueBlogException;
import cn.smilex.vueblog.common.function.TryRunExceptionHandler;
import cn.smilex.vueblog.common.function.TryRunTask;
import cn.smilex.vueblog.common.function.TryRunTaskNotResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.linecorp.armeria.common.*;
import com.linecorp.armeria.internal.server.annotation.AnnotatedService;
import com.linecorp.armeria.server.HttpService;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author smilex
 * @date 2022/9/29/18:09
 * @since 1.0
 */
@SuppressWarnings("all")
@Slf4j
public final class CommonUtil {

    /**
     * 创建任务并提交到公共线程池
     *
     * @param runnable 任务
     * @return 结果
     */
    public static Future<?> createTask(Runnable runnable) {
        return CommonConfig.COMMON_THREAD_POOL.submit(runnable);
    }

    public static boolean isInForArray(Class<?> clazz, Class<?>[] array) {
        for (Class<?> aClass : array) {
            if (aClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据每页数量查询起始查询位置
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 起始查询位置
     */
    public static long calcLimit(long currentPage, long pageSize) {
        return (currentPage - 1L) * pageSize;
    }

    /**
     * 根据总数量和每页数量计算总页数
     *
     * @param totalCount 总数量
     * @param pageSize   每页数量
     * @return 总页数
     */
    public static long calcPageCount(long totalCount, long pageSize) {
        long pageCount = 0;

        if (totalCount > 0) {
            if (totalCount <= pageSize) {
                pageCount = 1;
            } else if (totalCount % pageSize == 0) {
                pageCount = totalCount / pageSize;
            } else {
                pageCount = (totalCount / pageSize) + 1;
            }
        }

        return pageCount;
    }

    /**
     * object集合检查存在null
     *
     * @param objectList object集合
     * @return 检查存在null
     */
    public static boolean objectListCheckHaveNull(Object... objectList) {
        for (Object object : objectList) {
            if (object == null) {
                return true;
            }

            if (object instanceof String) {
                if (StringUtils.isBlank((String) object)) {
                    return true;
                }
            }

            if (object instanceof Optional) {
                if (!((Optional<?>) object).isPresent()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * try catch 运行 task, 如果发生异常 则调用exceptionHandler进行异常处理
     *
     * @param task                   任务
     * @param tryRunExceptionHandler 异常处理
     */
    public static void tryRun(TryRunTaskNotResult tryRunTaskNotResult, TryRunExceptionHandler exceptionHandler) {
        try {
            tryRunTaskNotResult.run();
        } catch (Throwable e) {
            exceptionHandler.handler(e);
        }
    }

    /**
     * try catch 运行 task, 如果发生异常 则调用exceptionHandler进行异常处理
     *
     * @param task                   任务
     * @param tryRunExceptionHandler 异常处理
     * @param <T>                    unknown type1
     * @param <E>                    unknown type2
     * @return 结果
     */
    @SuppressWarnings("all")
    public static <T> Result<?> tryRun(TryRunTask<T> task, Optional<TryRunExceptionHandler> exceptionHandler) {
        try {
            return Result.success(task.run());
        } catch (Throwable e) {
            if (exceptionHandler.isPresent()) {
                exceptionHandler.get()
                        .handler(e);
            }
            return Result.error();
        }
    }

    /**
     * try catch 运行 task, 如果发生异常 则调用exceptionHandler进行异常处理并调用 defaultValue返回默认值
     *
     * @param task                   任务
     * @param tryRunExceptionHandler 异常处理
     * @param <T>                    unknown type1
     * @param <E>                    unknown type2
     * @return 结果
     */
    public static <T, E> T tryRun(TryRunTask<T> task, Supplier<T> defaultValue, TryRunExceptionHandler exceptionHandler) {
        try {
            return task.run();
        } catch (Throwable e) {
            exceptionHandler.handler(e);
        }
        return defaultValue.get();
    }

    /**
     * 检查请求对象中的token有效性
     *
     * @param request 请求对象
     * @return 有效性
     */
    public static Map<String, Object> checkTokenAndGetData(HttpRequest request) {
        return checkTokenAndGetData(
                request.headers()
                        .get("authorization")
        );
    }

    /**
     * 检查token有效性
     *
     * @param token token
     * @return 有效性
     */
    public static Map<String, Object> checkTokenAndGetData(String token) {
        if (StringUtils.isBlank(token)) {
            throw new VueBlogException(ResultCode.ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN);
        }

        Tuple<Boolean, Map<String, Object>> signResult = JwtUtil.signJWTToken(token);

        if (!signResult.getLeft()) {
            throw new VueBlogException(ResultCode.ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN);
        }

        return signResult.getRight();
    }

    /**
     * 计算a集合和b集合中被删除、新添加、未变动集合
     *
     * @param a   a集合
     * @param b   b集合
     * @param <T> unknown type
     * @return 集合
     */
    public static <T> Triplet<List<T>, List<T>, List<T>> getDelAndAddAndDefaultList(List<T> a, List<T> b) {
        List<T> _del = new ArrayList<>();
        List<T> _add = new ArrayList<>();
        List<T> _default = new ArrayList<>();

        // del
        for (T v : a) {
            if (!b.contains(v)) {
                _del.add(v);
            }
        }

        // add
        for (T v : b) {
            if (!a.contains(v)) {
                _add.add(v);
            }
        }

        // default
        for (T v : a) {
            if (!_del.contains(v) && !_add.contains(v) && !_default.contains(v)) {
                _default.add(v);
            }
        }

        for (T v : b) {
            if (!_del.contains(v) && !_add.contains(v) && !_default.contains(v)) {
                _default.add(v);
            }
        }

        return new Triplet<>(_del, _add, _default);
    }

    /**
     * 生成一个允许跨域的响应对象
     *
     * @return 响应对象
     */
    public static HttpResponse createCrossOriginHttpResponse() {
        return HttpResponse.of(
                ResponseHeaders.of(HttpStatus.OK)
                        .toBuilder()
                        .add("Access-Control-Allow-Origin", "*")
                        .add("Access-Control-Allow-Credentials", "true")
                        .add("Access-Control-Allow-Methods", "*")
                        .add("Access-Control-Allow-Headers", "*")
                        .add("Access-Control-Expose-Headers", "*")
                        .build()
        );
    }

    @FunctionalInterface
    public interface CollectionToStrMap<T> {
        String map(T t);
    }

    /**
     * 集合转字符串
     *
     * @param collection 集合
     * @param handler    处理方法
     * @param split      分隔符
     * @param <T>        unknown type
     * @return 字符串
     */
    public static <T> String collectionToStr(Collection<T> collection, CollectionToStrMap<T> handler, String split) {
        StringBuilder sb = new StringBuilder();
        for (T t : collection) {
            sb.append(handler.map(t))
                    .append(split);
        }

        return collection.size() == 0 ? CommonConfig.EMPTY_STRING : sb.substring(0, sb.length() - 1);
    }

    /**
     * 集合转字符串(最后一个不添加分割)
     *
     * @param collection 集合
     * @param handler    处理方法
     * @param split      分隔符
     * @param <T>        unknown type
     * @return 字符串
     */
    public static <T> String collectionToStrNotLast(Collection<T> collection, CollectionToStrMap<T> handler, String split) {
        final int len = collection.size();
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (T t : collection) {
            sb.append(handler.map(t));

            if (i + 1 == len) {
                continue;
            }

            sb.append(split);

            i++;
        }

        return collection.size() == 0 ? CommonConfig.EMPTY_STRING : sb.substring(0, sb.length() - 1);
    }

    /**
     * JsonNode转集合
     *
     * @param collection 集合
     * @param jsonNode   JsonNode
     * @param handler    处理方法
     * @param <R>        unknown type
     * @return 集合
     */
    public static <R> Collection<R> jsonNodeToCollection(Collection<R> collection, JsonNode jsonNode, Function<JsonNode, R> handler) {
        for (JsonNode node : jsonNode) {
            collection.add(handler.apply(node));
        }
        return collection;
    }

    /**
     * obj是否在集合中存在
     *
     * @param collection 集合
     * @param handler    处理方法
     * @param <T>        unknown type
     * @return 是否存在
     */
    public static <T> boolean objInCollectionExists(Collection<T> collection, Predicate<T> handler) {
        for (T t : collection) {
            if (handler.test(t)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 搜索客户端添加或更新
     *
     * @param blogIndex  博文索引
     * @param document   内容
     * @param searchBlog 实体
     * @throws MeilisearchException    unknown exception
     * @throws JsonProcessingException unknown exception
     */
    public static void searchClientAddOrUpdate(Index blogIndex, String document, SearchBlog searchBlog) throws MeilisearchException, JsonProcessingException {
        JsonNode root = CommonConfig.OBJECT_MAPPER.readTree(blogIndex.getDocuments());

        if (root.size() == 0) {
            blogIndex.addDocuments(document, CommonConfig.SEARCH_DOCUMENT_PRIMARY_KEY);
            return;
        }

        List<SearchBlog> searchBlogList = jsonNodeToCollection(
                new ArrayList<>(),
                root,
                node -> {
                    try {
                        return CommonConfig.OBJECT_MAPPER.readValue(
                                node.toString(),
                                new TypeReference<SearchBlog>() {
                                }
                        );
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                }
        ).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (
                objInCollectionExists(
                        searchBlogList,
                        searchBlogDocument -> searchBlog.getId().equals(searchBlogDocument.getId())
                )
        ) {
            blogIndex.updateDocuments(document, CommonConfig.SEARCH_DOCUMENT_PRIMARY_KEY);
            return;
        }

        blogIndex.addDocuments(document, CommonConfig.SEARCH_DOCUMENT_PRIMARY_KEY);
    }

    /**
     * 空任务 Lambda Interface
     */
    @FunctionalInterface
    public interface EmptyTask {
        void handler() throws Exception;
    }

    /**
     * 捕获异常转运行时异常, 避免直接抛出异常
     *
     * @param emptyTask 任务
     */
    public static void exceptionToRunTimeException(EmptyTask emptyTask) {
        try {
            emptyTask.handler();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断一个Optional内的值是否为空
     *
     * @param value 值
     * @param <T>   unknwon type
     * @return Optional值是否为空
     */
    @SuppressWarnings("all")
    public static <T> boolean isEmpty(Optional<T> value) {
        return !value.isPresent();
    }

    /**
     * 判断Optional数组内是否包含空数据
     *
     * @param values Optional 数组
     * @return 结果
     */
    public static boolean isEmpty(Optional<Object>... values) {
        for (Optional<Object> value : values) {
            if (!value.isPresent()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 备份数据库
     *
     * @param userName 数据库用户名
     * @param passWord 数据库密码
     * @param dataBase 数据库名称
     * @return 数据库sql
     * @throws IOException unknown exception
     */
    public static String dumpSql(String userName, String passWord, String dataBase) throws IOException {
        Process exec = Runtime.getRuntime()
                .exec(
                        new String[]{
                                "mysqldump",
                                String.format("-u%s", userName),
                                String.format("-p%s", passWord),
                                dataBase
                        }
                );

        return IOUtils.toString(exec.getInputStream(), StandardCharsets.UTF_8);
    }

    /**
     * 构建一个JSON响应对象根据ResultCode
     *
     * @param resultCode resultCode
     * @return 响应对象
     */
    public static HttpResponse buildJsonHttpResponseByResultCode(ResultCode resultCode) {
        return HttpResponse.ofJson(
                HttpStatus.OK,
                MediaType.JSON_UTF_8,
                Result.fromResultCode(resultCode)
        );
    }

    /**
     * 获取HttpService的Method对象
     *
     * @param httpService HttpService
     * @return Method对象
     * @throws NoSuchFieldException   unknown exception
     * @throws IllegalAccessException unknown exception
     */
    public static Method getHttpServiceMethodField(HttpService httpService) throws NoSuchFieldException, IllegalAccessException {
        if (httpService instanceof AnnotatedService) {
            AnnotatedService annotatedService = (AnnotatedService) httpService;
            Field methodField = AnnotatedService.class.getDeclaredField("method");
            methodField.setAccessible(true);
            return (Method) methodField.get(annotatedService);
        }

        return null;
    }

    /**
     * 解析QueryString到Map
     *
     * @param queryString queryString
     * @return Map
     */
    public static Map<String, Object> parseQueryStringToMap(Optional<String> queryString) {
        try {
            return CommonConfig.OBJECT_MAPPER.readValue(
                    queryString.get(),
                    new TypeReference<HashMap<String, Object>>() {
                    }
            );
        } catch (Exception ignore) {
        }

        return (Map<String, Object>) CommonConfig.EMPTY_MAP;
    }

    /**
     * 遍历JsonNode
     *
     * @param jsonNode JsonNode
     * @param map      map
     * @param <T>
     * @return
     */
    public static <T> List<T> mapJsonNode(JsonNode jsonNode, Function<JsonNode, T> mapHandler) {
        List<T> valueList = new ArrayList<>(jsonNode.size());

        for (JsonNode node : jsonNode) {
            T value;
            if ((value = mapHandler.apply(node)) != null) {
                valueList.add(value);
            }
        }

        return valueList;
    }
}
