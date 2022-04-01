package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import peoplesoft.logic.commands.ClearCommand;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.ExitCommand;
import peoplesoft.logic.commands.HelpCommand;
import peoplesoft.logic.commands.PersonAddCommand;
import peoplesoft.logic.commands.PersonDeleteCommand;
import peoplesoft.logic.commands.PersonEditCommand;
import peoplesoft.logic.commands.PersonExportCommand;
import peoplesoft.logic.commands.PersonFindCommand;
import peoplesoft.logic.commands.PersonListCommand;
import peoplesoft.logic.commands.job.AddCommand;
import peoplesoft.logic.commands.job.AssignCommand;
import peoplesoft.logic.commands.job.DeleteCommand;
import peoplesoft.logic.commands.job.FinalizeCommand;
import peoplesoft.logic.commands.job.FindCommand;
import peoplesoft.logic.commands.job.ListCommand;
import peoplesoft.logic.commands.job.MarkCommand;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.logic.parser.job.JobAddCommandParser;
import peoplesoft.logic.parser.job.JobAssignCommandParser;
import peoplesoft.logic.parser.job.JobDeleteCommandParser;
import peoplesoft.logic.parser.job.JobFinalizeCommandParser;
import peoplesoft.logic.parser.job.JobFindCommandParser;
import peoplesoft.logic.parser.job.JobMarkCommandParser;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case PersonAddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case PersonEditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case PersonDeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case PersonExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case PersonFindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case PersonListCommand.COMMAND_WORD:
            return new PersonListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

            // Job related commands

        case AddCommand.COMMAND_WORD:
            return new JobAddCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case DeleteCommand.COMMAND_WORD:
            return new JobDeleteCommandParser().parse(arguments);

        case MarkCommand.COMMAND_WORD:
            return new JobMarkCommandParser().parse(arguments);

        case FindCommand.COMMAND_WORD:
            return new JobFindCommandParser().parse(arguments);

        case AssignCommand.COMMAND_WORD:
            return new JobAssignCommandParser().parse(arguments);

        case FinalizeCommand.COMMAND_WORD:
            return new JobFinalizeCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
