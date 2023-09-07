package com.juharainto.app;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeTest implements TestLogger {
    public static Employee newEmployee(){
        Employee testEmployee = new Employee() {
            int id = 0;
            String fullName = "";
            double salary = 0;
    
            @Override
            public int getID() {
                return this.id;
            }
    
            @Override
            public void setID(int id) {
                this.id = id;
            }
    
            @Override
            public String getFullName() {
                return this.fullName;
            }
    
            @Override
            public void setFullName(String fullName) {
                if (!fullName.contains(" ")){
                    throw new IllegalArgumentException("Full name has to contain more than one word");
                }
    
                for(int i = 0; i < fullName.length(); i++){
                    if(Character.isDigit(fullName.charAt(i))){
                        throw new IllegalArgumentException("Full name can not contain numbers");
                    }
                }
    
                this.fullName = autoCapitalize(fullName);
            }
    
            @Override
            public double getHourlySalary() {
                return this.salary;
            }
    
            @Override
            public void setHourlySalary(double salary) {
                // Salary should be above zero
                if (salary < 0){
                    throw new IllegalArgumentException("Hourly salary can't be less than zero");
                } else {
                    // Salary isrounded to the nearest one cent
                    this.salary = Math.round(salary * 100.0) / 100.0;
                }
            }

            /*
             * Capitalize a given string
             */
            public String autoCapitalize(String toBeCapitalized){
                String capitalized = "";
                Boolean capitalizeNext = true;
                char letter;

                for(int i = 0; i < toBeCapitalized.length(); i++){
                    if (capitalizeNext == true){
                        letter = Character.toUpperCase(toBeCapitalized.charAt(i));
                        capitalizeNext = false;
                    } else {
                        letter = toBeCapitalized.charAt(i);
                    }

                    if(toBeCapitalized.charAt(i) == ' ' || toBeCapitalized.charAt(i) == '-'){
                        capitalizeNext = true;
                    }

                    capitalized += letter;
                }

                return capitalized;
            }
        };

        return testEmployee;
    }


   

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    /*
     * Setting hourly salary below zero should throw exception
     */

    @Test
    public void negativeSalaryShouldNotBeAccepted()
    {
        Employee testEmp = newEmployee();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testEmp.setHourlySalary(-20.00);
        });

        String expectedMessage = "Hourly salary can't be less than zero";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));    
    }

    /*
     * Setting and then getting a regular salary amount should not throw exception
     */
    @Test
    public void properSalaryShouldThrowNoException()
    {
        Employee testEmp = newEmployee();

        try {
            testEmp.setHourlySalary(10.00);
            Assertions.assertEquals(10.00, testEmp.getHourlySalary());
        } catch (Exception e) {
            Assert.fail("Exception " + e);
        }
    }

    /*
     * Employee class method setHourlySalary should round the given amount to the nearest cent
     */
    @Test
    public void salaryShouldBeRounded(){
        Employee testEmp = newEmployee();
        double[] testValue = {5, 4.15, 16.4456, 15.5512, 19.0988, 10.999, 14.1498796454};
        double[] testExpection = {5, 4.15, 16.45, 15.55, 19.10, 11.00, 14.15};

        for (int i = 0; i < testValue.length; i++){
            testEmp.setHourlySalary(testValue[i]);
            Assertions.assertEquals(testExpection[i], testEmp.getHourlySalary());
        }
    }

    /*
     * Full name should contain more than one word
     */
    @Test
    public void singleWordFullNameShouldNotBeAccepted(){
        Employee testEmp = newEmployee();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testEmp.setFullName("Juha");
        });

        String expectedMessage = "Full name has to contain more than one word";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    /*
     * Names should not contain numbers
     */
    @Test
    public void fullNamesContainingNumbersShouldNotBeAccepted(){
        Employee testEmp = newEmployee();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testEmp.setFullName("Juha Ra1nto");
        });

        String expectedMessage = "Full name can not contain numbers";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /*
     * Names should be automatically capitalized
     */
    @Test
    public void fullNamesShouldBeAutoCapitalized(){
        Employee testEmp = newEmployee();

        testEmp.setFullName("juha rainto-raintonpoika");
        Assertions.assertEquals("Juha Rainto-Raintonpoika", testEmp.getFullName());
    }    
}
