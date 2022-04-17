package com.warzone.team08;
/**
 * Central test suite for running all test cases of the application.
 *
 * @author CHARIT
 */

import com.warzone.team08.CLI.CLITestSuite;
import com.warzone.team08.VM.VMTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for <code>CLI</code> and <code>VM</code> test cases.
 *
 * @author CHARIT
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CLITestSuite.class,
        VMTestSuite.class
})
public class ApplicationTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations.
}
