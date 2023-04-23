package com.car.insurance.roadsideassistance.repository;

import com.car.insurance.roadsideassistance.domain.entity.AssistantEntiy;
import com.car.insurance.roadsideassistance.domain.entity.CustomerEntity;
import com.car.insurance.roadsideassistance.domain.entity.ReserveCustomerAssistant;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

public interface RoadsideAssistanceRepository {
    void update(AssistantEntiy assistantEntiy);

    Optional<AssistantEntiy> getAssistant(BigInteger assistantId);

    void reserve(ReserveCustomerAssistant reserveCustomerAssistant);

    Optional<ReserveCustomerAssistant> findReservation(CustomerEntity customerEntity, AssistantEntiy assistantEntiy);

    void release(ReserveCustomerAssistant reserveCustomerAssistant);

    Set<AssistantEntiy> findAllAssistantForGivenZipCode(String zipCode);

    void addAssistant(AssistantEntiy assistantEntiy);

    void cleanAllData();
}
