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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@Component
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    @PostMapping("/createUser")
    public ResponseEntity<HttpStatus> createUser(@RequestParam String userName, @RequestParam String passwd, @RequestParam("picture")MultipartFile picture, @RequestParam("banner") MultipartFile banner){
        return ResponseEntity.ok(service.createUser(userName,passwd,picture,banner));
    }
    @GetMapping("/getUser")
    public ResponseEntity<Optional<User>> getUserPerId(@RequestParam String userId)
    {
        return ResponseEntity.ok(service.getUserById(userId));
    }
    @GetMapping("/getVideosByUser")
    public ResponseEntity<List<Video>> getVideosByUser(String userId)
    {
        return service.getVideosByUser(userId);
    }

    @GetMapping("/getProfilePicture")
    public ResponseEntity<String> getProfilePicture(@RequestParam String userId){
        return service.getPictureById(userId);
    }
    @GetMapping("/getBanner")
    public ResponseEntity<String> getBanner(@RequestParam String userId){
        return service.getBannerById(userId);
    }
    public ResponseEntity<HttpStatus> getTwentyMostTrendingUsers(){return null;}

    //TODO: Notifications ProfileTab

}
