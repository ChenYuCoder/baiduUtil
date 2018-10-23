package com.vichen.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author chenyu
 * @Date: 2018/10/22 16:46
 */
@Component public class ImageHandle {

  @Autowired private final IImageService iImageService;

  public ImageHandle(IImageService iImageService) {
    this.iImageService = iImageService;
  }

  public Mono<ServerResponse> imageToWord(ServerRequest serverRequest) {
    Mono<ImageParams> imageParams = serverRequest.bodyToMono(ImageParams.class);
    return imageParams.flatMap(e -> Mono.just(iImageService.imageToWord(e.isHighPrecisionStr(),e.getImage(),e.getUrl())))
      .flatMap(result -> ServerResponse.ok().body(Mono.just(result), String.class))
      .switchIfEmpty(ServerResponse.ok()
        .body(Mono.just(""), String.class));

  }
}
