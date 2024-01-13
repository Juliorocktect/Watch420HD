package com.watch.HD.Controller;

import com.watch.HD.Model.Video;
import com.watch.HD.Service.SearchService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://192.168.178.95:3000"}) //TODO: fix for localhost
@Controller
@RequestMapping("/search")
public class SearchController {
    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }
    @GetMapping("/video")
    public List<Video> searchVideo(@RequestParam String query,@RequestParam @Nullable String filter){
        return service.searchVideos(query,filter);
    }
}
