package com.watch.HD.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Table(name = "_user")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    public User(String userName, String passwd, String pictureUrl, String bannerUrl) {
        this.userName = userName;
        this.passwd = passwd;
        this.pictureUrl = pictureUrl;
        this.bannerUrl = bannerUrl;
        this.liked = new ArrayList<>();
    }
    public void addLiked(String like){
        liked.add(like);
    }
}
