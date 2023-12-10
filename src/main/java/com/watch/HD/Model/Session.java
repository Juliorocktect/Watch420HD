package com.watch.HD.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@Entity
@Getter
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String session;
    private String userId;
    private LocalDateTime expiryDate;
    public Session(){}

    public Session(String userId){
        this.userId = userId;
        this.expiryDate = LocalDateTime.now().plusDays(15);
    }
    public boolean isActive(LocalDateTime dateNow){
        return dateNow.isBefore(expiryDate);
    }

}
