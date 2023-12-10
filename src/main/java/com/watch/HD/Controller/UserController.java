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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://loclhost:3000","http://192.168.178.95:3000"})
@Controller
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

    @PostMapping("/updatePaths")
    public void updatePath(){
        service.updatePath();
    }
    @PostMapping("/encrypt")
    public String encrypt(@RequestParam String text) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return service.encrypt(text);
    }
    @GetMapping("/decrypt")
    public String decrypt(@RequestParam String data) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return service.decrypt(data);
    }
    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestParam String userName, @RequestParam String password){
        return service.auth(userName,password);
    }
}
