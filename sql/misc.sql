CREATE OR REPLACE FUNCTION project.input_result(integer, integer, integer, integer, integer)
 RETURNS void AS '
DECLARE
race_id ALIAS FOR $1;
v_driver_id ALIAS FOR $2;
pos ALIAS FOR $3;
dnf ALIAS FOR $4;
fastest_lap ALIAS FOR $5;
v_points INTEGER;
v_team_id INTEGER;
BEGIN
SELECT d.team_id INTO v_team_id FROM project.drivers d WHERE v_driver_id = d.driver_id;
SELECT pd.points INTO v_points FROM project.points_dict pd WHERE pd.position = pos;
IF fastest_lap = 1 THEN
v_points = v_points + 1;
END IF;
INSERT INTO project.race_results VALUES(DEFAULT, race_id, v_driver_id, pos, v_points, dnf, fastest_lap);
UPDATE project.drivers SET points = points + v_points WHERE driver_id = v_driver_id;
UPDATE project.teams SET points = points + v_points WHERE v_team_id = team_id;
END;' LANGUAGE 'plpgsql';

-- DOESNT WORK YET
CREATE OR REPLACE FUNCTION project.evaluate_predictions(integer, integer)
 RETURNS void AS'
DECLARE
    v_race_id ALIAS FOR $1;
    v_season_id ALIAS FOR $2;
    v_title VARCHAR;
    v_code VARCHAR;
    v_team_id INTEGER;
    v_driver_id INTEGER;
    v_user_id INTEGER;
    user_row project.users%ROWTYPE;
    v_points INTEGER;
    v_fastest_lap INTEGER;
BEGIN
    v_points := 0;
    FOR user_row IN SELECT * FROM project.users
    LOOP
        FOR v_title, v_code IN SELECT up.title, ap.code
            FROM project.available_predictions ap, project.user_predictions up
            WHERE up.user_id = user_row.user_id AND up.prediction_id = ap.prediction_id
        LOOP
            IF v_code = ''DRIV1'' OR v_code = ''DRIV2'' THEN
                SELECT d.driver_id INTO v_driver_id
                    FROM project.drivers d
                    WHERE (d.fname || ' ' || d.lname) = v_title;
                SELECT rr.points INTO v_points
                    FROM project.race_results rr
                    WHERE rr.race_id = v_race_id AND rr.driver_id = v_driver_id;
                UPDATE project.ranking SET points=v_points WHERE user_row.user_id = user_id AND v_season_id = season_id;
            ELSIF v_code = ''TEAM'' THEN
                SELECT t.team_id INTO v_team_id FROM project.teams t WHERE t.name = v_title;
                SELECT sum(rr.points) INTO v_points
                    FROM project.drivers d JOIN (project.race_results rr JOIN project.races r ON r.race_id = rr.race_id) ON (d.driver_id = rr.driver_id)
                    WHERE d.season_id = v_season_id AND team_id = v_team_id;
                UPDATE project.ranking SET points=v_points WHERE user_row.user_id = user_id AND v_season_id = season_id;
            END IF;
        END LOOP;
    END LOOP;
END;'  LANGUAGE 'plpgsql';

