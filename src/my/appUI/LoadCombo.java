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
import javax.swing.JComboBox;

/**
 * Pomocnicza klasa obsługująca ładowanie danych do JComboBox'ów
 * @author kusmi
 */
public class LoadCombo {
    private final String dbaseURL = "jdbc:postgresql://localhost:5432/u8kusm";
    private final String dbUsername = "u8kusm";
    private final String dbPassword = "8kusm";
    
    /**
     * Funkcja ładująca sezony do JComboBox w porządku od najnowszego
     * @param comboBox JComboBox do którego mają być załadowane sezony
     */
    public void loadSeasons(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT s.year "
                    + "FROM project.season s "
                    + "ORDER BY s.year desc";
            PreparedStatement st = connection.prepareStatement(query);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                comboBox.addItem((String) rs.getObject(1));
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja ładująca wyścigi do JComboBox w porządku kalendarzowym
     * @param comboBox JComboBox do którego zostaną załadowane wyścigi
     * @param year Rok sezonu z którego wyścigi mają być załadowane
     */
    public void loadRaces(JComboBox<String> comboBox, String year) {
        comboBox.removeAllItems();
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT r.location "
                    + "FROM project.races r, project.season s WHERE r.season_id = s.season_id AND s.year = ?"
                    + " ORDER BY r.fp1";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                comboBox.addItem((String) rs.getObject(1));
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja ładująca kierowców do JComboBox w porządku alfabetycznym wg nazwiska
     * @param comboBox JComboBox do któego są ładowani kierowcy
     * @param year Rok sezonu z którego są ładowani kierowcy
     */
    public void loadDrivers(JComboBox<String> comboBox, String year) {
        comboBox.removeAllItems();
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT d.fname || ' ' || d.lname "
                    + "FROM project.drivers d, project.season s WHERE d.season_id = s.season_id AND s.year = ?"
                    + " ORDER BY d.lname";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                comboBox.addItem((String) rs.getObject(1));
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja ładująca zespoły do JComboBox w porządku alfabetycznym wg nazwy zespołu
     * @param comboBox comboBox JComboBox do którego są ładowane zespoły
     * @param year Rok sezonu z którego są ładowane zespoły
     */
    public void loadTeams(JComboBox<String> comboBox, String year) {
        comboBox.removeAllItems();
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT t.name "
                    + "FROM project.teams t, project.season s WHERE t.season_id = s.season_id AND s.year = ?"
                    + " ORDER BY t.name";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                comboBox.addItem((String) rs.getObject(1));
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    /**
     * Funkcja ładująca przewidywania specjalne do JComboBox 
     * w porządku alfabetycznym wg kodu przewidywania
     * Kody DRIV1, DRIV2 i TEAM są odfiltrowywane
     * @param comboBox comboBox JComboBox do którego są ładowane przewidywania
     * @param year Rok sezonu z którego są ładowane przewidywania
     */
    public void loadPredicitions(JComboBox<String> comboBox, String year) {
        comboBox.removeAllItems();
        try ( Connection connection = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT ap.code "
                    + "FROM project.available_predictions ap, project.season s WHERE ap.season_id = s.season_id AND s.year = ?"
                    + " ORDER BY ap.code";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, year);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String code = (String) rs.getObject(1);
                if(!code.equals("DRIV1") && !code.equals("DRIV2") && !code.equals("TEAM"))
                    comboBox.addItem(code);
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
