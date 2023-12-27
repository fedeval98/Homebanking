package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController // Controlador del tipo REST (recibe metodos HTTP (GET, POST, PUT, PATCH, DELETE) y devuelve JSON)
@RequestMapping("/api") // realiza un mapeo de nuestra solicitud (asocia nuestra peticion a un endPoint)
public class ClientController {

    // Inyeccion de dependencia

    @Autowired //Se encarga de realizar la inyeccion por construccion pero de forma automatica
               // hace algo SIMILAR a instanciar la clase del objeto que ejecuta
    private ClientRepository clientRepository;

    @RequestMapping ("/clients")
    public List<ClientDTO> getAllClient(){
        return clientRepository.findAll() //busco todos los clientes en mi repositorio
                .stream()// convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                // o terminales (count, collect, forEach, etc)
                .map(ClientDTO::new) // transformo cada client en un objeto DTO
                .collect(Collectors.toList()); //recopilo todos los objetos DTO y los transforma a una lista.
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null); // aca vamos a buscar por ID pero nos devuelve o un cliente o NULL
    }

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private AccountController accountController;

    @PostMapping("/clients")
    public ResponseEntity<String> createClient(
           @RequestParam String firstName,
           @RequestParam String lastName,
           @RequestParam String email,
           @RequestParam String password)
    {
        if(firstName.isBlank()){
            return new ResponseEntity<>("Name can't be blank", HttpStatus.FORBIDDEN);
        }
        if(lastName.isBlank()){
            return new ResponseEntity<>("Last name can't be blank", HttpStatus.FORBIDDEN);
        }
        if(email.isBlank()){
            return new ResponseEntity<>("Email can't be blank", HttpStatus.FORBIDDEN);
        }
        if(password.isBlank()){
            return new ResponseEntity<>("Password can't be blank", HttpStatus.FORBIDDEN);
        }

        if(clientRepository.existsByEmail(email)){
            return new ResponseEntity<>("Email already on use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName,lastName,email,passwordEncoder.encode(password));
        clientRepository.save(client);

        ResponseEntity<String> accountCreationResult = accountController.createAccountFirst(client);

        if (accountCreationResult.getStatusCode() != HttpStatus.CREATED) {
            // Manejar el caso en que la creación de la cuenta falla
            return new ResponseEntity<>("Failed to create client account", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Cliente y cuenta creados con éxito
        return new ResponseEntity<>("Client and account created", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ResponseEntity<Object> getOneClient (Authentication authentication){


        Client client = clientRepository.findByEmail(authentication.getName());

        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }
    // PathVariable es una notacion que nos permite variar la ruta para asi matchearla con el ID del cliente que llegue.
} // ClientController ends


//tambien se puede hacer una inyeccion por construccion que se puede hacer la siguiente forma:
// public ClientRepository (ClientRepository clientRepository){
//        this.clientRepository = clientRepository;
//        //Se llama a la interfaz ClientRepository y se le da el parametro de clientRepository,
//        // luego se vuelve a llamar al clientRepository y se le asigna el parametro clientRepository
//    }