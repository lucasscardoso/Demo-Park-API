package com.curso.demo_park_api.repository;

import com.curso.demo_park_api.entity.Usuario;
import com.curso.demo_park_api.roles.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {


    Optional<Usuario> findByUsername(String username);

    @Query("select u.role from Usuario u where u.username = :username")
    Roles findRoleByUsername(String username);
}
