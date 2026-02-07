package kz.nursayat.model;

import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.model.interfaces.Payable;
import kz.nursayat.model.interfaces.Validatable;
import java.time.LocalDate;

public class Bid extends BaseEntity implements Validatable<Bid>, Payable {
    private Project project;
    private Freelancer freelancer;
    private double bidAmount;
    private LocalDate bidDate;

    // Конструктор по умолчанию для Spring и Builder
    public Bid() {
        super(0);
    }

    // Оригинальный конструктор
    public Bid(int id, Project project, Freelancer freelancer, double bidAmount, LocalDate bidDate) {
        super(id);
        this.project = project;
        this.freelancer = freelancer;
        this.bidAmount = bidAmount;
        this.bidDate = bidDate;
    }

    @Override
    public String getEntityName() {
        // Используем метод родителя у связанного проекта
        return "Bid for " + (project != null ? project.getEntityName() : "Unknown Project");
    }

    @Override
    public String getFullDescription() {
        return "Bid: " + bidAmount +
                (freelancer != null ? " by " + freelancer.getEntityName() : "") +
                (project != null ? " on " + project.getEntityName() : "");
    }

    @Override
    public double getAmount() {
        return bidAmount;
    }

    @Override
    public void validate() {
        if (project == null || project.getId() <= 0) {
            throw new InvalidInputException("Bid must be associated with a valid project");
        }
        if (freelancer == null || freelancer.getId() <= 0) {
            throw new InvalidInputException("Bid must be associated with a valid freelancer");
        }
        if (bidAmount <= 0) {
            throw new InvalidInputException("Bid amount must be greater than 0");
        }
    }

    // Геттеры и сеттеры
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public Freelancer getFreelancer() { return freelancer; }
    public void setFreelancer(Freelancer freelancer) { this.freelancer = freelancer; }

    public double getBidAmount() { return bidAmount; }
    public void setBidAmount(double bidAmount) { this.bidAmount = bidAmount; }

    public LocalDate getBidDate() { return bidDate; }
    public void setBidDate(LocalDate bidDate) { this.bidDate = bidDate; }

    // --- ПАТТЕРН BUILDER ДЛЯ ENDTERM ---
    public static class Builder {
        private final Bid bid = new Bid();

        public Builder id(int id) {
            bid.setId(id);
            return this;
        }
        public Builder project(Project project) {
            bid.setProject(project);
            return this;
        }
        public Builder freelancer(Freelancer freelancer) {
            bid.setFreelancer(freelancer);
            return this;
        }
        public Builder bidAmount(double bidAmount) {
            bid.setBidAmount(bidAmount);
            return this;
        }
        public Builder bidDate(LocalDate bidDate) {
            bid.setBidDate(bidDate);
            return this;
        }
        public Bid build() {
            return bid;
        }
    }
}