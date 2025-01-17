package com.app.ExamManager.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, length = 255, unique = true)
    @Size(min = 5, max = 255)
    private String username;

    @Column(name = "senha", nullable = false, length = 255)
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dateBirth;

    @Column(name = "role", nullable = false)
    private eUserRole role;

    public Usuario (String username, String password, LocalDate dateBirth, eUserRole role){
        this.username = username;
        this.password = password;
        this.dateBirth = dateBirth;
        this.role = role;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == eUserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
