package kz.nursayat.service;

import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Freelancer;
import kz.nursayat.patterns.LoggingService;
import kz.nursayat.cache.CacheManager;
import kz.nursayat.repository.FreelancerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FreelancerService {
    private final FreelancerRepository repository;
    private final LoggingService logger = LoggingService.getInstance();
    private final CacheManager cacheManager = CacheManager.getInstance();
    private final String CACHE_KEY = "freelancers_all";

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
        cacheManager.clearAll();
    }

    public List<Freelancer> getAll() {
        List<Freelancer> cached = (List<Freelancer>) cacheManager.get(CACHE_KEY);
        if (cached != null) {
            logger.log("Returning freelancers from cache");
            return cached;
        }
        List<Freelancer> freelancers = repository.getAll();
        cacheManager.put(CACHE_KEY, freelancers);
        return freelancers;
    }

    public Freelancer getById(int id) {
        Freelancer freelancer = repository.getById(id);
        if (freelancer == null) {
            throw new ResourceNotFoundException("Freelancer not found with id: " + id);
        }
        return freelancer;
    }

    public Freelancer getByEmail(String email) {
        Freelancer freelancer = repository.findByEmail(String.valueOf(email));
        if (freelancer == null) {
            throw new ResourceNotFoundException("Freelancer not found with email: " + email);
        }
        return freelancer;
    }

    public void update(int id, Freelancer freelancer) {
        getById(id);
        freelancer.validate();
        logger.log("Updating freelancer ID: " + id);
        repository.update(id, freelancer);
        cacheManager.clearAll();
    }

    public void delete(int id) {
        getById(id);
        logger.log("Deleting freelancer ID: " + id);
        repository.delete(id);
        cacheManager.clearAll();
    }
}