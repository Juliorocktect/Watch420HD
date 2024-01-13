package com.watch.HD.Service;

import com.watch.HD.Model.Video;
import com.watch.HD.Repository.UserRepo;
import com.watch.HD.Repository.VideoRepo;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: implement Search Function
@Service
public class SearchService {
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private UserRepo userRepo;

    public List<Video> searchVideos(String query,String filter){
        if (filter == null){
            return videoRepo.searchVideo(query);
        }
        if (filter.equals("uploadDate")){
            return videoRepo.searchVideosByDate(query);
        }
        if (filter.equals("views")){
            return videoRepo.searchVideosByDate("");
        }
        return videoRepo.searchVideo(query);
    }
}