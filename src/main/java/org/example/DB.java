package org.example;

import java.sql.*;
import java.util.*;

public class DB {

    Statement statement;
    PreparedStatement prepStatement;
    ResultSet resultSet;
    HashMap<Integer, String> data;
    Connection conn;

    public void connect() throws SQLException {
        Scanner in = new Scanner(System.in);

        String server = "//localhost\\sqlexpress", dB = "clope", encrypt = "false", user = "L", password = "123";
        System.out.print("Input jdbc connection string(default: //localhost\\sqlexpress): ");
        server = Objects.equals(in.nextLine(), "") ? server : in.nextLine();

        System.out.print("Input DB name(default: clope): ");
        dB = Objects.equals(in.nextLine(), "") ? dB : in.nextLine();

        System.out.print("Input encrypt parameter (default: false): ");
        encrypt = Objects.equals(in.nextLine(), "") ? encrypt : in.nextLine();

        System.out.print("Input username (default: L): ");
        user = Objects.equals(in.nextLine(), "") ? user : in.nextLine();

        System.out.print("Input username (default: 123): ");
        password = Objects.equals(in.nextLine(), "") ? password : in.nextLine();

        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        String dbURL = "jdbc:sqlserver:" + server + ";databaseName=" + dB + ";encrypt=" + encrypt + ";user=" + user + ";password=" + password;
        conn = DriverManager.getConnection(dbURL);
    }

    public int getNumRows() throws SQLException {
        statement = conn.createStatement();
        statement.execute("SELECT COUNT(*) FROM Transactions");
        resultSet = statement.getResultSet();
        resultSet.next();
        return resultSet.getInt(1);
    }


    public ArrayList<String> readNote(Integer num) throws SQLException {
        String query = "SELECT * FROM Transactions where id=?";
        prepStatement = conn.prepareStatement(query);
        prepStatement.setString(1, num.toString());
        prepStatement.execute();
        resultSet = prepStatement.getResultSet();
        resultSet.next();
        String id = resultSet.getString("id");
        String transaction = resultSet.getString("transaction");
        String cluster = resultSet.getString("cluster");
        return new ArrayList<>(Arrays.asList(id, transaction, cluster));
    }

    public void setCluster(ArrayList<String> data) throws SQLException {
        String query = "UPDATE Transactions SET cluster=? where id=?";
        prepStatement = conn.prepareStatement(query);
        prepStatement.setString(1, data.get(2));
        prepStatement.setString(2, data.get(0));
        prepStatement.execute();
    }

    public void disconnect() throws SQLException {
        conn.close();
        statement.close();
        prepStatement.close();
        resultSet.close();
    }
}
