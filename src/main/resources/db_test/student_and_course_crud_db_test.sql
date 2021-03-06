DROP TABLE IF EXISTS STUDENTS_ENROLLED_TO_COURSES;
DROP TABLE IF EXISTS STUDENTS;
DROP TABLE IF EXISTS COURSES;

CREATE TABLE STUDENTS_ENROLLED_TO_COURSES
(
    STUDENT_ID VARCHAR(32),
    COURSE_ID  VARCHAR(32),
    PRIMARY KEY (STUDENT_ID, COURSE_ID)
);
CREATE TABLE STUDENTS
(
    ID              VARCHAR(32),
    NAME            VARCHAR(50),
    SURNAME         VARCHAR(50),
    DATE_OF_BIRTH   DATE,
    PHONE_NUMBER    VARCHAR(9),
    STUDENT_CARD_ID VARCHAR(6),
    PRIMARY KEY (ID)
);
CREATE TABLE COURSES
(
    ID          VARCHAR(32),
    NAME        VARCHAR(50),
    SCHOOL_NAME VARCHAR(100),
    PRIMARY KEY (ID)
);
ALTER TABLE STUDENTS_ENROLLED_TO_COURSES
    ADD FOREIGN KEY (STUDENT_ID) REFERENCES STUDENTS(ID);

ALTER TABLE STUDENTS_ENROLLED_TO_COURSES
    ADD FOREIGN KEY (COURSE_ID) REFERENCES COURSES(ID);

INSERT INTO STUDENTS (ID, NAME, SURNAME, DATE_OF_BIRTH, PHONE_NUMBER, STUDENT_CARD_ID)
VALUES ('00000000000000000000000000000001', 'JOHN', 'SMITH', '2002-04-04', '123456789', '999999');
INSERT INTO STUDENTS (ID, NAME, SURNAME, DATE_OF_BIRTH, PHONE_NUMBER, STUDENT_CARD_ID)
VALUES ('00000000000000000000000000000002', 'ADAM', 'SMITH', '2005-04-04', '123456788', '999998');

INSERT INTO COURSES (ID, NAME, SCHOOL_NAME)
VALUES ('00000000000000000000000000000111', 'MATH', 'SCHOOLNR1');
INSERT INTO COURSES (ID, NAME, SCHOOL_NAME)
VALUES ('00000000000000000000000000000222', 'BIOLOGY', 'SCHOOLNR1');
INSERT INTO COURSES (ID, NAME, SCHOOL_NAME)
VALUES ('00000000000000000000000000000333', 'HISTORY', 'SCHOOLNR1');
