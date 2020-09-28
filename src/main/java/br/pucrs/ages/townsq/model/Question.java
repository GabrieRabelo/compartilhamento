package br.pucrs.ages.townsq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Question {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Título não pode ser nulo.")
    @NotEmpty(message = "Título não pode ser vazio.")
    @Column(name = "title", columnDefinition = "VARCHAR(50)", nullable =  false)
    private String title;
    @NotNull(message = "Descrição não pode ser nula.")
    @NotEmpty(message = "Descrição não pode ser vazia.")
    @Column(name = "description", columnDefinition = "VARCHAR(512)", nullable =  false)
    private String description;
    @Column(name = "isActive")
    private int isActive;
    @UpdateTimestamp
    @Column(name = "updatedAt")
    private java.sql.Timestamp updatedAt;
    @CreationTimestamp
    @Column(name = "createdAt")
    private java.sql.Timestamp createdAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "topicId", nullable = false)
    private Topic topic;
    @Column(name = "status")
    private int status;

}
