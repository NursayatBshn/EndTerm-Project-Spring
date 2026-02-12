package kz.nursayat.controller;

import kz.nursayat.model.Bid;
import kz.nursayat.service.BidService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // CREATE
    @PostMapping
    public String addBid(@RequestBody Bid request) {
        Bid bid = new Bid.Builder()
                .project(request.getProject())
                .freelancer(request.getFreelancer())
                .bidAmount(request.getBidAmount())
                .bidDate(LocalDate.now())
                .build();

        bidService.create(bid);
        return "Bid for " + bid.getBidAmount() + " created successfully using Builder!";
    }

    // READ ALL
    @GetMapping
    public List<Bid> getAllBids() {
        return bidService.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Bid getBidById(@PathVariable int id) {
        return bidService.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public String updateBid(@PathVariable int id, @RequestBody Bid request) {
        bidService.update(id, request);
        return "Bid with ID " + id + " updated successfully!";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteBid(@PathVariable int id) {
        bidService.delete(id);
        return "Bid with ID " + id + " deleted successfully!";
    }
}