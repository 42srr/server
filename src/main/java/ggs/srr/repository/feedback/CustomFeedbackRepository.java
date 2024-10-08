package ggs.srr.repository.feedback;

import java.util.List;

import org.springframework.stereotype.Repository;

import ggs.srr.domain.feedback.Feedback;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class CustomFeedbackRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Feedback save(Feedback feedback) {
        entityManager.persist(feedback); // 새로운 피드백 저장
        return feedback;
    }

    public Feedback findById(Long id) {
        return entityManager.find(Feedback.class, id); // ID로 피드백 조회
    }

    public List<Feedback> findAll() {
        TypedQuery<Feedback> query = entityManager.createQuery("SELECT f FROM Feedback f", Feedback.class);
        return query.getResultList(); // 모든 피드백 조회
    }

    public List<Feedback> findByUserLogin(String userLogin) {
        TypedQuery<Feedback> query = entityManager.createQuery("SELECT f FROM Feedback f WHERE f.user.login = :userLogin", Feedback.class);
        query.setParameter("userLogin", userLogin);
        return query.getResultList(); // 특정 사용자 피드백 조회
    }

    public List<Feedback> findByRating(int rating) {
        TypedQuery<Feedback> query = entityManager.createQuery("SELECT f FROM Feedback f WHERE f.rating = :rating", Feedback.class);
        query.setParameter("rating", rating);
        return query.getResultList(); // 특정 평점으로 피드백 조회
    }

    public void delete(Feedback feedback) {
        entityManager.remove(entityManager.contains(feedback) ? feedback : entityManager.merge(feedback)); // 피드백 삭제
    }
}
