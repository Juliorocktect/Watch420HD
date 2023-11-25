package com.watch.HD.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity
public class Content {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String url;
    private String type;
    private long bytes;
    //TODO: https://stackoverflow.com/questions/29984612/how-can-i-get-video-length-in-java

    public Content(String name, String type,String url,long bytes) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.bytes = bytes;
    }
    public Content(String pName){
        name = pName;
        type = "";
        url = "";
        bytes = 0;
    }

    public Content() {

    }
}
