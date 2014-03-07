package backend.fileparser;

/**
 * Exception indicating that the
 * file format does not match the
 * required format.
 * @author Jayen kumar Jaentilal k1189304
 */
public class IncorrectFileFormatException extends Exception {

    public IncorrectFileFormatException() {
        super();
    }

    public IncorrectFileFormatException(String message) {
        super(message);
    }

}
