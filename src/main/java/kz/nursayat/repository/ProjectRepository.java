package kz.nursayat.repository;

import kz.nursayat.exception.DatabaseOperationException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Client;
import kz.nursayat.model.Project;
import kz.nursayat.repository.interfaces.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository implements CrudRepository<Project> {

    private final DataSource dataSource;

    public ProjectRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Project project) {
        String sql = "INSERT INTO projects (client_id, title, budget, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, project.getClient().getId());
            ps.setString(2, project.getTitle());
            ps.setDouble(3, project.getBudget());
            ps.setDate(4, Date.valueOf(project.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create project", e);
        }
    }

    @Override
    public List<Project> getAll() { // Сигнатура по вашему интерфейсу
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.project_id, p.title, p.budget, p.created_at, " +
                "c.client_id, c.first_name, c.last_name, c.email, c.registered_at " +
                "FROM projects p JOIN clients c ON p.client_id = c.client_id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch projects", e);
        }
        return projects;
    }

    @Override
    public Project getById(int id) { // Сигнатура по вашему интерфейсу
        String sql = "SELECT p.project_id, p.title, p.budget, p.created_at, " +
                "c.client_id, c.first_name, c.last_name, c.email, c.registered_at " +
                "FROM projects p JOIN clients c ON p.client_id = c.client_id " +
                "WHERE p.project_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProject(rs);
                }
            }
            throw new ResourceNotFoundException("Project not found with id: " + id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch project", e);
        }
    }

    @Override
    public void update(int id, Project project) { // Два аргумента, как в интерфейсе
        String sql = "UPDATE projects SET title = ?, budget = ? WHERE project_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, project.getTitle());
            ps.setDouble(2, project.getBudget());
            ps.setInt(3, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Project not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update project", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Project not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete project", e);
        }
    }

    private Project mapRowToProject(ResultSet rs) throws SQLException {
        // Сначала собираем клиента, как в вашем репозитории [cite: 16]
        Client client = new Client(
                rs.getInt("client_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getDate("registered_at").toLocalDate()
        );

        // Затем собираем проект через Builder (требование Endterm) [cite: 1, 25-31]
        return new Project.Builder()
                .id(rs.getInt("project_id"))
                .title(rs.getString("title"))
                .budget(rs.getDouble("budget"))
                .createdAt(rs.getDate("created_at").toLocalDate())
                .client(client)
                .build();
    }
}