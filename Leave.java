package com.example.DEMOOOO.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "leaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Leave {

    @Id
    private int id;

    private int employeeId;
    private String startDate;
    private String endDate;
    private String reason;
    private String status;

    private int actionBy;
    private String actionAt;
    private String decision;
}
