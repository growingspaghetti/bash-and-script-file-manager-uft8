/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.candidate;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;

/**
 *
 * @author ryoji
 */
public class CandidateIOTest {
    
    public CandidateIOTest() {
    }

    @org.junit.Test
    public void testMapper() {
        Map<Integer, String> map = new HashMap<>();
        map.put(2, "/home/ryoji/ssm/sql-script-manager/dummy_file_system/sqlscripts/2016/Dec/01_12_2016.sql");
        map.put(1, "/home/ryoji/ssm/sql-script-manager/dummy_file_system/sqlscripts/2016/Dec/01_12_2016.sql");
        CandidateIO.saveCandidates(map);
        TreeMap<Integer, String> candidate = CandidateIO.readCandidate();
        System.out.print(candidate.keySet());
        Assert.assertTrue(ArrayUtils.contains(candidate.keySet().toArray(), 2));
    }
}
