package com.formacionbdi.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacionbdi.springboot.app.oauth.clients.UsuarioFeignClient;

@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	private UsuarioFeignClient cliente;
	
	private Logger log = LoggerFactory.getLogger(UsuarioService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = cliente.findByUsername(username);
		
		if (usuario==null) {
			log.info("No existe el usuario" + username);
			throw new UsernameNotFoundException("No existe el usuario" + username);
			
		}
		
		List<GrantedAuthority> roles = usuario.getRoles()
				.stream().map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> log.info(authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new User(usuario.getUsername(), usuario.getPassword(),
				usuario.getEnabled(), true, true, true, roles);
	}

}
