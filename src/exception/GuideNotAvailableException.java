package exception;

public class GuideNotAvailableException extends Exception {
    public GuideNotAvailableException(String guideName) {
        super("Guide '" + guideName + "' is not available for the selected date/time.");
    }
}