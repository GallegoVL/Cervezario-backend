package com.gallego.curso.springboot.beerbackend.backend_beers.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
/*import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;*/
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_cervezas")
public class UserBeers {

    @EmbeddedId
    private UserBeersId id ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    @JsonIgnore
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cerveza_id")
    @MapsId("cervezaId")
    private Beers beer;

    private long etiquetas;

    
     public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Beers getBeer() {
        return beer;
    }

    public void setBeer(Beers beer) {
        this.beer = beer;
    }

    public long getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(long etiquetas) {
        this.etiquetas = etiquetas;
    }

    public UserBeersId getId() {
        return id;
    }

    public void setId(UserBeersId id) {
        this.id = id;
    }
    
}
