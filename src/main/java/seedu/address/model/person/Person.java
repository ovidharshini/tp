package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.job.Job;
import seedu.address.model.job.Money;
import seedu.address.model.job.exceptions.IllegalPaymentException;
import seedu.address.model.job.exceptions.JobNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Money owedSalary;
    private final Set<Tag> tags = new HashSet<>();
    private final HashMap<String, Boolean> jobs = new HashMap<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.owedSalary = new Money(0);
        this.tags.addAll(tags);
    }

    private Person(Name name, Phone phone, Email email, Address address, Money owedSalary,
                   Set<Tag> tags, HashMap<String, Boolean> jobs) {
        requireAllNonNull(name, phone, email, address, owedSalary, jobs, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.owedSalary = owedSalary;
        this.tags.addAll(tags);
        this.jobs.putAll(jobs);
    }

    /**
     * Constructor for Person.
     *
     * @param person person
     * @param owedSalary salary owed
     * @param jobs jobs to be added
     */
    public Person(Person person, Money owedSalary, HashMap<String, Boolean> jobs) {
        requireAllNonNull(person, owedSalary, jobs);
        this.name = person.name;
        this.phone = person.phone;
        this.email = person.email;
        this.address = person.address;
        this.owedSalary = owedSalary;
        this.tags.addAll(person.tags);
        this.jobs.putAll(jobs);
    }

    /**
     * Adds new jobs to the Person and updates owedSalary.
     * Updates job to paid and decreases owedSalary if Person
     * has unpaid job.
     *
     * @param job job to be added
     * @return Person with updated jobs.
     */
    public Person addJob(Job job) {
        requireAllNonNull(job);
        String jobId = job.getJobId();

        if (jobs.containsKey(jobId)) {
            if (jobs.get(jobId) && !job.hasPaid()) {
                throw new IllegalPaymentException();
            } else if (!jobs.get(jobId) && job.hasPaid()) {
                jobs.remove(jobId);
                Money owedSalary = this.owedSalary.subtract(job.calculatePay());
                return new Person(this, owedSalary, jobs);
            }
        }

        jobs.put(job.getJobId(), job.hasPaid());
        Money addedSalary = job.hasPaid() ? new Money(0) : job.calculatePay();
        Money owedSalary = this.owedSalary.add(addedSalary);
        return new Person(this, owedSalary, jobs);
    }

    /**
     * Removes job.
     *
     * @param job job to be added
     * @return Person with updated jobs.
     */
    public Person removeJob(Job job) {
        requireAllNonNull(job);
        String jobId = job.getJobId();

        if (!jobs.containsKey(jobId)) {
            throw new JobNotFoundException();
        }

        jobs.remove(jobId);
        return new Person(this, owedSalary, jobs);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Money getSalary() {
        return owedSalary;
    }

    /**
     * Returns an immutable Map of (jobId, hasPaid).
     *
     * @return map of jobs.
     */
    public Map<String, Boolean> getJobs() {
        return Collections.unmodifiableMap(jobs);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getSalary().equals(getSalary())
                && otherPerson.getJobs().equals(getJobs())
                && otherPerson.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, owedSalary, jobs, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail())
                .append("; Address: ")
                .append(getAddress())
                .append("; Salary: ")
                .append(getSalary());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }

}
