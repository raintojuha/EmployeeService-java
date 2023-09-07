package com.juharainto.app;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
//import org.junit.Test;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeServiceTest implements TestLogger {

    // List of employees in the company
    List<Employee> employees = new ArrayList<Employee>();
    // List of logged work
    List<WorkLog> worklogs = new ArrayList<WorkLog>();

    public EmployeeService newEmployeeService(String name, List<Employee> employeeList, List<WorkLog> worklogList){
        EmployeeService service = new EmployeeService() {
            // Get company name from method parameters
            String companyName = name;

            // Reference lists
            List<Employee> employees = employeeList;
            List<WorkLog> worklogs = worklogList;

            // AtomicInteger used for generating employee id numbers
            AtomicInteger atom = new AtomicInteger(-1);
                 

            @Override
            public String getName(){
                return this.companyName;
            }

            /**
             * Add employee to company at a given date and time
             */
            @Override
            public void AddEmployee(Employee employee, LocalDateTime contractStartDate){
                // Add employee immediately if contractStartDate has already passed
                if(LocalDateTime.now().isAfter(contractStartDate)){
                    // Add a unique ID number to the employee
                    employee.setID(getNextAvaileableId());
                    employees.add(employee);
                    return;
                } else {
                    // Calculate the delay between now and contractStartDate
                    long delayInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), contractStartDate);

                    // New Scheduled Executor Service with a thread pool of 1
                    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
                    
                    // Task to be run after delay
                    Runnable addEmployeeTask = () -> {
                        // Add a unique ID number to the employee
                        employee.setID(getNextAvaileableId());
                        employees.add(employee);
                    };

                    // Schedule task after delay
                    ses.schedule(addEmployeeTask, delayInSeconds, TimeUnit.SECONDS);

                    // Shut down Executor after all tasks are completed
                    ses.shutdown();
                }
            }

            /**
             * Remove employee data at a given date and time
             */
            @Override
            public void RemoveEmployee(int employeeId, LocalDateTime contractEndDate){
                // Remove employee immediately if contractEndDate has already passed
                if(LocalDateTime.now().isAfter(contractEndDate)){
                    removeEmployeeData(employeeId);
                } else {
                    // Calculate the difference between now and contractEndDate
                    long delayInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), contractEndDate);

                    // New Scheduled Execution Service with a thread pool of 1
                    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

                    // Task to be run after delay
                    Runnable removeEmployeeTask = () -> removeEmployeeData(employeeId);;

                    // Schedule task after delay
                    ses.schedule(removeEmployeeTask, delayInSeconds, TimeUnit.SECONDS);

                    // Shut down Executor after all tasks are completed
                    ses.shutdown();
                }
            }


            /**
             * Add reported hours to a list
             */
            @Override
            public void ReportHours(int employeeId, LocalDateTime dateAndTime, int hours, int minutes){
                // Date and time when work was started and when it ended
                LocalDateTime start = dateAndTime;
                LocalDateTime end = dateAndTime.plusHours(hours).plusMinutes(minutes);

                // Check if employee id matches current employees
                if(!doesEmployeeExist(employeeId)){
                    throw new IllegalArgumentException("Employee id doesn't match any employee");
                }

                // Check if work hours overlap with a previous worklog
                if(doesWorklogOverlap(employeeId, start, end)){
                    throw new IllegalArgumentException("Worklog overlap");
                }

                /**
                 * Search for previous worklogs for this employee
                 * Throw exception if worklog overlaps with a previous one
                 */
                for(int i = 0; i < worklogs.size(); i++){
                    if(worklogs.get(i).id == employeeId){
                        if(start.isAfter(worklogs.get(i).workStart) && start.isBefore(worklogs.get(i).workEnd) ||
                            end.isAfter(worklogs.get(i).workStart) && end.isBefore(worklogs.get(i).workEnd)){
                            
                        }
                    }
                }

                // Add worklog to the list
                worklogs.add(new WorkLog(employeeId, start, end));
            }


            /**
             * Remove employee from company and all reported hours from the employee
             * @param id employee id
             */
            public void removeEmployeeData(int id){
                // Remove employee from company
                for(int i = 0; i < employees.size(); i++){
                    if(employees.get(i).getID() == id){
                        employees.remove(i);
                    }
                }

                // Remove employee's reported hours
                for(int i = 0; i < worklogs.size(); i++){
                    if(worklogs.get(i).id == id){
                        worklogs.remove(i);
                    }
                }
                return;
            }


            /**
             * Check if employee with this id is in Employee list
             * @param id Employee id
             * @return Does employee exist
             */
            public Boolean doesEmployeeExist(int id){
                Boolean employeeExists = false;
                for(int i = 0; i < employees.size(); i++){
                    if(employees.get(i).getID() == id){
                        employeeExists = true;
                    }
                }
                return employeeExists;
            }


            /**
             * Check if new worklog overlaps a previous one
             * @param id Employee id number
             * @param start Date and time when work was started
             * @param end Date and time when work concluded
             * @return {Boolean} Do worklogs overlap
             */
            public Boolean doesWorklogOverlap(int id, LocalDateTime start, LocalDateTime end){
                // Go through worklogs list
                for(int i = 0; i < worklogs.size(); i++){
                    // Check only worklogs for this employee
                    if(worklogs.get(i).id == id){
                        // Compare start and end tiimes to old worklogs
                        if(start.isAfter(worklogs.get(i).workStart) && start.isBefore(worklogs.get(i).workEnd) ||
                            end.isAfter(worklogs.get(i).workStart) && end.isBefore(worklogs.get(i).workEnd)){
                            return true;
                        }
                    }
                }
                return false;
            }


            /**
             * Increment AtomicInteger and get new next id.
             * 
             * @return {int} idNumber
             */
            public int getNextAvaileableId(){
                return atom.incrementAndGet();
            }
        };

        return service;
    }


    /**
     * Set up an EmployeeService with one Employee
     * @return EmployeeService
     */
    public EmployeeService setupTestService(){
        // New empty EmployeeService
        EmployeeService testService = newEmployeeService("Test Service", employees, worklogs);

        // New default employee
        Employee testEmployee = EmployeeTest.newEmployee();
        testEmployee.setFullName("Test Employee");
        testEmployee.setHourlySalary(1000);

        // Add employee to service
        testService.AddEmployee(testEmployee, LocalDateTime.now());

        return testService;
    }

    /**
     * Clear employee and worklog lists after each test
     */
    @AfterEach
    public void clearTestData(){
        employees.clear();
        worklogs.clear();
    }

    /**
     * Add Employee with a contractStartDate 2 seconds in the future
     * Wait for 3 seconds so that the Employee should be added
     */
    @Test
    public void addEmployeeWithDelayAndWaitUntilAdded(){
        // New EmployeeService
        EmployeeService service = setupTestService();

        // New Employee
        Employee employee = EmployeeTest.newEmployee();
        employee.setFullName("John Doe");
        employee.setHourlySalary(100.00);

        // Add Emploiyee with 2 second delay
        service.AddEmployee(employee, LocalDateTime.now().plusSeconds(2));

        // Wait for 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Expect there to be two Employees
        assertEquals(2, employees.size());
    }

    /**
     * Add Employee with a contractStartDate 4 seconds in the future
     * Check EmployeeService list size immediately
     * The Employee should not have time to be added
     */
    @Test
    public void addEmployeeWithDelayWithNoWait(){
        // New EmployeeService
        EmployeeService service = setupTestService();

        // New Employee
        Employee employee = EmployeeTest.newEmployee();
        employee.setFullName("John Doe");
        employee.setHourlySalary(100.00);

        // Add Emploiyee with 4 second delay
        service.AddEmployee(employee, LocalDateTime.now().plusSeconds(4));

        // Expect there to be only be one Employee
        assertEquals(1, employees.size());
    }

    /**
     * Add Employee with a contractStartDate in the past
     * The Employee should be added immediately
     */
    @Test
    public void addEmployeeWithNoDelay(){
        // New EmployeeService
        EmployeeService service = setupTestService();

        // New Employee
        Employee employee = EmployeeTest.newEmployee();
        employee.setFullName("John Doe");
        employee.setHourlySalary(100.00);

        // Add Emploiyee with 4 second delay
        service.AddEmployee(employee, LocalDateTime.now().minusHours(1));

        // Expect there to be two Employees
        assertEquals(2, employees.size());
    }

    /**
     * Report hours for employee. Check that report goes into list.
     */
    @Test
    public void testHourReporting(){
        // New EmployeeService
        EmployeeService ser = setupTestService();

        // Report hours for default employee
        ser.ReportHours(0, LocalDateTime.now(), 1, 30);

        // Expect there to be one worklog
        assertEquals(1, employees.size());
    }

    /**
     * Reporting overlapping work hours should return an exception.
     */
    @Test
    public void overlappingWorkHoursShouldNotBeAccepted(){
        // New EmployeeService
        EmployeeService service = setupTestService();

        // Report hours for employee
        service.ReportHours(0, LocalDateTime.now(), 1, 30);
        
        // Report hours for same employee that overlaps previous report
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.ReportHours(0, LocalDateTime.now().plusMinutes(30), 1, 00);
        });

        // Expected message and message from exception
        String expectedMessage = "Worklog overlap";
        String actualMessage = exception.getMessage();

        // Compare messages
        assertTrue(actualMessage.contains(expectedMessage));
    }


    /**
     * Check that employee id exists when reporting hours.
     */
    @Test
    public void employeeIdShouldExistWhenReportingHours(){
        // New EmployeeService
        EmployeeService service = setupTestService();
        
        // Report hours for non-existent employee (id 1)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.ReportHours(1, LocalDateTime.now(), 1, 00);
        });

        // Expected message and message from exception
        String expectedMessage = "Employee id doesn't match any employee";
        String actualMessage = exception.getMessage();

        
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

