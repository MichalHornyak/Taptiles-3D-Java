package sk.tuke.gamestudio.game.taptiles.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.taptiles.entity.Rating;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RatingServiceJDBCTest {
//
//    private RatingService ratingService(){
//        return new RatingServiceJDBC();
//    }
//
//    @Test
//    void addRating() {
//        RatingService service = ratingService();
//        service.reset();
//        Date date = new Date();
//        service.setRating(new Rating("Miško","Taptiles",3,date));
//
//        List<Rating> ratings = service.getRating("Taptiles");
//
//        assertEquals("Miško", ratings.get(0).getPlayer());
//        assertEquals("Taptiles", ratings.get(0).getGame());
//        assertEquals(3, ratings.get(0).getRating());
//    }
//
//    @Test
//    void addInvalidRating() {
//        RatingService service = ratingService();
//        service.reset();
//        Date date = new Date();
//        service.setRating(new Rating("Miško","Taptiles",15,date));
//        service.setRating(new Rating("","Taptiles",5,date));
//        service.setRating(new Rating("miško","",5,date));
//
//        List<Rating> ratings = service.getRating("Taptiles");
//
//        assertEquals(0,ratings.size());
//    }
//
//    @Test
//    void getLastRatings() {
//        RatingService service = ratingService();
//        service.reset();
//        service.setRating(new Rating("Peter", "Taptiles", 3, new Date()));
//        for (int i = 0; i < 9; i++) {
//            service.setRating(new Rating("Miško", "Taptiles", 2, new Date()));
//        }
//        List<Rating> ratings = service.getRating("Taptiles");
//        assertEquals(10, ratings.size());
//        service.setRating(new Rating("Miško", "Taptiles", 4, new Date()));
//        ratings = service.getRating("Taptiles");
//        assertEquals(10, ratings.size());
//        assertEquals("Miško", ratings.get(ratings.size()-1).getPlayer());
//    }
//
//    @Test
//    void reset() {
//        RatingService service = ratingService();
//        service.reset();
//        assertEquals(0,service.getRating("Taptiles").size());
//    }
}