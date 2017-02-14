DROP DATABASE IF EXISTS worldoftext;
CREATE DATABASE worldoftext;
USE worldoftext;
CREATE TABLE logininfo (
Login VARCHAR(50),
Pwd VARCHAR(50),
SecurityQuestion VARCHAR(100),
SecurityAnswer VARCHAR(100),
CurrentlyLoggedIn BOOL
);