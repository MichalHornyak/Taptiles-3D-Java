package sk.tuke.gamestudio.game.taptiles.service;

import sk.tuke.gamestudio.game.taptiles.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String SELECT = "SELECT playername, game, comment, commentedOn FROM comment WHERE game = ? ORDER BY commentedOn DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM comment";
    public static final String INSERT = "INSERT INTO comment (playername, game, comment, commentedOn) VALUES (?, ?, ?, ?)";

    @Override
    public void addComment(Comment comment) {
        if ((comment.getPlayer().length() > 15 || comment.getPlayer().length() <=0) ||
                (comment.getGame().length() > 64 || comment.getGame().length() <=0)||
                (comment.getComment().length() > 250 || comment.getComment().length() <=0)) {
        } else {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(INSERT)
            ) {
                statement.setString(1, comment.getPlayer());
                statement.setString(2, comment.getGame());
                statement.setString(3, comment.getComment());
                statement.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new GameStudioException("Problem inserting comment", e);
            }
        }
    }


    @Override
    public List<Comment> getComments(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    comments.add(new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4)));
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new GameStudioException("Problem selecting comment", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new GameStudioException("Problem deleting comment", e);
        }
    }
}
