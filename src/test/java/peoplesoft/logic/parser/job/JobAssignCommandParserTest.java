package peoplesoft.logic.parser.job;

import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.parser.exceptions.ParseException;

public class JobAssignCommandParserTest {

    private JobAssignCommandParser parser = new JobAssignCommandParser();

    @Test
    public void parse_missingArgs_throwsParseException() {
        // Missing index
        assertThrows(ParseException.class, () -> parser.parse(" test"));
        // Missing preamble
        assertThrows(ParseException.class, () -> parser.parse(" i/1"));
    }

    // TODO: create test for successful construction
    // Current issue is that a new jobId will be made and that may not be deterministic
}
