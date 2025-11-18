package com.curso.demo_park_api.service;

import com.curso.demo_park_api.AppExceptions.EntityNotFoundException;
import com.curso.demo_park_api.AppExceptions.PasswordInvalidException;
import com.curso.demo_park_api.AppExceptions.UserUniqueViolationException;
import com.curso.demo_park_api.entity.Usuario;
import com.curso.demo_park_api.repository.UsuarioRepository;

import com.curso.demo_park_api.roles.Roles;
import com.curso.demo_park_api.web.controller.dto.UsuarioCreateDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvar(UsuarioCreateDto dto) {
        try{
            Usuario user = new Usuario();
            user.setUsername(dto.getUsername());
            user.setPassword(dto.getPassword());
            user.setRole(Roles.ROLE_CLIENTE);
            return usuarioRepository.save(user);
        }catch (DataIntegrityViolationException ex){
            throw new UserUniqueViolationException(String.format("Usuario {%s} já cadastrado!", dto.getUsername()));
        }

    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {

        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuario id = %s não encontrado.",id))
        );
    }

    @Transactional
    public Usuario alteraSenha(Long id, String password, String newPassword, String confirmPassword) {
        if(!newPassword.equals(confirmPassword) ){
            throw new PasswordInvalidException("A nova senha e a senha de confirmação são diferentes! por gentileza, digite novamente!");
        }
        Usuario user =  findById(id);
        if(!user.getPassword().equals(password)){
            throw new PasswordInvalidException("Houve um erro, a senha atual diferente da digitada.");
        }
        user.setPassword(newPassword);
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
