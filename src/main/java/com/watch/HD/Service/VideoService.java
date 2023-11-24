package com.watch.HD.Service;

import com.watch.HD.Model.Content;
import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Repository.VideoRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//TODO: implement Methods
@Service
@AllArgsConstructor
public class VideoService {
    private final VideoRepo videoRepo;
    private final UserService userService;
    @Autowired
    private final FileUploadService fileUploadService;


    public HttpStatus createNewVideo(String title, String authorId, String description, MultipartFile videoFile,MultipartFile thumbnail) {
        if (userService.checkIfUserExists(authorId)){
            Video video = new Video(title,authorId,description);
            videoRepo.save(video);
            if((fileUploadService.uploadVideo(videoFile,video.getId(),video.getTitle())
                    && (videoRepo.findById(video.getId()).isPresent()))
                    && (fileUploadService.uploadThumbnail(thumbnail, video.getId()))
            ) {
                    video.setVideoData(new Content(video.getTitle(),videoFile.getContentType(),getVideoUrl(video.getId()),videoFile.getSize()));
                    video.setThumbnailData(new Content(video.getTitle(),thumbnail.getContentType(),getThumbnailUrl(video.getId()),thumbnail.getSize()));
                    return HttpStatus.OK;
            }
        }
        return HttpStatus.BAD_REQUEST;
    }
    private String  getThumbnailUrl(String id){
        Optional<Video> videoPerId = videoRepo.findById(id);
        if (videoPerId.isPresent()){
            return "classpath:videos/" + id +"/" + videoPerId.get().getThumbnailData().getName();
        }
        return "bad";
    }
    public List<Video> getTenVideos(){
        return videoRepo.findAll().stream().limit(10).toList();
    }
    public Optional<User> getAuthor(String videoId)
    {
        Optional<Video> byId = videoRepo.findById(videoId);
        if(byId.isPresent()){
            String userId =  byId.get().getAuthorId();
            return userService.getUserById(userId);
        }
        return Optional.empty();
    }
    public String getTitle(String videoId){
        Optional<Video> videoById = videoRepo.findById(videoId);
        if(videoById.isPresent())
        {
            return videoById.get().getTitle();
        }
        return null;
    }
    public String getVideoUrl(String videoId){
        if(checkIfVideoExists(videoId)){
            return "http://localhost:9090/video/" + getTitle(videoId);
        }
        return null;
    }
    public boolean checkIfVideoExists(String videoId)
    {
        return videoRepo.findById(videoId).isPresent();
    }
    public HttpStatus view(){return null;}
    public HttpStatus like(){return null;}
}
