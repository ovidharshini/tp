package peoplesoft.logic.commands.job;

import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class JobMarkCommandTest {

    private static final String CORRECT_ARGS = "correct";
    private static final String INCORRECT_ARGS = "incorrect";

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobMarkCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobMarkCommand(CORRECT_ARGS).execute(null));
    }

    @Test
    public void execute_incorrectArgs_throwsCommandException() {
        // TODO
    }
    // TODO: Model stub
}
