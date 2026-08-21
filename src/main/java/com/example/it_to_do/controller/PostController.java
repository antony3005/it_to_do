package com.example.it_to_do.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PostController{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PostController(PostRepository postRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
// ========================Post===================================
    // Exibir pagina Salvar post
    @GetMapping("/salvarPost")
    public String exibirPaginaEscrever() {
        return "salvarPost";
    }
    // Salvar post
    @PostMapping("salvarPost")
    public String salvarPost(@RequestParam String post, @RequestParam Post.Visibilidade visibilidade) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User){
            User usuarioLogado = (User) auth.getPrincipal();
            Post novoPost = new Post();
            novoPost.setPost(post);
            novoPost.setVisibilidade(visibilidade);
            novoPost.setAutor(usuarioLogado);

            postRepository.save(novoPost);
            return "redirect:/afazeres";
        }
        return "redirect:/login";
    }
    // Listar post
    @GetMapping("/afazeres")
    public String listarPost (Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof User){

            User usuarioLogado = (User) auth.getPrincipal();

            // Busca posts públicos + posts próprios
            List<Post> postVisiveis = postRepository.findPostVisiveisParaUsuario(usuarioLogado);
            model.addAttribute("post", postVisiveis);
            return "afazeres";
        }
        // Se não estiver logado, exibe apenas os posts públicos
        List <Post> post = postRepository.findAll();
        model.addAttribute("post", post);
        return "afazeres";
    }
    // Atualizar post
    @GetMapping("/atualizarPost/{id}")
    public String exibirFormularioAtualizarPost(@PathVariable Long id, Model model) {
        Optional <Post> postExistente = postRepository.findById(id);
        if (postExistente.isPresent()) {
            model.addAttribute("post", postExistente.get());
        }else{
            model.addAttribute("erro", "Post não encontrado");
            return "redirect:/afazeres";
        }
        return "atualizarPost";
    }
    
    // Deletar post
    @PostMapping("/deletarMensagem/{id}")
    public String deletarPost(@PathVariable Long id){
        if(postRepository.existsById(id)){
            postRepository.deleteById(id);
            
        }
        return "redirect:/afazeres";
    }

// ==============================================================
// ========================User==================================
    // Exibir pagina Salvar usuario
    @GetMapping("/salvarUsuario")
    public String exibirPaginaSalvarUsuario() {
        return "salvarUsuario";
    }
    // Salvar Usuario
    @PostMapping("/salvarUsuario")
    public String salvarUsuario(@RequestParam String nome,
                                @RequestParam String email,
                                @RequestParam String senha) {
        User novoUsuario = new User();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email.trim().toLowerCase());
        novoUsuario.setSenha(passwordEncoder.encode(senha));
        userRepository.save(novoUsuario);
        return "redirect:/login";
    }
    // Listar usuario
    @GetMapping("/listarUsuario")
    public String listarUsuario(Model model) {
        List <User> user = userRepository.findAll(); 
        model.addAttribute("usuarios", user);
        return "listarUsuario";
    }
    // Deletar Usuario
    @PostMapping("/deletarUsuario/{id}")
    public ResponseEntity <Void> deletarUsuario(@PathVariable Long id) {
        if(postRepository.existsById(id)){
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
            }
        return ResponseEntity.notFound().build();
    }
    // Atualizar usuario
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
    // Exibir pagina de login
    @GetMapping("/login")
    public String exibirPaginaLogin() {
        return "login";
    }
    // Pagina de login
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                        @RequestParam String senha,
                        Model model,
                        HttpServletRequest request) {
        
        Optional <User> userOptional = userRepository.findByEmail(email.trim().toLowerCase());

        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(passwordEncoder.matches(senha, user.getPassword())){

                UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(user, null, List.of());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

                return "redirect:/afazeres";
            }
        }
        model.addAttribute("Erro", "Email ou senha incorretos");
        return "login";
    }


// ==============================================================
// ========================Dashboard=============================
@GetMapping("/dashboard")
public String dashboard() {
    return "dashboard";
}
// ==============================================================
}