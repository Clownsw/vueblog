package cn.smilex.vueblog.before.handler;

import cn.smilex.vueblog.before.controller.*;
import cn.smilex.vueblog.common.handler.GlobalErrorHandler;
import com.linecorp.armeria.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/10/21:37
 * @since 1.0
 */
@Component
public class StartListener {

    private GlobalErrorHandler globalErrorHandler;
    private BlogController blogController;
    private SortController sortController;
    private TagController tagController;
    private FriendController friendController;
    private SystemController systemController;
    private OtherController otherController;
    private SearchController searchController;

    @Autowired
    public void setGlobalErrorHandler(GlobalErrorHandler globalErrorHandler) {
        this.globalErrorHandler = globalErrorHandler;
    }

    @Autowired
    public void setBlogController(BlogController blogController) {
        this.blogController = blogController;
    }

    @Autowired
    public void setSortController(SortController sortController) {
        this.sortController = sortController;
    }

    @Autowired
    public void setTagController(TagController tagController) {
        this.tagController = tagController;
    }

    @Autowired
    public void setFriendController(FriendController friendController) {
        this.friendController = friendController;
    }

    @Autowired
    public void setSystemController(SystemController systemController) {
        this.systemController = systemController;
    }

    @Autowired
    public void setOtherController(OtherController otherController) {
        this.otherController = otherController;
    }

    @Autowired
    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void start() {
        Server server = Server.builder()
                .http(8888)
                .errorHandler(globalErrorHandler)
                .annotatedService(blogController)
                .annotatedService(sortController)
                .annotatedService(tagController)
                .annotatedService(friendController)
                .annotatedService(systemController)
                .annotatedService(otherController)
                .annotatedService(searchController)
                .build();

        server.start()
                .join();
    }
}