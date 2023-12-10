package com.watch.HD.Repository;

import com.watch.HD.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session,String> {
    Optional<Session> findByUserId(String userId);
}
