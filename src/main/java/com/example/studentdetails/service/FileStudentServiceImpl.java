package com.example.studentdetails.service;



//TO Create CRUD Operation using File System

import com.example.studentdetails.exception.APIException;
import com.example.studentdetails.exception.ResourceNotFoundException;
import com.example.studentdetails.model.Student;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileStudentServiceImpl implements FileStudentService {

    private static final String FILE_PATH = "C:\\Users\\DELL\\Desktop\\FileSystem.txt";

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof Student) {
                    students.add((Student) obj);
                }
            }
        } catch (EOFException e) {
            // End of file reached
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public Student getStudentById(Long id) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof Student && ((Student) obj).getStudentId().equals(id)) {
                    return (Student) obj;
                }
            }
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student getStudentByName(String name) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof Student && ((Student) obj).getName().equals(name)) {
                    return (Student) obj;
                }
            }
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student addStudent(Student student) {
        List<Student> students = getAllStudents();
        Long newId = getNextId(students);
        if (isEmailAlreadyExists(students, student.getEmail())) {
            throw new APIException("Student with email " + student.getEmail() + " already exists.");
        }
        student.setStudentId(newId);
        student.setImage("default.jpg");
        students.add(student);
        saveStudents(students);
        return student;
    }

    private Long getNextId(List<Student> students) {
        Long maxId = 0L;
        for (Student student : students) {
            if (student.getStudentId() > maxId) {
                maxId = student.getStudentId();
            }
        }
        return maxId + 1;
    }

    private boolean isEmailAlreadyExists(List<Student> students, String email) {
        return students.stream()
                .anyMatch(student -> student.getEmail().equals(email));
    }

    public Student updateStudent(Long id, Student updatedStudent)  {
        List<Student> students = getAllStudents();
        boolean studentFound = false;
        for (Student student : students) {
            if (student.getStudentId().equals(id)) {
                student.setName(updatedStudent.getName());
                student.setImage(updatedStudent.getImage());
                if (isEmailAlreadyExists(students, updatedStudent.getEmail())) {
                    throw new APIException("Student with email " + student.getEmail() + " already exists.");
                }else {
                    student.setEmail(updatedStudent.getEmail());
                }
                student.setPhoneNumber(updatedStudent.getPhoneNumber());
                studentFound = true;
                break;
            }
        }

        if (!studentFound) {
            throw new APIException("Student with ID " + id + " does not exist");
        }
        saveStudents(students);
        updatedStudent.setStudentId(id);
        return updatedStudent;
    }


    @Override
    public void deleteStudent(Long id) {
        List<Student> students = getAllStudents();
        if(students.removeIf(student -> student.getStudentId().equals(id))){
        saveStudents(students);
    }else {
            throw new ResourceNotFoundException("Student","studentId",id);
        }
    }

    private void saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            for (Student student : students) {
                oos.writeObject(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
