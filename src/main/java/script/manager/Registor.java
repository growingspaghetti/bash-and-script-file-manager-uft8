/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import script.candidate.CandidateIO;
import script.sqlite.ConnectionProvider;

/**
 *
 * @author ryoji
 */
public class Registor {
    
    private static final Logger LOG = Logger.getLogger(Registor.class.getName());
    /* DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(new Date()); cannot be ordered with ASC/DESC */
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public void register(JSONObject query) {
        switch (RegisterOption.valueOf(query.getString("MODE"))) {
            case ALL_AS_RAN: registerAllAsRan(); return;
            case OVERRIDE:   overrideRecords(      query.getJSONArray(RegisterOption.OVERRIDE.name())); return;
            case RAN:        registerRecordsAsRan( query.getJSONArray(RegisterOption.RAN.name())); return;
        }
    }
    
    private void overrideRecords(JSONArray recordNumbers) {
        TreeMap<Integer, String> candidate = CandidateIO.readCandidate();
        Iterator iterator = recordNumbers.iterator();
        while (iterator.hasNext()) {
            final int candidateNo = Integer.parseInt((String)iterator.next());
            final File file = new File(candidate.get(candidateNo));
            try ( final Connection        con = ConnectionProvider.passConnection();
                  final PreparedStatement pps = con.prepareStatement("UPDATE script_registry SET contents = ?, sha = ?, timestamp = ? WHERE file_path = ?;");){
                final String contents  = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                final String sha       = DigestUtils.sha256Hex(contents);
                final String timestamp = SDF.format(new Date());
                final String path      = file.getCanonicalPath();
                pps.setString(4, path);
                pps.setString(1, contents);
                pps.setString(2, sha);
                pps.setString(3, timestamp);
                pps.addBatch();
                pps.executeBatch();
                LOG.log(Level.INFO, "Overrided {0}", path);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void registerRecordsAsRan(JSONArray recordNumbers) {
        TreeMap<Integer, String> candidate = CandidateIO.readCandidate();
        Iterator iterator = recordNumbers.iterator();
        while (iterator.hasNext()) {
            final int candidateNo = Integer.parseInt((String)iterator.next());
            final File file = new File(candidate.get(candidateNo));
            try ( final Connection        con = ConnectionProvider.passConnection();
                  final PreparedStatement pps = con.prepareStatement("INSERT INTO script_registry VALUES (?,?,?,?);");){
                final String contents  = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                final String sha       = DigestUtils.sha256Hex(contents);
                final String timestamp = SDF.format(new Date());
                final String path      = file.getCanonicalPath();
                pps.setString(1, path);
                pps.setString(2, contents);
                pps.setString(3, sha);
                pps.setString(4, timestamp);
                pps.addBatch();
                pps.executeBatch();
                LOG.log(Level.INFO, "Registered as ran {0}", path);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void registerAllAsRan() {
        final String scriptHome = EnvSetter.fetchHomePath();
        LOG.log(Level.INFO, "SCRIPT_HOME_PATH: {0}", scriptHome);
        LOG.log(Level.INFO, "Starting registerAllMarkAsRan().");
        try {
            List<File> files = (List<File>) FileUtils.listFiles(new File(scriptHome), new String[]{EnvSetter.fetchFileExtension()}, true);
            LOG.log(Level.INFO, "Number of scripts found: {0}", files.size());
            for (File file : files) {
                try ( final Connection        con = ConnectionProvider.passConnection();
                      final PreparedStatement pps = con.prepareStatement("INSERT OR REPLACE INTO script_registry VALUES (?,?,?,?);");){
                    final String contents  = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    final String sha       = DigestUtils.sha256Hex(contents);
                    final String timestamp = SDF.format(new Date());
                    final String path      = file.getCanonicalPath();
                    pps.setString(1, path);
                    pps.setString(2, contents);
                    pps.setString(3, sha);
                    pps.setString(4, timestamp);
                    pps.addBatch();
                    pps.executeBatch();
                    LOG.log(Level.INFO, "Registerd {0}", path);
                } catch (ClassNotFoundException | SQLException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
            LOG.log(Level.INFO, "Finished registerAllMarkAsRan().");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
        }
    }
    
    public static String fetchContents(String pathValue) {
        try ( final Connection  con = ConnectionProvider.passConnection();
              final Statement   stt = con.createStatement();) {
            final ResultSet res = stt.executeQuery("SELECT contents FROM script_registry WHERE file_path='" + pathValue + "';");
            res.next();
            return res.getString("contents");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
        }
    }
    
    public static String fetchSha(String pathValue) {
        try ( final Connection  con = ConnectionProvider.passConnection();
              final Statement   stt = con.createStatement();) {
            final ResultSet res = stt.executeQuery("SELECT sha FROM script_registry WHERE file_path='" + pathValue + "';");
            res.next();
            return res.getString("sha");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
        }
    }
    
    public static List<String> fetchFileList() {
        List<String> dbPathList = new ArrayList<>();
        try ( final Connection  con = ConnectionProvider.passConnection();
              final Statement   stt = con.createStatement();) {
            final ResultSet res = stt.executeQuery("SELECT file_path FROM script_registry ORDER BY file_path ASC;");
            while (res.next()) {
                dbPathList.add(res.getString("file_path"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
        }
        return dbPathList;
    }
}
/**
 *                 try (final InputStream in  = new FileInputStream(file)) {
 *                   final String contents  = IOUtils.toString(in);
 */