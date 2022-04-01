package peoplesoft.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.ClearCommand;
import peoplesoft.logic.commands.ExitCommand;
import peoplesoft.logic.commands.HelpCommand;
import peoplesoft.logic.commands.PeopleAddCommand;
import peoplesoft.logic.commands.PeopleDeleteCommand;
import peoplesoft.logic.commands.PeopleEditCommand;
import peoplesoft.logic.commands.PeopleEditCommand.EditPersonDescriptor;
import peoplesoft.logic.commands.PeopleFindCommand;
import peoplesoft.logic.commands.PeopleListCommand;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;
import peoplesoft.testutil.EditPersonDescriptorBuilder;
import peoplesoft.testutil.PersonBuilder;
import peoplesoft.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().withNextId().build();
        PeopleAddCommand command = (PeopleAddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new PeopleAddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        PeopleDeleteCommand command = (PeopleDeleteCommand) parser.parseCommand(
                PeopleDeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new PeopleDeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        PeopleEditCommand command = (PeopleEditCommand) parser.parseCommand(PeopleEditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new PeopleEditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        PeopleFindCommand command = (PeopleFindCommand) parser.parseCommand(
                PeopleFindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new PeopleFindCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(PeopleListCommand.COMMAND_WORD) instanceof PeopleListCommand);
        assertTrue(parser.parseCommand(PeopleListCommand.COMMAND_WORD + " 3") instanceof PeopleListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
