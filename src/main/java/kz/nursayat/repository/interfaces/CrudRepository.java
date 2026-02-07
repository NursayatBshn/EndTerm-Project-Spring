package kz.nursayat.repository.interfaces;

import java.util.List;

public interface CrudRepository<T> {
    void create(T entity);
    T getById(int id);
    List<T> getAll();
    void update(int id, T entity);
    void delete(int id);
}