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

    // CREATE (Используем Factory + Builder)
    @PostMapping
    public String addClient(@RequestBody Client request) {
        // Фабрика создает пустой объект нужного типа
        Client client = (Client) UserFactory.createUser("CLIENT");

        // Builder наполняет его данными [cite: 1, 27-33]
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

    // UPDATE (Обновление)
    @PutMapping("/{id}")
    public String updateClient(@PathVariable int id, @RequestBody Client request) {
        // Для обновления мы передаем ID и объект с новыми данными [cite: 1, 19]
        clientService.update(id, request);
        return "Client with ID " + id + " updated successfully!";
    }

    // DELETE (Удаление)
    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable int id) {
        // Удаление происходит по ID [cite: 1, 20]
        clientService.delete(id);
        return "Client with ID " + id + " deleted successfully!";
    }
}