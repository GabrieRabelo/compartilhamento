package br.pucrs.ages.townsq.model;

import br.pucrs.ages.townsq.listeners.VoteLogListener;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@EntityListeners(VoteLogListener.class)
@Table(name = "vote_logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VoteLog {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "eventType", columnDefinition = "VARCHAR(30)", nullable = false)
    @NotEmpty(message = "Tipo do evento não pode ser vazio.")
    @NotNull(message = "Tipo do evento não pode ser nulo.")
    private String eventType;

    @Column(name = "score")
    @NotNull(message = "Pontuação não pode ser nula.")
    private Integer score;

    @CreationTimestamp
    @Column(name = "createdAt")
    private java.sql.Timestamp createdAt;

    @Column(name = "isActive")
    @NotNull(message = "isActive não pode ser nulo.")
    private Integer isActive = 1;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answerId")
    private Answer answer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
