package kz.nursayat.controller;

import kz.nursayat.model.Freelancer;
import kz.nursayat.patterns.UserFactory;
import kz.nursayat.service.FreelancerService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST контроллер для управления фрилансерами.
 * Реализует требования раздела 3: GET, POST, PUT, DELETE .
 */
@RestController
@RequestMapping("/api/freelancers")
public class FreelancerController {

    private final FreelancerService freelancerService;

    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    // CREATE: Создание нового фрилансера
    @PostMapping
    public String addFreelancer(@RequestBody Freelancer request) {
        // 1. Используем Factory для создания базового объекта [cite: 18-24]
        Freelancer freelancer = (Freelancer) UserFactory.createUser("FREELANCER");

        // 2. Используем Builder для наполнения данными из JSON-запроса [cite: 25-31]
        freelancer = new Freelancer.Builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .rating(0.0) // Начальный рейтинг по умолчанию
                .joinedAt(LocalDate.now()) // Дата регистрации сегодня
                .build();

        freelancerService.create(freelancer);
        return "Freelancer created successfully using Factory and Builder patterns!";
    }

    // READ: Получение списка всех фрилансеров
    @GetMapping
    public List<Freelancer> getAllFreelancers() {
        return freelancerService.getAll(); // Использует метод getAll из CrudRepository
    }

    // READ: Получение фрилансера по ID
    @GetMapping("/{id}")
    public Freelancer getFreelancerById(@PathVariable int id) {
        return freelancerService.getById(id); // Использует метод getById из CrudRepository
    }

    // UPDATE: Обновление данных фрилансера
    @PutMapping("/{id}")
    public String updateFreelancer(@PathVariable int id, @RequestBody Freelancer request) {
        // Обновление существующего объекта не требует Factory [cite: 47]
        freelancerService.update(id, request);
        return "Freelancer with ID " + id + " updated successfully!";
    }

    // DELETE: Удаление фрилансера
    @DeleteMapping("/{id}")
    public String deleteFreelancer(@PathVariable int id) {
        // Удаление записи по ID [cite: 49]
        freelancerService.delete(id);
        return "Freelancer with ID " + id + " deleted successfully!";
    }
}