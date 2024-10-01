package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // MySQL Operations
        try {
            // Connect to MySQL
            Connection mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");

            // CREATE operation in MySQL
            String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?)";
            PreparedStatement insertStmt = mysqlConn.prepareStatement(insertSQL);
            insertStmt.setString(1, "John Doe");
            insertStmt.setString(2, "john@example.com");
            insertStmt.executeUpdate();

            // READ operation in MySQL
            Statement readStmt = mysqlConn.createStatement();
            ResultSet rs = readStmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println("MySQL User: " + rs.getString("name") + ", " + rs.getString("email"));
            }

            // UPDATE operation in MySQL
            String updateSQL = "UPDATE users SET email = ? WHERE name = ?";
            PreparedStatement updateStmt = mysqlConn.prepareStatement(updateSQL);
            updateStmt.setString(1, "john.new@example.com");
            updateStmt.setString(2, "John Doe");
            updateStmt.executeUpdate();

            // DELETE operation in MySQL
            //Testing
            String deleteSQL = "DELETE FROM users WHERE name = ?";
            PreparedStatement deleteStmt = mysqlConn.prepareStatement(deleteSQL);
            deleteStmt.setString(1, "John Doe");
            deleteStmt.executeUpdate();

            mysqlConn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // MongoDB Operations
        try {
            // Create a MongoClient with retryWrites disabled
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/?retryWrites=false");

            // Connect to MongoDB
            MongoDatabase mongoDatabase = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = mongoDatabase.getCollection("users");

            // CREATE operation in MongoDB
            Document newUser = new Document("name", "Jane Doe")
                    .append("email", "jane@example.com");
            collection.insertOne(newUser);

            // READ operation in MongoDB
            for (Document doc : collection.find()) {
                System.out.println("MongoDB User: " + doc.getString("name") + ", " + doc.getString("email"));
            }

            // UPDATE operation in MongoDB
            collection.updateOne(new Document("name", "Jane Doe"),
                    new Document("$set", new Document("email", "jane.new@example.com")));

            // DELETE operation in MongoDB
            collection.deleteOne(new Document("name", "Jane Doe"));

            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
