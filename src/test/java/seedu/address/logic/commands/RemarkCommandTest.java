package seedu.address.logic.commands;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

import org.junit.jupiter.api.Test;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

class RemarkCommandTest {

    @Test
    void execute_addRemarkUnfilteredList_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person newPerson = new PersonBuilder(person).withRemark("blablabla").build();

        Remark remark = new Remark(newPerson.getRemark().value);
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, remark);

        CommandResult commandResult = new CommandResult(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS);
        Model newModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        newModel.setPerson(person, newPerson);

        assertCommandSuccess(remarkCommand, model, commandResult, newModel);
    }
}