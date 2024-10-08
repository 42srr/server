package ggs.srr.domain.feedback;

import lombok.Data;

@Data
public class FeedbackDetail{
    private Long id;
    private Integer rate;
    private String kind;
}

