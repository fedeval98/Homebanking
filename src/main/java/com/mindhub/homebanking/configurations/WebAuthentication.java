package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // notacion para decir que el metodo tiene mas de 1 o mas @bean
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    // inyeccion de dependencias de clientrepository
    @Autowired
    ClientRepository clientRepository;

// metodo para saber si un usuario esta o no dentro de nuestra base de datos.
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputName-> {
// hacemos una busqueda en el repostirio por email del usuario, este mail
// se recibe por queryparam al intentar logear
            Client client = clientRepository.findByEmail(inputName);

            if (client != null) {

                return new User(client.getEmail(), client.getPassword(),
// una vez chequeamos que existe y obtenemos sus datos, obtenemos su rol (client/admin)
                        AuthorityUtils.createAuthorityList(client.getRole().toString()));
            } else {
// si no encontramos su email, devolvemos un error.
                throw new UsernameNotFoundException("Unknown User");

            }
        });
    }
// metodo encargado de cifrar las contraseñas de nuestros usuarios.
    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

} // cierra WebAuth
