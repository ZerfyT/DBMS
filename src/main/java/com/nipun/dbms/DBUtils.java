package com.nipun.dbms;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Nipun
 */
public class DBUtils {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/";
    public static final String USER_NAME = "root";
    public static final String PASSWD = "root";

    /**
     * ++ Return Database Connection
     *
     * @param db Database name to connect.
     * @return Instance of connection to given database.
     */
    public static Connection getConnection(String db) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL + db, USER_NAME, PASSWD);
            if (conn != null) {
                Console.print("Database Connected.");
            }
        } catch (SQLException | NullPointerException e) {
            Console.print("Please Check The Database Server Connection.");
        }
        return conn;
    }

    /**
     * ++ Return Statement for Connection
     */
    public static Statement getStatement(Connection conn) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stmt;
    }

    /**
     * ++ Close Database Connection
     */
    public static void closeConnection(Connection conn, Statement stmt) {
        //Console.print("Closing DB Connection...");
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
                Console.print("Connection Closed.");
            }
        } catch (SQLException e) {
        }
    }

    /**
     * ++ Create Database
     */
    public static boolean createDatabase(Statement stmt, String db) {
        String sql = "CREATE DATABASE " + db + ";";
        Console.print("Database Created.");
        return execute(stmt, sql);
    }

    /**
     * ++ Delete Database
     */
    public static boolean deleteDatabase(Statement stmt, String db) {
        String sql = "DROP DATABASE " + db + ";";
        Console.print("Database Deleted.");
        return execute(stmt, sql);

    }

    /**
     * Create Table
     */
    public static boolean createTable(Statement stmt, String tb, List<String> columns) {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append("`").append(tb).append("`").append("(");
        int count = 0;
        for (String column : columns) {
            sql.append(column);
            count += 1;
            if (count < columns.size()) {
                sql.append(",");
            }
        }
        sql.append(") ENGINE = InnoDB;");
        try {
            if (!(stmt.executeUpdate(sql.toString()) < 0)) {
                Console.print("Table Created.");
                return true;
            }
            Console.print("Table Does not Created.");
            return false;
        } catch (SQLException e) {
            Console.print(e.getMessage());
            return false;
        }
    }

    /**
     * Delete Table but only data
     */
    public static boolean deleteTableData(Statement stmt, String db, String tb) {
        String sql = "DELETE FROM " + tb + "`";
        Console.print("All Data Deleted.");
        return execute(stmt, sql);

    }

    /**
     * Delete Table
     */
    public static boolean deleteTable(Statement stmt, String tb) {
        String sql = "DROP TABLE " + tb;
        Console.print("Table Deleted.");
        return execute(stmt, sql);

//        if () {
//
//            return true;
//        } else {
//            Console.print("Table Not Deleted.");
//            return false;
//        }
    }

    /**
     * for CREATE / DROP DATABASE,
     */
    public static boolean execute(Statement stmt, String sql) {
        try {
            return stmt.execute(sql);
        } catch (MysqlDataTruncation e2) {
            Console.print("Invalid Data Type for Column. Please Check the Data Type.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * for SHOW DATABASES / TABLES,
     */
    public static List<String> executeQuery(Statement stmt, String sql) {
        List<String> list = new ArrayList<>();
        try {
            ResultSet r1 = stmt.executeQuery(sql);
            while (r1.next()) {
                list.add(r1.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean executePrepareDStatement(Statement stmt, String sql) {
        return false;
    }

    public static List<String> getDataTypes() {
        List<String> dataTypes = new ArrayList<>();
        for (DataType d : DataType.values()) {
            dataTypes.add(d.toString());
        }
        return dataTypes;
    }

    /*
     * View Full Table Window
     */
    public void viewTable(String db, String tb) {
        String sql = "SELECT * FROM `" + db + "`.`" + tb + "`";
//        Table t = new Table(tb, sql);
//        t.createTableView();
    }

    /**
     * Get All Columns in Table
     */
    public static List<String> getColumns(Statement stmt, String tb, boolean withDataType) {
        List<String> columns = new ArrayList<>();
        String sql = "SELECT * FROM " + tb + ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rm = rs.getMetaData();
            if (withDataType) {
                for (int i = 1; i <= rm.getColumnCount(); i++) {
                    columns.add(rm.getColumnName(i) + " (" + rm.getColumnTypeName(i) + ")");
                }
            } else {
                for (int i = 1; i <= rm.getColumnCount(); i++) {
                    columns.add(rm.getColumnName(i));
                }
            }
        } catch (SQLException e) {
            Console.print(e.getMessage());
        }
        return columns;
    }

    /**
     * Get All Data in Table
     */
    public static List<Vector<String>> getData(Statement stmt, String tb) {
        List<Vector<String>> rows = new Vector<>();
        String sql = "SELECT * FROM " + tb + ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rm = rs.getMetaData();

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                for (int i = 1; i <= rm.getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                rows.add(row);
            }

            //if (rs.isAfterLast()) {
            Vector<String> eRow = new Vector<>();
            for (int i = 1; i <= rm.getColumnCount(); i++) {

                eRow.add(" ");
            }
            rows.add(eRow);
            //}

        } catch (SQLException e) {
            Console.print(e.getMessage());
        }
        return rows;
    }


    /*
     * View Table with Specify Columns
     */
//    public void viewTableSelected(String db, String tb, String col) {
//        String[] columnArray = col.split(",");
//        StringBuilder columns = new StringBuilder();
//        int count = 0;
//        for (String column : columnArray) {
//            columns.append("`").append(column).append("`");
//            count += 1;
//            if (count < columnArray.length) {
//                columns.append(",");
//            }
//        }
//        String sql = "SELECT " + columns + " FROM " + db + "." + tb;
//        Table t = new Table(tb, sql);
//        t.createTableView();
//    }

    /*
     * Insert Data to a Table
     */
    public static int insertData(Statement stmt, String tb, String data) {
        List<String> columnData = new ArrayList<>(Arrays.asList(data.split(",")));
        List<String> columns = getColumns(stmt, tb, false);
        //List<List<String>> rowsAndData = new ArrayList<>();

//        for (String row : rows) {
//            rowsAndData.add(Arrays.asList(row.split(",")));
//        }
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append("`").append(tb).append("`(");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
            if (!(i == columns.size() - 1)) {
                sql.append(",");
            }
        }

        sql.append(") VALUES(");

        for (int i = 0; i < columnData.size(); i++) {
            sql.append("'").append(columnData.get(i)).append("'");
            if (!(i == columns.size() - 1)) {
                sql.append(",");
            }
        }

        sql.append(")");

        try {
            //        int rowCount = 0;
//        for (List<String> row : rowsAndData) {
//            for (int i = 0; i < row.size(); i++) {
//                sql.append(row.get(i));
//                if (!(i == row.size() - 1)) {
//                    sql.append(",");
//                }
//            }
//            sql.append(")");
//            rowCount += 1;
//            if (rowCount < rowsAndData.size()) {
//                sql.append(",(");
//            }
//        }
            stmt.executeUpdate(sql.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //if (execute(stmt, sql.toString())) {
        //    return 0;
        //} else {
        return 1;
        //}
    }

    public static boolean deleteAllData(Statement stmt, String tb) {
        String sql = "DELETE FROM {0}";
        return execute(stmt, MessageFormat.format(sql, tb));
    }

    /**
     * Update Data
     */
    public boolean updateSimple(Statement stmt, String tb, String column_new_data, String new_data, String column_condition, String condition) {
        // UPDATE `db`.`tb` SET `column_new_data` = 'string'/int WHERE `column_condition` condition
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append("`").append(tb).append("` ");
        sql.append("SET `").append(column_new_data).append("` = ").append(new_data);
        sql.append(" WHERE `").append(column_condition).append("` ").append(condition);
        Console.print(sql.toString());
        try {
            stmt.executeUpdate(sql.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getPrimaryKey(Connection conn, String db, String tb) {
        String keys = "";
        try {
            DatabaseMetaData m = conn.getMetaData();
            ResultSet rs = m.getPrimaryKeys(db, null, tb);

            while (rs.next()) {
                keys = rs.getString("COLUMN_NAME");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return keys;
    }
}
