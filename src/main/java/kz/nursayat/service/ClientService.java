package kz.nursayat.service;

import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Client;
import kz.nursayat.patterns.LoggingService;
import kz.nursayat.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository repository;
    private final LoggingService logger = LoggingService.getInstance();

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public void create(Client client) {
        if (client == null) {
            throw new InvalidInputException("Client cannot be null");
        }
        client.validate();

        logger.log("Creating new client: " + client.getEmail());
        repository.create(client);
    }

    public List<Client> getAll() {
        return repository.getAll();
    }

    public Client getById(int id) {
        Client client = repository.getById(id);
        if (client == null) {
            throw new ResourceNotFoundException("Client not found with id: " + id);
        }
        return client;
    }

    public Client getByEmail(String email) {
        logger.log("Searching for client with email: " + email);
        Client client = repository.findByEmail(email);
        if (client == null) {
            throw new ResourceNotFoundException("Client not found with email: " + email);
        }
        return client;
    }

    public void update(int id, Client client) {
        getById(id);
        client.validate();
        logger.log("Updating client ID: " + id);
        repository.update(id, client);
    }

    public void delete(int id) {
        getById(id);
        logger.log("Deleting client ID: " + id);
        repository.delete(id);
    }
}