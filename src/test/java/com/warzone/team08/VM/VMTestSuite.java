package com.warzone.team08.VM;

import com.warzone.team08.VM.entities.EntityTestSuite;
import com.warzone.team08.VM.entities.PlayerTest;
import com.warzone.team08.VM.game_play.GamePlayTestSuite;
import com.warzone.team08.VM.map_editor.MapEditorTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for <code>VM</code> test cases.
 *
 * @author CHARIT
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GamePlayTestSuite.class,
        MapEditorTestSuite.class,
        EntityTestSuite.class
})
public class VMTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations.
}
