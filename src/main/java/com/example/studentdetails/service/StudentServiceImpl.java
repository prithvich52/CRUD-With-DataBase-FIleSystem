package com.example.studentdetails.service;
import com.example.studentdetails.exception.APIException;
import com.example.studentdetails.exception.ResourceNotFoundException;
import com.example.studentdetails.model.Student;
import com.example.studentdetails.payload.StudentDTO;
import com.example.studentdetails.payload.StudentResponse;
import com.example.studentdetails.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger =LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UpdateImageService updateImageService;

    @Value("${project.image}")
    private String path;
    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        logger.info("StudentServiceImpl implementation : saveStudent() method ");
        Student student=modelMapper.map(studentDTO,Student.class);
       Optional<Student> existingStudent = studentRepository.findStudentByEmail(student.getEmail());
        if(existingStudent.isPresent()){
            throw new APIException("Student with: "+studentDTO.getEmail() + " already exist!!");
        }
         student.setImage("default.png");
        Student saveStudent=studentRepository.save(student);
        return modelMapper.map(saveStudent, StudentDTO.class);
    }

    @Override
    public StudentResponse getAllStudents(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        logger.info("StudentServiceImpl implementation : getAllStudents() method");
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Student> pageProducts = studentRepository.findAll(pageDetails);
        List<Student> students = pageProducts.getContent();
        if(students.isEmpty()){
            throw new APIException("Student not added till now..!!");
        }
        List<StudentDTO> studentDTOS = students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .toList();
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setStudents(studentDTOS);
        studentResponse.setPageNumber(pageProducts.getNumber());
        studentResponse.setPageSize(pageProducts.getSize());
        studentResponse.setTotalElements(pageProducts.getTotalElements());
        studentResponse.setTotalPages(pageProducts.getTotalPages());
        studentResponse.setLastPage(pageProducts.isLast());
            return studentResponse;
    }

    @Override
    public StudentDTO getStudentByStudentId(Long studentId){
        logger.info("StudentServiceImpl implementation : getStudentByStudentId() method");
        Student existingStudent=studentRepository.findById(studentId).orElseThrow(()->
                new ResourceNotFoundException("Student","studentName",studentId));
        return modelMapper.map(existingStudent, StudentDTO.class);
    }

    @Override
    public StudentResponse getStudentByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        logger.info("StudentServiceImpl implementation : getStudentByKeyword() method");
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Student> pageProducts = studentRepository.findByNameLikeIgnoreCase('%' + keyword + '%', pageDetails);
        if(pageProducts.isEmpty()){
            throw new ResourceNotFoundException("Student","studentName",keyword);
        }
        List<StudentDTO> studentDTOS = pageProducts.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .toList();
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setStudents(studentDTOS);
        studentResponse.setPageNumber(pageProducts.getNumber());
        studentResponse.setPageSize(pageProducts.getSize());
        studentResponse.setTotalElements(pageProducts.getTotalElements());
        studentResponse.setTotalPages(pageProducts.getTotalPages());
        studentResponse.setLastPage(pageProducts.isLast());
        return studentResponse;
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO, Long studentId) {
        logger.info("StudentServiceImpl implementation : updateStudent() method ");
        Student existingStudent=studentRepository.findById(studentId).orElseThrow(()->new ResourceNotFoundException
                ("Student","studentName",studentId));
        Student student=modelMapper.map(studentDTO,Student.class);
        existingStudent.setName(student.getName());
        existingStudent.setImage("default.png");
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhoneNumber(student.getPhoneNumber());
        Student savedStudent=studentRepository.save(existingStudent);
        return modelMapper.map(savedStudent, StudentDTO.class);
    }

    @Override
    public StudentDTO deleteStudent(Long studentId) {
        logger.info("StudentServiceImpl implementation : deleteStudent() method" );
        Student existingStudent=studentRepository.findById(studentId).orElseThrow(()->new ResourceNotFoundException
                ("Student","studentName",studentId));
        studentRepository.delete(existingStudent);
        return modelMapper.map(existingStudent, StudentDTO.class);
    }

    @Override
    public StudentDTO updateStudentImage(Long studentId, MultipartFile image) throws IOException {
        logger.info("StudentServiceImpl implementation : updateStudentImage() method");
        Student exstingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));

        String fileName = updateImageService.uploadImage(path, image);
        exstingStudent.setImage(fileName);

        Student updatedProduct = studentRepository.save(exstingStudent);
        return modelMapper.map(updatedProduct, StudentDTO.class);
    }


}
