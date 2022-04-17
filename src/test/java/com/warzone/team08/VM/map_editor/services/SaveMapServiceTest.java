package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import com.warzone.team08.VM.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * This file tests whether the map file is save successfully or not.
 *
 * @author Rutwik
 */
public class SaveMapServiceTest {
    private static Application d_application = new Application();
    private static ContinentService d_ContinentService;
    private static CountryService d_CountryService;
    private static CountryNeighborService d_CountryNeighbourService;
    private static SaveMapService d_SaveMapService;
    private String testFile = "testing_save_file.map";

    /**
     * Create temporary folder for test case.
     */
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * This method runs before the test case runs. This method initializes different objects required to perform test.
     */
    @BeforeClass
    public static void before() {
        d_application.handleApplicationStartup();
        VirtualMachine.getInstance().initialise();

        d_ContinentService = new ContinentService();
        d_CountryService = new CountryService();
        d_CountryNeighbourService = new CountryNeighborService();
        d_SaveMapService = new SaveMapService();
    }

    /**
     * Re-initializes the continent list before test case run.
     */
    @Before
    public void beforeTestCase() {
        VirtualMachine.getGameEngine().getMapEditorEngine().initialise();
    }

    /**
     * This test will add content to Continent List, Country List and Neighbour List.
     *
     * @throws InvalidInputException   Any invalid input other than the reqired parameters will throw this error.
     * @throws EntityNotFoundException Any Continent that is not found in the Continent List but added in the Country
     *                                 List will throw this error.
     */
    @Before
    public void addContentToTheMapFile() throws InvalidInputException, EntityNotFoundException {
        d_ContinentService.add("Asia", "10");
        d_ContinentService.add("Australia", "15");
        d_CountryService.add("Delhi", "Asia");
        d_CountryService.add("Mumbai", "Asia");
        d_CountryService.add("Melbourne", "Australia");
        d_CountryNeighbourService.add("Delhi", "Mumbai");
        d_CountryNeighbourService.add("Mumbai", "Delhi");
        d_CountryNeighbourService.add("Melbourne", "Delhi");
    }

    /**
     * This test will save the content added in Continent, Country and Neighbour List into the .map file.
     *
     * @throws ResourceNotFoundException Throws if the Target File where content is to be saved is not found then this
     *                                   exception will be raised.
     * @throws InvalidInputException     Throws if input is invalid.
     * @throws IOException               Throws if IOException is generated.
     */
    @Test(expected = Test.None.class)
    public void testSaveFile() throws ResourceNotFoundException, InvalidInputException, IOException {
        // Create a temporary file.
        final File testFileObject = tempFolder.newFile(testFile);
        String response = d_SaveMapService.saveToFile(testFileObject);

        assert testFileObject.exists();
        assertNotNull(response);
    }

}
