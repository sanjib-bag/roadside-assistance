package com.car.insurance.roadsideassistance.domain.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Assistant {
    private BigInteger assistantId;

    private String assistantName;

    private Geolocation assistantLocation;

    private String assistantContactNumber;

    private boolean reserved;
}
