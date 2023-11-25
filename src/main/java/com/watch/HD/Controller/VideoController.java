package com.watch.HD.Controller;

import com.watch.HD.Model.Video;
import com.watch.HD.Service.StreamingService;
import com.watch.HD.Service.VideoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@Component
@CrossOrigin("http://localhost:3000")
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;
    private final StreamingService streamingService;

    public VideoController(VideoService videoService, StreamingService streamingService) {
        this.videoService = videoService;
        this.streamingService = streamingService;
    }

    @PostMapping("/upload")
    public HttpStatus upload(
            @RequestParam String title,
            @RequestParam String authorId,
            @RequestParam String description,
            @RequestParam("videoFile") MultipartFile videoFile,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile
    ){
        return videoService.createNewVideo(title,authorId,description,videoFile,thumbnailFile);
    }
    @GetMapping("/getVideoById")
    public ResponseEntity<Video> getVideoPerId(@RequestParam String videoId)
    {
        return videoService.getVideoById(videoId);
    }
    @GetMapping("/getTenVideos")
    public ResponseEntity<List<Video>> getVideos()
    {
        return videoService.getTenVideos();
    }
    @GetMapping("/getVideosByLimit")
    public ResponseEntity<List<Video>> getVideos(@RequestParam int limit)
    {
        return videoService.getVideosByLimit(limit);
    }
    @GetMapping(value = "/getVideo",produces = "video/mp4")
    public ResponseEntity<Mono<Resource>> getVideo(@RequestParam String videoId){
        return streamingService.getVideo(videoId);
    }
    @GetMapping("/thumbnail")
    public String thumbnail(@RequestParam String videoId)
    {
        return "";
    }
    public ResponseEntity<HttpStatus> getTenMostTrending(){return null;}
    public ResponseEntity<HttpStatus> getAuthorIdPerVideoId(String videoId){return null;}
    public ResponseEntity<HttpStatus> like(){return null;}
    @PostMapping("/view")
    public HttpStatus view(@RequestParam String videoId)
    {
        return videoService.view(videoId);
    }

}
