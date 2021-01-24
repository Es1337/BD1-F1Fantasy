/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.appUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Pomocnicza klasa obsługująca usuwanie rekordów z tabel
 * @author kusmi
 */
public class Deletes {
    private final String dbaseURL = "jdbc:postgresql://localhost:5432/u8kusm";
    private final String dbUsername = "u8kusm";
    private final String dbPassword = "8kusm";
    
    /**
     * Funkcja usuwająca zespół z tabeli teams i zwraca powodzenie tej operacji
     * @param teamId ID zespołu do usunięcia
     * @return True, jeśli zespół usunięty, inaczej false
     */
    public boolean removeTeam(int teamId) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "DELETE FROM project.teams WHERE team_id = ?";

            PreparedStatement st = connection.prepareStatement(query);

            st.setInt(1, teamId);
            
            if(st.executeUpdate() == 1) {
                success = true;
            }

            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return success;
    }
    
    /**
     * Funkcja usuwająca kierowcę z tabeli drivers i zwraca powodzenie tej operacji
     * @param driverId ID kierowcy do usunięcia
     * @return True, jeśli kierowca usunięty, inaczej false
     */
    public boolean removeDriver(int driverId) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "DELETE FROM project.drivers WHERE driver_id = ?";
            
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, driverId);

            if(st.executeUpdate() == 1) {
                success = true;
            }

            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Funkcja usuwająca wyścig z tabeli races i zwraca powodzenie tej operacji
     * @param raceId ID zespołu do usunięcia
     * @return True, jeśli wyścig usunięty, inaczej false
     */
    public boolean removeRace(int raceId) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "DELETE FROM project.races WHERE race_id = ?";
            PreparedStatement st = connection.prepareStatement(query);

            st.setInt(1, raceId);

            if(st.executeUpdate() == 1) {
                success = true;
            }
            else {
                JOptionPane.showMessageDialog(null, "Error");
            }

            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Funkcja usuwająca użytkownika z tabeli users i zwraca powodzenie tej operacji
     * @param username Nazwa użytkownika do usunięcia
     * @return True, jeśli użytkownik usunięty, inaczej false
     */
    public boolean removeUser(String username) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "DELETE FROM project.users WHERE email = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            
            String message = "Are you sure you want to remove " + username + "?";
            boolean sure = (JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION);

            if(pst.executeUpdate() == 1 && sure)
                success = true;

            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Funkcja usuwająca przewidywanie z tabeli user_predictions i zwraca powodzenie tej operacji
     * @param userId ID użytkownika którego przewidywania są do usunięcia
     * @param raceId ID wyśćigu dla którego przewidywania są do usunięcia
     * @return True, jeśli przewidywania usunięte, inaczej false
     */
    public boolean removePredictions(int userId, int raceId) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "DELETE FROM project.user_predictions WHERE user_id = ? AND race_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, userId);
            pst.setInt(2, raceId);

            if(pst.executeUpdate() == 1)
                success = true;

            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
}
