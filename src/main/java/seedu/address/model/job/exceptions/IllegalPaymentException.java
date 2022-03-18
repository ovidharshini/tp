package seedu.address.model.job.exceptions;

public class IllegalPaymentException extends RuntimeException {
    public IllegalPaymentException() {
        super("Operation would result in a paid job being unpaid");
    }
}
