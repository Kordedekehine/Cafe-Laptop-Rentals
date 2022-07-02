package laptopRentage.rentages.exception;

public class LaptopException extends RuntimeException {

    public LaptopException() {
        super();
    }

    public LaptopException(String message) {
        super(message);
    }

    public LaptopException(String message, Throwable cause) {
        super(message, cause);
    }

    public LaptopException(Throwable cause) {
        super(cause);
    }
}
