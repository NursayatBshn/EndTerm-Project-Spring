package kz.nursayat.service;

import org.springframework.stereotype.Service;
import kz.nursayat.exception.DuplicateResourceException;
import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.exception.ResourceNotFoundException; // Добавлено
import kz.nursayat.model.Bid;
import kz.nursayat.repository.BidRepository;
import kz.nursayat.patterns.LoggingService; // Наш Singleton

import java.util.List;

@Service
public class BidService {
    private final BidRepository repository;
    // Получаем экземпляр синглтона для соблюдения требований раздела 1.A
    private final LoggingService logger = LoggingService.getInstance();

    public BidService(BidRepository repository) {
        this.repository = repository;
    }

    public void create(Bid bid) {
        if (bid == null) {
            throw new InvalidInputException("Bid cannot be null");
        }

        bid.validate(); // SOLID: Валидация перед бизнес-логикой [cite: 61]

        int projectId = bid.getProject().getId();
        int freelancerId = bid.getFreelancer().getId();

        logger.log("Checking for duplicate bid for project " + projectId); // Используем Singleton

        if (repository.existsByProjectAndFreelancer(projectId, freelancerId)) {
            throw new DuplicateResourceException("Freelancer has already placed a bid for this project");
        }

        repository.create(bid);
        logger.log("Bid created successfully for project " + projectId);
    }

    public List<Bid> getAll() {
        return repository.getAll();
    }

    public Bid getById(int id) {
        Bid bid = repository.getById(id);
        if (bid == null) {
            // Критически важно для корректных REST ответов
            throw new ResourceNotFoundException("Bid not found with id: " + id);
        }
        return bid;
    }

    public void update(int id, Bid bid) {
        getById(id); // Сначала проверяем существование, чтобы выкинуть 404 если надо
        bid.validate();
        logger.log("Updating bid with id: " + id);
        repository.update(id, bid);
    }

    public void delete(int id) {
        getById(id); // Проверяем существование перед удалением [cite: 49]
        logger.log("Deleting bid with id: " + id);
        repository.delete(id);
    }
}