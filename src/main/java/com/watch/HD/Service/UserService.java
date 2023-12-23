package com.watch.HD.Service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.watch.HD.Model.Session;
import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Repository.SessionRepo;
import com.watch.HD.Repository.UserRepo;
import com.watch.HD.Repository.VideoRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private FileUploadService uploadService;
    @Autowired
    private SessionService sessionService;
    private final String key = "16SpxPMv2DNyw1JpjpWwKg==";

    public HttpStatus createUser(String userName, String passWd, MultipartFile picuture, MultipartFile banner) {
        User user = new User(userName, passWd);
        userRepo.save(user);
        if (userRepo.findById(user.getId()).isPresent()
                && uploadService.uploadProfileBanner(banner, user.getId())
                && uploadService.uploadProfilePicture(picuture, user.getId())) {
            user.setBannerUrl(uploadService.getBannerPath(user.getId(), banner.getContentType()));
            user.setPictureUrl(uploadService.getPicturePath(user.getId(), picuture.getContentType()));
            userRepo.save(user);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus addToUploaded(String session,String userId, String videoId) {
        Optional<User> userById = getUserById(userId);
        if (userById.isPresent() && sessionService.isActive(session)) {
            userById.get().addToUploaded(videoId);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    public boolean checkIfUserExists(String authorId) {
        return userRepo.findById(authorId).isPresent();
    }

    public Optional<User> getUserById(String userId) {
        return userRepo.findById(userId);
    }

    public HttpStatus like(String id) {
        List<User> list = userRepo.findAll().stream().filter(p -> p.getId().equals(id)).toList();
        if (!list.isEmpty()) {
            User user = list.get(0);
            user.addLiked("0981293");
            userRepo.save(user);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    public ResponseEntity<List<Video>> getVideosByUser(String userId) {
        Optional<User> byId = userRepo.findById(userId);
        if (byId.isPresent()) {
            List<Video> videoList = new ArrayList<>();
            for (String c : byId.get().getVideosUploaded()) {
                Optional<Video> videoById = videoRepo.findById(c);
                if (videoById.isPresent()) {
                    videoList.add(videoById.get());
                }
            }
            return ResponseEntity.ok(videoList);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<String> getPictureById(String userId) {
        Optional<User> byId = userRepo.findById(userId);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get().getPictureUrl());
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<String> getBannerById(String userId) {
        Optional<User> byId = userRepo.findById(userId);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get().getBannerUrl());
        }
        return ResponseEntity.badRequest().build();
    }

    public void updatePath() {
        List<User> all = userRepo.findAll();
        for (User u : all) {
            String oldPath = u.getPictureUrl();
            u.setPictureUrl("http://192.168.178.95" + oldPath.substring(16));
        }
        userRepo.saveAll(all);
    }
    //TODO: fix auth section
    public ResponseEntity<String> auth(String userName,String password) {
        Optional<User> byUserName = userRepo.findByUserName(userName);
        if(byUserName.isPresent()){
            if (byUserName.get().getPasswd().equals(password)) {
                if (sessionService.isActiveByUser(byUserName.get().getId())){
                    return ResponseEntity.ok(sessionService.getSessionByUser(byUserName.get().getId()));
                }
                else{
                    sessionService.deleteSession(sessionService.getSessionByUser(byUserName.get().getId()));
                    Session newSession = new Session(byUserName.get().getId());
                    sessionService.save(newSession);
                    return ResponseEntity.ok(newSession.getSession());
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }
    //TODO: fix
    public String decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] encoded =  Base64.getDecoder().decode(key);
        SecretKey key = new SecretKeySpec(encoded,0,encoded.length,"AES");
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] bytes = cipher.doFinal(Base64.getEncoder().encode(encryptedText.getBytes()));
        return bytes.toString();
    }
    //TODO:make thread work
    public String encrypt(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = text.getBytes();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] encoded =  Base64.getDecoder().decode(key);
        SecretKey secretKey = new SecretKeySpec(encoded,0,encoded.length,"AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        return Base64.getDecoder().decode(cipher.doFinal(text.getBytes())).toString();
    }

    public ResponseEntity<HttpStatus> delete(String userId, String session) {
        if (checkIfUserExists(userId)){
            if(sessionService.isActive(session) && sessionService.belongsToUser(session,userId)){
                if (uploadService.deleteUserData(userId)) {
                    userRepo.delete(userRepo.findById(userId).get()); //present check isnt needed
                    sessionService.deleteSession(session);
                    return ResponseEntity.ok(HttpStatus.OK);
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
    public ResponseEntity<List<Video>> getSavedVideos(String session){
        if(sessionService.isActive(session)){
            String userId = sessionService.getUserBySession(session);
            Optional<User> user = userRepo.findById(userId);
            if(user.isPresent()){
                List<Video> videos = new ArrayList<>();
                for (String s : user.get().getVideosSaved()){
                    videos.add(videoRepo.findById(s).get());
                }
                return ResponseEntity.ok(videos);
            }
        }
        return ResponseEntity.status(403).build();
    }
    public ResponseEntity addVideoToSaved(String session, String videoId){
        String userId = sessionService.getUserBySession(session);
        if(!userId.equals("")){
            Optional<User> userById = userRepo.findById(userId);
            if (userById.isPresent()) {
                userById.get().addToSaved(videoId);
                userRepo.save(userById.get());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.badRequest().build();
    }
    public ResponseEntity removeFromSaved(String session,String videoId){
        String userId = sessionService.getUserBySession(session);
        if(!userId.equals("")){
            Optional<User> byId = userRepo.findById(userId);
            if (byId.isPresent()) {
                byId.get().removeFromSavd(videoId);
                userRepo.save(byId.get());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
