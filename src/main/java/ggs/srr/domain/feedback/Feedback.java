package ggs.srr.domain.feedback;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_info", columnDefinition = "TEXT")
    private String userInfo;

    @Column(name = "feedbackable_type")
    private String feedbackableType;

    @Column(name = "feedbackable_id")
    private Long feedbackableId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "feedback_details", columnDefinition = "TEXT")
    private String feedbackDetails;

    // Other utility methods as needed
}


