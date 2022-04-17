package com.warzone.team08.VM.map_editor.services;

import com.warzone.team08.Application;
import com.warzone.team08.VM.VirtualMachine;
import com.warzone.team08.VM.exceptions.EntityNotFoundException;
import com.warzone.team08.VM.exceptions.InvalidInputException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * This class tests the add and remove operations on continent.
 *
 * @author CHARIT
 * @author Brijesh Lakkad
 */
public class ContinentServiceTest {
    private static Application d_Application = new Application();
    private static ContinentService d_ContinentService;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application.handleApplicationStartup();
    }

    /**
     * Re-initializes the continent list before test case run.
     */
    @Before
    public void beforeTestCase() {
        VirtualMachine.getInstance().initialise();
        d_ContinentService = new ContinentService();
    }

    /**
     * Tests whether the wrong continent value is being shown or not.
     *
     * @throws InvalidInputException Throws if country value is not number.
     */
    @Test(expected = InvalidInputException.class)
    public void testWrongContinentValue() throws InvalidInputException {
        d_ContinentService.add("Asia", "StringValue");
    }

    /**
     * Tests whether the continent is successfully added and removed or not; Passes if continent is removed.
     *
     * @throws EntityNotFoundException Throws if continent is not available in list.
     * @throws InvalidInputException   Throws if country value is not number.
     */
    @Test(expected = Test.None.class)
    public void testAddAndRemoveContinent() throws EntityNotFoundException, InvalidInputException {
        String l_responseOfAddOp = d_ContinentService.add("Asia", "10");
        assertNotNull(l_responseOfAddOp);

        String l_responseOfRemoveOp = d_ContinentService.remove("Asia");
        assertNotNull(l_responseOfRemoveOp);
    }
}