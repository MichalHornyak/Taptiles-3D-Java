package sk.tuke.gamestudio.game.taptiles.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

public class User {
    private String login;

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
