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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Pomocnicza klasa obsługująca ładowanie widoków klasyfikacji do tabel 
 * @author kusmi
 */
public class LoadRank {
    private final String dbaseURL = "jdbc:postgresql://localhost:5432/u8kusm";
    private final String dbUsername = "u8kusm";
    private final String dbPassword = "8kusm";
    
    /**
     * Funkcja ładująca widok klasyfikacji kierowców
     * @param comboBox JComboBox z wybranym rokiem sezonu dla którego jest ładowana klasyfikacja
     * @param table Tabela do której jest łądowana klasyfikacja
     */
    public void loadDriversRank(JComboBox comboBox, JTable table) {
        String year = (String) comboBox.getSelectedItem();
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT row_number() over(order by d.points desc)"
                    + " as Position, d.fname, d.lname, t.name, d.points "
                    + "FROM project.drivers d, project.teams t, project.season s "
                    + "WHERE t.team_id = d.team_id AND s.season_id = d.season_id AND s.year = ?";
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
                rows[1] = rs.getObject(2) + " " + rs.getObject(3);
                rows[2] = rs.getObject(4);
                rows[3] = rs.getObject(5);

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
     * Funkcja ładująca widok klasyfikacji zespołów
     * @param comboBox JComboBox z wybranym rokiem sezonu dla którego jest ładowana klasyfikacja
     * @param table Tabela do której jest łądowana klasyfikacja
     */
    public void loadTeamsRank(JComboBox comboBox, JTable table) {
        String year = (String)comboBox.getSelectedItem();
        try ( Connection connection
                = DriverManager.getConnection(dbaseURL, dbUsername, dbPassword)) {
            String query = "SELECT row_number() over(order by t.points desc) "
                    + "as Position, t.name, t.points "
                    + "FROM project.teams t, project.season s "
                    + "WHERE t.season_id = s.season_id AND s.year = ?";
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
}
