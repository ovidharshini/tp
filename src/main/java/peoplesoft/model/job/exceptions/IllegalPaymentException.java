package peoplesoft.model.job.exceptions;

/**
 * Signals that the operation will result in a previously paid Job being updated to be unpaid
 * (Jobs are considered to be updated if an instance of a Job is effectively replaced by
 * another instance of a job).
 */
public class IllegalPaymentException extends RuntimeException {
    public IllegalPaymentException() {
        super("Operation would result in a paid job being unpaid");
    }
}
