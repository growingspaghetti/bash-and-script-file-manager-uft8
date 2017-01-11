/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.json.JSONObject;

/**
 *
 * @author ryoji
 */
public class App {
    public static final Logger LOG = Logger.getGlobal();
    public static final String LOGGER_PROPERTY_FILE = "bashsm.properties";
    
    private void printUsage() {
        LOG.log(Level.INFO, "Usage:");
        LOG.log(Level.INFO, " java -jar bsm.jar initbashsmdb");
        LOG.log(Level.INFO, " java -jar bsm.jar setextension \"*|file_extention_like_sql_or_sh|*\"");
        LOG.log(Level.INFO, " java -jar bsm.jar sethome \"*|script_root_directory_absolute_path|*\"");
        LOG.log(Level.INFO, " java -jar bsm.jar registerallasran");
        LOG.log(Level.INFO, " java -jar bsm.jar printnotran");
        LOG.log(Level.INFO, " java -jar bsm.jar printhashdiff");
        LOG.log(Level.INFO, " java -jar bsm.jar printscriptnumber");
        LOG.log(Level.INFO, " java -jar bsm.jar printscript 1 2");
        LOG.log(Level.INFO, " java -jar bsm.jar overridediff 1 2");
        LOG.log(Level.INFO, " java -jar bsm.jar registerasran 1 2 3");
        LOG.log(Level.INFO, " java -jar bsm.jar setexectemplate \"*|bashcommand_like_/usr/bin/psql_-d_MYDB_-a_-f_" + Executor.PLACE_HOLDER + "|*\"");
        LOG.log(Level.INFO, " java -jar bsm.jar execute 1 2");
    }
    
    public static void main(String[] args) throws IOException {
        try (final InputStream in = new FileInputStream(LOGGER_PROPERTY_FILE);) {
            LogManager.getLogManager().readConfiguration(in);
        }
        final App app = new App();
        if (ArrayUtils.isEmpty(args)) {
            app.printUsage();
            return;
        }
        String internalJsonQuery;
        try {
            internalJsonQuery = QueryUtils.buildInternalQuery(args);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            app.printUsage();
            return;
        }
        app.parse(internalJsonQuery);
    }
   
    /**
     * parse internal JSON query
     * @param jsonQuery use QueryUtils
     */
    private void parse(String jsonQuery) {
        LOG.log(Level.INFO, "Query internally being used: {0}", jsonQuery);
        JSONObject json;
        try {
            json = new JSONObject(jsonQuery);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }
        Goal goal;
        try {
            goal = Goal.valueOf(json.getString(Param.GOAL.name()));
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "EXCEPTION: {0}. Calling fallback... returning Goal.NONE", new Object[]{ex.toString()});
            goal = Goal.NONE;
        } 
        switch (goal) {
            case INIT_BASHSM_DB:     new Initializer().initialize(); return;
            case SET_SCRIPT_HOME:    new EnvSetter().setScriptHome(    json.getJSONObject(Goal.SET_SCRIPT_HOME.name())); return;
            case SET_FILE_EXTENSION: new EnvSetter().setFileExtension( json.getJSONObject(Goal.SET_FILE_EXTENSION.name())); return;
            case SET_EXEC_TEMPLATE:  new EnvSetter().setExecTemplate(  json.getJSONObject(Goal.SET_EXEC_TEMPLATE.name())); return;
            case REGISTER:           new Registor().register(          json.getJSONObject(Goal.REGISTER.name())); return;
            case PRINT:              new Printer().print(              json.getJSONObject(Goal.PRINT.name())); return;
            case EXECUTE:            new Executor().execute(           json.getJSONArray(Goal.EXECUTE.name())); return;
            default: printUsage(); return;
        }
    }
}
