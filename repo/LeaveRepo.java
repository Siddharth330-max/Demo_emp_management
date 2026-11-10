package com.example.DEMOOOO.repo;

import com.example.DEMOOOO.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepo extends JpaRepository<Leave, Integer> {

    List<Leave> findByEmployeeId(int employeeId);

    List<Leave> findByEmployeeIdAndStatusIn(
            int employeeId,
            List<String> statuses
    );
}
