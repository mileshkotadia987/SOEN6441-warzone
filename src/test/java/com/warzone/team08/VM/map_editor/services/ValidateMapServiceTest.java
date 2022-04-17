package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.exceptions.InvalidMapException;
import com.warzone.team08.VM.exceptions.VMException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class tests <code>ValidateMapService</code> class for different types of validation requires for Map.
 */
public class ValidateMapServiceTest {
    private static Application d_application = new Application();
    private ValidateMapService d_validateMapService;
    private EditMapService d_editMapService;
    private URL d_testFilePath;

    /**
     * This method runs before the test case runs. This method initializes different objects required to perform test.
     */
    @BeforeClass
    public static void before() {
        d_application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();

    }

    /**
     * This method initialize the <code>EditMapService</code> object for fetching file data in Validation.
     */
    @Before
    public void beforeTest() {
        d_editMapService = new EditMapService();
        d_validateMapService = new ValidateMapService();
    }

    /**
     * Map validation - map is a connected graph.
     *
     * @throws URISyntaxException If the provided path had invalid characters.
     * @throws VMException        If there was an exception while loading the file.
     */
    @Test(expected = InvalidMapException.class)
    public void testMapIsConnectedGraph() throws VMException, URISyntaxException {
        // In Windows, URL will create %20 for space. To avoid, use the below logic.
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_map_connectedGraph.map");

        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);

        d_validateMapService.execute(null);
    }

    /**
     * Continent validation - continent is a connected sub-graph.
     *
     * @throws URISyntaxException If the provided path had invalid characters.
     * @throws VMException        If there was an exception while loading the file.
     */
    @Test(expected = InvalidMapException.class)
    public void testContinentConnectedSubGraph() throws VMException, URISyntaxException {
        // In Windows, URL will create %20 for space. To avoid, use the below logic.
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_continent_subgraph.map");

        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);

        d_validateMapService.execute(null);
    }

    /**
     * It checks the ValidateMapService's execute method.
     *
     * @throws URISyntaxException If the provided path had invalid characters.
     * @throws VMException        If there was an exception while loading the file.
     */
    @Test
    public void testValidateMapService() throws VMException, URISyntaxException {
        // In Windows, URL will create %20 for space. To avoid, use the below logic.
        d_testFilePath = getClass().getClassLoader().getResource("map_files/solar.map");

        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);

        String l_actualValue = d_validateMapService.execute(null);
        assertEquals(l_actualValue, "Map validation passed successfully!");
    }
}