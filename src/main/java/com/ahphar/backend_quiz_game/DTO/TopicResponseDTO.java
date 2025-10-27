package com.ahphar.backend_quiz_game.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponseDTO implements Serializable{

    private Long topicId;
    private String name;
    private String image;
    private String desc;
    private LocalDateTime createdAt;
    private String phaseName;
    private Long phaseId;
    
}
