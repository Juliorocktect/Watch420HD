package com.watch.HD.Controller;

import com.watch.HD.Model.User;
import com.watch.HD.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin("localhost")
@Component
public class UserController {
    private final UserService service;
    @PostMapping("/createUser")
    public ResponseEntity<HttpStatus> createUser(@RequestParam String userName,@RequestParam String passwd,@RequestParam String pictureUrl,@RequestParam String banner){
        return ResponseEntity.ok(service.createUser(userName,passwd,pictureUrl,banner));
    }
    @PostMapping("/like")
    public ResponseEntity<HttpStatus> like(@RequestParam String id){
        return ResponseEntity.ok(service.like(id));
    }

    public ResponseEntity<HttpStatus> getUserPerId(){}

    public ResponseEntity<HttpStatus> getvideosByUser(String userId){}

    public ResponseEntity<HttpStatus> getTwentyMostTrendingUsers(){}

    //TODO: Notifications ProfileTab

}
