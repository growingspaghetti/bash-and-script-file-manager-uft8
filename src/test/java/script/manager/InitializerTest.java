/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import script.sqlite.ConnectionProvider;

/**
 *
 * @author ryoji
 */
public class InitializerTest {
    
    private static final File DB_FILE = new File(ConnectionProvider.DB_FILE);
    
    public InitializerTest() {
    }

    @Test
    public void testDbCreation() throws IOException {
        App.main(new String[]{"initbashsmdb"});
        assertTrue(DB_FILE.exists());
    }
    
    @After
    public void testSchemaExist() {
        List<String> tables = fetchTables();
        System.out.println(StringUtils.join(tables.toArray(), ", "));
        assertArrayEquals(new String[]{"environments", "script_registry"}, tables.toArray());
        DB_FILE.delete();
    }
    
    private static List<String> fetchTables() {
        List<String> tableList = new ArrayList<>();
        try ( final Connection  con = ConnectionProvider.passConnection();
              final Statement   stt = con.createStatement();) {
            final ResultSet res = stt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;");
            while (res.next()) {
                tableList.add(res.getString("name"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return tableList;
    }
}
