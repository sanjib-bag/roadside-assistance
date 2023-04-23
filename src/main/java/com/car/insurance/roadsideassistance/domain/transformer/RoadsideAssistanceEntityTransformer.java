package com.car.insurance.roadsideassistance.domain.transformer;

import com.car.insurance.roadsideassistance.domain.dto.Assistant;
import com.car.insurance.roadsideassistance.domain.dto.Customer;
import com.car.insurance.roadsideassistance.domain.dto.Geolocation;
import com.car.insurance.roadsideassistance.domain.entity.AssistantEntiy;
import com.car.insurance.roadsideassistance.domain.entity.CustomerEntity;
import com.car.insurance.roadsideassistance.domain.entity.GeolocationEntity;
import org.springframework.stereotype.Component;

/**
 * Transformation class to transform DTO to DAO and vice versa
 */
@Component
public class RoadsideAssistanceEntityTransformer {
    public AssistantEntiy transform(Assistant assistant) {
        AssistantEntiy assistantEntiy = new AssistantEntiy();
        assistantEntiy.setAssistantId(assistant.getAssistantId());
        assistantEntiy.setAssistantName(assistant.getAssistantName());
        assistantEntiy.setAssistentContactNumber(assistant.getAssistantContactNumber());

        return assistantEntiy;
    }

    public GeolocationEntity transform(Geolocation geoLocation) {
        GeolocationEntity geolocationEntity = new GeolocationEntity();
        geolocationEntity.setLatitude(geoLocation.getLatitude());
        geolocationEntity.setLongitude(geoLocation.getLongitude());
        geolocationEntity.setZipCode(geoLocation.getZipCode());

        return geolocationEntity;
    }

    public Assistant transform(AssistantEntiy assistantEntiy) {
        Assistant assistant = new Assistant();
        assistant.setAssistantId(assistantEntiy.getAssistantId());
        assistant.setAssistantName(assistantEntiy.getAssistantName());
        assistant.setAssistantContactNumber(assistantEntiy.getAssistentContactNumber());
        assistant.setReserved(assistantEntiy.isReserved());

        assistant.setAssistantLocation(transform(assistantEntiy.getAssistantLocation()));

        return assistant;
    }

    public Geolocation transform(GeolocationEntity geolocationEntity) {
        Geolocation geolocation = new Geolocation();
        geolocation.setZipCode(geolocationEntity.getZipCode());
        geolocation.setLongitude(geolocationEntity.getLongitude());
        geolocation.setLatitude(geolocationEntity.getLatitude());

        return geolocation;
    }

    public CustomerEntity transform(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerId(customer.getCustomerId());
        customerEntity.setCustomerName(customer.getCustomerName());
        customerEntity.setCustomerContactNumber(customer.getCustomerContactNumber());

        return customerEntity;
    }
}
