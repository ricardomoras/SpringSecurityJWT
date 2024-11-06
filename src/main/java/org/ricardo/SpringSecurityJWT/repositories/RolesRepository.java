package org.ricardo.SpringSecurityJWT.repositories;

import org.ricardo.SpringSecurityJWT.models.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends CrudRepository<RoleEntity, Long> {

}
