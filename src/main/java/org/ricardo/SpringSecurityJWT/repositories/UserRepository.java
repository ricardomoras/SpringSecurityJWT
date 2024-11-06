package org.ricardo.SpringSecurityJWT.repositories;

import java.util.Optional;

import org.ricardo.SpringSecurityJWT.models.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);
	
	@Query("select u from UserEntity u where u.username = ?1")
	Optional<UserEntity> getName(String username);
}
