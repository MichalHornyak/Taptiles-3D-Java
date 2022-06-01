package sk.tuke.gamestudio.game.taptiles.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;
    private String player;
    private int rating;
    private Date ratedOn;

    public Rating(String player, String game, int rating, Date playedDate) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedOn = playedDate;
    }

    public Rating() {

    }

    public Rating(int ident) {
        this.ident = ident;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public String getGame() {
        return game;
    }

    public int getRating() {
        return rating;
    }

    public String getPlayer() {
        return player;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setRatedOn(Date playedDate) {
        this.ratedOn = playedDate;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rating=" + rating +
                ", playedDate=" + ratedOn +
                '}';
    }
}
