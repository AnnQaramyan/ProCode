package com.example.procodetask.repository;


import com.example.procodetask.model.Authority.Authority;
import com.example.procodetask.model.Authority.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority getByName(AuthorityType name);
}
