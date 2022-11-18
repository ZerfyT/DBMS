package com.nipun.dbms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nipun
 */
public class DBManager {

    private static final DBManager dbManager = new DBManager();
    private static Connection conn;
    private static Statement stmt;
    private PreparedStatement pstmt;

    private DBManager() {
    }

    public static DBManager getInstance() {
        return dbManager;
    }

    /**
     * ++ Return Selected Database Connection
     *
     * @param db Database name to connect.
     * @return Return true if successfully connected to custom db. Else return
     *         false.
     */
    public boolean getDBConnection(String db) {
        conn = DBUtils.getConnection(db);
        if (!(conn == null)) {
            stmt = DBUtils.getStatement(conn);
            return true;
        }
        return false;
    }

    public void closeDBConnection() {
        DBUtils.closeConnection(conn, stmt);
    }

    public boolean createNewDatabase(String db) {
        return DBUtils.createDatabase(stmt, db);
    }

    public boolean deleteSelectedDatabase(String db) {
        return DBUtils.deleteDatabase(stmt, db);
    }

    public boolean createNewTable(String tb, DefaultListModel<String> columns) {
        List<String> tmp = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++) {
            tmp.add(i, columns.get(i));
        }
        return DBUtils.createTable(stmt, tb, tmp);
    }

    public boolean deleteSelectedTable(String tb) {
        return DBUtils.deleteTable(stmt, tb);
    }

    public DefaultTableModel setTableView(String tb) {
        DefaultTableModel model = new DefaultTableModel(0, 0);

        List<String> columnList = DBUtils.getColumns(stmt, tb, true);
        List<Vector<String>> dataList = DBUtils.getData(stmt, tb);

        model.setColumnIdentifiers(columnList.toArray());

        //table_view.setModel(model);
        //for (int i = 0; i <= dataList.size(); i++) {
        for (Vector<String> row : dataList) {
            model.addRow(row);
        }
        return model;
    }

    public int insertDataToTable(String tb, String data) {
        return DBUtils.insertData(stmt, tb, data);
    }

    //    private boolean preparedExecute(String sql, String... params) {
//        try {
//
//            System.out.println(params.length);
//            for (int i = 0; i < params.length; i++) {
//                stmt.execute(MessageFormat.format(sql, params));
//                pstmt.setString(i + 1, params[i]);
//            }
//            return pstmt.execute();
//        } catch (SQLException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }
    /*
     * dataTypes.add("INTEGER");
     * dataTypes.add("SMALLINT");
     * dataTypes.add("BIGINT");
     * dataTypes.add("FLOAT");
     * dataTypes.add("DOUBLE");
     * dataTypes.add("DECIMAL");
     * dataTypes.add("DATE");
     * dataTypes.add("TIME");
     * dataTypes.add("DATETIME");
     * dataTypes.add("YEAR");
     * dataTypes.add("CHAR");
     * dataTypes.add("VARCHAR");
     * dataTypes.add("ENUM");
     */
//    return dataTypes ;
//}
    public boolean deleteAllDataInTable(String currentTB) {
        return DBUtils.deleteAllData(stmt, currentTB);
    }
}
