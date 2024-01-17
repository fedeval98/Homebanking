package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

    //creamos una consulta del repositorio en la que
    // buscamos un cliente por su email
    Client findByEmail (String email);

    //creamos una consulta del repositorio en la que
    // nos devuelve true/false si el mail existe o no
    boolean existsByEmail(String email);

}
