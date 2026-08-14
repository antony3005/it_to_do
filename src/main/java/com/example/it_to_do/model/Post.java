package com.example.it_to_do.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Post")

public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String post;

    @Column(nullable = false, length = 20)
    private boolean status = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Visibilidade visibilidade = Visibilidade.PUBLICO;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "user_id", nullable= false)
    private User autor;

    @ManyToMany
    @JoinTable(
        name = "post_compartilhamentos",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name= "user_id")
    )
    private Set<User> usuariosCompartilhados = new HashSet<>();

    public Post(String post, boolean status, Visibilidade visibilidade, User autor){
        this.post = post;
        this.status = status;
        this.visibilidade = visibilidade;
        this.autor = autor;
    }

    public Post(){

    }
    public Long getId(){return id;}

    public String getPost(){return post;}
    public void setPost(String post){ this.post = post; }

    public boolean getStatus(){return status;}
    public void setStatus(boolean status){this.status = status;};

    public enum Visibilidade{
        PUBLICO,
        AMIGOS,
        PRIVADO
    }

    public Visibilidade getVisibilidade(){return visibilidade;}
    public void setVisibilidade(Visibilidade visibilidade){this.visibilidade = visibilidade;}

    public void setAutor(User autor){this.autor = autor;}
    public User getAutor(){return autor;} 
    
    public Set<User> getUsuariosCompartilhados(){return usuariosCompartilhados;}
    public void setUsuariosCompartilhados(Set<User> usuariosCompartilhados) {this.usuariosCompartilhados = usuariosCompartilhados;}
    
}