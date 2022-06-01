package sk.tuke.gamestudio.game.taptiles.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
@Entity
public class Score implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;
    private String player;
    private int points;
    private Date playedOn;

    public Score(String player, String game, int points, Date playedOn) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.playedOn = playedOn;
    }

    public Score() {

    }

    public Score(int ident) {
        this.ident = ident;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public int getPoints() {
        return points;
    }

    public String getGame() {
        return game;
    }

    public String getPlayer() {
        return player;
    }


    public void setGame(String game) {
        this.game = game;
    }

    public void setPlayedOn(Date playedDate) {
        this.playedOn = playedDate;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", points=" + points +
                ", playedAt=" + playedOn +
                '}';
    }
}
