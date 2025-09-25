-- Subjects
insert into subjects (name, author) values ('Mathematics', 'Prof. Brown');
insert into subjects (name, author) values ('Physics', 'Dr. Green');
insert into subjects (name, author) values ('Computer Science', 'Dr. White');

-- Teachers
insert into teacher (full_name, department) values ('Dr. Stone', 'Engineering');
insert into teacher (full_name, department) values ('Dr. Carter', 'Science');
insert into teacher (full_name, department) values ('Dr. Miller', 'Arts');

-- Courses (linked to subject + teacher)
insert into course (name, author, subject_id, teacher_id) values ('Java Basics', 'John Doe', 3, 1);
insert into course (name, author, subject_id, teacher_id) values ('Spring Boot Intro', 'Jane Smith', 3, 2);
insert into course (name, author, subject_id, teacher_id) values ('Database Design', 'Alice', 3, 3);

-- Students
insert into student (full_name, email, enrolled_date) values ('Michael Scott', 'michael@school.com', current_date);
insert into student (full_name, email, enrolled_date) values ('Pam Beesly', 'pam@school.com', current_date);
insert into student (full_name, email, enrolled_date) values ('Jim Halpert', 'jim@school.com', current_date);

-- Enrollments (student ↔ course)
insert into enrollment (student_id, course_id, enroll_date) values (1, 1, current_date);
insert into enrollment (student_id, course_id, enroll_date) values (2, 2, current_date);
insert into enrollment (student_id, course_id, enroll_date) values (3, 3, current_date);
