package sk.tuke.gamestudio.game.taptiles.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.game.taptiles.entity.Comment;
import sk.tuke.gamestudio.game.taptiles.service.CommentService;

import java.util.List;
@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public void addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
    }

    @GetMapping("/{game}")
    public List<Comment> getLastComments(@PathVariable String game) {
        return commentService.getComments(game);
    }
}
