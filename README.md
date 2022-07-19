# School registration system

School registration system is Spring Boot Rest api that allows to manage the database of students and courses.

## Installation
Use this command to install aplication from github or use the provided Docker containers.

```bash
coming soon :)
```

## Build with
The application was created using:
- Java 11
- Spring Boot
- MySQL
- Hibernate

## Usage
To use rest api have to send one of the followin request

```java
/student
GET(/{id}) - get a student
GET(/all) - get all students 
POST() - create a new student 
DELETE(/{id}) - delete a student
PUT(/{id}) - update a student
PATCH(/{id}) - update field of student

/course
GET(/{id}) - get a course
GET(/all) - get all courses
POST() - create a new course
DELETE(/{id}) - delete a course
PUT(/{id}) - update a course
PATCH(/{id}) - update field of course

/system
POST(/register) - register student to course 
POST(/unregister) - unregister student from course
GET(/{schoolName}/{courseName}/students) get all students from school
GET(/{studentCardID}/courses) - get all courses which student is enrolled
GET(/students_without_courses) - get all students which are not enrolled to any course
GET(/{schoolName}/courses_without_students) - get all courses without any enrolled students
```

Details (request objects and paramteres) are available and widely described in the open api3 based documentation available at
```
http://localhost:8082/swagger-ui/index.html#/
```

## Testing
The application was tested with unit tests for every method from service and by integration tests for every endpoint to check comunication between controlle, service and respository. 
To check each request available has been prepered collection of request in Postman application.

```
https://www.getpostman.com/collections/67ab1b9ecbfc56dc533c
```

## License
Free / Open to use

## Version
V1.0 - realased 19.07.2022
