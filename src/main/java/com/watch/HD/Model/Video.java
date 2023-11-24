package com.watch.HD.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
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
    @ManyToOne
    private Content thumbnailData;
    @ManyToOne
    private Content videoData;
    private LocalDateTime uploadDate;

    public Video(String title,String authorId,String description)
    {
        this.title = title;
        this.authorId = authorId;
        this.description = description;
        views = 0;
        likes = 0;
        videoData = new Content(title);
        thumbnailData = new Content(title);
    }
    public void view(){views++;}
    public void  like(){likes++;}
}
