package com.example.it_to_do.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.it_to_do.model.Post;
import com.example.it_to_do.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Optional<Post> findById(Long id);

    @Transactional
    void deleteByPost(String post);

    @Query("Select p from Post p where p.visibilidade = com.example.it_to_do.model.Post.Visibilidade.PUBLICO OR p.autor = :usuario")
    List<Post> findPostVisiveisParaUsuario(@Param ("usuario") User usuario);
}