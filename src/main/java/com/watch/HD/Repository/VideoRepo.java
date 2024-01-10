package com.watch.HD.Repository;

import com.watch.HD.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepo extends JpaRepository<Video,String> {
    @Query( value = "SELECT * FROM videos ORDER BY likes DESC limit 10;", nativeQuery = true)
    public List<Video> getMostLikes();
    /*
    @Query(value = "select row_number() over(),id,title,video_tags from videos",nativeQuery = true)
    public List<Video> getVideosForRecommandation();
    */
}

