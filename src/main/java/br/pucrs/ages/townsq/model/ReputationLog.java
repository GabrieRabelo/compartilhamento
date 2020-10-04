package br.pucrs.ages.townsq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "reputation_logs")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReputationLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "eventType", columnDefinition = "VARCHAR(30)", nullable = false)
    @NotEmpty(message = "Tipo do evento não pode ser vazio.")
    @NotNull(message = "Tipo do evento não pode ser nulo.")
    private String eventType;

    @Column(name = "points")
    @NotNull(message = "Pontuação não pode ser nula.")
    private Integer points;

    @CreationTimestamp
    @Column(name = "createdAt")
    private java.sql.Timestamp createdAt;

    @Column(name = "isActive")
    @NotNull(message = "isActive não pode ser nulo.")
    private Integer isActive = 1;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    @ManyToOne(optional = false)
    @JoinColumn(name = "toUserId", nullable = false)
    private User toUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fromUserId", nullable = false)
    private User fromUser;

}
