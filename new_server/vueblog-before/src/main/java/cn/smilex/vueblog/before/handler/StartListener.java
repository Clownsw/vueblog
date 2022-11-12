package cn.smilex.vueblog.before.handler;

import cn.smilex.vueblog.before.controller.BlogController;
import cn.smilex.vueblog.before.controller.SortController;
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

    public void start() {
        Server server = Server.builder()
                .http(1111)
                .errorHandler(globalErrorHandler)
                .annotatedService(blogController)
                .annotatedService(sortController)
                .build();

        server.start()
                .join();
    }
}
