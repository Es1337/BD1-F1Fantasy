CREATE OR REPLACE FUNCTION project.validate_driver() returns trigger as '
DECLARE
    team_count INT;
    year VARCHAR;
    driver_count INT;
BEGIN
    team_count := (SELECT COUNT(*) FROM project.teams t WHERE t.team_id = NEW.team_id);
    year := (SELECT s.year FROM project.season s WHERE (s.season_id = NEW.season_id));
    driver_count := (SELECT COUNT(*) FROM project.drivers d WHERE d.fname = NEW.fname AND d.lname = NEW.lname AND d.season_id = NEW.season_id);
    IF (team_count = 0) THEN
        RAISE EXCEPTION ''Nie ma takiego zespołu w sezonie %'', year;
    END IF;
    IF (driver_count != 0) THEN
        RAISE EXCEPTION ''Kierowca % % już istnieje w sezonie %'', NEW.fname, NEW.lname, year;
    END IF;
    RETURN NEW;
END;

' language 'plpgsql';

CREATE OR REPLACE FUNCTION project.validate_team() RETURNS trigger AS'
DECLARE
    team_count INT;
    year VARCHAR;
BEGIN
    team_count := (SELECT COUNT(*) FROM project.teams t WHERE NEW.name = t.name AND NEW.season_id = t.season_id);
    year := (SELECT s.year FROM project.season s WHERE NEW.season_id = s.season_id);
    IF (team_count != 0) THEN
        RAISE EXCEPTION ''Team % already exists in season %.'', NEW.name, year;
    END IF;
    RETURN NEW;
END;' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION project.validate_season() RETURNS trigger AS '
DECLARE
    season_count INT;
BEGIN
    season_count := (SELECT COUNT(*) FROM project.season s WHERE NEW.year = s.year);
    IF (season_count != 0) THEN
        RAISE EXCEPTION ''Season % already exists.'', NEW.year;
    END IF;
    RETURN NEW;
END;'  LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION project.validate_username() RETURNS trigger AS '
    DECLARE
        user_count int;
    BEGIN
        user_count := (SELECT COUNT(*) FROM project.users u WHERE (u.email = NEW.email AND u.user_id != NEW.user_id));
        IF (user_count != 0) THEN
            RAISE EXCEPTION ''Nazwa uzytkownika % jest zajeta.'', NEW.email;
        END IF;
        RETURN NEW;
    END;' LANGUAGE 'plpgsql';

