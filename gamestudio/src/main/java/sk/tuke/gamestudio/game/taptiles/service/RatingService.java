package sk.tuke.gamestudio.game.taptiles.service;

import sk.tuke.gamestudio.game.taptiles.entity.Rating;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    void reset() throws RatingException;
}

