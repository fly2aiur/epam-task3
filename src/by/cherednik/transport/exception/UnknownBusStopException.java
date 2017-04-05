package by.cherednik.transport.exception;

public class UnknownBusStopException extends Exception {

    public UnknownBusStopException() {
    }

    public UnknownBusStopException(String message) {
        super(message);
    }

    public UnknownBusStopException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownBusStopException(Throwable cause) {
        super(cause);
    }
}
