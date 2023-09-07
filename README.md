Java Home Task
===
Juha Rainto 2023
___

EmployeeService is a Java task I completed as part of a recruitment process.

The task included code for two Java interfaces, Employee and EmployeeService. The given code was not to be changed so it is left unaltered.


## Features
- Company can have any number of employees
- New Employees can join, old ones can leave
- (The same employee can return to the company)
    - They will goo through the same proocess as a new employee
    - They will not receive their old id number since their data is deleted upon leaving

## Rules implemented
#### Employee
- Employee name has to be more than one word
- Employee name can not contain numbers
- Employee name is automatically capitalized if needed
- Hourly salary can not be negative
- Hourly salary in rounded to the nearest cent

#### EmployeeService
- Reported hours can not overlap previously reported hours
