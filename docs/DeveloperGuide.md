---
layout: page
title: Developer Guide
---

## Introduction

PeopleSoft is a desktop app for **calculating the salary for shift-based contractors**, optimized for use via a **Command Line Interface (CLI)**. If you are a **HR manager** and you can type fast, PeopleSoft can get your payroll tasks done **much faster** than traditional GUI apps.

**PeopleSoft helps to:**
* Simplify the management of data
* Reduce menial labour
* Reduce mistakes due to human error in calculation / accidental edits
* Helps employees be assured that their hours and pay are registered correctly in the system

--------------------------------------------------------------------------------------------------------------------

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Acknowledgements

* Project adapted from [addressbook-level3](https://se-education.org/addressbook-level3/DeveloperGuide.html#product-scope)
* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## Setting up, getting started

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## Design

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* allows for the automatic serialization and deserialization of `AddressBook` objects (and any component objects, e.g. `Person`, `Email`, etc) to and from JSON.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `peoplesoft.commons` package.

--------------------------------------------------------------------------------------------------------------------

## Implementation

This section describes some noteworthy details on how certain features are implemented.

### JSON serialization and deserialization

The serialization and deserialization of model objects (e.g. `AddressBook`, `UniquePersonList`, `Person`, `Tag`) is handled by custom serializers and deserializers, implemented as nested class within each model class.

These serializers and deserializers are automatically used by Jackson during serialization and deserialization.

The serializer and deserializer for each class determine how the objects are to be serialized and deserialized, including but not limited to:  
* which fields are to be stored,
* how each field should be (de)serialized, e.g. by directly converting it to/from a JSON type, or by delegating it to Jackson (which will use the serializer/deserializer for the field type), and
* how the fields and current object are to be represented as (or parsed from) JSON values, e.g. objects, strings, numbers.

This architecture has some advantages:  
* The serdes implementations are kept together with the related classes; developers adding new model classes will not have to modify files in other packages.
   * The previous implementation (with `JsonAdaptedPerson`, etc) requires that developers update the `JsonAdapted` classes belonging in the `Storage` component; this may not be immediately evident to developers.
* Developers adding new model classes can incorporate existing types (that already have corresponding serializers/deserializers) without needing to duplicate the serdes code, unlike with the previous implementation.
* Developers will also not need to (practically) duplicate classes, e.g. `Job` -> `JsonAdaptedJob` (with the `@JsonCreator` annotation), just so that Jackson has something to serialize from/deserialize to.

However, it also has some drawbacks:  
* It can be rather verbose, since each serializer/deserializer class contains a portion of boilerplate code
* Developers writing serializers/deserializers will need to have basic knowledge of JSON, e.g. the types that are available, the structure of JSON objects and arrays, etc
* Some knowledge of Jackson components (e.g. `JsonParser`, `JsonGenerator`, `ObjectNode`) is also required, as developers will need to use them to write values to/read values from the internal Jackson representation of a JSON value/object.

### \[Proposed\] Addition of pay multipliers to Job
The proposed addition of pay multipliers to `Job` objects is facilitated by `Employment` which implements the operation `Employment#calculatePay()`. `Employment#calculatePay()` calls `Job#calculatePay()` based on optional `Tag` parameters. 

`Tag` contains a Map of `multiplierHistory` which stores pay multiplier values and their time of addition. This is then passed to `Job#calculatePay()` which returns the appropriately scaled pay amount. 

#### Design considerations:

* **Alternative 1 (current choice):** Saves a Map of previous multipliers and time of addition in Tag.
    * Pros: Easy to implement. Pay breakdown can be useful in the implementation of other features.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Saves pay amount as a fixed value and updates it when Tag is edited.
    * Pros: Will use less memory as only one value is being stored.
    * Cons: Loss of useful pay breakdown information.
``



### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### The `Find` command

The `Find` command is an enhancement of the `Find` feature provided in AB3. 
It is structured using an object of `PersonContainsKeywordPredicate`, adapted from `NameContainsKeywordPredicate`. It 
has the following attributes:
* `static final String COMMAND_WORD` initialised to `'find'`
* `static final String MESSAGE_USAGE` initialised to the relevant message.
* `PersonContainsKeywordPredicate predicate` used to find `Person` objects that match with the given keyword.

Applying this filter to the entire list means that only `Persons` that match **ALL** the keywords would be retained in 
the filtered list.
#### The `PersonContainsKeywordPredicate` class

The **match** itself is defined as follows (within the `PersonContainsKeywordPredicate` class which implements the 
`Predicate` interface):
If a `Person` contains **ALL** the keywords passed in the query, either in their `name` field or as equivalent to an 
element in their `tags` set of `Tag` objects, then, passed as a parameter to `test()`,
it is a valid match. 

The implementation is achieved through using stream manipulations to iterate through each person object, 
and for any such object, iterating through each keyword passed in the query. The keyword is then checked if it is 
contained within the name or among the tags.

One motivation behind using streams rather than iteration was that streams can be better optimized, given the need or 
bandwith arising later. 

### The `JobList` interface and `UniqueJobList` class

The `JobList` is an interface for the list of jobs, that implements the `Iterable` interface and supports minimal list 
operations. 

The `UniqueJobList` class implements the `JobList` interface to that enforces uniqueness between its elements and does 
not allow nulls.
Furthermore, it has the following attributes, to interact with `java.fx` effectively.
* `ObservableList<Job> internalList`
* `ObservableList<Job> internalUnmodifiableList`


### The `Job` class

The `Job` class is an abstraction for a job stored in PeopleSoft. A `Job` object is immutable and contains the
following attributes:
* `String jobId` - Jobs are referenced by this attribute.
* `String desc` - A user-readable description of this job.
* `Rate rate` - The hourly pay of a job. A `Rate` object contains a `Money` object which saves money values as 
a `java.math.BigDecimal` with 6 decimal places.
* `Duration duration` - The duration that a job has been worked. Is used together with `rate` to calculate the
total job earnings. Uses `java.time.Duration`.
* `boolean hasPaid` - A boolean to denote if this job has been paid for. Used to calculate the salary of a
`Person`.

The use of immutability ensures that there are no unintended side effects of modifying a `Job`.
Whenever a `Job` needs to be modified (for example setting the value of `hasPaid`), a new immutable copy
of the `Job` with the desired changes is created to replace the old instance. Two `Job` objects are considered
the same job if they share the same `jobId`, which can be compared using `Job#isSameJob()`.

### The `Job` and `Person` association - `Employment`

In order to represent how a job may be assigned to a person (or a person may take on a job) in real life, an
association class `Employment` is used. The class handles the following responsibilities:
* Assigning a `Job` to a `Person`.
* Removing all necessary associations on the deletion/edit of a `Job` or `Person`.
* Filtering the `Job` objects that are mapped to a `Person`.

In the current implementation, there is a one-to-many mapping of `Job` objects to `Person` objects. An
association can be created using `Employment#associate(job, person)`. The jobs are internally referenced by
`jobId`, while the persons are referenced by `Name`.

Future implementations may work on allowing for a many-to-many relationship between `Job` objects and `Person`
objects (to represent how some jobs may have multiple persons involved). Currently, the class `Employment` is
written as a singleton. This may be changed to be a field of `AddressBook` due to potential obstacles with the
testing of the serialization/deserialization of the class.

#### Design considerations:

**Aspect: How the relational mapping between Job and Person is stored:**

* **Alternative 1 (current choice):** Saves the mapping of `Job` objects to `Person` objects in `Employment`.
    * Pros: Guarantee of commutative association.
    * Cons: Harder to implement. 

* **Alternative 2 (rejected):** Saves a map of the ids of related `Person` objects in `Job` objects, and a map of the ids of related `Job` objects in `Person` objects.
    * Pros: Easier to implement.
    * Cons: Mutual associations are not guaranteed. The possible extension of a one-to-many relationship between `Person` and `Tag` would be harder to implement.

--------------------------------------------------------------------------------------------------------------------

## Documentation, logging, testing, configuration, dev-ops

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## Appendix: Requirements

### Product scope

**Target user profile**:
HR Managers of companies offering contractor services
* have a need to manage a significant number of employees and jobs
* employee pay is calculated based on hours worked
* prefer desktop apps over other types
* can type fast
* prefer typing to mouse interactions
* are reasonably comfortable using CLI apps

**Value proposition**:
* Simplify the management of data
* Reduce menial labour
* Reduce mistakes due to human error in calculation / accidental edits
* Helps employees be assured that their hours and pay are registered correctly in the system



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​        | I want to …​                                                                   | So that I can…​                                                               |
|----------|----------------|--------------------------------------------------------------------------------|-------------------------------------------------------------------------------|
| `* *`    | new user       | see usage instructions                                                         | refer to instructions when I forget how to use the App                        |
| `* *`    | potential user | see the app populated with sample data                                         | easily see how the app will look like when it is in use                       |
| `* * *`  | HR Manager     | add a new employee                                                             |                                                                               |
| `* * *`  | HR Manager     | add tags to employees                                                          | identify their roles                                                          |
| `* * *`  | HR Manager     | edit an employee's information                                                 | rectify mistakes or update their personal information if need be              |
| `* * *`  | HR Manager     | delete an employee                                                             |                                                                               |
| `* * *`  | HR Manager     | delete all employees                                                           | mass-remove entries that I no longer need                                     |
| `* * *`  | HR Manager     | list all employees                                                             |                                                                               |
| `* * *`  | HR Manager     | find a person by name or tag                                                   | locate details of persons without having to go through the entire list        |
| `* * *`  | HR Manager     | view the salary owed to a given employee                                       | pay them                                                                      |
| `* * *`  | HR Manager     | pay for a given type of job                                                    |                                                                               |
| `* * *`  | HR Manager     | load and save data in human-readable data files                                | I can backup the data externally or access it in a different application      |
| `* * *`  | HR Manager     | exit the application                                                           |                                                                               |
| `* * *`  | HR Manager     | create, update, read, delete jobs                                              | manage the jobs that my employees are working on                              |
| `* * *`  | HR Manager     | see which jobs each employee is working on                                     | pay them accordingly                                                          |
| `* *`    | HR Manager     | log into separate modes for HR-related functions and for job-related functions | easily access relevant data for the type of work I am doing at any given time |
| `* *`    | HR Manager     | edit pay multiplier factors (e.g. overtime, experience, emergency on-calls)    | apply changes in payment policies across the organization                     |


### Use cases

(For all use cases below, the **System** is `PeopleSoft` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete an employee**

**MSS**

1.  User requests to list employees
2.  PeopleSoft shows a list of employees
3.  User requests to delete a specific employee in the list
4.  PeopleSoft deletes the employee

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. PeopleSoft shows an error message.

      Use case resumes at step 2.

**Use case: Update an employee's data**

**MSS**

1.  User requests to list employees
2.  PeopleSoft shows a list of employees
3.  User requests to edit a specific employee in the list with the updated information
4.  PeopleSoft updates the employee to match user input

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. PeopleSoft shows an error message.

      Use case resumes at step 2.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java 11 or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. Should not rely on database-management systems to store data.
4. Should not require an installer; should be packaged into a single reasonably-sized (i.e. within 100MB) JAR file.
5. Should not be hosted on remote servers.
6. Should not make use of proprietary third-party frameworks, libraries and services.
7. Should have a responsive GUI. GUI should function well (i.e., should not cause any resolution-related inconveniences to the user) for standard screen resolutions and higher and for screen scales 100% and 125%. GUI should be usable - even if suboptimal - for resolutions 1280x720 and higher and for screen scales 150%.
8. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Standard screen resolution**: 1920x1080

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
