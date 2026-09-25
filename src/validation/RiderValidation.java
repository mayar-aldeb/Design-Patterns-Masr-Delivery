package validation;

import exception.BusyRiderException;
import model.Rider;

public class RiderValidation {

    public static void validateRider(Rider rider){
        if(!rider.isAvailable())
            throw new BusyRiderException("Rider is not available.");
    }
}
