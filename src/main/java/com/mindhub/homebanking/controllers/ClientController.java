package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.dto.newClient;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Controlador del tipo REST (recibe metodos HTTP (GET, POST, PUT, PATCH, DELETE) y devuelve JSON)
@RequestMapping("/api") // realiza un mapeo de nuestra solicitud (asocia nuestra peticion a un endPoint)
public class ClientController {

    // Inyeccion de dependencia

    @Autowired //Se encarga de realizar la inyeccion por construccion pero de forma automatica
               // hace algo SIMILAR a instanciar la clase del objeto que ejecuta
    private ClientService clientService;

    @RequestMapping("/clients")
    public List<ClientDTO> getAllClient(){
        return clientService.getAllClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id) {
        ClientDTO clientDTO = clientService.getClientById(id);
        if (clientDTO != null) {
            return new ResponseEntity<>(clientDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private AccountController accountController;

    @PostMapping("/clients")
    public ResponseEntity<String> createClient(@RequestBody newClient newClient){
        if(newClient.getFirstName().isBlank()){
            return new ResponseEntity<>("Name can't be blank", HttpStatus.FORBIDDEN);
        }
        if(newClient.getLastName().isBlank()){
            return new ResponseEntity<>("Last name can't be blank", HttpStatus.FORBIDDEN);
        }
        if(newClient.getEmail().isBlank()){
            return new ResponseEntity<>("Email can't be blank", HttpStatus.FORBIDDEN);
        }
        if(newClient.getPassword().isBlank()){
            return new ResponseEntity<>("Password can't be blank", HttpStatus.FORBIDDEN);
        }

        if(clientService.existsByEmail(newClient.getEmail())){
            return new ResponseEntity<>("Email already on use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(newClient.getFirstName(),newClient.getLastName(),newClient.getEmail(),passwordEncoder.encode(newClient.getPassword()));

        ResponseEntity<String> accountCreationResult = accountController.createAccountFirst(newClient.getType(), client);

        if (accountCreationResult.getStatusCode() != HttpStatus.CREATED) {
            // Maneja el caso en que la creación de la cuenta falla
            return new ResponseEntity<>("Failed to create client account", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        clientService.saveClient(client);

        return new ResponseEntity<>("Client and account created", HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current")
    public ResponseEntity<Object> getOneClient(Authentication authentication) {
        ClientDTO clientDTO = clientService.getAuthClientDTO(authentication.getName());
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