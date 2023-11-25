package com.watch.HD.Controller;

import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin("localhost")
@Component
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    @PostMapping("/createUser")
    public ResponseEntity<HttpStatus> createUser(@RequestParam String userName,@RequestParam String passwd,@RequestParam String pictureUrl,@RequestParam String banner){
        return ResponseEntity.ok(service.createUser(userName,passwd,pictureUrl,banner));
    }
    @PostMapping("/like")
    public HttpStatus like(@RequestParam String videoId){
        return service.like(videoId);
    }

    @GetMapping("/getUser")
    public ResponseEntity<Optional<User>> getUserPerId(@RequestParam String userId)
    {
        return ResponseEntity.ok(service.getUserById(userId));
    }
/*
    public ResponseEntity<List<Video>> getvideosByUser(String userId)
    {
        return service.getVideosByUser(userId);
    }
    */

    public ResponseEntity<HttpStatus> getTwentyMostTrendingUsers(){return null;}

    //TODO: Notifications ProfileTab

}
