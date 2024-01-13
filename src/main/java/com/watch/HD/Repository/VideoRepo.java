package com.watch.HD.Repository;

import com.watch.HD.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface VideoRepo extends JpaRepository<Video,String> {
    @Query( value = "SELECT * FROM videos ORDER BY likes DESC limit 10;", nativeQuery = true)
    public List<Video> getMostLikes();
    @Query("SELECT v From Videos v" + " WHERE v.title LIKE CONCAT('%',:query,'%')" + "OR v.description LIKE CONCAT('%',:query,'%')" + "ORDER BY likes DESC LIMIT 15")
    public List<Video> searchVideo(@Param("query")String query);

    @Query("SELECT v From Videos v" + " WHERE v.title LIKE CONCAT('%',:query,'%')" + "OR v.description LIKE CONCAT('%',:query,'%')" + "ORDER BY uploadDate LIMIT 15")
    public List<Video> searchVideosByDate(@Param("query") String query);
}

