/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryoji
 */
public class AppTest {
    
    public AppTest() {
    }

    @Test
    public void testPropertyFileExist() {
        assertTrue(new File(App.LOGGER_PROPERTY_FILE).exists());
    }
    
    /**
     * check it doesn't throw exception
     * @throws java.io.IOException
     */
    @Test
    public void testMainWithoutArgs() throws IOException {
        App.main(null);
        App.main(new String[]{});
        App.main(new String[]{"", ""});
    }
    
    /**
     * check it doesn't throw exception
     * @throws java.io.IOException
     */
    @Test
    public void testMainWithoutWrongArgs() throws IOException {
        App.main(new String[]{"inutdb"});
    }
}
