package com.watch.HD.Repository;

import com.watch.HD.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepo extends JpaRepository<Video,String> {
}

