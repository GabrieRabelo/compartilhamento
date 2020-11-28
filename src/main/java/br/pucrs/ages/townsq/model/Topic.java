package br.pucrs.ages.townsq.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "topics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Topic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "VARCHAR(80)", unique = true)
    @NotEmpty
    @NotNull
    private String name;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private java.sql.Timestamp updatedAt;
    @CreationTimestamp
    @Column(name = "createdAt")
    private java.sql.Timestamp createdAt;

    /**
     * Status:
     * 1: Active
     * 0: Inactive
     */
    @Column(name = "status")
    private int status = 1;

    /**
     * Checks if the topic is selected on the frontend filter for the index page
     * @param topic String topic id param
     * @return boolean
     */
    public boolean isTopicSelectedForFilter(String topic){
        if(topic == null || StringUtils.isEmpty(topic)) return false;
        return Long.valueOf(topic).equals(this.id);
    }

}
