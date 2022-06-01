package sk.tuke.gamestudio.game.taptiles.service;

import sk.tuke.gamestudio.game.taptiles.entity.Rating;

import java.sql.*;


public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String SELECT = "SELECT player, game, rating, rateddate FROM rating WHERE game = ? AND player = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT = "INSERT INTO rating (playername, game, rating, rateddate) VALUES (?, ?, ?, ?)";

    @Override
    public void setRating(Rating rating) {
        if ((rating.getPlayer().length() > 15 || rating.getPlayer().length() <= 0) ||
                (rating.getGame().length() > 64 || rating.getGame().length() <= 0) ||
                (rating.getRating() > 5 || rating.getRating() < 0)) {
        } else {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(INSERT)
            ) {
                statement.setString(1, rating.getPlayer());
                statement.setString(2, rating.getGame());
                statement.setInt(3, rating.getRating());
                statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new GameStudioException("Problem inserting rating", e);
            }
        }
    }

    @Override
    public int getRating(String game, String player) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)).getRating();
                } else{
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new GameStudioException("Problem selecting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        return 0;
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new GameStudioException("Problem deleting rating", e);
        }
    }
}
