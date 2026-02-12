package kz.nursayat.service;

import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Client;
import kz.nursayat.cache.CacheManager;
import kz.nursayat.patterns.LoggingService;
import kz.nursayat.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository repository;
    private final LoggingService logger = LoggingService.getInstance();
    private final CacheManager cacheManager = CacheManager.getInstance();
    private final String CACHE_KEY = "clients_all";

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
        cacheManager.clearAll();
    }

    public List<Client> getAll() {
        List<Client> cached = (List<Client>) cacheManager.get(CACHE_KEY);
        if (cached != null) {
            logger.log("Returning clients from cache");
            return cached;
        }
        List<Client> clients = repository.getAll();
        cacheManager.put(CACHE_KEY, clients);
        return clients;
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
        cacheManager.clearAll();
    }

    public void delete(int id) {
        getById(id);
        logger.log("Deleting client ID: " + id);
        repository.delete(id);
        cacheManager.clear(CACHE_KEY);
    }
}