package com.car.insurance.roadsideassistance.domain.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class AssistantEntiy {
    private BigInteger assistantId;

    private String assistantName;

    private GeolocationEntity assistantLocation;

    private String assistentContactNumber;

    private boolean reserved;

    private ReserveCustomerAssistant reserveCustomerAssistant;
}
