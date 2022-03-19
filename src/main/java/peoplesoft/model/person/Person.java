package peoplesoft.model.person;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import peoplesoft.model.job.Job;
import peoplesoft.model.job.Money;
import peoplesoft.model.job.exceptions.DuplicateJobException;
import peoplesoft.model.job.exceptions.IllegalPaymentException;
import peoplesoft.model.tag.Tag;

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
    private final Money salary;
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
        this.salary = new Money(0);
        this.tags.addAll(tags);
    }

    /**
     * Constructor for Person.
     *
     * @param person person
     * @param salary salary owed
     * @param jobs jobs to be added
     */
    public Person(Person person, Money salary, HashMap<String, Boolean> jobs) {
        requireAllNonNull(person, salary, jobs);
        this.name = person.name;
        this.phone = person.phone;
        this.email = person.email;
        this.address = person.address;
        this.tags.addAll(person.tags);
        this.salary = salary;
        this.jobs.putAll(jobs);
    }

    /**
     * Updates a job as paid and subtracts from salary.
     *
     * @param job job to be paid.
     * @return new instance of Person with updated job.
     */
    private Person payJob(Job job) {
        HashMap<String, Boolean> jobs = new HashMap<>(this.jobs);
        jobs.remove(job.getJobId());

        Money salary = this.salary.subtract(job.calculatePay());
        return new Person(this, salary, jobs);
    }

    /**
     * Adds a new job and adds to salary if job is not paid.
     *
     * @param job job to be added.
     * @return new instance of Person with updated job.
     */
    private Person addNewJob(Job job) {
        String jobId = job.getJobId();

        if (jobs.containsKey(jobId)) {
            throw new DuplicateJobException();
        }

        HashMap<String, Boolean> jobs = new HashMap<>(this.jobs);
        jobs.put(job.getJobId(), job.hasPaid());

        Money addedSalary = job.hasPaid() ? new Money(0) : job.calculatePay();
        Money salary = this.salary.add(addedSalary);
        return new Person(this, salary, jobs);
    }

    /**
     * Updates jobs with a new job.
     * Updates job to paid and decreases salary if Person
     * has unpaid job.
     *
     * @param job job to be added
     * @return Person with updated jobs.
     * @throws DuplicateJobException if job is added twice.
     */
    public Person updateJobs(Job job) throws DuplicateJobException {
        requireAllNonNull(job);
        String jobId = job.getJobId();

        if (!jobs.containsKey(jobId)) {
            return addNewJob(job);
        }
        if (!jobs.get(jobId) && job.hasPaid()) {
            return payJob(job);
        }
        if (jobs.get(jobId) && !job.hasPaid()) {
            throw new IllegalPaymentException();
        }

        return this;
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
        return salary;
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
        return Objects.hash(name, phone, email, address, salary, jobs, tags);
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
