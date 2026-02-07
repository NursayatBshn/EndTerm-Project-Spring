package kz.nursayat.controller;

import kz.nursayat.model.Bid;
import kz.nursayat.service.BidService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST контроллер для управления ставками (Bids).
 * Реализует требования разделов 1.C (Builder) и 3 (REST API) [cite: 25-31, 44-49].
 */
@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // CREATE: Создание ставки через паттерн Builder
    @PostMapping
    public String addBid(@RequestBody Bid request) {
        // Ставка — сложный объект, объединяющий проект, фрилансера и сумму.
        // Используем Builder для обеспечения гибкости и чистоты кода .
        Bid bid = new Bid.Builder()
                .project(request.getProject())
                .freelancer(request.getFreelancer())
                .bidAmount(request.getBidAmount())
                .bidDate(LocalDate.now()) // Устанавливаем текущую дату ставки
                .build();

        bidService.create(bid);
        return "Bid for " + bid.getBidAmount() + " created successfully using Builder!";
    }

    // READ ALL: Получение всех ставок
    @GetMapping
    public List<Bid> getAllBids() {
        // Название метода совпадает с вашим интерфейсом CrudRepository
        return bidService.getAll();
    }

    // READ BY ID: Получение конкретной ставки
    @GetMapping("/{id}")
    public Bid getBidById(@PathVariable int id) {
        // Название метода совпадает с вашим интерфейсом CrudRepository
        return bidService.getById(id);
    }

    // UPDATE: Обновление суммы ставки
    @PutMapping("/{id}")
    public String updateBid(@PathVariable int id, @RequestBody Bid request) {
        // Сигнатура (int id, T entity) строго по вашему интерфейсу
        bidService.update(id, request);
        return "Bid with ID " + id + " updated successfully!";
    }

    // DELETE: Удаление ставки
    @DeleteMapping("/{id}")
    public String deleteBid(@PathVariable int id) {
        bidService.delete(id);
        return "Bid with ID " + id + " deleted successfully!";
    }
}