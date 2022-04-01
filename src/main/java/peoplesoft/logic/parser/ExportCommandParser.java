package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.PersonExportCommand;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PersonExportCommand object
 */
public class ExportCommandParser implements Parser<PersonExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PersonExportCommand
     * and returns a PersonExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PersonExportCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new PersonExportCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PersonExportCommand.MESSAGE_USAGE), pe);
        }
    }

}
