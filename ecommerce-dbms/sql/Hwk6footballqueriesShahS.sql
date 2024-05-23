DROP DATABASE IF exists premiere;
CREATE DATABASE premiere;
use premiere;

-- Q 1.
CREATE TABLE STADIUM (
TEAM VARCHAR(225) NOT NULL primary key, 
VENUE VARCHAR(225) NOT NULL
);

CREATE TABLE EPL_MATCHES (
MATCH_NUMBER INT NOT NULL primary key , 
MATCH_DAY INT NOT NULL, 
DATE date  NOT NULL, 
TEAM1 VARCHAR(225) NOT NULL, 
TEAM2 VARCHAR(225) NOT NULL,
half_time_score_team_1 INT NOT NULL, 
half_time_score_team_2 INT NOT NULL, 
full_time_score_team_1 INT NOT NULL, 
full_time_score_team_2 INT NOT NULL,
constraint team_foreign_key_constraint1 FOREIGN KEY (TEAM1) references STADIUM(TEAM),
constraint team_foreign_key_constraint2 FOREIGN KEY (TEAM2) references STADIUM(TEAM)
);

CREATE TABLE EPL_MANAGERS(
MANAGER VARCHAR(225) NOT NULL,
TEAM VARCHAR(225) NOT NULL, 
NATIONALITY VARCHAR(225) NOT NULL, 
STATUS VARCHAR(225) NOT NULL, 
primary key (MANAGER, TEAM)
);

-- Q 2.
-- Please find the pdf hwk6EERShahS.pdf

-- Q 3.
-- Imported the .csv files into the tables using the Import table wizard tool.

-- Q 4. 
SELECT MATCH_NUMBER, TEAM1, TEAM2 FROM EPL_MATCHES WHERE full_time_score_team_1 > full_time_score_team_2 AND MATCH_DAY = 1;

-- Q 5. 
SELECT TEAM, COUNT(TEAM) AS count FROM EPL_MANAGERS GROUP BY TEAM HAVING COUNT(TEAM) > 1;

-- Q 6.
SELECT MANAGER, COUNT(*) AS count FROM EPL_MANAGERS GROUP BY MANAGER HAVING COUNT(*) > 1;

-- Q 7.
SELECT 
    MANAGER, TEAM, SUM(full_time_score_team_1) as scores
FROM
    EPL_MATCHES AS matches
        INNER JOIN
    EPL_MANAGERS AS manager ON matches.TEAM1 = manager.TEAM
WHERE
    manager.STATUS = 'active'
GROUP BY TEAM
ORDER BY scores DESC;

-- Q 8. 
SELECT man.MANAGER, COUNT(team) as count FROM (
SELECT TEAM1 AS team_playing from EPL_MATCHES as mat
WHERE 
mat.full_time_score_team_1 > mat.full_time_score_team_2
UNION ALL 
SELECT TEAM2 AS team_playing from EPL_MATCHES as mat
WHERE 
mat.full_time_score_team_1 < mat.full_time_score_team_2
) as matches
INNER JOIN 
EPL_MANAGERS as man 
on man.team = matches.team_playing WHERE man.STATUS = 'ACTIVE' group by matches.team_playing order by count DESC;

-- Q 9.
SELECT 
    stad.VENUE,
    SUM(mat.full_time_score_team_1 + mat.full_time_score_team_2) as total
FROM
    STADIUM AS stad
        INNER JOIN
    EPL_MATCHES AS mat ON mat.team1 = stad.TEAM
GROUP BY mat.TEAM1
ORDER BY total desc limit 1;

-- Q 10.
SELECT TEAM, COUNT(TEAM) as count FROM (
SELECT  TEAM1 AS TEAM, full_time_score_team_1, full_time_score_team_2  FROM EPL_MATCHES
UNION ALL
SELECT TEAM2 AS TEAM, full_time_score_team_1, full_time_score_team_2 FROM EPL_MATCHES
) AS TEAM_MERGED
WHERE 
full_time_score_team_2 = full_time_score_team_1
GROUP BY TEAM
ORDER BY count DESC;


-- Q 11. 
SELECT TEAM, COUNT(TEAM) as count FROM (
SELECT  TEAM1 AS TEAM, full_time_score_team_1, full_time_score_team_2  FROM EPL_MATCHES
WHERE 
full_time_score_team_2 = 0 
UNION ALL
SELECT TEAM2 AS TEAM, full_time_score_team_1, full_time_score_team_2 FROM EPL_MATCHES
WHERE 
full_time_score_team_1 = 0
) AS TEAM_MERGED
GROUP BY TEAM
ORDER BY count DESC LIMIT 5;

-- Q 12. 
SELECT * FROM EPL_MATCHES
WHERE DATE BETWEEN '2017-12-25' and '2018-01-03'
AND full_time_score_team_1 > 2;

-- Q 13. 
SELECT * FROM EPL_MATCHES 
WHERE
(
half_time_score_team_1 < half_time_score_team_2
AND 
full_time_score_team_1 > full_time_score_team_2
)
OR 
(
half_time_score_team_2 < half_time_score_team_1
AND 
full_time_score_team_2 > full_time_score_team_1
);

-- Q 14 change. 
SELECT team_playing FROM 
(
SELECT TEAM1 AS team_playing from EPL_MATCHES as mat
WHERE 
mat.full_time_score_team_1 > mat.full_time_score_team_2
UNION ALL 
SELECT TEAM2 AS team_playing from EPL_MATCHES as mat
WHERE 
mat.full_time_score_team_1 < mat.full_time_score_team_2
) as matches
group by(team_playing)  ORDER BY COUNT(team_playing) desc
LIMIT 5;

-- Q 15 CHANGE. 
SELECT 
    TEAM1,
    SCORED_SCORED_HOME,
    CONCEDED_GOAL_HOME,
    GOAL_SCORED_AWAY,
    GOAL_CONCEDED_AWAY
FROM
    (SELECT 
        TEAM1,
            AVG(full_time_score_team_1) AS SCORED_SCORED_HOME,
            AVG(full_time_score_team_2) AS CONCEDED_GOAL_HOME
    FROM
        EPL_MATCHES AS MATCHES1
    GROUP BY team1
    ) as home_team
    inner join
        (SELECT 
        TEAM2,
            AVG(full_time_score_team_2) AS GOAL_SCORED_AWAY,
            AVG(full_time_score_team_1) AS GOAL_CONCEDED_AWAY
    FROM
        EPL_MATCHES AS MATCHES2
    GROUP BY team2
    ) as away_team
    on home_team.team1 = away_team.team2;


