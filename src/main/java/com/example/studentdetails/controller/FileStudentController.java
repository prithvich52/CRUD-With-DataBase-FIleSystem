package com.example.studentdetails.controller;

import com.example.studentdetails.exception.APIException;
import com.example.studentdetails.exception.ResourceNotFoundException;
import com.example.studentdetails.model.Student;
import com.example.studentdetails.service.FileStudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileStudentController {

    private static final Logger logger = LoggerFactory.getLogger(FileStudentController.class);

    @Autowired
    private FileStudentService fileStudentService;

    @Operation(summary = "get All Student")
    @GetMapping("/public/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        logger.info("FileStudentController implementation : getAllStudents() method ");
        List<Student> students = fileStudentService.getAllStudents();
        if (students.isEmpty()) {
            throw new APIException("No students found in the file system");
        }
        return new ResponseEntity<>(students, HttpStatus.FOUND);
    }
    @Operation(summary = "get Student by Id")
    @GetMapping("/public/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        logger.info("FileStudentController implementation : getStudentById() method ");
        Student student = fileStudentService.getStudentById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Student","studentId",id);
        }
        return new ResponseEntity<>(student,HttpStatus.FOUND);
    }

    @Operation(summary = "get Student by name")
    @GetMapping("/public/students/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable String name) {
        logger.info("FileStudentController implementation : getStudentByName() method ");
        Student student = fileStudentService.getStudentByName(name);
        if (student == null) {
            throw new ResourceNotFoundException("Student","studentName",name);
        }
        return new ResponseEntity<>(student,HttpStatus.FOUND);
    }

    @Operation(summary = "Save Student")
    @PostMapping("/public/student")
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
        logger.info("FileStudentController implementation : addStudent() method ");
        Student createdStudent = fileStudentService.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @Operation(summary = "Update Student")
    @PutMapping("/public/student/{studentId}")
    public ResponseEntity<Student> updateStudent(@Valid @PathVariable Long studentId, @RequestBody Student student) {
        logger.info("FileStudentController implementation : updateStudent() method ");
       Student updatedStudent=fileStudentService.updateStudent(studentId,student);
        return new ResponseEntity<>(updatedStudent,HttpStatus.OK);
    }

    @Operation(summary = "Delete Student")
    @DeleteMapping("/public/student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        logger.info("FileStudentController implementation : deleteStudent() method ");
        fileStudentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
