CREATE TABLE users ( 
   username VARCHAR(45) NOT NULL , password VARCHAR(45) NOT NULL , 
   account_non_locked TINYINT NOT NULL DEFAULT 1 , 
   PRIMARY KEY (username)
);


CREATE TABLE attempts ( 
   id int(45) NOT NULL AUTO_INCREMENT, 
   username varchar(45) NOT NULL, attempts varchar(45) NOT NULL, PRIMARY KEY (id) 
);


ALTER TABLE `attempts` 
ADD FOREIGN KEY (`username` ) REFERENCES `users` (`username` );

INSERT INTO users(username,password,account_non_locked) 
VALUES ('user','12345', true);
