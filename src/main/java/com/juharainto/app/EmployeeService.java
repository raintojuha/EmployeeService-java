package com.juharainto.app;

import java.time.LocalDateTime;


public interface EmployeeService {
    /**
     * Name of the company
     */
    String getName();


    /**
    * Adds new employee from the given date. Employee Id must be unique.
    * @param employee Employee to add.
    * @param contractStartDate Employee work start date and time.
    */
    void AddEmployee(Employee employee, LocalDateTime contractStartDate);


    /**
    * Remove employee from the company at the given date.
    * @param employeeId Id of the employee.
    * @param contractEndDate Employee work end date and time.
    */
    void RemoveEmployee(int employeeId, LocalDateTime contractEndDate);

    
    /**
    * Report worked time at given day and time.
    * If an employee reports 1 hour and 30 minutes at 13:00, it means that the employee was working from 13:00 to 14:30.
    * @param employeeId Id of the employee.
    * @param dateAndTime Date when work was started.
    * @param hours Full hours.
    * @param minutes Full minutes.
    */
    void ReportHours(int employeeId, LocalDateTime dateAndTime, int hours, int minutes);
}