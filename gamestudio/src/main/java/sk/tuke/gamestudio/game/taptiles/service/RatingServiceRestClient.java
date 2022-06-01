package sk.tuke.gamestudio.game.taptiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.taptiles.entity.Rating;

public class RatingServiceRestClient implements RatingService {
    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url + "/rating", rating, Rating.class);
    }

    @Override
    public int getRating(String game, String player) {
        if ((restTemplate.getForEntity(url + "/rating/" + game + "/" + player, Rating.class).getBody()) == null) {
            System.out.println("i donnt get it");
            return 0;
        } else
            return (restTemplate.getForEntity(url + "/rating/" + game + "/" + player, int.class).getBody());
    }

    @Override
    public int getAverageRating(String game) {
        if (restTemplate.getForEntity(url + "/rating/average/" + game, Rating.class).getBody() != null) {
            return restTemplate.getForEntity(url + "/rating/average/" + game, int.class).getBody();
        } else {
            return 0;
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported");
    }
}
