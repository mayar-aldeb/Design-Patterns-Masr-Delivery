package exception;

public class ClosedRestaurantException extends RuntimeException{

    public ClosedRestaurantException(String message){
        super(message);
    }
}
