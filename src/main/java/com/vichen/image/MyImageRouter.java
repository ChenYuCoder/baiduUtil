package com.vichen.image;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author chenyu
 * @Date: 2018/10/22 16:40
 */
@Configuration public class MyImageRouter {
  @Bean public RouterFunction<ServerResponse> imageRouter(ImageHandle imageHandle) {
    return RouterFunctions.route(RequestPredicates.POST("/image/imageToWord")
      .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), imageHandle::imageToWord);
  }
}
