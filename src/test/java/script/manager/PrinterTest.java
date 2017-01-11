/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryoji
 */
public class PrinterTest {
    
    public PrinterTest() {
    }

    @Test(expected = org.json.JSONException.class)
    public void testPrintWrongFormat() {
        new Printer().print(new JSONObject());
    }
    
    public void testSkipWrongQuery() {
        new Printer().print(new JSONObject().put("MODE", "modedoesntexist"));
    }
}
