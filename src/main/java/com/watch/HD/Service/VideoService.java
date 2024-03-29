package com.watch.HD.Service;

import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Repository.VideoRepo;
import com.watch.HD.Response.TrendsResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoService {
    private final VideoRepo videoRepo;
    private final UserService userService;
    @Autowired
    private final FileUploadService fileUploadService;
    @Autowired
    private SessionService sessionService;
    public ResponseEntity createNewVideo(String title, String authorId, String description, MultipartFile videoFile,MultipartFile thumbnail,String session) {
        if (sessionService.isActive(session)){
            if (userService.checkIfUserExists(authorId)){
                Video video = new Video(title,authorId,description,userService.getUserById(authorId).get().getPictureUrl(),videoFile.getContentType());
                videoRepo.save(video);
                if((fileUploadService.uploadVideo(videoFile,video.getId(),video.getTitle())
                        && (videoRepo.findById(video.getId()).isPresent()))
                        && (fileUploadService.uploadThumbnail(thumbnail, video.getId(),video.getTitle()))
                ) {
                    video.setThumbnailUrl(getThumbnailOutUrl(video.getId(),thumbnail.getContentType()));
                    video.setVideoUrl(getVideoUrl(video.getId()));
                    userService.addToUploaded(session,authorId,video.getId());
                    videoRepo.save(video); //TODO: delete video if problems occure
                    return ResponseEntity.ok().build();
                }
                videoRepo.delete(video);
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(401).build();
    }
    public ResponseEntity<Video> getVideoById(String videoId){
        Optional<Video> byId = videoRepo.findById(videoId);
        if (byId.isPresent()){
            return ResponseEntity.ok(byId.get());
        }
        return ResponseEntity.badRequest().build();
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
        return "http://192.168.178.95/videos/" + videoId + "/" + videoId + typeFinished;
    }
    private String convert(String type){
        String convertedString = "";
        switch(type) {
            case "image/jpg":
                convertedString = ".jpg";
                break;
            case "image/png":
                convertedString = ".png";
                break;
            case "image/jpeg":
                convertedString = ".jpeg";
            case "video/mp4":
                convertedString = ".mp4";
                break;
            case "video/mov":
                convertedString = ".mov";
                break;
            case "video/mkv":
                convertedString = ".mkv";
            default:
                convertedString = "false";
        }
        return convertedString;
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
        if (list.size() > limit){
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
            return "http://192.168.178.95:9090/video/getVideo?videoId=" + videoId;
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
    public HttpStatus like(String videoId,String session)
    {
        Optional<Video> videoById = videoRepo.findById(videoId);
        if (videoById.isPresent() && sessionService.isActive(session)){
            videoById.get().like();
            videoRepo.save(videoById.get());
            return HttpStatus.OK;
        } else if (!sessionService.isActive(session)) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //TODO: still needed
    public void updatePath(){
        List<Video> all = videoRepo.findAll();
        for (Video v : all){
            String oldPath = v.getVideoUrl();
            v.setVideoUrl("http://192.168.178.95" + oldPath.substring(16));
            v.setThumbnailUrl("http://192.168.178.95" + oldPath.substring(16));
        }
        videoRepo.saveAll(all);
    }

    public ResponseEntity<HttpStatus> delete(String videoId, String session) {
        if (sessionService.isActive(session)) {
            if (fileUploadService.deleteVideo(videoId)) {
                Optional<Video> byId = videoRepo.findById(videoId);
                if (byId.isPresent()){
                    videoRepo.delete(byId.get());
                    return ResponseEntity.ok(HttpStatus.OK);
                }
            }
            ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.badRequest().build();
    }
    public ResponseEntity<List<TrendsResponse>> getTenTrending(){
        List<Video> mostLikes = videoRepo.getMostLikes();
        if(!mostLikes.isEmpty()){
            List<TrendsResponse> response = new ArrayList<>();
            for (Video v: mostLikes){
                response.add(new TrendsResponse(v.getThumbnailUrl(),v.getId()));
            }
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
