package kz.nursayat.controller;

import kz.nursayat.model.Freelancer;
import kz.nursayat.patterns.UserFactory;
import kz.nursayat.service.FreelancerService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/freelancers")
public class FreelancerController {

    private final FreelancerService freelancerService;

    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @PostMapping
    public String addFreelancer(@RequestBody Freelancer request) {
        UserFactory.createUser("FREELANCER");
        Freelancer freelancer;

        freelancer = new Freelancer.Builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .rating(0.0)
                .joinedAt(LocalDate.now())
                .build();

        freelancerService.create(freelancer);
        return "Freelancer created successfully using Factory and Builder patterns!";
    }

    // READ
    @GetMapping
    public List<Freelancer> getAllFreelancers() {
        return freelancerService.getAll();
    }

    // READ
    @GetMapping("/{id}")
    public Freelancer getFreelancerById(@PathVariable int id) {
        return freelancerService.getById(id);
    }

    // READ
    @GetMapping("/{email}")
    public Freelancer getFreelancerByEmail(@PathVariable String email) {
        return freelancerService.getByEmail(email);
    }

    // UPDATE
    @PutMapping("/{id}")
    public String updateFreelancer(@PathVariable int id, @RequestBody Freelancer request) {
        freelancerService.update(id, request);
        return "Freelancer with ID " + id + " updated successfully!";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteFreelancer(@PathVariable int id) {
        freelancerService.delete(id);
        return "Freelancer with ID " + id + " deleted successfully!";
    }
}