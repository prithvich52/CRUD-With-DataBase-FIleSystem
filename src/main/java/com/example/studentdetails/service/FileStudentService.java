package com.example.studentdetails.service;


import com.example.studentdetails.model.Student;
import java.util.List;

//TO Create CRUD Operation using File System
public interface FileStudentService {

    public Student addStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    public Student getStudentByName(String name);

    Student updateStudent(Long id, Student updatedStudent) ;

    void deleteStudent(Long id);


}
