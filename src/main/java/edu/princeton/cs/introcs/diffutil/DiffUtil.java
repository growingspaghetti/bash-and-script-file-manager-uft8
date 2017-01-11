/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.princeton.cs.introcs.diffutil;

import java.util.ArrayList;
import java.util.List;

public class DiffUtil {

    /**
     *
     * @param previousData
     * @param dataToBeCompared
     * @return
     */
    public static List<String> getUnifiedDiff(String previousData, String dataToBeCompared){
        if (dataToBeCompared == null) throw new IllegalArgumentException();
        String[] x   = previousData.split("\n");
        String[] y   = dataToBeCompared.split("\n");        
        List<String> diffList = new ArrayList<>();
        // number of lines of each file
        int M = x.length;
        int N = y.length;

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M+1][N+1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (x[i].equals(y[j]))
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }

        // recover LCS itself and print out non-matching lines to standard output
        int i = 0, j = 0;
        while(i < M && j < N) {
            if (x[i].equals(y[j])) {
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]) diffList.add("< " + x[i++]);
            else                                 diffList.add("> " + y[j++]);
        }

        // dump out one remainder of one string if the other is exhausted
        while(i < M || j < N) {
            if      (i == M) diffList.add("> " + y[j++]);
            else if (j == N) diffList.add("< " + x[i++]);
        }
        return diffList;
    }
}
