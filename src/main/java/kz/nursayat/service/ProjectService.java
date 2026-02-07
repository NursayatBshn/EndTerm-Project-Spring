package kz.nursayat.service;

import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Project;
import kz.nursayat.patterns.LoggingService;
import kz.nursayat.repository.ClientRepository;
import kz.nursayat.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository; // Добавляем это поле
    private final LoggingService logger = LoggingService.getInstance(); // Singleton [cite: 17]

    // Инжектим оба репозитория через конструктор
    public ProjectService(ProjectRepository projectRepository, ClientRepository clientRepository) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
    }

    public void create(Project project) {
        // Проверяем, существует ли клиент, прежде чем создавать проект
        if (clientRepository.getById(project.getClient().getId()) == null) {
            throw new ResourceNotFoundException("Cannot create project: Client not found");
        }

        project.validate();
        logger.log("Creating project: " + project.getTitle());
        projectRepository.create(project);
    }

    public List<Project> getAll() {
        return projectRepository.getAll();
    }

    public Project getById(int id) {
        Project project = projectRepository.getById(id);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        return project;
    }

    public void update(int id, Project project) {
        getById(id); // Проверяем существование перед обновлением
        project.validate();
        logger.log("Updating project ID: " + id);
        projectRepository.update(id, project);
    }

    public void delete(int id) {
        getById(id); // Проверяем существование перед удалением
        logger.log("Deleting project ID: " + id);
        projectRepository.delete(id);
    }
}