package com.car.insurance.roadsideassistance.service.impl;

import com.car.insurance.roadsideassistance.domain.dto.Assistant;
import com.car.insurance.roadsideassistance.domain.dto.Customer;
import com.car.insurance.roadsideassistance.domain.dto.Geolocation;
import com.car.insurance.roadsideassistance.domain.entity.AssistantEntiy;
import com.car.insurance.roadsideassistance.exceptions.ValidationException;
import com.car.insurance.roadsideassistance.repository.RoadsideAssistanceRepository;
import com.car.insurance.roadsideassistance.service.RoadsideAssistanceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RoadsideAssistanceServiceImplTest {

    @Autowired
    RoadsideAssistanceService roadsideAssistanceService;
    @Autowired
    RoadsideAssistanceRepository roadsideAssistanceRepository;

    @BeforeEach
    void setUp() {
        //For updateAssistantLocation tests
        setupUpdateAssistantLocationData();

        //For findNearestAssistants tests
        setupFindNearestAssistants();
    }

    private void setupUpdateAssistantLocationData() {
        AssistantEntiy assistantEntiy1 = new AssistantEntiy();
        assistantEntiy1.setAssistantId(new BigInteger("1"));
        assistantEntiy1.setAssistantName("Assistant1");
        roadsideAssistanceRepository.addAssistant(assistantEntiy1);

        AssistantEntiy assistantEntiy2 = new AssistantEntiy();
        assistantEntiy2.setAssistantId(new BigInteger("2"));
        assistantEntiy2.setAssistantName("Assistant2");
        roadsideAssistanceRepository.addAssistant(assistantEntiy2);
    }

    @AfterEach
    void tearDown() {
        roadsideAssistanceRepository.cleanAllData();
    }

    private void setupFindNearestAssistants() {

        //Create 5 assistants and update their locations in ascending order of distance.
        // Assistant10 is close and Assistant14 is furthest
        AssistantEntiy assistantEntiy10 = new AssistantEntiy();
        assistantEntiy10.setAssistantId(new BigInteger("10"));
        assistantEntiy10.setAssistantName("Assistant10");
        roadsideAssistanceRepository.addAssistant(assistantEntiy10);

        AssistantEntiy assistantEntiy11 = new AssistantEntiy();
        assistantEntiy11.setAssistantId(new BigInteger("11"));
        assistantEntiy11.setAssistantName("Assistant11");
        roadsideAssistanceRepository.addAssistant(assistantEntiy11);

        AssistantEntiy assistantEntiy12 = new AssistantEntiy();
        assistantEntiy12.setAssistantId(new BigInteger("12"));
        assistantEntiy12.setAssistantName("Assistant12");
        roadsideAssistanceRepository.addAssistant(assistantEntiy12);

        AssistantEntiy assistantEntiy13 = new AssistantEntiy();
        assistantEntiy13.setAssistantId(new BigInteger("13"));
        assistantEntiy13.setAssistantName("Assistant13");
        roadsideAssistanceRepository.addAssistant(assistantEntiy13);

        AssistantEntiy assistantEntiy14 = new AssistantEntiy();
        assistantEntiy14.setAssistantId(new BigInteger("14"));
        assistantEntiy14.setAssistantName("Assistant14");
        roadsideAssistanceRepository.addAssistant(assistantEntiy14);

        Assistant assistant10 = new Assistant();
        assistant10.setAssistantId(new BigInteger("10"));

        Geolocation geolocation10 = new Geolocation();
        geolocation10.setZipCode("11111");
        geolocation10.setLatitude(0);
        geolocation10.setLongitude(5);
        roadsideAssistanceService.updateAssistantLocation(assistant10, geolocation10);

        Assistant assistant11 = new Assistant();
        assistant11.setAssistantId(new BigInteger("11"));

        Geolocation geolocation11 = new Geolocation();
        geolocation11.setZipCode("11111");
        geolocation11.setLatitude(0);
        geolocation11.setLongitude(10);
        roadsideAssistanceService.updateAssistantLocation(assistant11, geolocation11);

        Assistant assistant12 = new Assistant();
        assistant12.setAssistantId(new BigInteger("12"));

        Geolocation geolocation12 = new Geolocation();
        geolocation12.setZipCode("11111");
        geolocation12.setLatitude(0);
        geolocation12.setLongitude(20);
        roadsideAssistanceService.updateAssistantLocation(assistant12, geolocation12);

        Assistant assistant13 = new Assistant();
        assistant13.setAssistantId(new BigInteger("13"));

        Geolocation geolocation13 = new Geolocation();
        geolocation13.setZipCode("11111");
        geolocation13.setLatitude(0);
        geolocation13.setLongitude(30);
        roadsideAssistanceService.updateAssistantLocation(assistant13, geolocation13);

        Assistant assistant14 = new Assistant();
        assistant14.setAssistantId(new BigInteger("14"));

        Geolocation geolocation14 = new Geolocation();
        geolocation14.setZipCode("11111");
        geolocation14.setLatitude(0);
        geolocation14.setLongitude(40);
        roadsideAssistanceService.updateAssistantLocation(assistant14, geolocation14);
    }

    /**
     * All test cases related to updateAssistant below
     */
    @Test
    void updateAssistantLocation() {
        Assistant assistant = new Assistant();
        assistant.setAssistantId(new BigInteger("1"));

        Geolocation assistantLocation = new Geolocation();
        assistantLocation.setLatitude(12);
        assistantLocation.setLongitude(10);
        assistantLocation.setZipCode("11111");

        AssistantEntiy beforeUpdate = roadsideAssistanceRepository.getAssistant(new BigInteger("1")).get();

        assertNull(beforeUpdate.getAssistantLocation());

        roadsideAssistanceService.updateAssistantLocation(assistant, assistantLocation);

        AssistantEntiy afterUpdate = roadsideAssistanceRepository.getAssistant(new BigInteger("1")).get();

        assertEquals(assistantLocation.getLatitude(), afterUpdate.getAssistantLocation().getLatitude());
        assertEquals(assistantLocation.getLongitude(), afterUpdate.getAssistantLocation().getLongitude());
    }

    @Test
    void updateAssistantLocation_Without_Zip() {
        Assistant assistant = new Assistant();
        assistant.setAssistantId(new BigInteger("2"));

        Geolocation assistantLocation = new Geolocation();
        assistantLocation.setLatitude(12);
        assistantLocation.setLongitude(10);

        AssistantEntiy beforeUpdate = roadsideAssistanceRepository.getAssistant(new BigInteger("2")).get();

        assertNull(beforeUpdate.getAssistantLocation());

        roadsideAssistanceService.updateAssistantLocation(assistant, assistantLocation);

        AssistantEntiy afterUpdate = roadsideAssistanceRepository.getAssistant(new BigInteger("2")).get();

        assertEquals(assistantLocation.getLatitude(), afterUpdate.getAssistantLocation().getLatitude());
        assertEquals(assistantLocation.getLongitude(), afterUpdate.getAssistantLocation().getLongitude());
    }

    @Test
    void updateAssistantLocation_Null_Assistant() {
        Geolocation assistantLocation = new Geolocation();
        assistantLocation.setLatitude(12);
        assistantLocation.setLongitude(10);
        assistantLocation.setZipCode("11111");

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.updateAssistantLocation(null, assistantLocation),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid assistant", thrown.getMessage());
    }

    @Test
    void updateAssistantLocation_Null_Assistant_Id() {
        Assistant assistant = new Assistant();

        Geolocation assistantLocation = new Geolocation();
        assistantLocation.setLatitude(12);
        assistantLocation.setLongitude(10);
        assistantLocation.setZipCode("11111");

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.updateAssistantLocation(assistant, assistantLocation),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid assistant", thrown.getMessage());
    }

    @Test
    void updateAssistantLocation_Null_GeoLocation() {
        Assistant assistant = new Assistant();
        assistant.setAssistantId(new BigInteger("1"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.updateAssistantLocation(assistant, null),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid Assistant Location", thrown.getMessage());
    }

    @Test
    void updateAssistantLocation_Assistant_Not_Found() {
        Assistant assistant = new Assistant();
        assistant.setAssistantId(new BigInteger("3"));

        Geolocation assistantLocation = new Geolocation();
        assistantLocation.setLatitude(12);
        assistantLocation.setLongitude(10);
        assistantLocation.setZipCode("11111");

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> roadsideAssistanceService.updateAssistantLocation(assistant, assistantLocation),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Given Assistant is not found", thrown.getMessage());
    }

    /**
     * All test cases related to findNearestAssistants below
     */

    @Test
    void findNearestAssistants_with_limit() {
        Geolocation geolocation = new Geolocation();
        geolocation.setZipCode("11111");
        geolocation.setLatitude(0);
        geolocation.setLongitude(0);

        SortedSet<Assistant> assitants = roadsideAssistanceService.findNearestAssistants(geolocation, 2);

        assertEquals(2, assitants.size());

        List<Assistant> lstAssistants = assitants.stream().collect(Collectors.toList());
        assertEquals("Assistant10", lstAssistants.get(0).getAssistantName());
        assertEquals("Assistant11", lstAssistants.get(1).getAssistantName());
    }

    @Test
    void findNearestAssistants_without_limit_return_all() {
        Geolocation geolocation = new Geolocation();
        geolocation.setZipCode("11111");
        geolocation.setLatitude(0);
        geolocation.setLongitude(0);

        SortedSet<Assistant> assitants = roadsideAssistanceService.findNearestAssistants(geolocation, -1);
        assertEquals(5, assitants.size());
    }

    @Test
    void findNearestAssistants_invalid_geo_location() {
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.findNearestAssistants(null, 5),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid Assistant Location", thrown.getMessage());
    }

    @Test
    void findNearestAssistants_invalid_geo_location_zip() {
        Geolocation geolocation = new Geolocation();
        geolocation.setLatitude(0);
        geolocation.setLongitude(0);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.findNearestAssistants(geolocation, 5),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid ZipCode", thrown.getMessage());
    }

    /**
     * All test cases related to reserveAssistant below
     */

    @Test
    void reserveAssistant_all_free() {
        Customer customer = new Customer();
        customer.setCustomerName("Customer1");
        customer.setCustomerId(new BigInteger("1"));

        Geolocation customerLocation = new Geolocation();
        customerLocation.setLongitude(0);
        customerLocation.setLatitude(0);
        customerLocation.setZipCode("11111");

        Optional<Assistant> assistantOptional = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

        assertTrue(assistantOptional.isPresent());
        assertEquals("Assistant10", assistantOptional.get().getAssistantName());
        assertTrue(assistantOptional.get().isReserved());
    }

    @Test
    void reserveAssistant_first_assistant_reserved() {
        Customer customer = new Customer();
        customer.setCustomerName("Customer1");
        customer.setCustomerId(new BigInteger("1"));

        Geolocation customerLocation = new Geolocation();
        customerLocation.setLongitude(0);
        customerLocation.setLatitude(0);
        customerLocation.setZipCode("11111");

        Optional<Assistant> assistantOptional = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

        assertTrue(assistantOptional.isPresent());
        assertEquals("Assistant10", assistantOptional.get().getAssistantName());
        assertTrue(assistantOptional.get().isReserved());

        Customer customer2 = new Customer();
        customer.setCustomerName("Customer2");
        customer.setCustomerId(new BigInteger("2"));

        Optional<Assistant> assistantOptional2 = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

        assertTrue(assistantOptional2.isPresent());
        assertEquals("Assistant11", assistantOptional2.get().getAssistantName());
        assertTrue(assistantOptional2.get().isReserved());
    }

    @Test
    void reserveAssistant_all_assistant_reserved() {

        //Exhausts all available assistants by reserving them
        for (int i = 0; i < 5; i++) {
            Customer customer = new Customer();
            customer.setCustomerName("Customer" + i);
            customer.setCustomerId(new BigInteger(String.valueOf(i)));

            Geolocation customerLocation = new Geolocation();
            customerLocation.setLongitude(0);
            customerLocation.setLatitude(0);
            customerLocation.setZipCode("11111");

            Optional<Assistant> assistantOptional = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

            assertTrue(assistantOptional.isPresent());
            assertEquals("Assistant1" + i, assistantOptional.get().getAssistantName());
            assertTrue(assistantOptional.get().isReserved());
        }

        Customer customer = new Customer();
        customer.setCustomerName("Customer5");
        customer.setCustomerId(new BigInteger("5"));

        Geolocation customerLocation = new Geolocation();
        customerLocation.setLongitude(0);
        customerLocation.setLatitude(0);
        customerLocation.setZipCode("11111");

        Optional<Assistant> assistantOptional = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

        assertFalse(assistantOptional.isPresent());
    }

    @Test
    void reserveAssistant_invalid_customer() {
        Geolocation geolocation = new Geolocation();
        geolocation.setZipCode("11111");
        geolocation.setLatitude(0);
        geolocation.setLongitude(0);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.reserveAssistant(null, geolocation),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid customer", thrown.getMessage());
    }

    @Test
    void reserveAssistant_invalid_customer_id() {
        Customer customer = new Customer();

        Geolocation geolocation = new Geolocation();
        geolocation.setZipCode("11111");
        geolocation.setLatitude(0);
        geolocation.setLongitude(0);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.reserveAssistant(customer, geolocation),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid customer", thrown.getMessage());
    }

    @Test
    void reserveAssistant_invalid_geo_location() {
        Customer customer = new Customer();
        customer.setCustomerId(new BigInteger("1"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.reserveAssistant(customer, null),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid Assistant Location", thrown.getMessage());
    }

    /**
     * All test cases related to releaseAssistant below
     */
    @Test
    void releaseAssistant() {
        //Setup customer reservation
        Customer customer = new Customer();
        customer.setCustomerName("Customer1");
        customer.setCustomerId(new BigInteger("1"));

        Geolocation customerLocation = new Geolocation();
        customerLocation.setLongitude(0);
        customerLocation.setLatitude(0);
        customerLocation.setZipCode("11111");

        Optional<Assistant> assistantOptional = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

        assertTrue(assistantOptional.isPresent());
        assertEquals("Assistant10", assistantOptional.get().getAssistantName());
        assertTrue(assistantOptional.get().isReserved());

        //Validate releaseAssistant feature
        roadsideAssistanceService.releaseAssistant(customer, assistantOptional.get());

        Optional<AssistantEntiy> retrievedAssistantOptional = roadsideAssistanceRepository.
                getAssistant(new BigInteger("10"));

        assertFalse(retrievedAssistantOptional.get().isReserved());
    }

    @Test
    void releaseAssistant_wrong_reservation_customer() {
        //Setup customer reservation
        Customer customer = new Customer();
        customer.setCustomerName("Customer1");
        customer.setCustomerId(new BigInteger("1"));

        Geolocation customerLocation = new Geolocation();
        customerLocation.setLongitude(0);
        customerLocation.setLatitude(0);
        customerLocation.setZipCode("11111");

        Optional<Assistant> assistantOptional = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

        assertTrue(assistantOptional.isPresent());
        assertEquals("Assistant10", assistantOptional.get().getAssistantName());
        assertTrue(assistantOptional.get().isReserved());

        //releaseAssistant called with wrong customer
        Customer customer2 = new Customer();
        customer2.setCustomerId(new BigInteger("2"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.releaseAssistant(customer2, assistantOptional.get()),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Reservation not found", thrown.getMessage());
    }

    @Test
    void releaseAssistant_wrong_reservation_assistant() {
        //Setup customer reservation
        Customer customer = new Customer();
        customer.setCustomerName("Customer1");
        customer.setCustomerId(new BigInteger("1"));

        Geolocation customerLocation = new Geolocation();
        customerLocation.setLongitude(0);
        customerLocation.setLatitude(0);
        customerLocation.setZipCode("11111");

        Optional<Assistant> assistantOptional = roadsideAssistanceService.reserveAssistant(customer, customerLocation);

        assertTrue(assistantOptional.isPresent());
        assertEquals("Assistant10", assistantOptional.get().getAssistantName());
        assertTrue(assistantOptional.get().isReserved());

        //releaseAssistant called with wrong assistant
        Assistant assistant = new Assistant();
        assistant.setAssistantId(new BigInteger("11"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.releaseAssistant(customer, assistant),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Reservation not found", thrown.getMessage());
    }

    @Test
    void releaseAssistant_invalid_customer() {
        Assistant assistant = new Assistant();
        assistant.setAssistantId(new BigInteger("1"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.releaseAssistant(null, assistant),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid customer", thrown.getMessage());
    }

    @Test
    void releaseAssistant_invalid_customer_id() {
        Assistant assistant = new Assistant();
        assistant.setAssistantId(new BigInteger("1"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.releaseAssistant(new Customer(), assistant),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid customer", thrown.getMessage());
    }

    @Test
    void releaseAssistant_invalid_assistant() {
        Customer customer = new Customer();
        customer.setCustomerName("Customer1");
        customer.setCustomerId(new BigInteger("1"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.releaseAssistant(customer, null),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid assistant", thrown.getMessage());
    }

    @Test
    void releaseAssistant_invalid_assistant_id() {
        Customer customer = new Customer();
        customer.setCustomerName("Customer1");
        customer.setCustomerId(new BigInteger("1"));

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> roadsideAssistanceService.releaseAssistant(customer, new Assistant()),
                "Expected doThing() to throw, but it didn't"
        );

        assertEquals("Invalid assistant", thrown.getMessage());
    }
}