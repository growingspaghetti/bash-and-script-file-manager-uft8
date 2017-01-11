/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import script.candidate.CandidateIO;

/**
 *
 * @author ryoji
 */
public class Executor {
    private final static Logger LOG = Logger.getLogger(Executor.class.getName());
    private final static String SHELL_FILE_NAME = "bashsmcommand.sh";
    public  final static String PLACE_HOLDER    = "[SCRIPT_FILE]";
    
    public void execute(JSONArray recordNumbers) {
        final String template = EnvSetter.fetchExecTemplate();
        TreeMap<Integer, String> candidate = CandidateIO.readCandidate();
        Iterator iterator = recordNumbers.iterator();
        while (iterator.hasNext()) {
            final int candidateNo = Integer.parseInt((String)iterator.next());
            executeCommand(template.replace(PLACE_HOLDER, "\"" + candidate.get(candidateNo) + "\""));
        }
    }
    
    /**
     * http://stackoverflow.com/questions/16217060/psql-script-not-committing-when-executed-from-java
     * @param command
     */
    public void executeCommand(String command) {
        try {
            FileUtils.writeStringToFile(new File(SHELL_FILE_NAME), command, StandardCharsets.UTF_8);
            ArrayList<String> cmdargs = new ArrayList<>();
            cmdargs.add("bash");
            cmdargs.add(SHELL_FILE_NAME);
            ProcessBuilder pb = new ProcessBuilder(cmdargs);
            LOG.log(Level.INFO, "Wrote the commmand file. {1}, COMMAND: {0}", new Object[]{command, pb.command().toString()});
//            Map<String, String> env = pb.environment();
//            env.put("PGPASSWORD", "user");
            Process p = pb.start();
            try (final BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                 final BufferedReader stdIn    = new BufferedReader(new InputStreamReader(p.getInputStream()));) {
                String s;
                while ((s = stdError.readLine()) != null) {
                    LOG.log(Level.WARNING, s);
                }
                while ((s = stdIn.readLine()) != null) {
                    LOG.log(Level.INFO, s);
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
/**
 * IOUtils.write(command, new FileOutputStream("bashsmcommand.sh"), StandardCharsets.UTF_8);
 */