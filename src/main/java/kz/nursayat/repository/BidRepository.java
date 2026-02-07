package kz.nursayat.repository;

import kz.nursayat.exception.DatabaseOperationException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Bid;
import kz.nursayat.model.Freelancer;
import kz.nursayat.model.Project;
import kz.nursayat.repository.interfaces.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BidRepository implements CrudRepository<Bid> {

    private final DataSource dataSource;

    public BidRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Bid bid) {
        String sql = "INSERT INTO bids (project_id, freelancer_id, bid_amount, bid_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bid.getProject().getId());
            ps.setInt(2, bid.getFreelancer().getId());
            ps.setDouble(3, bid.getBidAmount());
            ps.setDate(4, Date.valueOf(bid.getBidDate() != null ? bid.getBidDate() : java.time.LocalDate.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create bid", e);
        }
    }

    @Override
    public List<Bid> getAll() {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT b.bid_id, b.bid_amount, b.bid_date, " +
                "p.project_id, p.title, p.budget, p.created_at, " +
                "f.freelancer_id, f.first_name, f.last_name, f.email, f.rating, f.joined_at, f.phone " +
                "FROM bids b " +
                "JOIN projects p ON b.project_id = p.project_id " +
                "JOIN freelancers f ON b.freelancer_id = f.freelancer_id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                bids.add(mapRowToBid(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch bids", e);
        }
        return bids;
    }

    @Override
    public Bid getById(int id) {
        String sql = "SELECT b.bid_id, b.bid_amount, b.bid_date, " +
                "p.project_id, p.title, p.budget, p.created_at, " +
                "f.freelancer_id, f.first_name, f.last_name, f.email, f.rating, f.joined_at, f.phone " +
                "FROM bids b " +
                "JOIN projects p ON b.project_id = p.project_id " +
                "JOIN freelancers f ON b.freelancer_id = f.freelancer_id " +
                "WHERE b.bid_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBid(rs);
                }
            }
            throw new ResourceNotFoundException("Bid not found with id: " + id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch bid", e);
        }
    }

    @Override
    public void update(int id, Bid bid) {
        String sql = "UPDATE bids SET bid_amount = ? WHERE bid_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, bid.getBidAmount());
            ps.setInt(2, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Bid not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update bid", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM bids WHERE bid_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Bid not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete bid", e);
        }
    }

    public boolean existsByProjectAndFreelancer(int projectId, int freelancerId) {
        String sql = "SELECT COUNT(*) FROM bids WHERE project_id = ? AND freelancer_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setInt(2, freelancerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error checking bid existence", e);
        }
    }
    private Bid mapRowToBid(ResultSet rs) throws SQLException {
        // Собираем связанный проект через его Builder
        Project project = new Project.Builder()
                .id(rs.getInt("project_id"))
                .title(rs.getString("title"))
                .budget(rs.getDouble("budget"))
                .createdAt(rs.getDate("created_at").toLocalDate())
                .build();

        // Собираем связанного фрилансера через его конструктор/будущий Builder
        Freelancer freelancer = new Freelancer(
                rs.getInt("freelancer_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getDouble("rating"),
                rs.getDate("joined_at").toLocalDate(),
                rs.getString("phone")
        );

        // Собираем сам Bid через Builder
        return new Bid.Builder()
                .id(rs.getInt("bid_id"))
                .bidAmount(rs.getDouble("bid_amount"))
                .bidDate(rs.getDate("bid_date").toLocalDate())
                .project(project)
                .freelancer(freelancer)
                .build();
    }
}