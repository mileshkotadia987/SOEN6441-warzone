package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.exceptions.AbsentTagException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

/**
 * This class tests the blank fields available in the map file.
 *
 * @author Brijesh Lakkad
 * @author CHARIT
 */
public class LoadMapServiceTest {
    private EditMapService d_editMapService;
    private static URL d_testCorruptedFilePath;
    private static URL d_testCorrectFilePath;

    /**
     * Sets the path to the files.
     */
    @BeforeClass
    public static void beforeClass() {
        Application l_application = new Application();
        l_application.handleApplicationStartup();
        VirtualMachine.getGameEngine().initialise();
        d_testCorruptedFilePath = LoadMapServiceTest.class.getClassLoader().getResource("test_map_files/test_blank_data_fields.map");
        d_testCorrectFilePath = LoadMapServiceTest.class.getClassLoader().getResource("map_files/solar.map");
    }

    /**
     * This method runs before the test case runs. This method initializes different objects required to perform test.
     */
    @Before
    public void before() {
        d_editMapService = new EditMapService();
        VirtualMachine.getGameEngine().getMapEditorEngine().initialise();
    }

    /**
     * This is a method that performs actual test. It test passes if .map file consists of any empty field.
     *
     * @throws Exception IOException
     */
    @Test(expected = AbsentTagException.class)
    public void testLoadCorruptedMap() throws Exception {
        // In Windows, URL will create %20 for space. To avoid, use the below logic.
        String l_url = new URI(d_testCorruptedFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
    }

    /**
     * This method loads the map file and expects the none exception.
     *
     * @throws Exception IOException
     */
    @Test(expected = Test.None.class)
    public void testLoadCorrectMapFile() throws Exception {
        // In Windows, URL will create %20 for space. To avoid, use the below logic.
        String l_url = new URI(d_testCorrectFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
    }
}
