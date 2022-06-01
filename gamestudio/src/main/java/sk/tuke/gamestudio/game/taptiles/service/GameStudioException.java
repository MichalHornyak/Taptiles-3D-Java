package sk.tuke.gamestudio.game.taptiles.service;

public class GameStudioException extends RuntimeException {
    public GameStudioException() {
    }

    public GameStudioException(String message) {
        super(message);
    }

    public GameStudioException(String message, Throwable cause) {
        super(message, cause);
    }
}
