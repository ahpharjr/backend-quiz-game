package com.ahphar.backend_quiz_game.models;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phases")
public class Phase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phaseId;

    private String name;
    private String image;

    @Column(name = "`desc`")
    private String desc;
    
}
