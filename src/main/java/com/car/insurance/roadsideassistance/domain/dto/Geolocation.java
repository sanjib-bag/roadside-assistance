package com.car.insurance.roadsideassistance.domain.dto;

import lombok.Data;

@Data
public class Geolocation {
    private double latitude;

    private double longitude;

    private String zipCode;
}
