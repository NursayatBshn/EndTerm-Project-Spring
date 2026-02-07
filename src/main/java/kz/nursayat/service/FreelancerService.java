package kz.nursayat.service;

import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Freelancer;
import kz.nursayat.patterns.LoggingService;
import kz.nursayat.repository.FreelancerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FreelancerService {
    private final FreelancerRepository repository;
    private final LoggingService logger = LoggingService.getInstance();

    public FreelancerService(FreelancerRepository repository) {
        this.repository = repository;
    }

    public void create(Freelancer freelancer) {
        if (freelancer == null) {
            throw new InvalidInputException("Freelancer cannot be null");
        }
        freelancer.validate();

        logger.log("Creating freelancer: " + freelancer.getEmail());
        repository.create(freelancer);
    }

    public List<Freelancer> getAll() {
        return repository.getAll();
    }

    public Freelancer getById(int id) {
        Freelancer freelancer = repository.getById(id);
        if (freelancer == null) {
            throw new ResourceNotFoundException("Freelancer not found with id: " + id);
        }
        return freelancer;
    }

    public void update(int id, Freelancer freelancer) {
        getById(id); // Проверка существования (выбросит 404 если нет)
        freelancer.validate();
        logger.log("Updating freelancer ID: " + id);
        repository.update(id, freelancer);
    }

    public void delete(int id) {
        getById(id); // Проверка существования
        logger.log("Deleting freelancer ID: " + id);
        repository.delete(id);
    }
}