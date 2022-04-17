package com.warzone.team08.VM.entities;

import com.warzone.team08.VM.entities.orders.*;
import com.warzone.team08.VM.entities.strategy.AggressiveStrategyTest;
import com.warzone.team08.VM.entities.strategy.BenevolentStrategyTest;
import com.warzone.team08.VM.entities.strategy.CheaterStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for test cases of the different concrete classes of <code>Order</code> and other entities.
 *
 * @author Brijesh Lakkad
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdvanceOrderTest.class,
        AirliftOrderTest.class,
        BlockadeOrderTest.class,
        BombOrderTest.class,
        DeployOrderTest.class,
        PlayerTest.class,
        NegotiateOrderTest.class,
        AggressiveStrategyTest.class,
        BenevolentStrategyTest.class,
        CheaterStrategyTest.class
})
public class EntityTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}
