package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.entities.Continent;
import com.warzone.team08.VM.exceptions.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * This class tests the add and remove operations on country.
 *
 * @author CHARIT
 * @version 1.0.0
 */
public class CountryServiceTest {
    private static Application d_Application = new Application();
    private static CountryService d_CountryService;
    private List<Continent> d_continentList;
    private EditMapService d_editMapService;
    private URL d_testFilePath;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeTestClass() {
        d_Application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();
        d_CountryService = new CountryService();
    }

    /**
     * Re-initializes the continent list before test case run.
     *
     * @throws AbsentTagException        Throws if any tag is missing in map file.
     * @throws InvalidMapException       Throws if map is invalid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if input is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        If error while parsing the string representing the path.
     */
    @Before
    public void beforeTestCase() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
        d_editMapService = new EditMapService();
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_map.map");
        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
        d_continentList = VirtualMachine.getGameEngine().getMapEditorEngine().getContinentList();
    }

    /**
     * Tests whether the wrong continent value is being shown or not.
     *
     * @throws EntityNotFoundException Throws if name of the continent which doesn't exists is provided.
     */
    @Test(expected = EntityNotFoundException.class)
    public void testInvalidContinentName() throws EntityNotFoundException {
        d_CountryService.add("India", "ABC");
    }

    /**
     * Tests whether the country is successfully added and removed or not; Passes if continent is removed.
     *
     * @throws EntityNotFoundException Throws if name of the continent which doesn't exists is provided.
     */
    @Test(expected = Test.None.class)
    public void testAddRemoveCountry()
            throws EntityNotFoundException {

        String l_continentName = d_continentList.get(0).getContinentName();
        String l_responseStringAddOp = d_CountryService.add("India", l_continentName);
        assertNotNull(l_responseStringAddOp);

        String l_responseStringRemoveOp = d_CountryService.remove("India");
        assertNotNull(l_responseStringRemoveOp);
    }
}