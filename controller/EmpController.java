package com.example.DEMOOOO.controller;

import com.example.DEMOOOO.entity.Employee;
import com.example.DEMOOOO.repo.EmpRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")
public class EmpController {

     // This is COnstructor Injection
     private final EmpRepo repo;

     public EmpController(EmpRepo repo){
         this.repo = repo;
     }

     // Get all employees
    @GetMapping
    public List<Employee> getEmployees() {
        return repo.findAll();
    }

    //Post employees
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return repo.save(employee);
    }

    // Get using an ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return repo.findById(id).orElse(null);
    }

    // Update employee
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee employee) {

        Employee existing = repo.findById(id).orElse(null);

        if (existing != null) {
            existing.setName(employee.getName());
            existing.setEmail(employee.getEmail());

            return repo.save(existing);
        }

        return null;
    }
}
