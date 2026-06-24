package exception;

public class InvalidRatingException extends Exception {
    public InvalidRatingException(int rating) {
        super("Invalid rating: " + rating + ". Rating must be between 1 and 5.");
    }
}