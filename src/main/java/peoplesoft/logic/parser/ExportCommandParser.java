package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.PeopleExportCommand;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PeopleExportCommand object
 */
public class ExportCommandParser implements Parser<PeopleExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PeopleExportCommand
     * and returns a PeopleExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PeopleExportCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new PeopleExportCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PeopleExportCommand.MESSAGE_USAGE), pe);
        }
    }

}
