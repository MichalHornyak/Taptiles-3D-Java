package sk.tuke.gamestudio.game.taptiles.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.taptiles.entity.Comment;
import sk.tuke.gamestudio.game.taptiles.entity.Rating;
import sk.tuke.gamestudio.game.taptiles.service.CommentService;
import sk.tuke.gamestudio.game.taptiles.service.RatingService;
import sk.tuke.gamestudio.game.taptiles.service.ScoreService;

import java.util.Date;


@Controller
@RequestMapping("/services")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ServicesController {
    @Autowired
    private UserController userController;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ScoreService scoreService;

    private String size = "2";

    @RequestMapping()
    public String services() {
        return "services";
    }

    @RequestMapping("/scores")
    public String scores(@RequestParam(required = false) String boardSize, Model model) {
        if (boardSize != null)
            this.size = boardSize;
        fillModelScore(model);
        return "/scores";
    }


    @RequestMapping("/ratings")
    public String ratings(Model model) {
        fillModelRatings(model);
        return "/ratings";
    }

    @RequestMapping("/comments")
    public String comments(Model model) {
        fillModelComments(model);
        return "/comments";
    }

    @RequestMapping("/comments/newcomment")
    public String newcomment(String commentText) {
        commentService.addComment(new Comment(userController.getLoggedUser().getLogin(), "taptiles", commentText, new Date()));
        return "redirect:/services/comments";
    }

    @RequestMapping("/ratings/addRating")
    public String newRating(@RequestParam(required = false) String rating) {
        ratingService.setRating(new Rating(userController.getLoggedUser().getLogin(), "taptiles", Integer.parseInt(rating), new Date()));
        return "redirect:/services/ratings";
    }

    private void fillModelScore(Model model) {
        model.addAttribute("scores", scoreService.getTopScores("taptiles " + size));
    }

    private void fillModelComments(Model model) {
        model.addAttribute("comments", commentService.getComments("taptiles"));
    }

    private void fillModelRatings(Model model) {
        model.addAttribute("ratings", ratingService.getAverageRating("taptiles"));
        if (userController.getLoggedUser() != null) {
            model.addAttribute("playerRating", ratingService.getRating("taptiles", userController.getLoggedUser().getLogin()));
        }
    }


    public String scoreTemplate() {
        StringBuilder sb = new StringBuilder();
        String cssClass = "board-id";
        sb.append("<div id='board-choose-container'>");
        for (int i = 2; i < 7; i++) {
            if (Integer.parseInt(size) == i) {
                sb.append("<a href='/services/scores?boardSize=").append(i).append("'><h1 class='board-size-selector chosen'>").append(i).append("</h1></a>");
            } else {
                sb.append("<a href='/services/scores?boardSize=").append(i).append("'><h1 class='board-size-selector'>").append(i).append("</h1></a>");
            }
        }
        sb.append("</div>");
        return sb.toString();
    }
}
