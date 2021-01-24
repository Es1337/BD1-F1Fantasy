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
import java.sql.Timestamp;

/**
 * Pomocnicza klasa obsługująca modyfikację rekordów w tabelach
 * @author kusmi
 */
public class Updates {
    private final String dbaseURL = "jdbc:postgresql://localhost:5432/u8kusm";
    private final String dbUsername = "u8kusm";
    private final String dbPassword = "8kusm";
    
    private GetIds getIds;
    
    public void Updates() {
        getIds = new GetIds();
    }
    
    /**
     * Funkcja zmieniająca uprawnienia administratora użytkownika,
     * zwracająca powodzenie tej operacji
     * @param username Nazwa użytkownika dla którego mają być zmienione uprawnienia
     * @param privilege Wartość uprawnienia któa ma być ustawiona
     * @return True, jeśli przewidywania usunięte, inaczej false
     */
    public boolean updateAdminPriv(String username, int privilege) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "UPDATE project.users SET admin_privileges = ? WHERE email = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, privilege);
            pst.setString(2, username);
            
            if(pst.executeUpdate() == 1)
                success = true;
            
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Funkcja modyfikująca rekord w tabeli teams
     * zwracająca powodzenie tej operacji
     * @param teamName Nazwa zespołu do ustawienia
     * @param points Punkty zespołu do ustawienia
     * @param teamId ID zespołu który jest modyfikowany
     * @return True, jeśli przewidywania usunięte, inaczej false
     */
    public boolean updateTeam(String teamName, int points, int teamId) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "UPDATE project.teams SET name = ?, points = ? WHERE team_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, teamName);
            pst.setInt(2, points);
            pst.setInt(3, teamId);
            if(pst.executeUpdate() == 1) {
                success = true;
            }
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Funkcja modyfikująca rekord w tabeli drivers
     * zwracająca powodzenie tej operacji
     * @param year Rok sezonu do którego jest wpisany zespół kierowcy
     * @param fName Imię kierowcy do ustawienia
     * @param lName Nazwisko kierowcy do ustawienia
     * @param teamName Nazwa zespołu do ustawienia
     * @param points Punkty zespołu do ustawienia
     * @param driverId ID kierowcy który jest modyfikowany
     * @return True, jeśli przewidywania usunięte, inaczej false
     */
    public boolean updateDriver(String year, String fName, String lName, String teamName, int points, int driverId) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            int teamId = getIds.getTeamId(year, teamName);
            if(teamId != -1) {
                String query = "UPDATE project.drivers SET team_id = ?, fname = ?, lname = ?, points = ? WHERE driver_id = ?";
                PreparedStatement pst = connection.prepareStatement(query);
                
                pst.setInt(1, teamId);
                pst.setString(2, fName);
                pst.setString(3, lName);
                pst.setInt(4, points);
                pst.setInt(5, driverId);

                if(pst.executeUpdate() == 1)
                    success = true;

                pst.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return success;
    }
    
    /**
     * Funkcja modyfikująca rekord w tabeli races
     * zwracająca powodzenie tej operacji
     * @param location Lokalizacja wyścigu do ustawienia
     * @param track Tor do ustawienia
     * @param fp1 Timestamp FP1 do ustawienia
     * @param fp2 Timestamp FP2 do ustawienia
     * @param fp3 Timestamp FP3 do ustawienia
     * @param quali Timestamp kwalifikacji do ustawienia
     * @param race Timestamp wyścigu do ustawienia
     * @param raceId ID wyścigu do modyfikacji
     * @return True, jeśli przewidywania usunięte, inaczej false 
     */
    public boolean updateRace(String location, String track, Timestamp fp1, Timestamp fp2, Timestamp fp3, Timestamp quali, Timestamp race, int raceId) {
        boolean success = false;
        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {            
            String query = "UPDATE project.races SET location = ?, track = ?, fp1 = ?, fp2 = ?, fp3 = ?, quali = ?, race = ? WHERE race_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, location);
            pst.setString(2, track);
            pst.setTimestamp(3, fp1);
            pst.setTimestamp(4, fp2);
            pst.setTimestamp(5, fp3);
            pst.setTimestamp(6, quali);
            pst.setTimestamp(7, race);
            pst.setInt(8, raceId);

            if(pst.executeUpdate() == 1) {
                success = true;
            }

            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return success;
    }
}
