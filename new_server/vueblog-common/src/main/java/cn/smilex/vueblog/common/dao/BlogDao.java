package cn.smilex.vueblog.common.dao;

import cn.smilex.vueblog.common.entity.Blog;
import cn.smilex.vueblog.common.entity.SelectBlogInfo;
import cn.smilex.vueblog.common.entity.SelectShowBlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:41
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mapper
public interface BlogDao extends BaseMapper<Blog> {

    /**
     * 分页查询博文
     *
     * @param currentPage 当前页
     * @return 博文集合
     */
    List<SelectShowBlog> selectBlogPage(
            @Param("pageSize") Long pageSize,
            @Param("currentPage") Long currentPage
    );

    /**
     * 根据指定标签分页查询所有文章
     *
     * @param currentPage 当前页
     * @return 博文集合
     */
    List<SelectShowBlog> selectBlogPageByTagId(
            @Param("pageSize") Long pageSize,
            @Param("currentPage") Long currentPage,
            @Param("tagId") Long tagId
    );

    /**
     * 根据ID获取文章信息
     *
     * @param id id
     * @return 文章信息
     */
    SelectBlogInfo selectBlogById(@Param("id") Long id);

    /**
     * 根据标签ID查询文章个数
     *
     * @param tagId 标签ID
     * @return 文章个数
     */
    long selectBlogCountByTagId(@Param("tagId") Long tagId);
}
