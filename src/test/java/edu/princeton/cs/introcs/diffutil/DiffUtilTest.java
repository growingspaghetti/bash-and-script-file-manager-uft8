/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.princeton.cs.introcs.diffutil;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryoji
 */
public class DiffUtilTest {
    
    public DiffUtilTest() {
    }

    @Test
    public void testSomeMethod() {
        final List<String> diff = DiffUtil.getUnifiedDiff("go right\ngo up", "go right\ngo down\ndig");
        final String joinedList = StringUtils.join(diff.toArray(), "\n");
        System.out.println(joinedList);
        assertEquals("< go up\n> go down\n> dig", joinedList);
    }
}
