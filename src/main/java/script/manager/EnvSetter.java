/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import script.sqlite.ConnectionProvider;

/**
 *
 * @author ryoji
 */
public class EnvSetter {
    
    private static final Logger LOG = Logger.getLogger(EnvSetter.class.getName());
            
    public void setScriptHome(final JSONObject query) {
        setEnv(query, SetScriptHomeOption.ABSOLUTE_PATH.name());
    }
    public void setExecTemplate(final JSONObject query) {
        setEnv(query, SetExecTemplateOption.EXEC_TEMPLATE.name());
    }
    public void setFileExtension(final JSONObject query) {
        setEnv(query, SetFileExtensionOption.FILE_EXTENSION.name());
    }
    private void setEnv(final JSONObject query, final String envkey) {
        try ( final Connection        con = ConnectionProvider.passConnection();
              final PreparedStatement pps = con.prepareStatement("INSERT OR REPLACE INTO environments VALUES (?,?);");){
            pps.setString(1, envkey);
	    pps.setString(2, query.getString(envkey));
            pps.addBatch();
            pps.executeBatch();
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public static String fetchHomePath() {
        return fetchEnv(SetScriptHomeOption.ABSOLUTE_PATH.name());
    }
    public static String fetchExecTemplate() {
        return fetchEnv(SetExecTemplateOption.EXEC_TEMPLATE.name());
    }
    public static String fetchFileExtension() {
        return fetchEnv(SetFileExtensionOption.FILE_EXTENSION.name());
    }
    private static String fetchEnv(final String envkey) {
        try ( final Connection  con = ConnectionProvider.passConnection();
              final Statement   stt = con.createStatement();) {
            final ResultSet res = stt.executeQuery("SELECT value FROM environments WHERE env='" + envkey + "';");
            res.next();
            return res.getString("value");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
        }
    }
}