package com.watch.HD.Service;

import com.watch.HD.Model.Video;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class StreamingService {
    private final VideoService videoService;
    private static final String FORMAT = "classpath:video/%s.mp4";

    private final ResourceLoader resourceLoader;

    public ResponseEntity<Mono<Resource>> getVideo(String videoId){
        Video video = videoService.getVideoById(videoId).getBody();
        if(video.getTitle() == null){
            return ResponseEntity.badRequest().build();
        }
        String path = "file:///srv/http/" + videoId + "/" + "%s" + video.getVideoData().getType();
        return ResponseEntity.ok(Mono.fromSupplier(() -> resourceLoader.getResource(String.format(path,video.getTitle()))));
    }


}
