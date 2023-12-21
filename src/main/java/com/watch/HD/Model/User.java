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
    private ArrayList<String> liked;
    private ArrayList<String> videosUploaded;
    private ArrayList<String> videosWatched;
    private ArrayList<String> videosSaved;
    //TODO: refactor in new class
    //private Map<String,Integer> watchTime;
    //subs
    //gender
    //age
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
    }
    public void addLiked(String like){
        liked.add(like);
    }
    public void addToUploaded(String videoId){
        videosUploaded.add(videoId);
    }
}
