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
    // ИСПРАВЛЕНИЕ 1: Используем конкретный репозиторий, чтобы был доступен метод findByEmail
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
        // ИСПРАВЛЕНИЕ 2: Теперь этот вызов работает корректно
        Client client = repository.findByEmail(email);
        if (client == null) {
            throw new ResourceNotFoundException("Client not found with email: " + email);
        }
        return client;
    }

    public void update(int id, Client client) {
        getById(id); // Проверка существования
        client.validate();
        logger.log("Updating client ID: " + id);
        repository.update(id, client);
    }

    public void delete(int id) {
        getById(id); // Проверка существования
        logger.log("Deleting client ID: " + id);
        repository.delete(id);
    }
}