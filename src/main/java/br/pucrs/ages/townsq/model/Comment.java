package br.pucrs.ages.townsq.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Comentário não pode ser nulo.")
    @NotEmpty(message = "Comentário não pode ser vazio.")
    @Size(min = 1, max = 512)
    @Column(name = "text", columnDefinition = "VARCHAR(512)", nullable =  false)
    private String text;

    @Column(name = "isActive")
    private int isActive = 1;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private java.sql.Timestamp updatedAt;

    @CreationTimestamp
    @Column(name = "createdAt")
    private java.sql.Timestamp createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "questionId", nullable = true)
    private Question question;

    @ManyToOne(optional = true)
    @JoinColumn(name = "answerId", nullable = true)
    private Answer answer;

}
