package com.ahphar.backend_quiz_game.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "topics")
public class Topic {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long topicId;
	
	private String name;
	private String image;
	
	@Column(name = "`desc`")
	private String desc;
	
	@ManyToOne
	@JoinColumn(name = "phase_id", nullable = false)
	private Phase phase;

	@OneToOne(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Quiz quiz;

	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Flashcard> flashcards;

}
