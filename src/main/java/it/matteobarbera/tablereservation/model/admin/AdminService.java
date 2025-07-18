package it.matteobarbera.tablereservation.model.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void addAdmin(Admin admin){
        adminRepository.save(admin);
    }

    public Set<Admin> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return Set.copyOf(admins);
    }
}
