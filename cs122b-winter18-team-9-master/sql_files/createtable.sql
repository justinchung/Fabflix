use `moviedb`;

DROP TABLE IF EXISTS movies;
CREATE TABLE movies (
   id 				varchar(10) NOT NULL,
   title 			varchar(100),
   year 			int(11),
   director 		varchar(100),
   PRIMARY KEY (id)
 ); 
 
 DROP TABLE IF EXISTS stars;
 CREATE TABLE stars (
   id 				varchar(10) NOT NULL,
   name 			varchar(100) NOT NULL,
   birthYear 		int(11) DEFAULT NULL,
   PRIMARY KEY (id)
 );
 
 DROP TABLE IF EXISTS stars_in_movies;
 CREATE TABLE stars_in_movies (
   starId 			varchar(10) NOT NULL,
   movieId 			varchar(10) NOT NULL,
   #KEY fk_movies_has_stars_stars1_idx (starId),
   #KEY fk_movies_has_stars_movies_idx (movieId),
   #CONSTRAINT fk_movies_has_stars_movies 
   FOREIGN KEY(movieId) REFERENCES movies(id),
   #CONSTRAINT fk_movies_has_stars_stars1 
   FOREIGN KEY(starId) REFERENCES stars(id)
);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres (
   id 				int(11) NOT NULL AUTO_INCREMENT,
   name 			varchar(32) NOT NULL,
   PRIMARY KEY (id)
 );
 
 DROP TABLE IF EXISTS genres_in_movies;
 CREATE TABLE genres_in_movies (
   genreId 			int(11) NOT NULL,
   movieId 			varchar(10) NOT NULL,
   #KEY genreId_idx (genreId),
   #KEY movieId_idx (movieId),
   #CONSTRAINT genreId 
   FOREIGN KEY(genreId) REFERENCES genres(id),
   #CONSTRAINT movieId 
   FOREIGN KEY(movieId) REFERENCES movies(id)
);
   
DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
   id 				int(11) NOT NULL AUTO_INCREMENT,
   firstName 		varchar(50) NOT NULL,
   lastName 		varchar(50) NOT NULL,
   ccId 			varchar(20) NOT NULL,
   address 			varchar(200) NOT NULL,
   email 			varchar(50) NOT NULL,
   password 		varchar(20) NOT NULL,
   PRIMARY KEY (id)
 );  

DROP TABLE IF EXISTS sales;
CREATE TABLE sales (
   id int(11) 		NOT NULL AUTO_INCREMENT,
   customerId 		int(11) NOT NULL,
   movieId 			varchar(10) NOT NULL,
   saleDate 		date NOT NULL,
   PRIMARY KEY (id)
);
 
DROP TABLE IF EXISTS creditcards;
CREATE TABLE creditcards (
   id 				varchar(20) NOT NULL,
   firstName 		varchar(50) NOT NULL,
   lastName			varchar(50) NOT NULL,
   expiration 		date NOT NULL,
   PRIMARY KEY (id)
);

DROP TABLE IF EXISTS ratings;
CREATE TABLE ratings (
   movieId 			varchar(10) NOT NULL,
   rating 			float NOT NULL,
   numVotes 		int(11) NOT NULL,
   PRIMARY KEY (movieId)
);
