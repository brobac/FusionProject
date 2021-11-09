package domain.repository;

import domain.model.Admin;

import java.util.List;

public interface AdminRepository {
    public Admin findByID(long id);
    public List<Admin> findAll();
    public void save(Admin admin);
}