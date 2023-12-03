package cn.smilex.vueblog.before.controller

import cn.smilex.vueblog.common.annotation.CrossOrigin
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.service.BlogService
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.PathPrefix
import com.linecorp.armeria.server.annotation.ProducesJson
import org.springframework.stereotype.Component
import java.util.*

/**
 * TODO
 * @author smilex
 * @date 2023/12/3 17:26:46
 */
@PathPrefix("/tag")
@ProducesJson
@CrossOrigin
@Component
class TagController(private val blogService: BlogService) {

    /**
     * 分页查询指定标签下的所有文章
     *
     * @param currentPage 当前页
     * @param tagId       标签ID
     * @return 指定标签下的所有文章
     */
    @Get("/blogList")
    fun blogList(
        @Param("currentPage") currentPage: Optional<Long?>,
        @Param("tagId") tagId: Optional<Long?>
    ): Result<*> {
        return Result.success(blogService.selectBlogPageByTagId(currentPage.orElse(0L), tagId.orElse(0L)))
    }

}