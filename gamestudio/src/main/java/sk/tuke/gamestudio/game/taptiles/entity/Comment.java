package sk.tuke.gamestudio.game.taptiles.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;
    private String player;
    private String comment;
    private Date commentedOn;

    public Comment(String player, String game, String comment, Date commentedOn){
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public Comment() {

    }

    public Comment(int ident) {
        this.ident = ident;
    }

    public String getPlayer() {
        return player;
    }

    public String getGame() {
        return game;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public String getComment() {
        return comment;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public void setCommentedOn(Date commentedDate) {
        this.commentedOn = commentedDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", comment='" + comment + '\'' +
                ", commentedDate=" + commentedOn +
                '}';
    }
}
