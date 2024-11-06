package org.ricardo.SpringSecurityJWT;

import java.util.Set;

import org.ricardo.SpringSecurityJWT.models.ERole;
import org.ricardo.SpringSecurityJWT.models.RoleEntity;
import org.ricardo.SpringSecurityJWT.models.UserEntity;
import org.ricardo.SpringSecurityJWT.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;

	@Bean
	CommandLineRunner init() {
		return args -> {
			
			UserEntity userEntity = UserEntity.builder()
					.email("ricardo@hotmail.com")
					.username("ricardo")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.ADMIN.name()))
							.build()))
					.build();
			
			userRepository.save(userEntity);
			
			UserEntity userEntity2 = UserEntity.builder()
					.email("joe@hotmail.com")
					.username("joe")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.USER.name()))
							.build()))
					.build();
			
			userRepository.save(userEntity2);
			
			UserEntity userEntity3 = UserEntity.builder()
					.email("any@hotmail.com")
					.username("any")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.INVITED.name()))
							.build()))
					.build();
			
			userRepository.save(userEntity3);
		};
	}
}
