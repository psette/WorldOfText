DROP DATABASE IF EXISTS Jeopardy;
CREATE DATABASE Jeopardy;
USE Jeopardy;

create table Users( userID int(11) not null primary key auto_increment,
username text not null,
pass text not null);
insert into Users ( username, pass )
values ('sampleUser', 'samplePassword' ),
('SampleUserName','SamplePasswordField');