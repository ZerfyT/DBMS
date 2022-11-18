package com.nipun.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;

/**
 *
 * @author Nipun
 */
public class DBDataStore {

    private static final String DATASTORE_DB = "dbmanager";
    private static final String TB_DB_LIST = "database_list";
    private static final String TB_TB_LIST = "table_list";
    private static final DBDataStore dbDataStore = new DBDataStore();
    private static Connection conn;
    private static Statement stmt;
    private static PreparedStatement pstmt;

    public static DBDataStore getInstance() {
        return dbDataStore;
    }

    private DBDataStore() {
    }

    /**
     * Init Datastore for custom databases and tables
     *
     * @return Return true if Datastore db connection successful. Else return
     *         false.
     */
    public boolean initDataStore() {
        String sql1 = "CREATE DATABASE IF NOT EXISTS {0}";
        String sql2 = "CREATE TABLE IF NOT EXISTS {0}(db_name VARCHAR(20) PRIMARY KEY)";
        String sql3 = "CREATE TABLE IF NOT EXISTS {0}(tb_name VARCHAR(20) PRIMARY KEY, db_name VARCHAR(20), FOREIGN KEY(db_name) REFERENCES {1}(db_name) ON DELETE CASCADE ON UPDATE CASCADE);";

        // Connect to MYSQL Server.
        conn = DBUtils.getConnection("");

        if (conn == null) {
            // Connection Error. Can't connect to MYSQL Server.
            return false;
        } else {
            stmt = DBUtils.getStatement(conn);
            // Create Datastore DB.
            DBUtils.execute(stmt, MessageFormat.format(sql1, DATASTORE_DB));

            // Change Connection to Datastore DB.
            conn = DBUtils.getConnection(DATASTORE_DB);

            if (conn == null) {
                // Datastore DB Connection Error. Can't Connet to Datastore DB.
                return false;
            } else {
                stmt = DBUtils.getStatement(conn);
                // Create Tables for Datastore DB.
                DBUtils.execute(stmt, MessageFormat.format(sql2, TB_DB_LIST));
                DBUtils.execute(stmt, MessageFormat.format(sql3, TB_TB_LIST, TB_DB_LIST));
                // Successfully connected to Datastore DB.
                return true;
            }
        }
    }

    /**
     * Get All Custom Created Database List from Datastore DB.
     *
     * @return Datastore Database List
     */
    public List<String> getDatabases() {
        Console.print("Getting Databases...");
        String sql = "SELECT db_name FROM {0}";
        return DBUtils.executeQuery(stmt, MessageFormat.format(sql, TB_DB_LIST));
    }

    /**
     * Get All Tables in Database
     */
    public List<String> getTables(String db) {

        Console.print("Getting Tables...");
        String sql = "SELECT tb_name FROM {0} WHERE db_name = {1}";
        return DBUtils.executeQuery(stmt, MessageFormat.format(sql, TB_TB_LIST, "'" + db + "'"));
    }

    public void addDBToDataStore(String db) {

        try {
            pstmt = conn.prepareStatement("INSERT INTO " + TB_DB_LIST + "(db_name) VALUES(?);");
            pstmt.setString(1, db);
            pstmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
//        StringBuilder sql = new StringBuilder("INSERT INTO ").append(TB_DB_LIST).append("(db_name)");
//        sql.append(" VALUES(").append(db).append(")");
//        StringBuilder sql = new StringBuilder("INSERT INTO ");
//        sql.append("`").append(DATASTORE_DB).append("`.`").append(TB_DB_LIST).append("`(");
//        sql.append(") VALUES(").append(db).append(")");
//
//        DBUtils.execute(stmt, sql.toString());
    }

    public void deleteDBFromDataStore(String db) {

        try {
            pstmt = conn.prepareStatement("DELETE FROM " + TB_DB_LIST + " WHERE db_name = ?");
            pstmt.setString(1, db);
            pstmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

//        StringBuilder sql = new StringBuilder("DELETE FROM ").append(TB_DB_LIST);
//        sql.append(" WHERE ").append("db_name = ").append(db);
//
//        DBUtils.execute(stmt, sql.toString());
    }

    public void addTBToDataStore(String db, String tb) {

        try {
            pstmt = conn.prepareStatement("INSERT INTO " + TB_TB_LIST + "(db_name, tb_name) VALUES(?, ?);");
            pstmt.setString(1, db);
            pstmt.setString(2, tb);
            pstmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
//        StringBuilder sql = new StringBuilder("INSERT INTO ").append(TB_DB_LIST).append("(db_name)");
//        sql.append(" VALUES(").append(db).append(")");
//        StringBuilder sql = new StringBuilder("INSERT INTO ");
//        sql.append("`").append(DATASTORE_DB).append("`.`").append(TB_DB_LIST).append("`(");
//        sql.append(") VALUES(").append(db).append(")");
//
//        DBUtils.execute(stmt, sql.toString());
    }

    public void deleteTBFromDataStore(String tb) {

        try {
            pstmt = conn.prepareStatement("DELETE FROM " + TB_TB_LIST + " WHERE tb_name = ?");
            pstmt.setString(1, tb);
            pstmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

//        StringBuilder sql = new StringBuilder("DELETE FROM ").append(TB_DB_LIST);
//        sql.append(" WHERE ").append("db_name = ").append(db);
//
//        DBUtils.execute(stmt, sql.toString());
    }

}
