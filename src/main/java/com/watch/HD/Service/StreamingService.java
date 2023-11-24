package com.watch.HD.Service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class StreamingService {
    private final VideoService videoService;
    private static final String FORMAT = "classpath:video/%s.mp4";

    private final ResourceLoader resourceLoader;

    public Mono<Resource> getVideo(String videoId){
        String title = videoService.getTitle(videoId);
        String path = "file:///srv/http/" + videoId + "/" + "%s.mp4";
        return Mono.fromSupplier(() -> resourceLoader.getResource(String.format(path,title)));
    }


}
