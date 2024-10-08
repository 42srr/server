package ggs.srr.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ggs.srr.domain.feedback.Feedback;

@Repository
public interface  FeedbackRepository extends JpaRepository<Feedback, Long>{

}

