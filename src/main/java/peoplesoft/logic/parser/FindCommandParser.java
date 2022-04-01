package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import peoplesoft.logic.commands.PeopleFindCommand;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new PeopleFindCommand object
 */
public class FindCommandParser implements Parser<PeopleFindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PeopleFindCommand
     * and returns a PeopleFindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PeopleFindCommand parse(String args) throws ParseException {
        String trimmedArgs;
        try {
            trimmedArgs = ParserUtil.parseString(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PeopleFindCommand.MESSAGE_USAGE), pe);
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new PeopleFindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
