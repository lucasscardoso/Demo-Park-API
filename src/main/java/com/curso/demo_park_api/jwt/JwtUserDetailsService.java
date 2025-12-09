package com.curso.demo_park_api.jwt;

import com.curso.demo_park_api.entity.Usuario;
import com.curso.demo_park_api.roles.Roles;
import com.curso.demo_park_api.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public JwtUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     Usuario usuario = usuarioService.buscarPorUsername(username);
      return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String username){
      Roles role = usuarioService.buscarRolePorUsername(username);
        return JwtUtils.createToken(username,role.name().substring("ROLE_".length()));
    }
}
