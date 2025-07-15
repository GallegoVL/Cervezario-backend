package com.gallego.curso.springboot.beerbackend.backend_beers.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserBeersId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "cerveza_id")
    private Long cervezaId;

    public UserBeersId(){};

    public UserBeersId(Long userId, Long cervezaId){
        this.userId= userId;
        this.cervezaId=cervezaId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCervezaId() {
        return cervezaId;
    }

    public void setCervezaId(Long cervezaId) {
        this.cervezaId = cervezaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBeersId)) return false;
        UserBeersId that = (UserBeersId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(cervezaId, that.cervezaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, cervezaId);
    }
}
