package kz.nursayat.model;

import kz.nursayat.exception.InvalidInputException;
import kz.nursayat.model.interfaces.Payable;
import kz.nursayat.model.interfaces.Validatable;
import java.time.LocalDate;

public class Project extends BaseEntity implements Validatable<Project>, Payable {
    private String title;
    private double budget;
    private LocalDate createdAt;
    private Client client;

    public Project() {
        super(0);
    }

    public Project(int id, String title, double budget, LocalDate createdAt, Client client) {
        super(id);
        this.title = title;
        this.budget = budget;
        this.createdAt = createdAt;
        this.client = client;
    }

    @Override
    public String getEntityName() {
        return title;
    }

    @Override
    public String getFullDescription() {
        return "Project: " + title + " | Budget: " + budget +
                (client != null ? " | Client: " + client.getEntityName() : "");
    }

    @Override
    public double getAmount() {
        return budget;
    }

    @Override
    public void validate() {
        if (title == null || title.isBlank()) {
            throw new InvalidInputException("Project title cannot be empty");
        }
        if (budget <= 0) {
            throw new InvalidInputException("Project budget must be greater than 0");
        }
        if (client == null || client.getId() <= 0) {
            throw new InvalidInputException("Project must have a valid client");
        }
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public Client getClient() { return client; } // Тот самый метод, который я пропустил
    public void setClient(Client client) { this.client = client; }

    public static class Builder {
        private final Project project = new Project();

        public Builder id(int id) {
            project.setId(id);
            return this;
        }
        public Builder title(String title) {
            project.setTitle(title);
            return this;
        }
        public Builder budget(double budget) {
            project.setBudget(budget);
            return this;
        }
        public Builder createdAt(LocalDate createdAt) {
            project.setCreatedAt(createdAt);
            return this;
        }
        public Builder client(Client client) {
            project.setClient(client);
            return this;
        }
        public Project build() {
            return project;
        }
    }
}