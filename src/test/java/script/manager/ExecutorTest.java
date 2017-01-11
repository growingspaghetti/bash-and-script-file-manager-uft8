/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryoji
 */
public class ExecutorTest {
    
    public ExecutorTest() {
    }

    @Test
    public void testPlaceHolderName() {
        assertEquals("[SCRIPT_FILE]", Executor.PLACE_HOLDER);
    }
}
