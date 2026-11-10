package com.example.DEMOOOO.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {

    @Id
    private int id;
    private String name;
    private String email;

    private int leaveBalance = 20;
}
