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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Pomocnicza klasa aplikacji obsługująca ładowanie widoków do tabel
 * @author kusmi
 */
public class LoadTable {
    private final String dbaseURL = "jdbc:postgresql://localhost:5432/u8kusm";
    private final String dbUsername = "u8kusm";
    private final String dbPassword = "8kusm";
    
    GetIds getIds = new GetIds();
    
    /**
     * Funkcja wyświetlająca widok zespołów w tabeli
     * @param table Tabela w któej ma być wyświetlony widok
     * @param year Rok sezonu dla którego mają byż załadowane zespoły
     */
    public void loadTeamsTable(JTable table, String year) {
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT t.team_id, t.name, t.points "
                    + "FROM project.teams t, project.season s "
                    + "WHERE t.season_id = s.season_id AND s.year = ? ORDER BY t.points DESC";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            ResultSet rs = st.executeQuery();

            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }

            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] rows = new Object[col];
                rows[0] = rs.getObject(1);
                rows[1] = rs.getObject(2);
                rows[2] = rs.getObject(3);

                ((DefaultTableModel) table.getModel()).
                        insertRow(rs.getRow() - 1, rows);
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja wyświetlająca widok kierowców w tabeli
     * @param table Tabela w któej ma być wyświetlony widok
     * @param year Rok sezonu dla którego mają być załadowani kierowcy
     */
    public void loadDriversTable(JTable table, String year) {
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT d.driver_id, d.fname || ' ' || d.lname as name, t.name as team, d.points "
                    + "FROM project.drivers d, project.teams t, project.season s "
                    + "WHERE t.team_id = d.team_id AND d.season_id = s.season_id AND s.year = ? ORDER BY d.points DESC";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            ResultSet rs = st.executeQuery();

            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }

            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] rows = new Object[col];
                rows[0] = rs.getObject(1);
                rows[1] = rs.getObject(2);
                rows[2] = rs.getObject(3);
                rows[3] = rs.getObject(4);

                ((DefaultTableModel) table.getModel()).
                        insertRow(rs.getRow() - 1, rows);
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja wyświetlająca widok wyścigów w tabeli
     * @param table Tabela w któej ma być wyświetlony widok
     * @param year Rok sezonu dla którego mają być załadowane wyścigi
     */
    public void loadRacesTable(JTable table, String year) {
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT r.race_id, r.location, r.track, r.fp1, r.fp2, r.fp3, r.quali, r.race "
                    + "FROM project.races r, project.season s "
                    + "WHERE r.season_id = s.season_id AND s.year = ? ORDER BY fp1";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            ResultSet rs = st.executeQuery();

            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }

            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] rows = new Object[col];
                rows[0] = rs.getObject(1);
                rows[1] = rs.getObject(2);
                rows[2] = rs.getObject(3);
                rows[3] = rs.getObject(4);
                rows[4] = rs.getObject(5);
                rows[5] = rs.getObject(6);
                rows[6] = rs.getObject(7);
                rows[7] = rs.getObject(8);

                ((DefaultTableModel) table.getModel()).
                        insertRow(rs.getRow() - 1, rows);
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja wyświetlająca widok wyników w tabeli
     * @param table Tabela dla której w której ma być wyświetlony widok
     * @param location Lokalizacja wyścigu któego wyniki mają być wyświetlone
     * @param year Rok sezonu w którym odbywa się wyścig
     */
    public void loadResultsTable(JTable table, String location, String year) {
        int raceId = getIds.getRaceId(location, year);
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            if(raceId != -1) {
                String query = "SELECT d.fname || ' ' || d.lname, rr.position, rr.dnf, rr.fastest_lap, pd.points "
                        + "FROM project.race_results rr, project.drivers d, project.points_dict pd "
                        + "WHERE rr.race_id = ? AND d.driver_id = rr.driver_id AND rr.position = pd.position";
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, raceId);
                ResultSet rs = st.executeQuery();

                while (table.getRowCount() > 0) {
                    ((DefaultTableModel) table.getModel()).removeRow(0);
                }

                int col = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] rows = new Object[col];
                    rows[0] = rs.getObject(1);
                    rows[1] = rs.getObject(2);
                    rows[2] = rs.getObject(3);
                    rows[3] = rs.getObject(4);
                    int points = rs.getInt(5);
                    if(rs.getInt(4) == 1 && rs.getInt(2) < 10)
                        points += 1;
                    rows[4] = points;

                    ((DefaultTableModel) table.getModel()).
                            insertRow(rs.getRow() - 1, rows);
                }
                rs.close();
                st.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
  
    }
    
    /**
     * Funkcja wyszukująca użytkowników i wyświetlająca ich widok w tabeli
     * @param table Tabela w któej ma być wyświetlony widok
     * @param userString Fragment nazwy użytkownika który ma zostać znaleziony
     */
    public void findUsers(JTable table, String userString) {        
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String getUser = "SELECT u.email, u.fname || ' ' || u.lname, u.admin_privileges FROM project.users u WHERE u.email LIKE ?";
            PreparedStatement pst = connection.prepareStatement(getUser);
            pst.setString(1, userString+'%');
            ResultSet rs = pst.executeQuery();
            
            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }

            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] rows = new Object[col];
                rows[0] = rs.getObject(1);
                rows[1] = rs.getObject(2);
                boolean admin = (rs.getInt(3) == 1)?true:false;
                rows[2] = admin;


                ((DefaultTableModel) table.getModel()).
                        insertRow(rs.getRow() - 1, rows);
            }
            
            rs.close();
            pst.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja wyświetlająca widok przewidywań w tabeli 
     * @param table Tabela w której ma być wyświetlony widok
     * @param username Nazwa użytkownika którego przewidywania będą wyświetlone
     * @param location Lokalizacja wyścigu dla którego przewidywania mają być wyświetlone
     * @param year Rok sezonu w którym odbywa się wyścig
     */
    public void loadPredictionsTable(JTable table, String username, String location, String year) {
        int predValue;
        int raceId = getIds.getRaceId(location, year);
        int userId = getIds.getUserId(username);
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            if(raceId != -1) {
                String query = "SELECT up.title, ap.code "
                        + "FROM project.user_predictions up, project.available_predictions ap "
                        + "WHERE up.user_id = ? AND up.race_id = ? AND ap.prediction_id = up.prediction_id";
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, userId);
                st.setInt(2, raceId);
                
                ResultSet rs = st.executeQuery();

                while (table.getRowCount() > 0) {
                    ((DefaultTableModel) table.getModel()).removeRow(0);
                }

                int col = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] rows = new Object[col+1];
                    rows[0] = rs.getObject(1);
                    rows[1] = rs.getObject(2);
                    rows[2] = 0;
                    
                    ((DefaultTableModel) table.getModel()).
                            insertRow(rs.getRow() - 1, rows);
                }
                rs.close();
                st.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
  
    }
}
