package br.pucrs.ages.townsq.model;

import br.pucrs.ages.townsq.listeners.QuestionListener;
import br.pucrs.ages.townsq.utils.Chronos;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@EntityListeners(QuestionListener.class)
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Question {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Título não pode ser nulo.")
    @NotEmpty(message = "Título não pode ser vazio.")
    @Column(name = "title", columnDefinition = "VARCHAR(50)", nullable =  false)
    private String title;
    @NotNull(message = "Descrição não pode ser nula.")
    @NotEmpty(message = "Descrição não pode ser vazia.")
    @Column(name = "description", columnDefinition = "VARCHAR(512)", nullable =  false)
    private String description;
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "topicId", nullable = false)
    private Topic topic;
    @Column(name = "status")
    private int status = 1;

    public void setUser(User u){
        if(this.user == null){
            this.user = u;
        }
    }

    public String getCreatedAtString(){
        return Chronos.dateToPrettyTimeString(this.createdAt);
    }

}
