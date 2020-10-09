create database employeeDB;

Drop table employeeDB.Employee;

#create table
create table employeeDB.Employee(
employee_no INT NOT NULL AUTO_INCREMENT,
birth_date date,
first_name varchar(15),
last_name varchar(15),
gender enum('male','female'),
hire_date date,
PRIMARY KEY (employee_no)
);
insert into Employee (birth_date,first_name,last_name,gender,hire_date) values(?,?,?,?,?);

#insert rows
insert employeeDB.Employee (birth_date,first_name,last_name,gender,hire_date) values ('1995-12-26','priya','ravi','female','2020-11-01');
insert employeeDB.Employee (birth_date,first_name,last_name,gender,hire_date)  values ('1990-03-01','sundar','raja','male','2020-11-02');
insert employeedb.Employee (birth_date,first_name,last_name,gender,hire_date)  values ('2000-03-02','aishu','ravi','female','2020-11-03');


#select all
select * from employeedb.Employee;


#where condition
select hire_date from employeedb.Employee where employee_no=789;
select employee_no from employeedb.Employee where birth_date='1995-12-26' and hire_date='2020-11-01';
select gender from employeedb.Employee where not gender='male';

#delete row
delete from employeedb.Employee where employee_no='456';

#update 
update employeedb.Employee set first_name='priyanka' where last_name='ravi' and first_name='priya';

select employee_no,first_name,last_name,gender from employeedb.Employee where hire_date<'2020-11-30' ;
	