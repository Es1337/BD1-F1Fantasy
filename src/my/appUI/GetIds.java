/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.appUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kusmi
 */
public class GetIds {
    private final String dbaseURL = "jdbc:postgresql://localhost:5432/u8kusm";
    private final String dbUsername = "u8kusm";
    private final String dbPassword = "8kusm";
        
    public int getTeamId(String teamName, String year) {
        int teamId = -1;
                
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getTeamId = "SELECT t.team_id FROM project.teams t, project.season s WHERE t.name = ? AND t.season_id = s.season_id AND s.year = ?";
            PreparedStatement pst = connection.prepareStatement(getTeamId);
            pst.setString(1, teamName);
            pst.setString(2, year);
            ResultSet rs = pst.executeQuery();

            if(rs.next())
                teamId = rs.getInt(1);         
            
            rs.close();
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return teamId;
    }
    
    public int getRaceId(String location, String year) {
        int raceId = -1;
        
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getRaceId = "SELECT r.race_id FROM project.races r, project.season s WHERE r.location = ? AND r.season_id = s.season_id AND s.year=?";
            PreparedStatement pst = connection.prepareStatement(getRaceId);
            pst.setString(1, location);
            pst.setString(2, year);
            
            ResultSet rs = pst.executeQuery();
            if(rs.next()) raceId = rs.getInt(1);
            
            rs.close();
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return raceId;
    }
    
    public int getUserId(String username) {
        int userId = -1;
        
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getUserId = "SELECT u.user_id FROM project.users u WHERE u.email = ?";
            PreparedStatement pst = connection.prepareStatement(getUserId);
            pst.setString(1, username);
            
            ResultSet rs = pst.executeQuery();
            if(rs.next()) userId = rs.getInt(1);
            
            rs.close();
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return userId;
    }
    
    public int getPredictionId(String code, String year) {
        int predictionId = -1;
        
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getPredictionId = "SELECT ap.prediction_id FROM project.available_predictions ap, project.season s WHERE ap.code = ? AND ap.season_id = s.season_id AND s.year = ?";
            PreparedStatement pst = connection.prepareStatement(getPredictionId);
            pst.setString(1, code);
            pst.setString(2, year);
            
            ResultSet rs = pst.executeQuery();
            if(rs.next()) predictionId = rs.getInt(1);
            
            rs.close();
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return predictionId;
    }
}
