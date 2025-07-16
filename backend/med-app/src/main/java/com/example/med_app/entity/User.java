package com.example.med_app.entity;

import com.example.med_app.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;

@Data
@Entity
@Table(name = "users", schema = "medical_app")
public class User extends BaseEntityAudit{

    private String email;
    private String firstName;
    private String lastName;
//    @Pattern(
//            regexp = "^\\+[1-9]\\d{1,14}$",
//            message = "The telephone number must match with the standard E.164 (e.g. +48123456789)"
//    ) FOR THE FUTURE
    private String password;
    private String phoneNumber;
    private Gender gender;
}
