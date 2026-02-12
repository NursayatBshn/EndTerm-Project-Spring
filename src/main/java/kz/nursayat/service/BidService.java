package kz.nursayat.service;

import org.springframework.stereotype.Service;
import kz.nursayat.exception.DuplicateResourceException;
import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Bid;
import kz.nursayat.repository.BidRepository;
import kz.nursayat.patterns.LoggingService;
import kz.nursayat.cache.CacheManager;

import java.util.List;

@Service
public class BidService {
    private final BidRepository repository;
    private final LoggingService logger = LoggingService.getInstance();
    private final CacheManager cacheManager = CacheManager.getInstance();
    private final String CACHE_KEY = "bids_all";

    public BidService(BidRepository repository) {
        this.repository = repository;
    }

    public void create(Bid bid) {
        if (bid == null) {
            throw new InvalidInputException("Bid cannot be null");
        }

        bid.validate();

        int projectId = bid.getProject().getId();
        int freelancerId = bid.getFreelancer().getId();

        logger.log("Checking for duplicate bid for project " + projectId);

        if (repository.existsByProjectAndFreelancer(projectId, freelancerId)) {
            throw new DuplicateResourceException("Freelancer has already placed a bid for this project");
        }

        repository.create(bid);
        logger.log("Bid created successfully for project " + projectId);
        cacheManager.clearAll();
    }

    public List<Bid> getAll() {
        List<Bid> cached = (List<Bid>) cacheManager.get(CACHE_KEY);
        if (cached != null)  {
            logger.log("Returning bids from cache");
            return cached;
        }

        logger.log("Cache miss. Fetching bids from database");
        List<Bid> bids = repository.getAll();

        cacheManager.put(CACHE_KEY, bids);
        return bids;
    }

    public Bid getById(int id) {
        Bid bid = repository.getById(id);
        if (bid == null) {
            throw new ResourceNotFoundException("Bid not found with id: " + id);
        }
        return bid;
    }

    public void update(int id, Bid bid) {
        getById(id);
        bid.validate();
        logger.log("Updating bid with id: " + id);
        repository.update(id, bid);
        cacheManager.clearAll();
    }

    public void delete(int id) {
        getById(id);
        logger.log("Deleting bid with id: " + id);
        repository.delete(id);
        cacheManager.clearAll();
    }
}