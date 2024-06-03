package com.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLConnector {

    private static Connection connection;
    private static final String db = "rms_db";
    private static final String username = "root";
    private static final String password = "Vheshan37@37";
    private static final String url = "jdbc:mysql://localhost:3306/" + db + "?autoReconnect=true&useSSL=false";

    // Method to ensure connection is valid and establish if needed
    public static Connection ensureConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed() || !connection.isValid(2)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    // Method to create a Statement
    public static Statement createStatement() throws SQLException, ClassNotFoundException {
        ensureConnection();
        return connection.createStatement();
    }

    // Method to create a PreparedStatement
    public static PreparedStatement prepareStatement(String query) throws SQLException, ClassNotFoundException {
        ensureConnection();
        return connection.prepareStatement(query);
    }

    // Method to execute insert, update, delete operations with last inserted id retrieval
    public static Long iud(String query) throws SQLException, ClassNotFoundException {
        ensureConnection();
        Long lastId = null;
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lastId = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing IUD operation: " + e.getMessage());
        }
        return lastId;
    }

    // Method to execute a search query (unchanged)
    public static ResultSet search(String query) throws SQLException, ClassNotFoundException {
        Statement stmt = createStatement();
        return stmt.executeQuery(query);
    }
}
