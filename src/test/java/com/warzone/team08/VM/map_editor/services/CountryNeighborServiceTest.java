package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.VMException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * This class tests the add and remove operations of neighbours of the country.
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 */
public class CountryNeighborServiceTest {
    private static Application d_Application = new Application();
    private static CountryNeighborService d_CountryNeighbourService;
    private EditMapService d_editMapService;
    private URL d_testFilePath;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();
        d_CountryNeighbourService = new CountryNeighborService();
    }

    /**
     * Re-initializes the continent list before test case run.
     *
     * @throws VMException        Exception generated during execution.
     * @throws URISyntaxException If error while parsing the string representing the path.
     */
    @Before
    public void beforeTestCase() throws VMException, URISyntaxException {
        VirtualMachine.getGameEngine().initialise();
        d_editMapService = new EditMapService();
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_map.map");
        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
    }

    /**
     * Tests whether the country and neighbour country name are valid or not.
     *
     * @throws EntityNotFoundException Throws if data tag is absent in map file.
     */
    @Test(expected = EntityNotFoundException.class)
    public void testWrongCountryValues() throws EntityNotFoundException {
        //If both country name and neighbor country name are invalid(Do not exists in map file).
        d_CountryNeighbourService.add("ABC", "DEF");

        //If neighbor country name is invalid (Do not exists in map file).
        d_CountryNeighbourService.add("Mercury-South", "DEF");

        //If country name is invalid (Do not exists in map file).
        d_CountryNeighbourService.add("ABC", "Mercury-West");
    }

    /**
     * Tests whether the neighbour is successfully added and removed. Passes if added and then removed, otherwise
     * fails.
     *
     * @throws EntityNotFoundException Throws if data tag is absent in map file.
     */
    @Test(expected = Test.None.class)
    public void testAdd() throws EntityNotFoundException {
        String l_addResponse = d_CountryNeighbourService.add("Mercury-South", "Mercury-West");
        assertNotNull(l_addResponse);

        String l_removeResponse = d_CountryNeighbourService.remove("Mercury-South", "Mercury-West");

        assertNotNull(l_removeResponse);
    }
}