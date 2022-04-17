package com.warzone.team08.CLI.constants;

import com.warzone.team08.Application;
import com.warzone.team08.CLI.layouts.UserCommandLayout;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class to test the command text if it belongs to the current state of the game.
 */
public class UserCommandTest {
    /**
     * Command need to be run
     */
    String d_headOfCommand;
    /**
     * Correct argument for the d_headOfCommand command
     */
    String d_argKey;
    /**
     * Wrong argument for the d_headOfCommand command
     */
    String d_wrongArgKey;

    /**
     * Sets the context for the test
     */
    @Before
    public void before() {
        d_headOfCommand = "editcontinent";
        d_argKey = "-add";
        d_wrongArgKey = "-delete";
        // As the value of d_headOfCommand belongs to MAP_EDITOR
        // The below code will set the game state to it according¬ly
        Application l_application = new Application();
        l_application.handleApplicationStartup();
    }

    /**
     * Tests if the argument key belongs to the same command or not.
     * <p>
     * Success if it can identify if the correct and wrong arguments
     */
    @Test
    public void testIsKeyOfCommand() {
        // Checks if 'add' argument belongs to 'editcontinent' command
        boolean isKeyOfEditContent = UserCommandLayout.matchAndGetUserCommand(d_headOfCommand).isKeyOfCommand(this.d_argKey);

        // Checks if 'delete' argument belongs to 'editcontinent' command
        boolean isNotKeyOfEditContent = UserCommandLayout.matchAndGetUserCommand(d_headOfCommand).isKeyOfCommand(this.d_wrongArgKey);

        assertTrue(isKeyOfEditContent);
        assertFalse(isNotKeyOfEditContent);
    }
}
