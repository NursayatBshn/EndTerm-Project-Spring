package kz.nursayat.controller;

import kz.nursayat.model.Project;
import kz.nursayat.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // CREATE
    @PostMapping
    public String addProject(@RequestBody Project request) {
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

    // READ ALL
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable int id) {
        return projectService.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public String updateProject(@PathVariable int id, @RequestBody Project request) {
        projectService.update(id, request);
        return "Project with ID " + id + " updated successfully!";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable int id) {
        projectService.delete(id);
        return "Project with ID " + id + " deleted successfully!";
    }
}