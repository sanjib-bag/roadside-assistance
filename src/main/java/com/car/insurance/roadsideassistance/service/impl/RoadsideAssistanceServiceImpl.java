package com.car.insurance.roadsideassistance.service.impl;

import com.car.insurance.roadsideassistance.domain.dto.Assistant;
import com.car.insurance.roadsideassistance.domain.dto.Customer;
import com.car.insurance.roadsideassistance.domain.dto.Geolocation;
import com.car.insurance.roadsideassistance.domain.entity.AssistantEntiy;
import com.car.insurance.roadsideassistance.domain.entity.CustomerEntity;
import com.car.insurance.roadsideassistance.domain.entity.GeolocationEntity;
import com.car.insurance.roadsideassistance.domain.entity.ReserveCustomerAssistant;
import com.car.insurance.roadsideassistance.domain.transformer.RoadsideAssistanceEntityTransformer;
import com.car.insurance.roadsideassistance.exceptions.ValidationException;
import com.car.insurance.roadsideassistance.repository.RoadsideAssistanceRepository;
import com.car.insurance.roadsideassistance.service.RoadsideAssistanceService;
import com.car.insurance.roadsideassistance.utis.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoadsideAssistanceServiceImpl implements RoadsideAssistanceService {
    @Autowired
    private RoadsideAssistanceEntityTransformer roadsideAssistanceEntityTransformer;

    @Autowired
    private RoadsideAssistanceRepository roadsideAssistanceRepository;

    private final Random random = new Random();

    /**
     * Updates Assistants location.
     *
     * @param assistant         represents the roadside assistance service provider
     * @param assistantLocation represents the location of the roadside assistant
     */
    @Override
    public void updateAssistantLocation(Assistant assistant, Geolocation assistantLocation) {

        validateUpdateAssistantInputs(assistant, assistantLocation);

        Optional<AssistantEntiy> optionalAssistantEntity = roadsideAssistanceRepository.getAssistant(assistant.getAssistantId());

        if (!optionalAssistantEntity.isPresent()) {
            throw new RuntimeException("Given Assistant is not found");
        }

        optionalAssistantEntity.ifPresent(assistantEntiy -> {
            GeolocationEntity assistantLocationEntity = roadsideAssistanceEntityTransformer.transform(assistantLocation);
            assistantEntiy.setAssistantLocation(assistantLocationEntity);

            roadsideAssistanceRepository.update(assistantEntiy);
        });
    }

    /**
     * Returns all nearest assistant locations. If limit is not positive it will return all assistants
     *
     * @param geolocation - geolocation from which to search for assistants
     * @param limit       - the number of assistants to return
     * @return SortedSet<Assistant>
     */
    @Override
    public SortedSet<Assistant> findNearestAssistants(Geolocation geolocation, int limit) {
        validateGeoLocation(geolocation);
        validateZipCode(geolocation);

        Set<AssistantEntiy> listOfAssistant = roadsideAssistanceRepository.findAllAssistantForGivenZipCode(geolocation.getZipCode());

        TreeSet<AssistantEntiy> sortedAssistantEntities = new TreeSet<>((assistant1, assistant2) -> compareDistance(geolocation.getLatitude(), geolocation.getLongitude(),
                assistant1.getAssistantLocation().getLatitude(), assistant1.getAssistantLocation().getLongitude(),
                assistant2.getAssistantLocation().getLatitude(), assistant2.getAssistantLocation().getLongitude()));

        sortedAssistantEntities.addAll(listOfAssistant);

        List<Assistant> assistants = sortedAssistantEntities.stream()
                .map(assistantEntiy -> roadsideAssistanceEntityTransformer.transform(assistantEntiy))
                .collect(Collectors.toList());

        if (limit > 0) {
            assistants = assistants.stream().limit(limit).collect(Collectors.toList());
        }

        TreeSet<Assistant> sortedAssistants = new TreeSet<>((assistant1, assistant2) -> compareDistance(geolocation.getLatitude(), geolocation.getLongitude(),
                assistant1.getAssistantLocation().getLatitude(), assistant1.getAssistantLocation().getLongitude(),
                assistant2.getAssistantLocation().getLatitude(), assistant2.getAssistantLocation().getLongitude()));

        sortedAssistants.addAll(assistants);

        return sortedAssistants;
    }

    /**
     * Reserves nearest available assistants for the customer
     *
     * @param customer         - Represents a Geico customer
     * @param customerLocation - Location of the customer
     * @return Optional<Assistant>
     */
    @Override
    public Optional<Assistant> reserveAssistant(Customer customer, Geolocation customerLocation) {

        validateReserveAssistantInput(customer, customerLocation);

        SortedSet<Assistant> findAllNearestAssistants = findNearestAssistants(customerLocation, -1);

        Optional<Assistant> optionalAssistant = findAllNearestAssistants.stream().filter(assistant -> !assistant.isReserved()).findFirst();

        optionalAssistant.ifPresent(assistant -> {
            assignAssistantToCustomer(customer, assistant);
            assistant.setReserved(true);
        });

        return optionalAssistant;
    }

    /**
     * Releases the customer reservation
     *
     * @param customer  - Represents a Geico customer
     * @param assistant - An assistant that was previously reserved by the customer
     */
    @Override
    public void releaseAssistant(Customer customer, Assistant assistant) {
        validateReleaseAssistantInput(customer, assistant);

        CustomerEntity customerEntity = roadsideAssistanceEntityTransformer.transform(customer);
        AssistantEntiy assistantEntiy = roadsideAssistanceEntityTransformer.transform(assistant);

        Optional<ReserveCustomerAssistant> reserveCustomerAssistantOptional = roadsideAssistanceRepository.findReservation(customerEntity, assistantEntiy);

        if (!reserveCustomerAssistantOptional.isPresent()) {
            throw new ValidationException("Reservation not found");
        }

        reserveCustomerAssistantOptional.ifPresent(reserveCustomerAssistant -> {
            CustomerEntity customerEntityLocal = reserveCustomerAssistant.getCustomerEntity();
            AssistantEntiy assistantEntiyLocal = reserveCustomerAssistant.getAssistantEntiy();

            customerEntityLocal.setReserveCustomerAssistant(null);
            assistantEntiyLocal.setReserveCustomerAssistant(null);

            roadsideAssistanceRepository.release(reserveCustomerAssistant);
        });
    }

    /**
     * Validate release assistant input
     *
     * @param customer
     * @param assistant
     */
    private void validateReleaseAssistantInput(Customer customer, Assistant assistant) {
        validateCustomer(customer);
        validateAssistant(assistant);
    }

    /**
     * Validate assistant
     *
     * @param assistant
     */
    private void validateAssistant(Assistant assistant) {
        if (assistant == null || assistant.getAssistantId() == null) {
            throw new ValidationException("Invalid assistant");
        }
    }

    /**
     * Validate customer
     *
     * @param customer
     */
    private void validateCustomer(Customer customer) {
        if (customer == null || customer.getCustomerId() == null) {
            throw new ValidationException("Invalid customer");
        }
    }

    /**
     * Validate update location inputs
     *
     * @param assistant
     * @param assistantLocation
     */
    private void validateUpdateAssistantInputs(Assistant assistant, Geolocation assistantLocation) {
        validateAssistant(assistant);
        validateGeoLocation(assistantLocation);
    }

    /**
     * Validate geo location
     *
     * @param assistantLocation
     */
    private void validateGeoLocation(Geolocation assistantLocation) {
        if (assistantLocation == null) {
            throw new ValidationException("Invalid Assistant Location");
        }
    }

    /**
     * Validate reservation input
     *
     * @param customer
     * @param customerLocation
     */
    private void validateReserveAssistantInput(Customer customer, Geolocation customerLocation) {
        validateCustomer(customer);
        validateGeoLocation(customerLocation);
    }

    /**
     * Valiadte zip code
     *
     * @param geolocation
     */
    private void validateZipCode(Geolocation geolocation) {
        if (geolocation.getZipCode() == null) {
            throw new ValidationException("Invalid ZipCode");
        }
    }

    /**
     * Assigns assistant to customer
     *
     * @param customer
     * @param assistant
     */
    private void assignAssistantToCustomer(Customer customer, Assistant assistant) {
        CustomerEntity customerEntity = roadsideAssistanceEntityTransformer.transform(customer);
        AssistantEntiy assistantEntiy = roadsideAssistanceEntityTransformer.transform(assistant);

        ReserveCustomerAssistant reserveCustomerAssistant = new ReserveCustomerAssistant();
        reserveCustomerAssistant.setReservationId(BigInteger.valueOf(random.nextLong()));
        reserveCustomerAssistant.setAssistantEntiy(assistantEntiy);
        reserveCustomerAssistant.setCustomerEntity(customerEntity);

        customerEntity.setReserveCustomerAssistant(reserveCustomerAssistant);
        assistantEntiy.setReserveCustomerAssistant(reserveCustomerAssistant);
        assistantEntiy.setReserved(true);

        roadsideAssistanceRepository.reserve(reserveCustomerAssistant);
    }

    /**
     * Helper method to compare distances between two locations
     *
     * @param baseLatitude
     * @param baseLongitude
     * @param latitude1
     * @param longitude1
     * @param latitude2
     * @param longitude2
     * @return
     */
    private static int compareDistance(double baseLatitude, double baseLongitude, double latitude1, double longitude1,
                                       double latitude2, double longitude2) {
        Double distance1 = DistanceUtils.calculateDistance(baseLatitude, baseLongitude, latitude1, longitude1);
        Double distance2 = DistanceUtils.calculateDistance(baseLatitude, baseLongitude, latitude2, longitude2);

        return distance1.compareTo(distance2);
    }
}
