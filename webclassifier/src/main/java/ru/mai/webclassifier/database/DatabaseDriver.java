package ru.mai.webclassifier.database;

import java.sql.*;
import java.util.ArrayList;

import ru.mai.webclassifier.database.models.*;

public class DatabaseDriver {
    public ArrayList<BagWord> getBagWords() {
        ArrayList<BagWord> bagWords = new ArrayList<>();
        
        try (Connection conn = getConnect();
            PreparedStatement pstmt  = conn.prepareStatement(
                    "SELECT Word, PositionIndex FROM BagOfWord"
            )){
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bagWords.add(new BagWord( rs.getString(1), rs.getInt(2)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return bagWords;
    }
    
    public ArrayList<String> getDiscardedWords() {
        ArrayList<String> discardedWords = new ArrayList<>();
        
        try (Connection conn = getConnect();
            PreparedStatement pstmt  = conn.prepareStatement(
                    "SELECT Word FROM DiscardedWord"
            )){
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                discardedWords.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return discardedWords;
    }
    
    private Connection getConnect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:webclassifier.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}