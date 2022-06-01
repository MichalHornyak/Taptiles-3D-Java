package sk.tuke.gamestudio.game.taptiles.service;

import sk.tuke.gamestudio.game.taptiles.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void setRating(Rating rating) {
        Query query = entityManager.createQuery("SELECT r FROM Rating r where r.game=:game AND r.player =:player").setParameter("game", rating.getGame()).setParameter("player", rating.getPlayer());
        if (query.getResultList().size() == 0) {
            entityManager.persist(rating);
        } else {
            Rating ratingUpdate = (Rating) query.getSingleResult();
            ratingUpdate.setRating(rating.getRating());
        }

    }

    @Override
    public int getRating(String game, String player) {
        try {
            return (int) entityManager.createQuery("SELECT r.rating FROM Rating r where r.game=:game AND r.player =:player").setParameter("game", game).setParameter("player", player).getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public int getAverageRating(String game) {
        if (entityManager.createQuery("SELECT avg(r.rating) FROM Rating r WHERE r.game=:game").setParameter("game", game).setParameter("game", game).getSingleResult() == null)
            return 0;
        else {
            double average = (double) entityManager.createQuery("SELECT avg(r.rating) FROM Rating r WHERE r.game=:game").setParameter("game", game).getSingleResult();
            return (int) average;
        }
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("delete from rating").executeUpdate();
    }
}
