package com.car.insurance.roadsideassistance.domain.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.Objects;

@Data
public class ReserveCustomerAssistant {
    private BigInteger reservationId;

    private CustomerEntity customerEntity;

    private AssistantEntiy assistantEntiy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReserveCustomerAssistant that = (ReserveCustomerAssistant) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
}
