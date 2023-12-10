package com.watch.HD.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Service
public class FileUploadService {
    private final String PATH = "/srv/http/";

    private final String USER_PATH = "/srv/http/users/";

    private final String VIDEO_PATH = "/srv/http/videos/";

    public boolean uploadVideo(MultipartFile file,String videoId,String title){
        try{
            if(!file.isEmpty() && videoId != null) {
                createDirectoryForId(videoId);
                file.transferTo(new File(VIDEO_PATH + videoId + "/" +file.getOriginalFilename()));
                if(title != null)
                {
                    renameFile(file.getOriginalFilename(),title,videoId,file.getContentType());
                }
                return true;
            }
            return false;
        } catch (IOException e){
            System.out.println("Fehler beim Erstellen der Datei");
        }
        return false;
    }
    private String getVideoUrl(){return "";}
    public boolean uploadThumbnail(MultipartFile file,String videoId,String title) {
        try {
            if (!file.isEmpty() && videoId != null){
                file.transferTo(new File(VIDEO_PATH + videoId + "/" + file.getOriginalFilename()));
                renameFileImage(file.getOriginalFilename(),videoId,videoId,file.getContentType());
                return true;
            }
            return false;
        } catch(IOException e){
            System.out.println("Fehler beim Thumbnail upload" + e.getMessage());
            return false;
        }
    }
    public boolean uploadProfilePicture(MultipartFile file,String userId){
        try{
            if(!file.isEmpty() && userId != null)
            {
                File directory = new File(USER_PATH + userId + "/");
                if(!directory.exists()){
                    createDirectoryForUserId(userId);
                }
                file.transferTo(new File(USER_PATH + userId + "/" + file.getOriginalFilename()));
                renameFileImage(USER_PATH,file.getOriginalFilename(),"profilePicture",userId,file.getContentType());
                return true;
            }
            return false;
        } catch (IOException e){
            System.out.println("Fehler bei einer IO Operation" + e.getMessage());
            return false;
        }
    }
    public boolean uploadProfileBanner(MultipartFile file,String userId){
        try{
            if(!file.isEmpty() && userId != null)
            {
                File directory = new File(USER_PATH + userId + "/");
                if(!directory.exists()){
                    createDirectoryForUserId(userId);
                }
                file.transferTo(new File(USER_PATH + userId + "/" + file.getOriginalFilename()));
                renameFileImage(USER_PATH,file.getOriginalFilename(),"bannerPicture",userId,file.getContentType());
                return true;
            }
            return false;
        } catch (IOException e){
            System.out.println("Fehler bei einer IO Operation" + e.getMessage());
            return false;
        }
    }

    private void createDirectoryForUserId(String userId) {
        new File(PATH + "/users").mkdir();
        new File(PATH + "/users/" + userId).mkdir();
    }

    public void createDirectoryForId(String id){
        new File(PATH + "/videos").mkdir();
        new File(PATH + "/videos/" + id).mkdir();
    }
    private boolean renameFile(String oldname,String newName,String videoId,String type){
        File file = new File(VIDEO_PATH + videoId +"/" + oldname);
        File rename = new File(VIDEO_PATH + videoId +"/" + newName + convertVideoFileTypeToString(type));
        return file.renameTo(rename);

    }
    //TODO: rename method
    private String renameFileImage(String oldname,String newName,String videoId,String type) {
        File file = new File(VIDEO_PATH + videoId + "/" + oldname);
        File rename = new File(VIDEO_PATH + videoId + "/" + newName + converteFileTypeToString(type));
        boolean status = file.renameTo(rename);
        if (status) {
            return "success"; // was hab ich mir dabei gedacht?
        }
        return "failed";
    }
    private String renameFileImage(String path,String oldname,String newName,String videoId,String type) {
        File file = new File(path + videoId + "/" + oldname);
        File rename = new File(path + videoId + "/" + newName + converteFileTypeToString(type));
        boolean status = file.renameTo(rename);
        if (status) {
            return "success"; // was hab ich mir dabei gedacht?
        }
        return "failed";
    }

    private String convertVideoFileTypeToString(String fileType){
        String convertedString = "";
        switch (fileType){
            case "video/mp4":
                convertedString = ".mp4";
                break;
            case "video/mov":
                convertedString = ".mov";
                break;
            case "video/mkv":
                convertedString = ".mkv";
        }
        return convertedString;
    }
    private String converteFileTypeToString(String fileType){
        String convertedString = "";
        switch(fileType){
            case "image/jpg":
                convertedString = ".jpg";
                break;
            case "image/png":
                convertedString = ".png";
                break;
            case "image/jpeg":
                convertedString = ".jpeg";// ist btw das gleiche wie jpg
            default:
                System.out.println("Falscher DateiTyp");
        }
        return convertedString;
    }
    public String getBannerPath(String userId,String type){
        return "http://192.168.178.95/users/" + userId + "/" + "bannerPicture" + converteFileTypeToString(type);
    }
    public String getPicturePath(String userId,String type){
        return "http://192.169.178.95/users/" + userId + "/" + "profilePicture" + converteFileTypeToString(type);
    }
    //TODO: implement
    public boolean deleteVideo(String videoId){

        return false;
    }
    //TODO: implement
    public boolean deleteUserData(String userId){
        return false;
    }


}
