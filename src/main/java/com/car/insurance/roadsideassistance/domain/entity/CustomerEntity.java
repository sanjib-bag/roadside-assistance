package com.car.insurance.roadsideassistance.domain.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CustomerEntity {
    private BigInteger customerId;

    private String customerName;

    private String customerContactNumber;

    private ReserveCustomerAssistant reserveCustomerAssistant;
}
