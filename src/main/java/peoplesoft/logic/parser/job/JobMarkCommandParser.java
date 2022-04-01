package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.MarkCommand;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.employment.Employment;

/**
 * Parses an {@code Index} of a {@code Job} to mark.
 */
public class JobMarkCommandParser implements Parser<MarkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code MarkCommand}
     * and returns a {@code MarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new MarkCommand(index, Employment.getInstance());
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }
    }
}
