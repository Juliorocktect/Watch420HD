package com.watch.HD.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@CrossOrigin("http://localhost:3000")
public class VideoController {
    //TODO: implement methods
    public ResponseEntity<HttpStatus> getVideoPerId(){}

    public ResponseEntity<HttpStatus> getTenVideos(){}

    public ResponseEntity<HttpStatus> getTenMostTrending(){}
    public ResponseEntity<HttpStatus> getAuthorIdPerVideoId(String videoId){}
    public ResponseEntity<HttpStatus> like(){}
    public ResponseEntity<HttpStatus> view(){}

}
