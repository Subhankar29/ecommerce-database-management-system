use lotrfinal_1;

-- 1.) Write a procedure track_character(character_name)  that accepts a character name and returns a result set that contains a list of the other characters that the provided character has encountered. The result set should contain the character’s name, the region name, the book name, and the name of the encountered character.
DROP PROCEDURE IF EXISTS track_character; 
DELIMITER $$
CREATE PROCEDURE track_character(IN char_name varchar(60))
BEGIN 
SELECT 
    character1_name AS character_name,
    character2_name AS encountered_name,
    region_name,
    book.title AS book_name
FROM
    lotr_first_encounter
        INNER JOIN
    lotr_book AS book USING (book_id)
WHERE
    character1_name = char_name
UNION ALL
SELECT 
    character2_name AS character_name,
    character1_name AS encountered_name,
    region_name,
    book.title AS book_name
FROM
    lotr_first_encounter
        INNER JOIN
    lotr_book AS book USING (book_id)
WHERE
    character2_name = char_name;
END$$

DELIMITER ;

call track_character('Frodo');
call track_character('aragorn');

-- 2.)  Write a procedure track_region(region) that accepts a region name and returns a result set that contains the region name, the book name, the number of encounters for that region and the leader of that region. 
DROP PROCEDURE IF EXISTS track_region;
DELIMITER $$
CREATE PROCEDURE track_region(IN region_name varchar(60))
BEGIN
SELECT 
    encounter.region_name,
    book.title AS book_name,
    COUNT(encounter.region_name) AS number_of_encounters,
    region.leader
FROM
    lotr_first_encounter AS encounter
        INNER JOIN
    lotr_book AS book ON book.book_id = encounter.book_id
        INNER JOIN
    lotr_region AS region ON region.region_name = encounter.region_name
WHERE
    encounter.region_name = region_name
GROUP BY encounter.region_name;
END$$
DELIMITER ;

call track_region('rivendell');
call track_region('bree');


-- 3)  Write a function named strongerSpecie(sp1,sp2). 
-- It accepts 2 species and returns 1 if sp1 has a size larger than sp2, 0 if they have equal sizes, else -1

DROP FUNCTION IF EXISTS strongerSpecie;
DELIMITER $$
CREATE FUNCTION strongerSpecie(sp1 varchar(45) , sp2 varchar(45))
RETURNS int
DETERMINISTIC READS SQL DATA
BEGIN
DECLARE sp1_size int;
DECLARE sp2_size int;
SELECT size into sp1_size from lotr_species where species_name= sp1;
SELECT size into sp2_size from lotr_species where species_name= sp2;
IF sp1_size > sp2_size THEN RETURN 1;
ELSEIF sp1_size = sp2_size THEN RETURN 0;
ELSE RETURN -1;
END IF;
END $$

DELIMITER ;
select strongerSpecie('elf','ent');
select strongerSpecie('elf','human');

-- 4.)  Write a function named region_most_encounters(character_name) that accepts a character name and returns the name of the region where the character has had the most encounters. 
DROP FUNCTION IF EXISTS region_most_encounters;
DELIMITER $$
CREATE FUNCTION region_most_encounters(char_name varchar(60))
RETURNS VARCHAR(60)
DETERMINISTIC READS SQL DATA
BEGIN 
DECLARE REGION VARCHAR(60);
SELECT table1.region_name INTO REGION FROM 
(
SELECT 
    character1_name AS character_name,
    character2_name AS encountered_name,
    region_name,
    book.title AS book_name
FROM
    lotr_first_encounter
        INNER JOIN
    lotr_book AS book USING (book_id)
WHERE
    character1_name = char_name
UNION ALL
SELECT 
    character2_name AS character_name,
    character1_name AS encountered_name,
    region_name,
    book.title AS book_name
FROM
    lotr_first_encounter
        INNER JOIN
    lotr_book AS book USING (book_id)
WHERE
    character2_name = char_name
) as table1
GROUP BY region_name
ORDER BY count(region_name) DESC
LIMIT 1;
RETURN REGION;
END$$

DELIMITER ;
SELECT region_most_encounters('Frodo');
SELECT region_most_encounters('aragorn');
SELECT region_most_encounters('gandalf');
SELECT * FROM lotr_first_encounter;

-- 5.) Write a function named home_region_encounter(character_name) that accepts a character name and returns TRUE if the character has had a first encounter in his homeland. FALSE if the character has not had a first encounter in his homeland. or NULL if the character’s homeland is not known. 
DROP FUNCTION IF EXISTS home_region_encounter;
DELIMITER $$
CREATE FUNCTION home_region_encounter(char_name varchar(80)) RETURNS BOOLEAN
DETERMINISTIC READS SQL DATA
BEGIN
DECLARE encounter_region_name varchar(80);
DECLARE first_encounter_region varchar(80);
DECLARE check_if_homeland varchar(80);

SELECT homeland INTO check_if_homeland FROM lotr_character WHERE character_name = char_name;

IF isnull(check_if_homeland) THEN RETURN NULL;
END IF; 

SELECT region_name INTO encounter_region_name  FROM 
(
SELECT character1_name as char_name, region_name FROM lotr_first_encounter
UNION 
SELECT character2_name as char_name, region_name FROM lotr_first_encounter
) AS encounters
WHERE char_name = char_name AND region_name = check_if_homeland LIMIT 1;

IF isnull(encounter_region_name) THEN RETURN FALSE; 
ELSE RETURN TRUE; 
END IF;
END $$

DELIMITER ;
SELECT home_region_encounter('aragorn');
SELECT home_region_encounter('Frodo');



-- 6.)   Write a function named encounters_in_num_region(region_name)  that accepts a region’s name as an argument and returns the number of encounters for that region. (10 points)
DROP FUNCTION IF EXISTS region_most_encounters;
DELIMITER $$

CREATE FUNCTION region_most_encounters(char_name varchar(60))
RETURNS INT
DETERMINISTIC READS SQL DATA
BEGIN 
DECLARE COUNT INT;

SELECT ec.number_of_encounters INTO COUNT FROM 
(
SELECT 
    encounter.region_name,
    book.title AS book_name,
    COUNT(encounter.region_name) AS number_of_encounters,
    region.leader
FROM
    lotr_first_encounter AS encounter
        INNER JOIN
    lotr_book AS book ON book.book_id = encounter.book_id
        INNER JOIN
    lotr_region AS region ON region.region_name = encounter.region_name
WHERE
    encounter.region_name = char_name
GROUP BY encounter.region_name
) AS EC;

RETURN COUNT;
END$$

DELIMITER ;

SELECT region_most_encounters('rivendell');
SELECT region_most_encounters('rohan');
SELECT region_most_encounters('bree');

-- 7.) Write a procedure  named fellowship_encounters(book) that accepts a book’s name and returns the fellowship characters (all fields in the character table)  having first encounters in that book.
DROP PROCEDURE IF EXISTS fellowship_encounter;
DELIMITER $$
CREATE PROCEDURE fellowship_encounters(IN book_name varchar(60))
BEGIN 
SELECT characters.* FROM lotr_character as characters where 
character_name in 
(
select DISTINCT enc.character_name as name FROM
(SELECT character1_name AS character_name, region_name, encounter.book_id, book.title as book_name FROM lotr_first_encounter as encounter
INNER JOIN lotr_book as book ON book.book_id = encounter.book_id
UNION 
SELECT character2_name AS character_name, region_name, encounter.book_id,  book.title as book_name from lotr_first_encounter as encounter
INNER JOIN lotr_book as book ON book.book_id = encounter.book_id) as enc
WHERE enc.book_name = book_name);
END$$

DELIMITER ;
call fellowship_encounters('the two towers');
call fellowship_encounters('the fellowship of the ring');

-- 8.) Modify the books table to contain a field called encounters_in_book and write a procedure called initialize_encounters_count(book)  that accepts a book id and  initializes the field to the number of encounters that occur in that book for the current encounters table. The book table modification can occur outside or inside of the procedure.
DROP PROCEDURE IF EXISTS alter_table;
DELIMITER $$
CREATE procedure alter_table()
BEGIN
ALTER Table lotr_book
ADD COLUMN encounters_in_book INT;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS initialize_encounters_count;
DELIMITER $$
CREATE procedure initialize_encounters_count(IN book_id_input INT) 
BEGIN
DECLARE COUNT_ENCOUNTER INT;

SELECT 
    COUNT(encounter.book_id) AS number_of_encounters INTO COUNT_ENCOUNTER
FROM
    lotr_first_encounter AS encounter
        INNER JOIN
    lotr_book AS book ON book.book_id = encounter.book_id
        INNER JOIN
    lotr_region AS region ON region.region_name = encounter.region_name
WHERE
    book.book_id = book_id_input
GROUP BY book.title;

UPDATE lotr_book set encounters_in_book = COUNT_ENCOUNTER WHERE book_id = book_id_input;

END$$

DELIMITER ;
call alter_table();
CALL initialize_encounters_count(1);
CALL initialize_encounters_count(2);
CALL initialize_encounters_count(3);
SELECT * FROM lotr_book;

-- 9.)
DROP TRIGGER IF EXISTS firstencounters_after_insert;
DELIMITER $$
CREATE TRIGGER firstencounters_after_insert AFTER INSERT ON lotr_first_encounter
FOR EACH ROW BEGIN
DECLARE encounter_count INT;
SELECT count(character1_name) INTO encounter_count FROM lotr_first_encounter encounter WHERE book_id = NEW.book_id;
UPDATE lotr_book set encounters_in_book = encounter_count WHERE book_id = NEW.book_id;

END $$

DELIMITER ;

INSERT INTO lotr_first_encounter (character1_name, character2_name, book_id, region_name) VALUES ('Legolas', 'Frodo', 1,'Rivendell');
INSERT INTO lotr_first_encounter (character1_name, character2_name, book_id, region_name) VALUES ('aragorn', 'elrond', 1,'Rivendell');
SELECT * FROM lotr_first_encounter;
SELECT * FROM lotr_book;

-- 10.) Create and execute a prepared statement from the SQL workbench that calls home_region_encounter with the argument ‘Aragorn’. Use a user session variable to pass the argument to the function.
PREPARE home_encounter FROM 'SELECT home_region_encounter(?)';
SET @VARIABLE_ARAGON1 = 'Aragorn';
EXECUTE home_encounter USING @VARIABLE_ARAGON1;
DEALLOCATE PREPARE home_encounter;

-- 11.)   Create and execute a prepared statement that calls region_most_encounters() with the argument ‘Aragorn’. Once again use a user session variable to pass the argument to the function. 
PREPARE region_encounter FROM 'SELECT region_most_encounters(?)';
SET @VARIABLE_ARAGON2 = 'Aragorn';
EXECUTE region_encounter USING @VARIABLE_ARAGON;
DEALLOCATE PREPARE region_encounter;

 




