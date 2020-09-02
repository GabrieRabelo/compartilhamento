package br.pucrs.ages.townsq.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "bio")
    private String bio;
}
