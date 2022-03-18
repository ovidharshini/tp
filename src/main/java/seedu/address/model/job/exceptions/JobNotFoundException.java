package seedu.address.model.job.exceptions;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException() {
        super("Job was not found");
    }
}
