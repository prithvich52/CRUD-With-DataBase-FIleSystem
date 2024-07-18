package com.example.studentdetails.service;

import com.example.studentdetails.payload.StudentDTO;
import com.example.studentdetails.payload.StudentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudentService {
    StudentDTO saveStudent(StudentDTO studentDTO);

    StudentResponse getAllStudents(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    StudentDTO getStudentByStudentId(Long studentId);

    StudentResponse getStudentByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    StudentDTO updateStudent(StudentDTO studentDTO, Long studentId);

    StudentDTO deleteStudent(Long studentId);

    public StudentDTO updateStudentImage(Long studentId, MultipartFile image) throws IOException;
}
;