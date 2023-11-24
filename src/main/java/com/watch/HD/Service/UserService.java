package com.watch.HD.Service;

import com.watch.HD.Model.User;
import com.watch.HD.Repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    public HttpStatus createUser(String userName, String passWd, String pictureUrl, String bannerUrl){
        User user = new User(userName,passWd,pictureUrl,bannerUrl);
        userRepo.save(user);
        if(userRepo.findById(user.getId()).isPresent()){
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

}
