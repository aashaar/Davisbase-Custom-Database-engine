CS 6360 - Fall 2017
Project 02 : Davisbase
Submitted by: Aashaar Panchalan
Net ID : adp170630

To run this project : Browse to 'davisbase\src\davisbase' --> Run 'davisBase.java'.

NOTE : The data type used in this database for alphanumeric characters is "TEXT"
for example: create table test (id int primary key, name text);

Table catalog files are present in :- 'davisbase\data\catalog'.
User tables are present in : 'davisbase\data\user_data'.

Key Features implemented :
> Database supports unique Primary key and not nullable constraints.
> Duplicate table name is not allowed.
> Number of values passed in an insert query must be equal to the number of columns in the corresponding column.
> Checks if table name provided exists or not before executing any query operation like select/insert/delete.
> Checks for supported data types in the CREATE TABLE query and throws error if any invalid data type is passed.
> Handled paging overflow.


Example queries :

create table test (id int primary key, name text);
insert into test values (1,'aashaar');
select * from test;
select * from test where id=1;
delete from table test where id=1;
show tables;
drop table test;
help;

