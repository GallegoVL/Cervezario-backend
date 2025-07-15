package com.gallego.curso.springboot.beerbackend.backend_beers.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gallego.curso.springboot.beerbackend.backend_beers.validation.ExistsByEmail;
import com.gallego.curso.springboot.beerbackend.backend_beers.validation.ExistsByTlf;
import com.gallego.curso.springboot.beerbackend.backend_beers.validation.ExistsByUsername;
import com.gallego.curso.springboot.beerbackend.backend_beers.validation.IsRequired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idusers;

    @IsRequired (groups = {CreateUser.class,UpdateUser.class},message = "{IsRequired.users.username}")
    @Column(unique = true)
    @ExistsByUsername
    @Size (min=4, max=50, message = "{Size.users.username}")
    private String username ;
    
    //INTENTAR ARREGLAR EL ERROR DE VALIDACION DE LONGITUD
    @NotBlank(groups = {CreateUser.class},message = "{NotBlank.users.password}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @IsRequired (groups = {CreateUser.class,UpdateUser.class}, message = "{IsRequired.users.name}" )
    private String name;
    
    @IsRequired (groups = {CreateUser.class,UpdateUser.class}, message = "{IsRequired.users.lastname}")
    private String lastname;
    
    @IsRequired (groups = {CreateUser.class,UpdateUser.class},message = "{IsRequired.users.email}")
    @Column(unique = true)
    @ExistsByEmail
    @Email(groups = {CreateUser.class,UpdateUser.class},message = "{Email.users.email}")
    @Pattern(
      regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      message = "{Pattern.users.email}"
    )
    private String email;
    
    @IsRequired (groups = {CreateUser.class,UpdateUser.class},message = "{IsRequired.users.tlf}")
    @Column(unique = true)
    @ExistsByTlf
    @Size(min = 9, max = 15, message = "{Size.users.tlf}")
    private String tlf;
    
   
    private Date date;
    
    @IsRequired (groups = {CreateUser.class,UpdateUser.class},message = "{IsRequired.users.country}")
    private String country;

    @ManyToMany
    @JoinTable(
        name=("users_roles"),
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="role_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})}
    )
    @JsonIgnoreProperties ({"users","handler","hiberrnateLazyInitializer"})//evita el error ciclico
    private List<Role> roles;

    public Users(){
        roles=new ArrayList<>();
    }

    @ManyToMany
    @JoinTable(
        name=("users_cervezas"),
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="cerveza_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","cerveza_id"})}

    )
    private List<Beers> beers;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<UserBeers> userBeers = new ArrayList<>();

    @Transient //anotacion que indica que es un campo de la clase no de la tabla, lo cual no es persistente
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    
    private boolean enabled;
    
    @PrePersist
    public void prePersist(){
        enabled=true;
    }


    
    
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public List<Beers> getBeers() {
        return beers;
    }
    public void setBeers(List<Beers> beers) {
        this.beers = beers;
    }
    public Long getIdusers() {
        return idusers;
    }
    public void setIdusers(Long idusers) {
        this.idusers = idusers;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTlf() {
        return tlf;
    }
    public void setTlf(String tlf) {
        this.tlf = tlf;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }




    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idusers == null) ? 0 : idusers.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Users other = (Users) obj;
        if (idusers == null) {
            if (other.idusers != null)
                return false;
        } else if (!idusers.equals(other.idusers))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }




    public List<UserBeers> getUserBeers() {
        return userBeers;
    }




    public void setUserBeers(List<UserBeers> userBeers) {
        this.userBeers = userBeers;
    }
    
    
}
