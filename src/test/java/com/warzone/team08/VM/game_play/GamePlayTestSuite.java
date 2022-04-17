package com.warzone.team08.VM.game_play;

import com.warzone.team08.VM.game_play.services.AssignReinforcementServiceTest;
import com.warzone.team08.VM.game_play.services.DistributeCountriesServiceTest;
import com.warzone.team08.VM.game_play.services.ShowMapServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for <code>GAME_PLAY</code> test cases.
 *
 * @author CHARIT
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AssignReinforcementServiceTest.class,
        DistributeCountriesServiceTest.class,
        ShowMapServiceTest.class
})
public class GamePlayTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}
