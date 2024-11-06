package org.ricardo.SpringSecurityJWT.servers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.ricardo.SpringSecurityJWT.models.UserEntity;
import org.ricardo.SpringSecurityJWT.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServerImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("El usuario ".concat(username).concat(" no existe")));
		
		Collection<? extends GrantedAuthority> authorities = userEntity.getRoles().stream()
				.map(role  -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
				.collect(Collectors.toSet());
		
		return new User(userEntity.getUsername(), userEntity.getPassword(),
				true,
				true,
				true,
				true,
				authorities);
	}

}
