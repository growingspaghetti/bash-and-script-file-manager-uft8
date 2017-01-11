/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.util.LinkedHashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ryoji
 */
public class QueryUtils {
    /**
     * @param args args[0] = initbashsmdb, sethome...
     * @return JSON query
     * @throws UnsupportedOperationException if args[0] is not a correct command line option.
     */
    public static String buildInternalQuery(String[] args) {
        if (args[0].equals("initbashsmdb")) {
            return "{\"GOAL\":\"" + Goal.INIT_BASHSM_DB.name() + "\"}";
        }
        if (args[0].equals("sethome")) {
            final Goal goal = Goal.SET_SCRIPT_HOME;
            JSONObject o = new JSONObject("{\"GOAL\":\"" + goal.name() + "\"}");
            return o.put(goal.name(), new JSONObject().put(SetScriptHomeOption.ABSOLUTE_PATH.name(), args[1])).toString();
        }
        if (args[0].equals("setextension")) {
            final Goal goal = Goal.SET_FILE_EXTENSION;
            JSONObject o = new JSONObject("{\"GOAL\":\"" + goal.name() +"\"}");
            return o.put(goal.name(), new JSONObject().put(SetFileExtensionOption.FILE_EXTENSION.name(), args[1])).toString();
        }
        if (args[0].equals("registerallasran")) {
            return "{\"GOAL\":\"REGISTER\",\"REGISTER\":{\"MODE\":\"" + RegisterOption.ALL_AS_RAN.name() + "\"}}";
        }
        if (args[0].equals("printnotran")) {
            return "{\"GOAL\":\"PRINT\",\"PRINT\":{\"MODE\":\"" + PrintOption.NOT_RAN.name() + "\"}}";
        }
        if (args[0].equals("printhashdiff")) {
            return "{\"GOAL\":\"PRINT\",\"PRINT\":{\"MODE\":\"" + PrintOption.DIFFER.name() + "\"}}";
        }
        if (args[0].equals("printscriptnumber")) {
            return "{\"GOAL\":\"PRINT\",\"PRINT\":{\"MODE\":\"" + PrintOption.SCRIPT_NO.name() + "\"}}";
        }
        if (args[0].equals("printscript")) {
            final Goal goal = Goal.PRINT;
            Set<String> numbers = new LinkedHashSet();
            for (int i = 1; i < args.length; i++) {
                numbers.add(args[i]);
            }
            JSONObject goalJson = new JSONObject("{\"GOAL\":\"" + goal.name() + "\"}");
            goalJson.put(goal.name(), new JSONObject().put("MODE", PrintOption.SCRIPT_CONTENTS.name()).put(PrintOption.SCRIPT_CONTENTS.name(), new JSONArray(numbers)));
            return goalJson.toString();
        }
        if (args[0].equals("overridediff")) {
            final Goal goal = Goal.REGISTER;
            Set<String> numbers = new LinkedHashSet();
            for (int i = 1; i < args.length; i++) {
                numbers.add(args[i]);
            }
            JSONObject goalJson = new JSONObject("{\"GOAL\":\"" + goal.name() + "\"}");
            goalJson.put(goal.name(), new JSONObject().put("MODE", RegisterOption.OVERRIDE.name()).put(RegisterOption.OVERRIDE.name(), new JSONArray(numbers)));
            return goalJson.toString();
        }
        if (args[0].equals("registerasran")) {
            final Goal goal = Goal.REGISTER;
            Set<String> numbers = new LinkedHashSet();
            for (int i = 1; i < args.length; i++) {
                numbers.add(args[i]);
            }
            JSONObject goalJson = new JSONObject("{\"GOAL\":\"" + goal.name() + "\"}");
            goalJson.put(goal.name(), new JSONObject().put("MODE", RegisterOption.RAN.name()).put(RegisterOption.RAN.name(), new JSONArray(numbers)));
            return goalJson.toString();
        }
        if (args[0].equals("setexectemplate")) {
            final Goal goal = Goal.SET_EXEC_TEMPLATE;
            JSONObject o = new JSONObject("{\"GOAL\":\"" + goal.name() + "\"}");
            o.put(goal.name(), new JSONObject().put(SetExecTemplateOption.EXEC_TEMPLATE.name(), args[1]));
            return o.toString();
        }
        if (args[0].equals("execute")) {
            final Goal goal = Goal.EXECUTE;
            Set<String> numbers = new LinkedHashSet();
            for (int i = 1; i < args.length; i++) {
                numbers.add(args[i]);
            }
            JSONObject goalJson = new JSONObject("{\"GOAL\":\"" + goal.name() + "\"}");
            goalJson.put(goal.name(), new JSONArray(numbers));
            return goalJson.toString();
        }
        throw new UnsupportedOperationException("args[0] not defined. args[0]:" + args[0]);
    }
}
