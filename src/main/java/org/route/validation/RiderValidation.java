package org.route.validation;

import org.route.exception.BusyRiderException;
import org.route.model.Rider;

public class RiderValidation {

    public static void validateRider(Rider rider){
        if(!rider.isAvailable())
            throw new BusyRiderException("Rider is not available.");
    }
}
