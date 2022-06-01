package sk.tuke.gamestudio.game.taptiles.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.taptiles.entity.Comment;
import sk.tuke.gamestudio.game.taptiles.entity.Rating;
import sk.tuke.gamestudio.game.taptiles.entity.Score;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreServiceJDBCTest {

    private ScoreService scoreService(){
        return new ScoreServiceJDBC();
    }

    @Test
    void addScore() {
        ScoreService service = scoreService();
        service.reset();
        Date date = new Date();
        service.addScore(new Score("Miško","Taptiles", 30,date));
        service.addScore(new Score("Miško","Taptiles", 50,date));
        service.addScore(new Score("Miško","Taptiles", 70,date));
        service.addScore(new Score("Miško","Taptiles",90,date));
        List<Score> scores = service.getTopScores("Taptiles");
        assertEquals(4, scores.size());
        scores = service.getTopScores("Taptiles");
        assertEquals(0, scores.size());
    }

    @Test
    void testOrderPoints() {
        ScoreService service = scoreService();
        service.reset();
        Date date = new Date();
        service.addScore(new Score("Miško","Taptiles", 30,date));
        service.addScore(new Score("Miško","Taptiles", 150,date));
        service.addScore(new Score("Miško","Taptiles", 70,date));

        List<Score> scores = service.getTopScores("Taptiles");
        assertEquals(150, scores.get(0).getPoints());
        assertEquals(70, scores.get(1).getPoints());
        assertEquals(30, scores.get(2).getPoints());
    }

    @Test
    void testDifferentBoardSize() {
        ScoreService service = scoreService();
        service.reset();
        Date date = new Date();
        service.addScore(new Score("Miško","Taptiles", 10,date));
        service.addScore(new Score("Miško","Taptiles", 20,date));
        service.addScore(new Score("Miško","Taptiles", 30,date));

        List<Score> scores = service.getTopScores("Taptiles");
        assertEquals(3, scores.size());
    }
    @Test
    void testManyScores() {
        ScoreService service = scoreService();
        service.reset();
        Date date = new Date();
        for (int i = 0; i < 15; i++) {
            service.addScore(new Score("Miško", "Taptiles", 2, new Date()));
        }
        service.addScore(new Score("Miško", "Taptiles", 2, new Date()));

        List<Score> scores = service.getTopScores("Taptiles");
        assertEquals(10, scores.size());
        assertEquals(100, scores.get(0).getPoints());
    }

    @Test
    void reset() {
        ScoreService service = scoreService();
        service.reset();
        assertEquals(0,service.getTopScores("Taptiles").size());
    }
}