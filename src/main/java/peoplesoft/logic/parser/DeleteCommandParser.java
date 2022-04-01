package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.PeopleDeleteCommand;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PeopleDeleteCommand object
 */
public class DeleteCommandParser implements Parser<PeopleDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PeopleDeleteCommand
     * and returns a PeopleDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PeopleDeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new PeopleDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PeopleDeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
