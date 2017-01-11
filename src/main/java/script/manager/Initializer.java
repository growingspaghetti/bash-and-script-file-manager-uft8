/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import script.sqlite.ConnectionProvider;


/**
 *
 * @author ryoji
 */
public class Initializer {
    private static final String CREATE_PATH_TABLE  = "CREATE TABLE IF NOT EXISTS environments (env VARCHAR(128) PRIMARY KEY ASC, value VARCHAR(128));";
    private static final String CREATE_SSMDB_TABLE = "CREATE TABLE IF NOT EXISTS script_registry (file_path VARCHAR(128) PRIMARY KEY ASC, contents TEXT, sha VARCHAR(128), timestamp varchar(16));";
    public void initialize() {
        try ( final Connection  con = ConnectionProvider.passConnection();
              final Statement   stt = con.createStatement();) {
            stt.execute(CREATE_PATH_TABLE);
            stt.execute(CREATE_SSMDB_TABLE);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
