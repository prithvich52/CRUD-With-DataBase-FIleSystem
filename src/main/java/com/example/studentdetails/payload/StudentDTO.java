package com.example.studentdetails.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private Long studentId;
    private String name;
    private String image;
    private String email;
    private String phoneNumber;
}
