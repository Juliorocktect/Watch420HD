package com.watch.HD.Service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

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
    public boolean uploadThumbnail(MultipartFile file,String videoId,String title) {
        try {
            if (!file.isEmpty() && videoId != null){
                file.transferTo(new File(VIDEO_PATH + videoId + "/" + file.getOriginalFilename()));
                renameFileImage(file.getOriginalFilename(),videoId,videoId,file.getContentType());
                imageCompress(VIDEO_PATH + videoId + "/" + videoId + converteFileTypeToString(file.getContentType()));
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
    private boolean renameFileImage(String oldname,String newName,String videoId,String type) {
        File file = new File(VIDEO_PATH + videoId + "/" + oldname);
        File rename = new File(VIDEO_PATH + videoId + "/" + newName + converteFileTypeToString(type));
        return file.renameTo(rename);
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
        }
        return convertedString;
    }
    public String getBannerPath(String userId,String type){
        return "http://192.168.178.95/users/" + userId + "/" + "bannerPicture" + converteFileTypeToString(type);
    }
    public String getPicturePath(String userId,String type){
        return "http://192.169.178.95/users/" + userId + "/" + "profilePicture" + converteFileTypeToString(type);
    }
    public boolean deleteVideo(String videoId){
        try{
            FileUtils.deleteDirectory(new File(VIDEO_PATH + videoId));
            return true;
        }catch (IOException e){
            System.out.println("Error Deleting Video: " + e.getMessage());
            return false;
        }
    }
    public boolean deleteUserData(String userId){
        try{
            FileUtils.deleteDirectory(new File(USER_PATH + userId));
            return true;
        }catch (IOException e){
            System.out.println("Error Deleting User: " + e.getMessage());
            return false;
        }
    }
    //TODO: open new Thread
    private void imageCompress(String path){
        try {
            File input = new File(path);
            File out = new File(path);
            Image img = ImageIO.read(input);
            BufferedImage imageTmp = resizeImage(img,600,400);
            String imageType = input.getName().substring(input.getName().lastIndexOf('.')+1);
            ImageIO.write(imageTmp,imageType,out);
        } catch (IOException e){
            System.out.println("Error resizing image");
        }
    }
    private BufferedImage resizeImage(final Image image,final int width,final int height){
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }


}
