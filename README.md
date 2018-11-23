
# Employees REST server

## Requirements
 All data should be persisted into either memory, or externally. Please include instructions on how to run and interact with the web server.
 Please demonstrate use of Java 8
 Please demonstrate use of one or more design patterns and add comments as to why you choose that pattern.

Create a web application that exposes REST operations for employees. The API should be able to:

Get employees by an ID
Create new employees
Update existing employees
Delete employees
Get all employees
An employee is made up of the following data:

### Employee spec
ID - Unique identifier for an employee
FirstName - Employees first name
MiddleInitial - Employees middle initial
LastName - Employeed last name
DateOfBirth - Employee birthday and year
DateOfEmployment - Employee start date
Status - ACTIVE or INACTIVE

### Startup
On startup, the application should be able to ingest an external source of employees, and should make them available via the GET endpoint.

### ACTIVE vs INACTIVE employees

By default, all employees are active, but by way of the API, can be switched to inactive. This should be done by the delete API call. This call should require some manner of authorization header.
When an employee is inactive, they should no longer be able to be retrieved in either the get by id, or get all employees calls

## How to run the project

Please follow these steps to run the application on a local server:

* Clone git project
* Execute the following command on the project directory

	> mvn clean spring-boot:run
	* The application contains two profiles, as follows:
		* memory - Default profile. Will run an in-memory database
		* external - Will connect to an external database. To activate this profile, please:
			* Configure database connection parameters on: */src/main/resources/application-external.properties*
			* Run application with the following command:
				> mvn clean spring-boot:run -Pexternal
* Once the application starts please open a browser window and go to: *http://localhost:8080*. You'll be presented with a swagger documentation describing the application endpoints and models.

## How to interact with the project

The easiest way to interact with the project is to run it and then open the following URL in a browser window: http://localhost:8080

The default mapping redirects to the project's swagger documentation, where you may list all of the project endpoints, their parameters and  data models. You may execute each of these endpoints directly from the swagger documentation.

You may also interact with this project using any API development environment (such as [PostMan](https://www.getpostman.com/)).

## Security
The following endpoints require Basic HTTP security:
* /secure/deleteEmployee/{id}
* /secure/deleteEmployees/{ids}

By default the username and password are both **test** but you may change that by setting the following properties on the *application-{profile}.properties* file

> security.basic.username=test
> security.basic.password=test

## Startup

On startup the application will load employees defined on */src/main/resources/data.sql*