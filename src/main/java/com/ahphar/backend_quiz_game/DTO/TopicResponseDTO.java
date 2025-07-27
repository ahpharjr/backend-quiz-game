package com.ahphar.backend_quiz_game.DTO;

import java.io.Serializable;

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
    
}
