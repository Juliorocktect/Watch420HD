package com.watch.HD.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String title;
    private String authorId;
    private String description;
    private int likes;
    private int views;
    private String videoUrl;
    private String thumbnailUrl;
    private String profilePicture;
    private long videoDuration;
    private LocalDateTime uploadDate;
    private ArrayList<String> videoTags;
    //default constructor
    private String videoType;
    //TODO: https://stackoverflow.com/questions/29984612/how-can-i-get-video-length-in-java
    public Video(){}
    public Video(String title, String authorId, String description,String profilePicture,String videoType)
    {
        this.title = title;
        this.authorId = authorId;
        this.description = description;
        views = 0;
        likes = 0;
        videoUrl = "";
        thumbnailUrl = "";
        uploadDate = LocalDateTime.now();
        this.profilePicture = profilePicture;
        videoTags = new ArrayList<>();
        this.videoType = videoType;
    }
    public void view(){views++;}
    public void like(){likes++;}
}
