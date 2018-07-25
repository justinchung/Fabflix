use `moviedb`;
#

DELIMITER $$
Drop procedure if exists moviedb.add_movie $$
CREATE PROCEDURE add_movie
(
	IN movieid varchar(10), 
	IN movie_title varchar(100), 
	IN movie_year int, 
	IN movie_director varchar(100), 
	IN star varchar(100), 
	IN genre varchar(32)
)
begin
	set @max1 := (select max(id) from movies);
	set @max2 := (select max(id) from stars);
	set @max3 := (select max(id) from genres);
	set @m1 := (select substring_index(@max1, "t", -1));
	set @s1 := (select substring_index(@max2, "m", -1));
	set @finalm := (convert(@m1, unsigned int)); 
	set @finals := (convert(@s1, unsigned int)); 
	DROP TABLE IF EXISTS id_helper;
	create table id_helper
	(
		main INT(1),
		movie_id INT(7),
		star_id INT(7),
		genre_id INT(2),
		
		primary key(main)
	);
    
    
	insert into id_helper(main,movie_id, star_id,genre_id) values (1, @finalm , @finals, @max3);
	drop table if exists edits;
    create table edits
    (
		edit VARCHAR(240)
    );
	if (select id from movies where id = movieid and title = movie_title and year = movie_year and director = movie_director) is null then
		begin
			update id_helper set movie_id = movie_id + 1 where main =1;
			SET @v1 := (select movie_id from id_helper where id_helper.main =1);
			set @newmid = concat('tt', @v1);
			insert into movies(id,title,year,director) values (@newmid, movie_title,movie_year,movie_director);
            insert into edits(edit) values("New movie inserted.");
            if (select id from stars where name = star) is not null then
				begin
					set @starid := (select id from stars where name = star);
					insert into stars_in_movies(starId, movieId) values(@newmid,@starid);
                    insert into edits(edit) values("Existing star inserted into created movie cast.");
				end;
			else
				begin
					update id_helper set star_id = star_id + 1 where main =1;
					SET @v2 := (select star_id from id_helper where id_helper.main =1);
					set @newsid = concat('nm', @v2);
                    insert into stars(id, name, birthYear) values(@newsid, star,NULL);
                    insert into edits(edit) values("New star created and inserted into stars");
                    insert into stars_in_movies(starId, movieId) values(@newsid, @newmid);
                    insert into edits(edit) values("Created star inserted into created movie cast.");
                end;
			end if;
            
            if (select id from genres where name = genre) is not null then
				begin
					set @genreid := (select id from genres where name = genre);
                    insert into genres_in_movies(genreId, movieId) values(@newmid, @genreid);
                    insert into edits(edit) values("Existing genre inserted into created movie genres list.");
                end;
			else
				begin
					update id_helper set genre_id = genre_id + 1 where main =1;
					SET @v3 := (select genre_id from id_helper where id_helper.main =1);
                    insert into genres(id, name) values(@v3, genre);
                    insert into edits(edit) values("New genre created and inserted into existing genres.");
                    insert into genres_in_movies(genreId, movieId) values(@v3, @newmid);
                    insert into edits(edit) values("New genre inserted into created movie genre list.");
                end;
            end if;
		end;
	else
		begin
			if (select id from stars where name = star) is not null then
				begin
					set @starsid := (select id from stars where name = star);
					if (select starId from stars_in_movies where starId = @starsid) is null then
						begin
							insert into stars_in_movies(starId, movieId) values(@starsid, movieid);
                            insert into edits(edit) values("Existing star inserted into existing movie cast.");
                        end;
					end if;
                end;
			else
				begin
					update id_helper set star_id = star_id + 1 where main =1;
					SET @v4 := (select star_id from id_helper where id_helper.main =1);
					set @newsid = concat('nm', @v4);
                    insert into stars(id, name, birthYear) values(@newsid, star,NULL);
                    insert into edits(edit) values("New star created and inserted into stars.");
                    insert into stars_in_movies(starId, movieId) values(@newsid, movieid);
                    insert into edits(edit) values("New star inserted into existing movie cast.");
                end;
			end if;
            
            if (select id from genres where name = genre) is not null then
				begin
					set @genresid := (select id from genres where name = genre);
					if (select genreId from genres_in_movies where genreId = @genresid) is null then
						begin
							insert into genres_in_movies(genreId, movieId) values(@genresid, movieid);
                            insert into edits(edit) values("Existing genre inserted into existing movie genres list.");
                        end;
					end if;
                end;
			else
				begin
					update id_helper set genre_id = genre_id + 1 where main =1;
					SET @v5 := (select genre_id from id_helper where id_helper.main =1);
                    insert into genres(id, name) values(@v5, genre);
                    insert into edits(edit) values("New genre created and inserted into genres.");
                    insert into genres_in_movies(genreId, movieId) values(@v5, movieid);
                    insert into edits(edit) values("New genre inserted into movies genres list.");
                end;
			end if;
        end;
	end if;
    select * from edits;
END $$

delimiter ;

