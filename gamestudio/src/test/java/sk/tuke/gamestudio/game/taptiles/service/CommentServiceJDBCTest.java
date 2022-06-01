package sk.tuke.gamestudio.game.taptiles.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.taptiles.entity.Comment;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceJDBCTest {

    private CommentService createCommentService(){
        return new CommentServiceJDBC();
    }
    @Test
    void addValidComments() {
        CommentService service = createCommentService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment("Miško","Taptiles","Dobrá hra",date));
        assertEquals(1, service.getComments("Taptiles").size());

        List<Comment> comments = service.getComments("Taptiles");

        assertEquals("Miško", comments.get(0).getPlayer());
        assertEquals("Taptiles", comments.get(0).getGame());
        assertEquals("Dobrá hra", comments.get(0).getComment());
    }

    @Test
    void addInvalidComments(){
        CommentService service = createCommentService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment("1234567890123456", "Taptiles", "gg", date));
        service.addComment(new Comment("1234567890123456", "Taptiles", "gg", date));
        service.addComment(new Comment("123","", "", date));
        service.addComment(new Comment("","123", "", date));
        service.addComment(new Comment("","", "123", date));

        List<Comment> comments = service.getComments("Taptiles");
        assertEquals(0, comments.size());
    }

    @Test
    void testLastComments() {

        CommentService service = createCommentService();
        service.reset();
        service.addComment(new Comment("Peter", "Taptiles", "commentary", new Date()));
        for (int i = 0; i < 9; i++) {
            service.addComment(new Comment("Miško", "Taptiles", "commentary", new Date()));
        }
        List<Comment> comments = service.getComments("Taptiles");
        assertEquals(10, comments.size());
        assertEquals("Peter", comments.get(comments.size()-1).getPlayer());

        service.addComment(new Comment("Miško", "Taptiles", "commentary", new Date()));
        comments = service.getComments("Taptiles");
        assertEquals(10, comments.size());
        assertEquals("Miško", comments.get(comments.size()-1).getPlayer());
    }

    @Test
    void reset() {
        CommentService service = createCommentService();
        service.reset();
        assertEquals(0,service.getComments("Taptiles").size());
    }
}