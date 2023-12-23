package com.watch.HD.Model;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
@Entity
public class UserData {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @ElementCollection
    @MapKeyColumn(name="videoId")
    @Column(name="watchTime")
    @CollectionTable(name="watch", joinColumns=@JoinColumn(name="watch"))
    private Map<String,Integer> watchTime;
    private int age;
    private ArrayList<String> topCategories;

    public UserData(int age){
        this.age = age;
        watchTime = new HashMap<>();
        topCategories = new ArrayList<>();
    }
    public UserData(){
        this.age = 0;
        watchTime = new HashMap<>();
        topCategories = new ArrayList<>();
    }
    public void addWatchTime(String videoId,int watchTime)
    {
        this.watchTime.put(videoId,watchTime);
    }
    public void addTopCategory(String category)
    {
        topCategories.add(category);
    }

}

