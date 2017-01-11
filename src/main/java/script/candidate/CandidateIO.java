/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.candidate;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

/**
 *
 * @author ryoji
 */
public class CandidateIO {
    private static final File CANDIDATE_FILE = new File("bashsm-candidates.json");
    private static final Type TREE_MAP_TYPE  = new TypeToken<TreeMap<Integer, String>>(){}.getType();
    
    public static void saveCandidates(Map<Integer, String> candidates) {
        try {
            FileUtils.writeStringToFile(CANDIDATE_FILE, new JSONObject(candidates).toString(2), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(CandidateIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static TreeMap<Integer, String> readCandidate() {
        if(!CANDIDATE_FILE.exists()) return new TreeMap<>();
        try {
            return new Gson().fromJson(FileUtils.readFileToString(CANDIDATE_FILE, StandardCharsets.UTF_8), TREE_MAP_TYPE);
        } catch (IOException | JsonSyntaxException ex) {
            Logger.getLogger(CandidateIO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
        }
    }
}
