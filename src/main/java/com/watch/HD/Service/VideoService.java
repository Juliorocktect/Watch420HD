package com.watch.HD.Service;

import com.watch.HD.Model.Content;
import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Repository.VideoRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
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
                    && (fileUploadService.uploadThumbnail(thumbnail, video.getId(),video.getTitle()))
            ) {
                    video.setVideoData( new Content(video.getTitle(),videoFile.getContentType(),getVideoUrl(video.getId()),videoFile.getSize()));
                    video.setThumbnailData(new Content(video.getTitle(),thumbnail.getContentType(),getThumbnailOutUrl(video.getId(),thumbnail.getContentType()),thumbnail.getSize()));
                    video.setThumbnailUrl(getThumbnailOutUrl(video.getId(),thumbnail.getContentType()));
                    video.setVideoUrl(getVideoUrl(video.getId()));
                    videoRepo.save(video);
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
    public ResponseEntity<Video> getVideoById(String videoId){
        Optional<Video> byId = videoRepo.findById(videoId);
        if (byId.isPresent()){
            return ResponseEntity.ok(byId.get());
        }
        return (ResponseEntity<Video>) ResponseEntity.badRequest();
    }
    public Video getVideoWithId(String videoId){
        Optional<Video> byId = videoRepo.findById(videoId);
        if (byId.isPresent()){
            return byId.get();
        }
        return null;
    }
    private String getThumbnailOutUrl(String videoId,String type){
        String typeFinished = "";
        switch(type){
            case "image/jpg":
                typeFinished = ".jpg";
                break;
            case "image/png":
                typeFinished = ".png";
                break;
            case "image/jpeg":
                typeFinished = ".jpeg";
            default:
                System.out.println("Falscher DateiTyp");
        }
        return "http://localhost/" + videoId + "/" + videoId + typeFinished;
    }
    public ResponseEntity<List<Video>> getTenVideos(){
        List<Video> list = videoRepo.findAll().stream().limit(10).toList();
        if (list.size() < 10){
            return (ResponseEntity<List<Video>>) ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(list);
    }
    public ResponseEntity<List<Video>> getVideosByLimit(int limit){
        List<Video> list = videoRepo.findAll().stream().limit(limit).toList();
        if (list.size() < limit){
            return (ResponseEntity<List<Video>>) ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(list);
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
            return "http://localhost:9090/video/getVideo?videoId=" + videoId;
        }
        return null;
    }
    public boolean checkIfVideoExists(String videoId)
    {
        return videoRepo.findById(videoId).isPresent();
    }
    public HttpStatus view(String videoId)
    {
        Optional<Video> byId = videoRepo.findById(videoId);
        if(byId.isPresent()){
            byId.get().view();
            videoRepo.save(byId.get());
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }
    public HttpStatus like(){return null;}
}
