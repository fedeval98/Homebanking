package com.mindhub.homebanking.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController // Controlador del tipo REST (recibe metodos HTTP (GET, POST, PUT, PATCH, DELETE) y devuelve JSON)
@RequestMapping("/api/clients") // realiza un mapeo de nuestra solicitud (asocia nuestra peticion a un endPoint)
public class ClientController {

    // Inyeccion de dependencia

    @Autowired //Se encarga de realizar la inyeccion por construccion pero de forma automatica
               // hace algo SIMILAR a instanciar la clase del objeto que ejecuta
    private ClientRepository clientRepository;

    @RequestMapping ("/all")
    public List<ClientDTO> getAllClient(){
        return clientRepository.findAll() //busco todos los clientes en mi repositorio
                .stream()// convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                // o terminales (count, collect, forEach, etc)
                .map(ClientDTO::new) // transformo cada client en un objeto DTO
                .collect(Collectors.toList()); //recopilo todos los objetos DTO y los transforma a una lista.
    }

    @RequestMapping("/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null); // aca vamos a buscar por ID pero nos devuelve o un cliente o NULL
    }

    // PathVariable es una notacion que nos permite variar la ruta para asi matchearla con el ID del cliente que llegue.
} // ClientController ends


//tambien se puede hacer una inyeccion por construccion que se puede hacer la siguiente forma:
// public ClientRepository (ClientRepository clientRepository){
//        this.clientRepository = clientRepository;
//        //Se llama a la interfaz ClientRepository y se le da el parametro de clientRepository,
//        // luego se vuelve a llamar al clientRepository y se le asigna el parametro clientRepository
//    }