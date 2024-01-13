package com.watch.HD.Repository;

import com.watch.HD.Model.User;
import com.watch.HD.Response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<User,String> {
    Optional<User> findByUserName(String userName);
}
