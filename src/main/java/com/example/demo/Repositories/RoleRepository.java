package com.example.demo.Repositories;

import com.example.demo.Entities.Erole;
import com.example.demo.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRole(Erole role);
}
