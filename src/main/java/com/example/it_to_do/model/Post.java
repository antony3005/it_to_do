package com.example.it_to_do.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private boolean status; 

    public Post(String post, boolean status){
        this.post = post;
        this.status = status;
    }

    public Post(){

    }
    public Long getId(){return id;}

    public String getPost(){return post;}
    public void setPost(String post){ this.post = post; }

    public boolean getStatus(){return status;}
    public void setStatus(boolean status){this.status = status;};

}