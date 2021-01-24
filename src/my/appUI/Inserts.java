/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.appUI;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.JOptionPane;

/**
 *
 * @author kusmi
 */
public class Inserts {
    private final String dbaseURL = "jdbc:postgresql://localhost:5432/u8kusm";
    private final String dbUsername = "u8kusm";
    private final String dbPassword = "8kusm";
    
    boolean addSeason(String year, String wdc, String wcc) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            
            String query = "INSERT INTO project.season VALUES(DEFAULT, ?, ?, ?)";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            st.setString(2, wdc);
            st.setString(3, wcc);

            if(st.executeUpdate() == 1) {
                success = true;
            }

            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, sqle.getMessage());
        }
        
        return success;
    }
    
    boolean addTeam(String year, String teamName, int points) {
        boolean success = false;
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getSeasonId = "SELECT s.season_id FROM project.season s WHERE s.year = ?";
            PreparedStatement pst = connection.prepareStatement(getSeasonId);
            pst.setString(1, year);
            ResultSet rs = pst.executeQuery();

            int seasonId = 0;
            if(rs.next()) {
                seasonId = rs.getInt(1);
            }
            String query = "INSERT INTO project.teams VALUES(DEFAULT, ?, ?, ?)";
            pst = connection.prepareStatement(query);
            pst.setInt(1, seasonId);
            pst.setString(2, teamName);
            pst.setInt(3, points);
            if(pst.executeUpdate() == 1) {
                success = true;
            }
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, sqle.getMessage());
        }
        return success;
    }
    
    boolean addDriver(String year, String fName, String lName, String teamName, int points) {
        boolean success = false;
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getSeasonId = "SELECT s.season_id FROM project.season s WHERE s.year = ?";
            PreparedStatement pst = connection.prepareStatement(getSeasonId);
            pst.setString(1, year);
            ResultSet rs = pst.executeQuery();
            
            int seasonId = 0;
            if(rs.next()) {
                seasonId = rs.getInt(1);
            }
            
            String getTeamId = "SELECT t.team_id FROM project.teams t, project.season s WHERE t.name = ? AND t.season_id = s.season_id AND s.year = ?";
            pst = connection.prepareStatement(getTeamId);
            pst.setString(1, teamName);
            pst.setString(2, year);
            rs = pst.executeQuery();

            int teamId = 0;
            if(rs.next()) teamId = rs.getInt(1);
            
            String query = "INSERT INTO project.drivers VALUES(DEFAULT, ?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(query);
            pst.setInt(1, seasonId);
            pst.setInt(2, teamId);
            pst.setString(3, fName);
            pst.setString(4, lName);
            pst.setInt(5, points);
            if(pst.executeUpdate() == 1) {
                success = true;
            }
            rs.close();
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, sqle.getMessage());
        }
        return success;
    }
    
    boolean addRace(String year, String location, String track, Timestamp fp1, Timestamp fp2, Timestamp fp3, Timestamp quali, Timestamp race) {
        boolean success = false;
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getSeasonId = "SELECT s.season_id FROM project.season s WHERE s.year = ?";
            PreparedStatement pst = connection.prepareStatement(getSeasonId);
            pst.setString(1, year);
            ResultSet rs = pst.executeQuery();
            
            int seasonId = 0;
            if(rs.next()) {
                seasonId = rs.getInt(1);
            }

            String query = "INSERT INTO project.races VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(query);
            pst.setInt(1, seasonId);
            pst.setString(2, location);
            pst.setString(3, track);
            pst.setTimestamp(4, fp1);
            pst.setTimestamp(5, fp2);
            pst.setTimestamp(6, fp3);
            pst.setTimestamp(7, quali);
            pst.setTimestamp(8, race);
            
            if(pst.executeUpdate() == 1) {
                success = true;
            }
            rs.close();
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
    
    boolean addResult(String location, String driverLName, int position, int dnf, int fastestLap) {
        boolean success = false;
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) { 
            String getRaceId = "SELECT r.race_id FROM project.races r WHERE r.location = ?";
            PreparedStatement pst = connection.prepareStatement(getRaceId);
            pst.setString(1, location);
            ResultSet rs = pst.executeQuery();
            int raceId = 0;
            if(rs.next()) raceId = rs.getInt(1);
            
            String getDriverId = "SELECT d.driver_id FROM project.drivers d WHERE d.fname || ' ' || d.lname = ?";
            pst = connection.prepareStatement(getDriverId);
            pst.setString(1, driverLName);
            rs = pst.executeQuery();
            int driverId = 0;
            if(rs.next()) driverId = rs.getInt(1);
            
            CallableStatement cst = connection.prepareCall("{call project.input_result(?, ?, ?, ?, ?)}");
            cst.setInt(1, raceId);
            cst.setInt(2, driverId);
            cst.setInt(3, position);
            cst.setInt(4, dnf);
            cst.setInt(5, fastestLap);
            
            cst.executeUpdate();
            success = true;

            rs.close();
            cst.close();
            pst.close();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Failed");
            sqle.printStackTrace();
        }
        
        return success;
    }
    
    public boolean addPrediction(int raceId, int userId, int predictionId, String title) {
        boolean success = false;
        try(Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "INSERT INTO project.user_predictions VALUES(DEFAULT, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, predictionId);
            pst.setInt(2, userId);
            pst.setString(3, title);
            pst.setInt(4, raceId);

            if(pst.executeUpdate() == 1)
                success = true;

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
}
