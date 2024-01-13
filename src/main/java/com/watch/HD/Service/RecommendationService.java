package com.watch.HD.Service;

import com.watch.HD.Model.User;
import com.watch.HD.Model.Video;
import com.watch.HD.Repository.UserRepo;
import com.watch.HD.Repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private SessionService sessionService;

    public List<Video> recommend(String videoId,String session){
        if (session != null && sessionService.isActive(session)){
            Optional<User> user = userRepo.findById(sessionService.getUserBySession(session));
            if (user.isPresent()){
                User u = user.get();
                List<String> liked = u.getLiked();
                List<String> saved = u.getVideosSaved();
                List<String> topCategories = new ArrayList<>();
                List<String> recommendedVideos = new ArrayList<>();
                for (String s : liked){
                    Optional<Video> byId = videoRepo.findById(s);
                    if (byId.isPresent()){
                        topCategories.addAll(byId.get().getVideoTags());
                        recommendedVideos.addAll(recommendPerVideoId(s));
                    }
                }
                for (String s : saved){
                    Optional<Video> byId = videoRepo.findById(s);
                    if (byId.isPresent()){
                        topCategories.addAll(byId.get().getVideoTags());
                    }
                }
                List<Video> findRecommended = videoRepo.findAllById(recommendedVideos);
                List<Video> filteredByWatched = new ArrayList<>();
                for (Video v : findRecommended){
                    if (!u.hasWatched(v.getId())){
                        filteredByWatched.add(v);
                    }
                }
                //findRecommended = findRecommended.stream().filter(p -> p.containsOneTag(topCategories)).toList();
                List<Video> filteredByCategory = new ArrayList<>();
                for (Video v : filteredByWatched){
                    if (v.containsOneTag(topCategories)){
                        filteredByCategory.add(v);
                    }
                }
                return filteredByWatched;
            }
            return null;
        }
        else{
            List<Video> result = new ArrayList<>();
            List<String> resultStr = recommendPerVideoId(videoId);
            for (String s : resultStr){
                result.add(videoRepo.findById(s).get());
            }
            return result;
        }
    }



    private List<String> recommendPerVideoId(String videoId){
        List<User> userForRecommendation = userRepo.findAll();
        List<Video> videosForRecommandation = videoRepo.findAll();
        List<User> similarUsers = userForRecommendation.stream().filter(p->p.hasLiked(videoId)).toList();
        List<String> otherMoviesLiked  = new ArrayList<>();
        for (User u : similarUsers){
            otherMoviesLiked.addAll(u.getLiked());
        }
        otherMoviesLiked = otherMoviesLiked.stream().filter((p -> !p.contains(videoId))).toList();
        Map<String,Long> counts = otherMoviesLiked.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        Map<String,Double> countsByPercentage = new HashMap<>();
        for (String key: counts.keySet()){
            double percentage = (double) counts.get(key) / similarUsers.size();
            countsByPercentage.put(key,percentage);
        }
       List<String> results = new ArrayList<>();
        for (String key : countsByPercentage.keySet()){
            if (countsByPercentage.get(key) > 0.1){
             results.add(key);
            }
        }
        return results;
    }

}
