package com.example.studentdetails.repository;

import com.example.studentdetails.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    Page<Student> findByNameLikeIgnoreCase(String keyword, Pageable pageDetails);

    Optional<Student> findStudentByEmail(String email);
}
