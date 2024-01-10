package com.watch.HD.Repository;

import com.watch.HD.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<User,String> {
    Optional<User> findByUserName(String userName);
    /*
    @Query(value = "select  id,liked from _user",nativeQuery = true)
    public List<User> getUserForRecommendation();
    */
}
