INSERT INTO `user` (name,email,password,roles) VALUES
('TCS Recruiter','tcs@gmail.com','$2a$12$gHMt5Xx5llXbGV99Sr3eGed1TLXccjYjhCgZ0r13CIUAb24Hz70ly','RECRUITER'),
('Google Recruiter','google@gmail.com','$2a$12$gHMt5Xx5llXbGV99Sr3eGed1TLXccjYjhCgZ0r13CIUAb24Hz70ly','RECRUITER'),
('John Doe','john@gmail.com','$2a$12$gHMt5Xx5llXbGV99Sr3eGed1TLXccjYjhCgZ0r13CIUAb24Hz70ly','JOB_SEEKER'),
('Alice','alice@gmail.com','$2a$12$gHMt5Xx5llXbGV99Sr3eGed1TLXccjYjhCgZ0r13CIUAb24Hz70ly','JOB_SEEKER'),
('Bob','bob@gmail.com','$2a$12$gHMt5Xx5llXbGV99Sr3eGed1TLXccjYjhCgZ0r13CIUAb24Hz70ly','JOB_SEEKER'),
('Emma','emma@gmail.com','$2a$12$gHMt5Xx5llXbGV99Sr3eGed1TLXccjYjhCgZ0r13CIUAb24Hz70ly','JOB_SEEKER');


INSERT INTO job(title,description,company,location,salary,created_by_id,posted_at) VALUES
('Java Developer','Spring Boot Developer','TCS','Kolkata',650000,1,'2026-07-25 11:11:11'),
('Backend Engineer','Java Spring Backend','Infosys','Bangalore',850000,1,'2026-07-30 12:41:00'),
('Software Engineer','React + Spring Boot','Google','Hyderabad',1500000,2,'2026-07-30 12:41:00'),
('DevOps Engineer','AWS Docker Kubernetes','Amazon','Pune',1800000,2,'2026-07-13 12:41:00'),
('Full Stack Developer','React Node Spring','Microsoft','Noida',1400000,1,'2026-07-30 12:41:00'),
('Java Intern','Freshers Welcome','Capgemini','Kolkata',400000,1,'2026-06-30 12:41:00'),
('SDE-1','Backend APIs','Flipkart','Bangalore',1200000,2,'2026-07-30 12:41:00'),
('QA Engineer','Automation Testing','Wipro','Hyderabad',700000,1,'2026-07-30 12:41:00'),
('Cloud Engineer','AWS Azure','IBM','Pune',1300000,2,'2026-07-30 12:41:00'),
('Spring Boot Developer','REST APIs JWT Security','Cognizant','Chennai',900000,1,'2026-07-20 10:41:00');


INSERT INTO applications(user_id,job_id,status)VALUES
(3,1,'APPLIED'),(3,3,'APPLIED'),(3,5,'APPLIED'),(4,2,'APPLIED'),(4,6,'APPLIED'),
(5,4,'APPLIED'),(5,3,'APPLIED'),(6,3,'APPLIED'), (6,9,'APPLIED');