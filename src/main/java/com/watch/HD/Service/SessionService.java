package com.watch.HD.Service;

import com.watch.HD.Model.Session;
import com.watch.HD.Repository.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionService {
    @Autowired
    private SessionRepo sessionRepo;
    public boolean isActive(String sessionId){
        Optional<Session> session = sessionRepo.findById(sessionId);
        if(session.isPresent() && session.get().isActive(LocalDateTime.now())){
            return true;
        }
        else{ // else unecessary
            deleteSession(sessionId);
        }
        return false;
    }
    public boolean isActiveByUser(String userId){
        Optional<Session> session = sessionRepo.findByUserId(userId);
        return (session.isPresent() && session.get().isActive(LocalDateTime.now()));
        //delete old session
    }
    public void save(Session session){
        sessionRepo.save(session);
    }
    public String getSessionByUser(String userId){
        Optional<Session> session = sessionRepo.findByUserId(userId);
        if (session.isPresent()){
            return session.get().getSession();
        }
        return "";
    }
    public void deleteSession(String sessionId){
        Optional<Session> byId = sessionRepo.findById(sessionId);
        if (byId.isPresent()){
            sessionRepo.delete(byId.get());
        }
    }

    public boolean belongsToUser(String session, String userId) {
        Optional<Session> byUserId = sessionRepo.findByUserId(userId);
        if (byUserId.isPresent()) {
            return byUserId.get().getUserId().equals(userId) && byUserId.get().getSession().equals(session);
        }
        return false;
    }

    public String getUserBySession(String session) {
        if(isActive(session)){
            return sessionRepo.findById(session).get().getUserId();
        }
        return "";
    }
}
