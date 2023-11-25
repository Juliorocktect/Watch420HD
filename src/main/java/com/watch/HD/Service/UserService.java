package com.watch.HD.Service;

import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Repository.UserRepo;
import com.watch.HD.Repository.VideoRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    @Autowired
    private VideoRepo videoRepo;
    public HttpStatus createUser(String userName, String passWd, String pictureUrl, String bannerUrl){
        User user = new User(userName,passWd,pictureUrl,bannerUrl);
        userRepo.save(user);
        if(userRepo.findById(user.getId()).isPresent()){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }
    public HttpStatus addToUploaded(String userId,String videoId){
        Optional<User> userById = getUserById(userId);
        if(userById.isPresent()){
            userById.get().addToUploaded(videoId);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }
    public boolean checkIfUserExists(String authorId){
        return userRepo.findById(authorId).isPresent();
    }
    public Optional<User> getUserById(String userId){
        return userRepo.findById(userId);
    }
    public HttpStatus like(String id){
        List<User> list = userRepo.findAll().stream().filter(p -> p.getId().equals(id)).toList();
        if (!list.isEmpty()){
            User user = list.get(0);
            user.addLiked("0981293");
            userRepo.save(user);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }
    public ResponseEntity<List<Video>> getVideosByUser(String userId) {
         Optional<User> byId = userRepo.findById(userId);
        if (byId.isPresent()){
            List<Video> videoList = new ArrayList<>();
            for (String c: byId.get().getVideosUploaded()){
                Optional<Video> videoById = videoRepo.findById(c);
                if(videoById.isPresent()){
                    videoList.add(videoById.get());
                }
            }
            return ResponseEntity.ok(videoList);
        }
        return ResponseEntity.badRequest().build();
    }
}
