package kz.nursayat.controller;

import kz.nursayat.model.Client;
import kz.nursayat.patterns.UserFactory;
import kz.nursayat.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // CREATE
    @PostMapping
    public String addClient(@RequestBody Client request) {
        UserFactory.createUser("CLIENT");
        Client client;

        client = new Client.Builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .registeredAt(LocalDate.now())
                .build();

        clientService.create(client);
        return "Client created successfully!";
    }

    // READ ALL
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Client getClientById(@PathVariable int id) {
        return clientService.getById(id);
    }

    // READ BY EMAIL
    @GetMapping("/{email}")
    public Client getClientById(@PathVariable String email) {
        return clientService.getByEmail(email);
    }

    // UPDATE
    @PutMapping("/{id}")
    public String updateClient(@PathVariable int id, @RequestBody Client request) {
        clientService.update(id, request);
        return "Client with ID " + id + " updated successfully!";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable int id) {
        clientService.delete(id);
        return "Client with ID " + id + " deleted successfully!";
    }
}