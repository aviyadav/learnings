package com.sample.datacore.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSPivotUtil {

    private static Map<String, String> getColumnValuesAsCSV(ResultSet rs) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        
        rs = getResultSet();
                
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        int rowCount = 0;
        
        List<String> colCSV = new ArrayList<>(colCount);

        while(rs.next()) {
            for (int i = 0; i < colCount; i++) {
                if(colCSV.size() <= i) {
                    colCSV.add(i, "");
                }
                String rsValue = rs.getString(i+1);
                String existingValue = colCSV.get(i);
                
                if(rowCount == 0) {
                    colCSV.set(i, rsValue);
                } else {
                    colCSV.set(i, existingValue + ", " + rsValue);
                }
                
            }
            
            rowCount++;
        }
        
        for (int i = 1; i <= colCount; i++) {
            resultMap.put(rsmd.getColumnName(i), colCSV.get(i - 1));
        }
        
        return resultMap;
    }
    
    private static ResultSet getResultSet() {
        
        String query = "SELECT * FROM APP.CUSTOMER";
        Connection conn = getDBConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        if(conn != null) {
            try {
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(RSPivotUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rs;
    }
    
    private static Connection getDBConnection() {
        
        Connection con = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RSPivotUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return con;
    }
    
    public static void main(String[] args) throws SQLException {
        Map<String, String> finalResult =RSPivotUtil.getColumnValuesAsCSV(getResultSet());
        for (String key : finalResult.keySet()) {
            System.out.println("key - " + key + ": value - " + finalResult.get(key));
        }
    }
}
