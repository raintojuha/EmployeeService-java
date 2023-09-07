package com.juharainto.app;

public interface Employee {
    /**
    * Unique ID of the employee
    */
    int getID();
    void setID(int id);

    /**
     * Employee full name
     */
    String getFullName();
    void setFullName(String fullName);

    /**
     * Hourly salary of worked full hour. Use proportion for time smaller than 1 hour.
     */
    double getHourlySalary();
    void setHourlySalary(double salary);
}
