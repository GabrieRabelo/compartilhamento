package br.pucrs.ages.townsq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "banners")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Banner {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Título não pode ser nulo.")
    @NotEmpty(message = "Título não pode ser vazio.")
    @Column(name = "title", columnDefinition = "VARCHAR(50)", nullable =  false)
    @NotNull
    @NotEmpty
    private String title;

    @NotNull(message = "Descrição não pode ser nulo.")
    @NotEmpty(message = "Descrição não pode ser vazio.")
    @Column(name = "description", columnDefinition = "VARCHAR(512)", nullable =  false)
    @NotNull
    @NotEmpty
    private String description;

    @URL
    @NotNull(message = "Link não pode ser nulo.")
    @NotEmpty(message = "Link não pode ser vazio.")
    @Column(name = "url", columnDefinition = "VARCHAR(256)", nullable =  false)
    @NotNull
    @NotEmpty
    private String url;

    @Column(name = "isActive")
    private int isActive;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private java.sql.Timestamp updatedAt;
    @CreationTimestamp
    @Column(name = "createdAt")
    private java.sql.Timestamp createdAt;

}
