package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.Set;
import java.util.stream.Stream;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.AssignCommand;
import peoplesoft.logic.parser.ArgumentMultimap;
import peoplesoft.logic.parser.ArgumentTokenizer;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.Prefix;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.employment.Employment;

/**
 * Parses an {@code Index} for {@code Job} and {@code Indexes} for {@code Person}.
 */
public class JobAssignCommandParser implements Parser<AssignCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code AssignCommand}
     * and returns an {@code ArgumentMultimap} for {@code AssignCommand}.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INDEX);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX)
                || argMultimap.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AssignCommand.MESSAGE_USAGE));
        }
        Index jobIndex;
        Set<Index> personIndexes;

        try {
            jobIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
            personIndexes = ParserUtil.parseIndexes(argMultimap.getAllValues(PREFIX_INDEX));
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AssignCommand.MESSAGE_USAGE));
        }

        return new AssignCommand(jobIndex, personIndexes, Employment.getInstance());
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
