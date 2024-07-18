package com.example.studentdetails.controller;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.studentdetails.config.AppConstants;
import com.example.studentdetails.payload.StudentDTO;
import com.example.studentdetails.payload.StudentResponse;
import com.example.studentdetails.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Save Student")
    @PostMapping("/public/students")
    public ResponseEntity<StudentDTO> saveStudent(@Valid @RequestBody StudentDTO studentDTO){
        logger.info("StudentController implementation : savaStudent() method ");
        StudentDTO savedstudentDTO= studentService.saveStudent(studentDTO);
        return new ResponseEntity<>(savedstudentDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all students")
    @GetMapping ("/public/students")
    public ResponseEntity<StudentResponse> getAllStudents(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STUDENTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        logger.info("StudentController implementation : getALlStudents() method ");
        StudentResponse students= studentService.getAllStudents(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(students, HttpStatus.FOUND);
    }

    @Operation(summary = "Get Students by studentId")
    @GetMapping ("/public/student/{studentId}")
    public ResponseEntity<StudentDTO> getStudentByName(@PathVariable Long studentId){
        logger.info("StudentController implementation : getStudentByName() method ");
        StudentDTO studentDTO= studentService.getStudentByStudentId(studentId);
        return new ResponseEntity<>(studentDTO, HttpStatus.FOUND);
    }

    @Operation(summary = "Get List of student by keyword")
    @GetMapping ("/public/students/{keyword}")
    public ResponseEntity<StudentResponse> findByNameLikeIgnoreCase(@PathVariable String keyword,
             @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
             @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STUDENTS_BY, required = false) String sortBy,
             @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        logger.info("StudentController implementation : findByNameLikeIgnoreCase() method ");
        StudentResponse students= studentService.getStudentByKeyword(keyword,pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(students, HttpStatus.FOUND);
    }

    @Operation(summary = "Update the student")
    @PutMapping("/public/student/{studentId}")
    public ResponseEntity<StudentDTO> updateStudent(@Valid @RequestBody StudentDTO studentDTO,@PathVariable Long studentId){
        logger.info("StudentController implementation : updateStudent() method");
        StudentDTO savedstudentDTO= studentService.updateStudent(studentDTO,studentId);
        return new ResponseEntity<>(savedstudentDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete Student by studentId")
    @DeleteMapping("/public/student/{studentId}")
    public ResponseEntity<StudentDTO> deleteStudent(@PathVariable Long studentId){
        logger.info("StudentController implementation : deleteStudent() method");
        StudentDTO savedstudentDTO= studentService.deleteStudent(studentId);
        return new ResponseEntity<>(savedstudentDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update student image ")
    @PutMapping("/student/{studentId}/image")
    public ResponseEntity<StudentDTO> updateStudentImage(@PathVariable Long studentId,
                                                         @RequestParam("image") MultipartFile image) throws IOException, IOException {
        logger.info("StudentController implementation : updateProductImage() method");
        StudentDTO updatedProduct = studentService.updateStudentImage(studentId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
