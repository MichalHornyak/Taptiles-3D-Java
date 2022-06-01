package sk.tuke.gamestudio.game.taptiles.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.game.taptiles.entity.Rating;
import sk.tuke.gamestudio.game.taptiles.service.RatingService;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {
    @Autowired
    private RatingService ratingservice;

    @PostMapping
    public void addRating(@RequestBody Rating rating) {
        ratingservice.setRating(rating);
    }

    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game,@PathVariable String player) {
        return ratingservice.getRating(game, player);
    }

    @GetMapping("/average/{game}")
    public int getAverageRating(@PathVariable String game) {
        return ratingservice.getAverageRating(game);
    }

}
