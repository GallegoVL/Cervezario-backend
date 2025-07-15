package com.gallego.curso.springboot.beerbackend.backend_beers.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idroles;

    @NonNull
    @Column(unique = true)
    private String name;

    @JsonIgnoreProperties ({"roles","handler","hiberrnateLazyInitializer"})
    @ManyToMany(mappedBy = "roles")//relacion inversaa mappeada al campo roles de Users
    private List<Users> users;

    public Role(){
        users= new ArrayList<>();
    }

    
    public Role(String name){
        this.name=name;
    }

    

    public Long getIdroles() {
        return idroles;
    }

    public void setIdroles(Long idroles) {
        this.idroles = idroles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Users> getUsers() {
        return users;
    }


    public void setUsers(List<Users> users) {
        this.users = users;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idroles == null) ? 0 : idroles.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Role other = (Role) obj;
        if (idroles == null) {
            if (other.idroles != null)
                return false;
        } else if (!idroles.equals(other.idroles))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    
}
