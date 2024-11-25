package Exception;

public class InvalidJsonStringException extends Exception{
    public InvalidJsonStringException(String errorMessage) {
        super(errorMessage);
    }
}
