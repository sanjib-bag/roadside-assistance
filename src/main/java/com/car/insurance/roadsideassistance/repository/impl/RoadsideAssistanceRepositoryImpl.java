package com.car.insurance.roadsideassistance.repository.impl;

import com.car.insurance.roadsideassistance.domain.entity.AssistantEntiy;
import com.car.insurance.roadsideassistance.domain.entity.CustomerEntity;
import com.car.insurance.roadsideassistance.domain.entity.ReserveCustomerAssistant;
import com.car.insurance.roadsideassistance.exceptions.ValidationException;
import com.car.insurance.roadsideassistance.repository.RoadsideAssistanceRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

/**
 * RoadsideAssistanceRepository interface implementation to run tests and validate the functionalities.
 */
@Repository
@Profile("test")
public class RoadsideAssistanceRepositoryImpl implements RoadsideAssistanceRepository {

    private final Map<BigInteger, AssistantEntiy> assistantEntityDataStore = new HashMap<>();

    private final Map<String, ReserveCustomerAssistant> reservationDataStore = new HashMap<>();

    private final Map<String, Set<AssistantEntiy>> assistantEntitiesDataStoreByZip = new HashMap<>();

    @Override
    public void update(AssistantEntiy assistantEntity) {
        validateAssistantEntity(assistantEntity);
        assistantEntityDataStore.put(assistantEntity.getAssistantId(), assistantEntity);

        Set<AssistantEntiy> assistants = assistantEntitiesDataStoreByZip.getOrDefault(assistantEntity.getAssistantLocation()
                .getZipCode(), new HashSet<>());
        assistants.add(assistantEntity);
        assistantEntitiesDataStoreByZip.put(assistantEntity.getAssistantLocation().getZipCode(), assistants);
    }

    @Override
    public Optional<AssistantEntiy> getAssistant(BigInteger assistantId) {
        if (assistantId == null) {
            throw new ValidationException("Invalid assistant id");
        }

        return Optional.ofNullable(assistantEntityDataStore.get(assistantId));
    }

    @Override
    public void reserve(ReserveCustomerAssistant reserveCustomerAssistant) {
        reservationDataStore.put("Reservation: " + reserveCustomerAssistant.getReservationId(), reserveCustomerAssistant);
        reservationDataStore.put("Customer: " + reserveCustomerAssistant.getCustomerEntity().getCustomerId(), reserveCustomerAssistant);
        reservationDataStore.put("Assistant: " + reserveCustomerAssistant.getAssistantEntiy().getAssistantId(), reserveCustomerAssistant);

        Optional<AssistantEntiy> assistantEntiyOptional = getAssistant(reserveCustomerAssistant.getAssistantEntiy()
                .getAssistantId());
        assistantEntiyOptional.ifPresent(assistantEntiy -> assistantEntiy.setReserved(true));
    }

    @Override
    public Optional<ReserveCustomerAssistant> findReservation(CustomerEntity customerEntity, AssistantEntiy assistantEntiy) {
        validateCustomerEntity(customerEntity);
        validateAssistantEntity(assistantEntiy);

        ReserveCustomerAssistant customerReservation = reservationDataStore.get("Customer: " + customerEntity.getCustomerId());
        ReserveCustomerAssistant assistantReservation = reservationDataStore.get("Assistant: " + assistantEntiy.getAssistantId());

        if (customerReservation != null && customerReservation.equals(assistantReservation)) {
            return Optional.of(customerReservation);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void release(ReserveCustomerAssistant reserveCustomerAssistant) {
        if (reserveCustomerAssistant == null) {
            throw new ValidationException("Invalid reservation");
        }

        reservationDataStore.remove("Customer: " + reserveCustomerAssistant.getCustomerEntity().getCustomerId());
        reservationDataStore.remove("Assistant: " + reserveCustomerAssistant.getAssistantEntiy().getAssistantId());

        Optional<AssistantEntiy> assistantEntiyOptional = getAssistant(reserveCustomerAssistant.getAssistantEntiy().getAssistantId());
        assistantEntiyOptional.ifPresent(assistantEntiy -> assistantEntiy.setReserved(false));

        reserveCustomerAssistant.setCustomerEntity(null);
        reserveCustomerAssistant.setAssistantEntiy(null);

        reservationDataStore.remove("Reservation: " + reserveCustomerAssistant.getReservationId());
    }

    @Override
    public Set<AssistantEntiy> findAllAssistantForGivenZipCode(String zipCode) {
        return assistantEntitiesDataStoreByZip.getOrDefault(zipCode, new HashSet<>());
    }

    @Override
    public void addAssistant(AssistantEntiy assistantEntiy) {
        assistantEntityDataStore.put(assistantEntiy.getAssistantId(), assistantEntiy);
    }

    private static void validateAssistantEntity(AssistantEntiy assistantEntiy) {
        if (assistantEntiy == null || assistantEntiy.getAssistantId() == null) {
            throw new ValidationException("Invalid entity object");
        }
    }

    private static void validateCustomerEntity(CustomerEntity customerEntity) {
        if (customerEntity == null || customerEntity.getCustomerId() == null) {
            throw new ValidationException("Invalid customer entity");
        }
    }

    @Override
    public void cleanAllData() {
        assistantEntityDataStore.clear();
        reservationDataStore.clear();
        assistantEntitiesDataStoreByZip.clear();
    }
}
