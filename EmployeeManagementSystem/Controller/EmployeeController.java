package com.example.lab6.EmployeeManagementSystem.Controller;

import com.example.lab6.EmployeeManagementSystem.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity getEmployees() {
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.add(employee);
        return ResponseEntity.status(200).body("Employee added");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @RequestBody @Valid Employee updatedEmployee, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        Employee employee = findEmployeeById(id);

        if (employee == null)
            return ResponseEntity.status(400).body("No employee found");

        employees.set(employees.indexOf(employee), updatedEmployee);
        return ResponseEntity.status(200).body("Employee updated");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id) {

        Employee employee = findEmployeeById(id);

        if (employee == null)
            return ResponseEntity.status(400).body("No employee found");

        employees.remove(employee);
        return ResponseEntity.status(200).body("Employee deleted");

    }

    @GetMapping("/search/{position}")
    public ResponseEntity searchEmployeesByPosition(@PathVariable String position) {

        if (!position.equalsIgnoreCase("supervisor") && !position.equalsIgnoreCase("coordinator"))
            return ResponseEntity.status(400).body("Position must be either 'Supervisor' or 'Coordinator' only");

        ArrayList<Employee> list = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getPosition().equalsIgnoreCase(position))
                list.add(e);
        }

        if (list.isEmpty())
            return ResponseEntity.status(400).body("No employee found");
        else
            return ResponseEntity.status(200).body(list);
    }

    @GetMapping("/get/employeesByAgeRange/{minAge}/{maxAge}")
    public ResponseEntity getEmployeesByAgeRange(@PathVariable  Integer minAge, @PathVariable  Integer maxAge) {

        if(minAge < 25 || maxAge < 25)
            return ResponseEntity.status(400).body("Age must at least 25");

        if (maxAge <= minAge)
            return ResponseEntity.status(400).body("Max age must be grater than min age");

        ArrayList<Employee> list = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getAge() >= minAge && e.getAge() <= maxAge)
                list.add(e);
        }

        if (list.isEmpty())
            return ResponseEntity.status(400).body("No employee found");
        else
            return ResponseEntity.status(200).body(list);
    }

    @PutMapping("apply/annualLeave/{id}")
    public ResponseEntity applyForAnnualLeave(@PathVariable String id) {

        Employee employee = findEmployeeById(id);

        if (employee == null)
            return ResponseEntity.status(400).body("No employee found");

        if (employee.isOnLeave())
            return ResponseEntity.status(400).body("Employee is already on leave");

        if (employee.getAnnualLeave() == 0)
            return ResponseEntity.status(400).body("Employee has no remaining annual leave");

        employee.setOnLeave(true);
        employee.setAnnualLeave(employee.getAnnualLeave() - 1);
        return ResponseEntity.status(200).body("Applied annual leave for employee successfully ");


    }

    @GetMapping("/get/employeesNoAnnualLeave")
    public ResponseEntity getEmployeesWithNoAnnualLeave() {

        ArrayList<Employee> list = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getAnnualLeave() == 0)
                list.add(e);
        }

        if (list.isEmpty())
            return ResponseEntity.status(400).body("No employee found");
        else
            return ResponseEntity.status(200).body(list);
    }


    @PutMapping("/promote/{supervisorId}/{employeeId}")
    public ResponseEntity promoteEmployee(@PathVariable String supervisorId, @PathVariable String employeeId) {

        Employee supervisor = findEmployeeById(supervisorId);
        Employee employee = findEmployeeById(employeeId);

        if (supervisor == null || employee == null)
            return ResponseEntity.status(400).body("No employee found");

        if (!supervisor.getPosition().equalsIgnoreCase("supervisor"))
            return ResponseEntity.status(400).body("Not a supervisor");

        if (employee.getAge() < 30) {
            return ResponseEntity.status(400).body("Employee is not over 30");
        }
        if (employee.isOnLeave()) {
            return ResponseEntity.status(400).body("Employee is on leave");
        }

        if (employee.getPosition().equalsIgnoreCase("supervisor")) {
            return ResponseEntity.status(400).body("Employee is already promoted");
        }

        employee.setPosition("Supervisor");
        return ResponseEntity.status(200).body("Employee promoted successfully");

    }

    public Employee findEmployeeById(String id) {

        for (Employee e : employees) {
            if (e.getId().equals(id))
                return e;
        }

        return null;
    }


}
