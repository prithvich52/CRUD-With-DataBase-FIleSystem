package com.example.studentdetails.controller;

import com.example.studentdetails.exception.APIException;
import com.example.studentdetails.exception.ResourceNotFoundException;
import com.example.studentdetails.model.Student;
import com.example.studentdetails.service.FileStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileStudentController {

    @Autowired
    private FileStudentService fileStudentService;

    @GetMapping("/public/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = fileStudentService.getAllStudents();
        if (students.isEmpty()) {
            throw new APIException("No students found in the file system");
        }
        return new ResponseEntity<>(students, HttpStatus.FOUND);
    }

    @GetMapping("/public/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = fileStudentService.getStudentById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Student","studentId",id);
        }
        return new ResponseEntity<>(student,HttpStatus.FOUND);
    }

    @GetMapping("/public/students/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable String name) {
        Student student = fileStudentService.getStudentByName(name);
        if (student == null) {
            throw new ResourceNotFoundException("Student","studentName",name);
        }
        return new ResponseEntity<>(student,HttpStatus.FOUND);
    }


    @PostMapping("/public/student")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student createdStudent = fileStudentService.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/public/student/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student student) {
       Student updatedStudent=fileStudentService.updateStudent(studentId,student);
        return new ResponseEntity<>(updatedStudent,HttpStatus.OK);
    }

    @DeleteMapping("/public/student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        fileStudentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
