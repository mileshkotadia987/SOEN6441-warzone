package com.warzone.team08.VM.map_editor;

import com.warzone.team08.VM.map_editor.services.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for <code>MAP_EDITOR</code> test cases.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        LoadMapServiceTest.class,
        ShowMapServiceTest.class,
        ContinentServiceTest.class,
        ValidateMapServiceTest.class,
        CountryServiceTest.class,
        CountryNeighborServiceTest.class,
        SaveMapServiceTest.class,
        EditConquestMapServiceTest.class
})
public class MapEditorTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}