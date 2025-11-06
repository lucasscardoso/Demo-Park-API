package com.curso.demo_park_api.service;

import com.curso.demo_park_api.entity.Usuario;
import com.curso.demo_park_api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {

        return usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado.")
        );
    }

    @Transactional
    public Usuario alteraSenha(Long id, String password) {
        Usuario user =  findById(id);
        user.setPassword(password);
        return user;
    }

    public List<Usuario> buscaTodos() {
        List<Usuario> users =  usuarioRepository.findAll();
        return users;
    }

    public Usuario deletaUsuario(Long id) {
        Usuario user = findById(id);
        usuarioRepository.deleteById(id);
        return user;
    }
}
