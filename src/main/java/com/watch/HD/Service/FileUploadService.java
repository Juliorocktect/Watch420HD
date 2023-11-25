package com.watch.HD.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLOutput;

@Service
public class FileUploadService {
    private final String PATH = "/srv/http/";

    public boolean uploadVideo(MultipartFile file,String videoId,String title){
        try{
            if(!file.isEmpty() && videoId != null) {
                createDirectoryForVideo(videoId);
                file.transferTo(new File(PATH +videoId + "/" +file.getOriginalFilename()));
                if(title != null)
                {
                    renameFile(file.getOriginalFilename(),title,videoId);
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
            //TODO: rename file to id
            if (!file.isEmpty() && videoId != null){
                file.transferTo(new File(PATH + videoId + "/" + file.getOriginalFilename()));
                renameFileThumnail(file.getOriginalFilename(),videoId,videoId,file.getContentType());
                return true;
            }
            return false;
        } catch(IOException e){
            System.out.println("Fehler beim Thumbnail upload" + e.getMessage());
            return false;
        }
    }
    public void createDirectoryForVideo(String videoId){
        new File(PATH + videoId).mkdir();
    }
    private String renameFile(String oldname,String newName,String videoId){
        File file = new File(PATH + videoId +"/" + oldname);
        File rename = new File(PATH + videoId +"/" + newName + ".mp4");
        boolean status = file.renameTo(rename);
        if (status){
            return "success";
        }
        return "failed";
    }
    private String renameFileThumnail(String oldname,String newName,String videoId,String type){
        File file = new File(PATH + videoId +"/" + oldname);
        File rename = new File(PATH + videoId +"/" + newName + type);
        boolean status = file.renameTo(rename);
        if (status){
            return "success";
        }
        return "failed";
    }

}
