package com.watch.HD.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
    @ManyToOne(cascade = CascadeType.ALL)
    private Content thumbnailData;
    @ManyToOne(cascade = CascadeType.ALL)
    private Content videoData;
    private LocalDateTime uploadDate;
    //default constructor
    public Video(){}
    public Video(String title, String authorId, String description,String profilePicture)
    {
        this.title = title;
        this.authorId = authorId;
        this.description = description;
        views = 0;
        likes = 0;
        videoUrl = "";
        thumbnailUrl = "";
        videoData = new Content(title);
        thumbnailData = new Content(title);
        uploadDate = LocalDateTime.now();
        this.profilePicture = profilePicture;
    }
    public void view(){views++;}
    public void  like(){likes++;}
}
