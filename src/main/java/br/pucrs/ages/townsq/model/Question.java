package br.pucrs.ages.townsq.model;

import br.pucrs.ages.townsq.listeners.QuestionListener;
import br.pucrs.ages.townsq.utils.Chronos;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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
    @OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL, mappedBy = "question")
    private List<Comment> comments;
    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL, mappedBy = "question")
    private List<Answer> answers;

    public void setUser(User u){
        if(this.user == null){
            this.user = u;
        }
    }

    public String getCreatedAtString(){
        return Chronos.dateToPrettyTimeString(this.createdAt);
    }

    public List<Comment> getAllActiveComments(){
        return comments.stream().filter(e -> e.getIsActive() == 1).collect(Collectors.toList());
    }

    public List<Answer> getAllActiveAnswers(){
        return answers.stream().filter(e -> e.getIsActive() == 1).collect(Collectors.toList());
    }

    public String getCommentCountString(){
        int count = getAllActiveComments().size();
        if (count == 1) return "1 comentário";
        return count + " comentários";
    }

}
