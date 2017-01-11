/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryoji
 */
public class QueryUtilsTest {
    
    public QueryUtilsTest() {
    }

    @Test
    public void testQueryBuilder() {
        Map<String[], String> argsAndExpectedQuery = new LinkedHashMap<>();
        argsAndExpectedQuery.put(new String[]{"initbashsmdb"}, 
                "{\"GOAL\":\"INIT_BASHSM_DB\"}");
        argsAndExpectedQuery.put(new String[]{"setextension", "sql"}, 
                "{\"GOAL\":\"SET_FILE_EXTENSION\",\"SET_FILE_EXTENSION\":{\"FILE_EXTENSION\":\"sql\"}}");
        argsAndExpectedQuery.put(new String[]{"sethome", "/home/ryoji/scripts"}, 
                "{\"GOAL\":\"SET_SCRIPT_HOME\",\"SET_SCRIPT_HOME\":{\"ABSOLUTE_PATH\":\"/home/ryoji/scripts\"}}");
        argsAndExpectedQuery.put(new String[]{"registerallasran"}, 
                "{\"GOAL\":\"REGISTER\",\"REGISTER\":{\"MODE\":\"ALL_AS_RAN\"}}");
        argsAndExpectedQuery.put(new String[]{"printnotran"}, 
                "{\"GOAL\":\"PRINT\",\"PRINT\":{\"MODE\":\"NOT_RAN\"}}");
        argsAndExpectedQuery.put(new String[]{"printhashdiff"}, 
                "{\"GOAL\":\"PRINT\",\"PRINT\":{\"MODE\":\"DIFFER\"}}");
        argsAndExpectedQuery.put(new String[]{"printscriptnumber"}, 
                "{\"GOAL\":\"PRINT\",\"PRINT\":{\"MODE\":\"SCRIPT_NO\"}}");
        argsAndExpectedQuery.put(new String[]{"printscript", "1", "5"}, 
                "{\"PRINT\":{\"SCRIPT_CONTENTS\":[\"1\",\"5\"],\"MODE\":\"SCRIPT_CONTENTS\"},\"GOAL\":\"PRINT\"}");
        argsAndExpectedQuery.put(new String[]{"overridediff", "2", "3"}, 
                "{\"REGISTER\":{\"OVERRIDE\":[\"2\",\"3\"],\"MODE\":\"OVERRIDE\"},\"GOAL\":\"REGISTER\"}");
        argsAndExpectedQuery.put(new String[]{"registerasran", "1"}, 
                "{\"REGISTER\":{\"MODE\":\"RAN\",\"RAN\":[\"1\"]},\"GOAL\":\"REGISTER\"}");
        argsAndExpectedQuery.put(new String[]{"setexectemplate", "cat [SCRIPT_FILE]"}, 
                "{\"GOAL\":\"SET_EXEC_TEMPLATE\",\"SET_EXEC_TEMPLATE\":{\"EXEC_TEMPLATE\":\"cat [SCRIPT_FILE]\"}}");
        argsAndExpectedQuery.put(new String[]{"execute", "1"}, 
                "{\"EXECUTE\":[\"1\"],\"GOAL\":\"EXECUTE\"}");

        for (Map.Entry<String[], String> entrySet : argsAndExpectedQuery.entrySet()) {
            String[] key = entrySet.getKey();
            String value = entrySet.getValue();
            final String result = QueryUtils.buildInternalQuery(key);
            System.out.println(result);
            assertEquals(value, result);
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testQueryBuilderDoesntExistCommand() {
        QueryUtils.buildInternalQuery(new String[]{"inutdb"});
    }
}
