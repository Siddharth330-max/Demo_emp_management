package com.example.DEMOOOO.controller;

import com.example.DEMOOOO.entity.Employee;
import com.example.DEMOOOO.entity.Leave;
import com.example.DEMOOOO.repo.EmpRepo;
import com.example.DEMOOOO.repo.LeaveRepo;

import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    // Constructor Injection
    private final LeaveRepo repo;
    private final EmpRepo empRepo;

    public LeaveController(LeaveRepo repo, EmpRepo empRepo) {
        this.repo = repo;
        this.empRepo = empRepo;
    }

    // Submit leave request
    @PostMapping
    public Leave submitLeave(@RequestBody Leave leave) {

        LocalDate start = LocalDate.parse(leave.getStartDate());
        LocalDate end = LocalDate.parse(leave.getEndDate());

        // Validate date
        if (end.isBefore(start)) {
            throw new RuntimeException(
                    "End date cannot be before start date"
            );
        }

        // Check overlapping PENDING or APPROVED leaves
        List<String> statuses = List.of("PENDING", "APPROVED");

        List<Leave> existingLeaves =
                repo.findByEmployeeIdAndStatusIn(
                        leave.getEmployeeId(),
                        statuses
                );

        LocalDate newStart = start;
        LocalDate newEnd = end;

        for (Leave existing : existingLeaves) {

            LocalDate existingStart =
                    LocalDate.parse(existing.getStartDate());

            LocalDate existingEnd =
                    LocalDate.parse(existing.getEndDate());

            if (!newEnd.isBefore(existingStart)
                    && !newStart.isAfter(existingEnd)) {

                throw new RuntimeException(
                        "Leave dates overlap with an existing leave"
                );
            }
        }

        leave.setStatus("PENDING");

        return repo.save(leave);
    }

    // View leave request by ID
    @GetMapping("/{id}")
    public Leave getLeaveById(@PathVariable int id) {

        return repo.findById(id).orElse(null);
    }

    // View employee leave history
    @GetMapping("/employee/{employeeId}")
    public List<Leave> getEmployeeLeaves(
            @PathVariable int employeeId) {

        return repo.findByEmployeeId(employeeId);
    }

    // Cancel PENDING leave
    @PutMapping("/{id}/cancel")
    public Leave cancelLeave(@PathVariable int id) {

        Leave leave = repo.findById(id).orElse(null);

        if (leave != null
                && "PENDING".equals(leave.getStatus())) {

            leave.setStatus("CANCELLED");

            return repo.save(leave);
        }

        return null;
    }

    // Approve leave
    @PutMapping("/{id}/approve/{managerId}")
    public Leave approveLeave(
            @PathVariable int id,
            @PathVariable int managerId) {

        Leave leave = repo.findById(id).orElse(null);

        // Only PENDING leave can be approved
        if (leave != null
                && "PENDING".equals(leave.getStatus())) {

            // Employee cannot approve their own leave
            if (leave.getEmployeeId() == managerId) {
                return null;
            }

            // Get employee
            Employee employee =
                    empRepo.findById(leave.getEmployeeId())
                            .orElse(null);

            if (employee != null) {

                LocalDate start =
                        LocalDate.parse(leave.getStartDate());

                LocalDate end =
                        LocalDate.parse(leave.getEndDate());

                // Count only working days
                long days =
                        calculateWorkingDays(start, end);

                // Check leave balance
                if (employee.getLeaveBalance() < days) {
                    return null;
                }

                // Deduct leave balance
                employee.setLeaveBalance(
                        employee.getLeaveBalance()
                                - (int) days
                );

                empRepo.save(employee);

                // Update leave
                leave.setStatus("APPROVED");
                leave.setDecision("APPROVED");
                leave.setActionBy(managerId);
                leave.setActionAt(
                        LocalDateTime.now().toString()
                );

                return repo.save(leave);
            }
        }

        return null;
    }

    // Reject leave
    @PutMapping("/{id}/reject/{managerId}")
    public Leave rejectLeave(
            @PathVariable int id,
            @PathVariable int managerId) {

        Leave leave = repo.findById(id).orElse(null);

        // Only PENDING leave can be rejected
        if (leave != null
                && "PENDING".equals(leave.getStatus())) {

            // Employee cannot reject their own leave
            if (leave.getEmployeeId() == managerId) {
                return null;
            }

            leave.setStatus("REJECTED");
            leave.setDecision("REJECTED");
            leave.setActionBy(managerId);
            leave.setActionAt(
                    LocalDateTime.now().toString()
            );

            return repo.save(leave);
        }

        return null;
    }

    // Calculate working days
    // Saturday and Sunday are not counted
    private long calculateWorkingDays(
            LocalDate start,
            LocalDate end) {

        long days = 0;

        while (!start.isAfter(end)) {

            DayOfWeek day =
                    start.getDayOfWeek();

            if (day != DayOfWeek.SATURDAY
                    && day != DayOfWeek.SUNDAY) {

                days++;
            }

            start = start.plusDays(1);
        }

        return days;
    }
}
