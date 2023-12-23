package com.watch.HD.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Map;

@Component
@Table(name = "_user")
@Data
@Entity
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String userName;
    private String passwd;
    private String pictureUrl;
    private String bannerUrl;
    private int subs;
    private ArrayList<String> liked;
    private ArrayList<String> videosUploaded;
    private ArrayList<String> videosWatched;
    private ArrayList<String> videosSaved;
    @OneToOne(cascade=CascadeType.PERSIST)
    private UserData userData;
    //default Constructor
    public User(){
    }

    public User(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
        this.pictureUrl = "";
        this.bannerUrl = "";
        this.liked = new ArrayList<>();
        this.videosUploaded = new ArrayList<>();
        videosWatched = new ArrayList<>();
        videosSaved = new ArrayList<>();
        userData = new UserData();
        userData.addTopCategory("Sui");
        userData.setAge(21);
        userData.addWatchTime("8971231",12);
    }
    public void addLiked(String like){
        liked.add(like);
    }
    public void addToUploaded(String videoId){
        videosUploaded.add(videoId);
    }
    public void addToWatched(String videoId){
        videosWatched.add(videoId);
    }
    public void removeLike(String videoId){
        liked.remove(videoId);
    }
    public void addToSaved(String id){
        videosSaved.add(id);
    }
    public void removeFromSavd(String id){
        videosSaved.remove(id);
    }
}

