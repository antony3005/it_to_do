package com.example.it_to_do.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.it_to_do.model.Post;
import com.example.it_to_do.model.User;
import com.example.it_to_do.repository.PostRepository;
import com.example.it_to_do.repository.UserRepository;







@Controller
public class PostController{
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public PostController(PostRepository postRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
// ========================Post===================================
    @GetMapping("/salvarPost")
    public String exibirPaginaCadastro() {
        return "salvarPost";
    }

    @PostMapping("salvarPost")
    public String salvarPost(@RequestParam String post, @RequestParam boolean status) {
        Post novoPost = new Post();
        novoPost.setPost(post);
        novoPost.setStatus(status);
        postRepository.save(novoPost);
        return "";
    }

    @GetMapping("/listarPost")
    public String listarPost (Model model){
        List <Post> post = postRepository.findAll();
        model.addAttribute("post", post);
        return "listarPost";
    }

    @PostMapping("/atualizarPost")
    public String atualizarPost(@ModelAttribute("post") Post postAtualizado, Model model){
        Optional <Post> postExistente = postRepository.findById(postAtualizado.getId());
        if(postExistente.isPresent()){

            Post post = postExistente.get();

            post.setPost(postAtualizado.getPost());
            post.setStatus(postAtualizado.getStatus());

            postRepository.save(post);

            model.addAttribute("post", "Post atualzado com sucesso");
        }else{
            model.addAttribute("erro", "Post não encontrado");
        }
        return "atualizarPost";
    }
    
    @PostMapping("/deletarMensagem/{id}")
    public ResponseEntity<Void> deletarPost(@PathVariable Long id){
        if(postRepository.existsById(id)){
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

// ==============================================================
// ========================User==================================
@GetMapping("/salvarUsuario")
public String exibirPaginaSalvarUsuario() {
    return "salvarUsuario";
}

@PostMapping("/salvarUsuario")
public String salvarUsuario(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String senha) {
    User novoUsuario = new User();
    novoUsuario.setNome(nome);
    novoUsuario.setEmail(email.trim().toLowerCase());
    novoUsuario.setSenha(passwordEncoder.encode(senha));
    userRepository.save(novoUsuario);
    
    
    return "redirect:/";
}

@GetMapping("/listarUsuario")
public String listarUsuario(Model model) {
    List <User> user = userRepository.findAll(); 
    model.addAttribute("Usuario", user);
    return "listarUsuario";
}

@PostMapping("/deletarUsuario/{id}")
public ResponseEntity <Void> deletarUsuario(@PathVariable Long id) {
    if(postRepository.existsById(id)){
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
        }
    return ResponseEntity.notFound().build();
}
@PostMapping("/atualizarUsuario")
public String atualizarUsuario(@ModelAttribute("user") User userAtualizado, Model model) {
    Optional <User> userExistente = userRepository.findByEmail(userAtualizado.getEmail());
    if(userExistente.isPresent()){
        User user = userExistente.get();
        user.setNome(userAtualizado.getNome());
        userRepository.save(userAtualizado);
        model.addAttribute("Usuario", "Usuario atualizado com sucesso");
    }else{
        model.addAttribute("Erro", "Não foi possivel atualizar o usuario");
    }
    return "atualizarUsuario";
}
// ==============================================================
// ========================Login=================================




// ==============================================================

}