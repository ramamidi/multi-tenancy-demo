package com.multitenant.demo.database.repositories;

import com.multitenant.demo.database.entities.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Resource, Long> {
}
