package com.car.insurance.roadsideassistance.domain.entity;

import lombok.Data;

@Data
public class GeolocationEntity {
    private double latitude;

    private double longitude;

    private String zipCode;
}
