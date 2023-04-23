package com.car.insurance.roadsideassistance.domain.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Customer {
    private BigInteger customerId;

    private String customerName;

    private String customerContactNumber;
}
