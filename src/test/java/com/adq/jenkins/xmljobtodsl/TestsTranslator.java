
package com.adq.jenkins.xmljobtodsl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestsTranslator {

    @Test
    public void testReadFile() throws IOException {
        assertEquals(TestsConstants.getDSL(),
                new DSLTranslator(new JobDescriptor[] { TestsConstants.getJobDescriptor() }).toDSL());
    }
}