/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import edu.princeton.cs.introcs.diffutil.DiffUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import script.candidate.CandidateIO;

/**
 *
 * @author ryoji
 */
public class Printer {
    private static final Logger LOG = Logger.getLogger(Printer.class.getName());
    public void print(JSONObject query) {
        switch (PrintOption.valueOf(query.getString("MODE"))) {
            case NOT_RAN:         printNotRan(); return;
            case DIFFER:          printDiffer(); return;
            case SCRIPT_NO:       printScriptNo(); return;
            case SCRIPT_CONTENTS: printScriptContents(query.getJSONArray(PrintOption.SCRIPT_CONTENTS.name())); return;
        }
    }
    
    private void printScriptContents(final JSONArray recordNumbers) {
        TreeMap<Integer, String> candidate = CandidateIO.readCandidate();
        Iterator iterator = recordNumbers.iterator();
        while (iterator.hasNext()) {
            try {
                final int candidateNo = Integer.parseInt((String)iterator.next());
                LOG.log(Level.INFO, "{0}| {1}", new Object[]{StringUtils.leftPad(Integer.toString(candidateNo), 3), candidate.get(candidateNo)});
                final String contents = FileUtils.readFileToString(new File(candidate.get(candidateNo)), StandardCharsets.UTF_8);
                LOG.log(Level.INFO, contents);
            } catch (NumberFormatException | IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void printScriptNo() {
        TreeMap<Integer, String> candidate = CandidateIO.readCandidate();
        for (Map.Entry<Integer, String> entrySet : candidate.entrySet()) {
            final Integer no = entrySet.getKey();
            LOG.log(Level.INFO, "{0}| {1}", new Object[]{StringUtils.leftPad(Integer.toString(no), 3), entrySet.getValue()});
        }
    }
    
    private void printDiffer() {
        List<String> dbScriptPathes = Registor.fetchFileList();
        Map<Integer, String> candidate = new LinkedHashMap<>();
        int index = 1;
        for (String dbScriptPath : dbScriptPathes) {
            final File expectedScriptlFile = new File(dbScriptPath);
            if (expectedScriptlFile.exists()) {
                try {
                    final String contentsLive  = FileUtils.readFileToString(expectedScriptlFile, StandardCharsets.UTF_8);
                    final String shaLive       = DigestUtils.sha256Hex(contentsLive);
                    final String contentsDb    = Registor.fetchContents(dbScriptPath);
                    final String shaDb         = Registor.fetchSha(dbScriptPath);
                    if (!shaDb.equals(shaLive)) {
                        LOG.log(Level.INFO, "{0}| {1}", new Object[]{StringUtils.leftPad(Integer.toString(index), 3), dbScriptPath});
                        LOG.log(Level.INFO, StringUtils.join(DiffUtil.getUnifiedDiff(contentsDb, contentsLive).iterator(), "\n"));
                        candidate.put(index, dbScriptPath);
                        index++;
                    }
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        }
        CandidateIO.saveCandidates(candidate);
    }
    
    private void printNotRan() {
        List<String> diff = DiffUtil.getUnifiedDiff(StringUtils.join(Registor.fetchFileList().iterator(), "\n"), StringUtils.join(generateLivePathList().iterator(), "\n"));
        List<String> numdiff = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        Map<Integer, String> candidate = new LinkedHashMap<>();
        int index = 1;
        for (String diffline : diff) {
            if (diffline.startsWith("> ")) {
                final String path = StringUtils.substringAfter(diffline, "> ");//StringUtils.right(diffline, 3);
                numdiff.add("> " + StringUtils.leftPad(Integer.toString(index), 3) + "| " + path);
                candidate.put(index, path);
                index++;
            } else if (diffline.startsWith("< ")) {
                removed.add(diffline);
            }
        }
        numdiff.addAll(removed);
        CandidateIO.saveCandidates(candidate);
        LOG.log(Level.INFO, StringUtils.join(numdiff.iterator(), "\n"));
    }
    
    public static List<String> generateLivePathList() {
        List<File> files = (List<File>) FileUtils.listFiles(new File(EnvSetter.fetchHomePath()), new String[]{EnvSetter.fetchFileExtension()}, true);
        List<String> livePathList = (ArrayList<String>) CollectionUtils.collect(files, new Transformer() {
            @Override
            public String transform(Object input) {
                try {
                    return ((File)input).getCanonicalPath();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    throw new IllegalStateException(ex);
                }
            }
        });
        Collections.sort(livePathList);
        return livePathList;
    }
}
