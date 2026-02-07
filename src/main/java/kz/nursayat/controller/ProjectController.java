package kz.nursayat.controller;

import kz.nursayat.model.Project;
import kz.nursayat.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST контроллер для управления проектами.
 * Реализует требования раздела 3 (Spring Boot API) и 1.C (Builder Pattern) [cite: 3, 44-49].
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // CREATE: Создание проекта через паттерн Builder
    @PostMapping
    public String addProject(@RequestBody Project request) {
        // Использование Singleton для записи лога о создании проекта
        kz.nursayat.patterns.LoggingService.getInstance().log("Attempting to create a new project: " + request.getTitle());

        Project project = new Project.Builder()
                .title(request.getTitle())
                .budget(request.getBudget())
                .client(request.getClient())
                .createdAt(LocalDate.now())
                .build();

        projectService.create(project);
        return "Project created successfully!";
    }

    // READ ALL: Получение всех проектов
    @GetMapping
    public List<Project> getAllProjects() {
        // Название метода строго соответствует вашему интерфейсу CrudRepository
        return projectService.getAll();
    }

    // READ BY ID: Получение конкретного проекта
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable int id) {
        // Название метода строго соответствует вашему интерфейсу CrudRepository
        return projectService.getById(id);
    }

    // UPDATE: Обновление существующего проекта
    @PutMapping("/{id}")
    public String updateProject(@PathVariable int id, @RequestBody Project request) {
        // Сигнатура метода строго соответствует вашему интерфейсу (int id, T entity)
        projectService.update(id, request);
        return "Project with ID " + id + " updated successfully!";
    }

    // DELETE: Удаление проекта
    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable int id) {
        projectService.delete(id);
        return "Project with ID " + id + " deleted successfully!";
    }
}