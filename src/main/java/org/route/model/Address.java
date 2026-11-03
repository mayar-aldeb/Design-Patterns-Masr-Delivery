package org.route.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address {
    private String district;
    private String details;

    public Address(String district, String details) {
        this.district = district;
        this.details = details;
    }

    @Override
    public String toString() {
        return "Address{" +
                "district='" + district + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
