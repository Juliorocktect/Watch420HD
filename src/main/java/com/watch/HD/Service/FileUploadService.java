package com.watch.HD.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLOutput;
//TODO: make uploadProfile Pic & Banner method
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
    private String renameFile(String oldname,String newName,String videoId,String type){
        File file = new File(PATH + videoId +"/" + oldname);
        File rename = new File(PATH + videoId +"/" + newName + convertVideoFileTypeToString(type));
        boolean status = file.renameTo(rename);
        if (status){
            return "success";
        }
        return "failed";
    }
    private String renameFileThumnail(String oldname,String newName,String videoId,String type){
        File file = new File(PATH + videoId +"/" + oldname);
        File rename = new File(PATH + videoId +"/" + newName + converteFileTypeToString(type));
        boolean status = file.renameTo(rename);
        if (status){
            return "success";
        }
        return "failed";
    }
    //TODO: FileTypeConverterForVideoTypes

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
                convertedString = ".jpeg";
            default:
                System.out.println("Falscher DateiTyp");
        }
        return convertedString;
    }

}
