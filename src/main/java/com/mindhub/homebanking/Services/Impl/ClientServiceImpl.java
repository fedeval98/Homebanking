package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getAllClientsDTO() {
       return getAllClients().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Override
    public Client getAuthClient(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public ClientDTO getAuthClientDTO(String email) {
        return new ClientDTO(getAuthClient(email));
    }

    @Override
    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @Override
    public List <Client> getAllClients(){
        return clientRepository.findAll();
    }

    @Override
    public boolean existsByEmail(String email){
        return clientRepository.existsByEmail(email);
    }

    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }

}