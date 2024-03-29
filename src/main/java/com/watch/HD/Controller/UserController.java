package com.watch.HD.Controller;

import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Response.UserResponse;
import com.watch.HD.Service.RecommendationService;
import com.watch.HD.Service.UserService;
import jakarta.annotation.Nullable;
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
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000","http://192.168.178.95:3000"}) //TODO: fix for localhost
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    private final RecommendationService recommendationService;
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
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestParam String userId,@RequestParam String session){
        return service.delete(userId,session);
    }
    @PostMapping("/addToSaved")
    public ResponseEntity addToSaved(@RequestParam String session,@RequestParam String videoId){
        return service.addVideoToSaved(session,videoId);
    }
    @DeleteMapping("/removeFromSaved")
    public ResponseEntity removeFromSaved(@RequestParam String session,@RequestParam String videoId){
        return service.removeFromSaved(session,videoId);
    }
    @PostMapping("/watch")
    public ResponseEntity watch(@RequestParam String session,@RequestParam String videoId){
        return service.addToHistory(session,videoId);
    }
    @GetMapping("/saved")
    public ResponseEntity<List<Video>> getAllSaved(@RequestParam String session){
        return service.getSavedVideos(session);
    }
    @PostMapping("/like")
    public ResponseEntity like(@RequestParam String session,@RequestParam String videoId){
        return service.like(session,videoId);
    }
    @GetMapping("/isValid")
    public boolean isCookieValid(@RequestParam String session){
        return service.isSessionValid(session);
    }
    @GetMapping("/history")
    public ResponseEntity<List<Video>> getHistory(@RequestParam String session){
        return service.getHistory(session);
    }
    @GetMapping("/liked")
    public ResponseEntity<List<Video>> getLiked(@RequestParam String session){
        return service.getLiked(session);
    }
    @GetMapping("/upload")
    public ResponseEntity<List<Video>> getUploaded(@RequestParam String session){
        return service.getUploaded(session);
    }
    @GetMapping("/recommend")
    public List<Video> recommend(@Nullable @RequestParam String videoId,@Nullable @RequestParam String session){
        return recommendationService.recommend("6daabef7-d1fb-4ba9-92bb-6fa323462bb3",session);
    }
    @GetMapping("/getBySession")
    public ResponseEntity<UserResponse> getBySession(@RequestParam String session){
        return service.getBySession(session);
    }
}
