package com.example.it_to_do.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.it_to_do.model.Post;

import jakarta.transaction.Transactional;

@Repository
public interface MensagemRepository extends JpaRepository<Post, Long> {
    Optional<Post> findbyPost(String post);

    @Transactional
    void deleteByPost(String post);
}