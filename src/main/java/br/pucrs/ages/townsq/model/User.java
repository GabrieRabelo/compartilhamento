package br.pucrs.ages.townsq.model;

import br.pucrs.ages.townsq.components.UserListener;
import br.pucrs.ages.townsq.utils.Chronos;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@EntityListeners(UserListener.class)
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Nome não pode ser nulo.")
    @NotEmpty(message = "Nome não pode ser vazio.")
    @Column(name = "name", columnDefinition = "VARCHAR(50)", nullable =  false)
    private String name;
    @NotNull(message = "Senha não pode ser nula.")
    @NotEmpty(message = "Senha não pode ser vazia.")
    @Column(name = "password", columnDefinition = "VARCHAR(256)", nullable =  false)
    private String password;
    @NotNull(message = "E-mail não pode ser nulo.")
    @NotEmpty(message = "E-mail não pode ser vazio.")
    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(256)", nullable =  false)
    private String email;
    @Column(name = "score")
    private int score;
    @Column(name = "bio", columnDefinition = "VARCHAR(256)")
    private String bio;
    @Column(name = "company", columnDefinition = "VARCHAR(256)")
    private String company;
    @URL
    @Column(name = "website", columnDefinition = "VARCHAR(256)")
    private String website;
    @Column(name = "image", columnDefinition = "VARCHAR(256)")
    private String image;
    @Column(name = "role", columnDefinition = "VARCHAR(30)")
    private String role;
    @UpdateTimestamp
    @Column(name = "updatedAt")
    private java.sql.Timestamp updatedAt;
    @CreationTimestamp
    @Column(name = "createdAt")
    private java.sql.Timestamp createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getCreatedAtString(){
        return Chronos.dateToPrettyTimeString(this.createdAt);
    }
}
