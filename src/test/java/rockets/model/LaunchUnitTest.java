package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LaunchUnitTest {

    private Launch target;

    @BeforeEach
    public void setUp() {
        target = new Launch();
    }

    @DisplayName("should throw exception when pass null to setLauchDate function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchDateToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchDate(null));
        assertEquals("launch date cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when launch date lee or equal than 1900")
    @ParameterizedTest
    @ValueSource(ints = {1900,1899,1500})
    public void ShouldThrowExceptionWhenSetLaunchDateToIllegalYear(int year)
    {
        LocalDate date = LocalDate.of(year,10,21);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLaunchDate(date));
        assertEquals("launch date cannot equals or less than 1900",exception.getMessage());
    }

    @DisplayName("should throw exception when launch date greater than current day")
    @ParameterizedTest
    @ValueSource(ints = {1,50})
    public void shouldThrowExceptionWhenSetLaunchDateGreaterThanCurrentDate(int dayIncrement)
    {
        LocalDate date = LocalDate.now();
        LocalDate date1 = date.plusDays(dayIncrement);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLaunchDate(date1));
        assertEquals("launch date cannot greater than current date", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchVehicle function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchVehicleToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchVehicle(null));
        assertEquals("launch vehicle cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchServiceProvider function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchServiceProviderToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchServiceProvider(null));
        assertEquals("launch service provider cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setPayload function")
    @Test
    public void shouldThrowExceptionWhenSetPayloadstoNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPayload(null));
        assertEquals("payloads cannot be null",exception.getMessage());
    }

    @DisplayName("should throw exception when passed payloads has null object")
    @Test
    public void shouldThrowExceptionWhenPassedPayloadsIncludeNull(){
        Set<Payload> payloadSet = new HashSet<Payload>();
        payloadSet.add(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPayload(payloadSet));
        assertEquals("payloads cannot contain null object",exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchSite function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchSiteToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchSite(null));
        assertEquals("launch site cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty launch site to setLaunchSite function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetLaunchSiteToEmpty(String launchSite) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLaunchSite(launchSite));
        assertEquals("launch site cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setOrbit function")
    @Test
    public void shouldThrowExceptionWhenSetOrbitToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setOrbit(null));
        assertEquals("orbit cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty orbit to setOrbit function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetOrbitToEmpty(String orbit) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setOrbit(orbit));
        assertEquals("orbit cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setFunction function")
    @Test
    public void shouldThrowExceptionWhenSetFunctionToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setFunction(null));
        assertEquals("function cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty function to setFunction function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetFunctionToEmpty(String function) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFunction(function));
        assertEquals("function cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setPrice function")
    @Test
    public void shouldThrowExceptionWhenSetPriceToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPrice(null));
        assertEquals("price cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass negative value to setPrice function")
    @Test
    public void shouldThrowExceptionWhenSetPriceToNegative() {
        BigDecimal price = new BigDecimal(-1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPrice(price));
        assertEquals("price cannot be negative", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchOutCome function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchOutComeToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchOutcome(null));
        assertEquals("launch outcome cannot be null", exception.getMessage());
    }



    @DisplayName("should return true when two launches have the same launch date, launch vehicle, launch service provider and orbit")
    @Test
    public void shouldReturnTrueWhenLaunchesHaveSameDateVehicleServiceANDOrbit() {
        LocalDate date = LocalDate.of(2019, 3, 17);
        LaunchServiceProvider serviceProvider = new LaunchServiceProvider("China National Space Administration", 1920, "China");
        Rocket vehicle = new Rocket("300th Long March", "China",serviceProvider);
        String orbit = "low earth orbit";
        target.setLaunchDate(date);
        target.setLaunchVehicle(vehicle);
        target.setLaunchServiceProvider(serviceProvider);
        target.setOrbit(orbit);
        Launch anotherLaunch = new Launch();
        anotherLaunch.setLaunchDate(date);
        anotherLaunch.setLaunchVehicle(vehicle);
        anotherLaunch.setLaunchServiceProvider(serviceProvider);
        anotherLaunch.setOrbit(orbit);
        assertTrue(target.equals(anotherLaunch));

    }

    @DisplayName("should return false when launches have different launch date ,launch vehicle, launch service provider or orbit")
    @Test
    public void shouldReturnFalseWhenLaunchesHaveDifferentDateVehicleServiceOROrbit()
    {
        LocalDate date1 = LocalDate.of(2019,3,17);
        LocalDate date2 = LocalDate.of(2018,4,25);
        LocalDate[] dates = {date1,date2};
        LaunchServiceProvider serviceProvider1 = new LaunchServiceProvider("China National Space Administration", 1920, "China");
        LaunchServiceProvider serviceProvider2 = new LaunchServiceProvider("American National Space Administration", 1910, "American");
        Rocket vehicle1 = new Rocket("300th Long March", "China", serviceProvider1);
        Rocket vehicle2 = new Rocket("cat1000", "American", serviceProvider2);
        Rocket[] vehicles = {vehicle1,vehicle2};
        LaunchServiceProvider[] serviceProviders = {serviceProvider1,serviceProvider2};
        String[] orbits = {"low earth orbit","geostationary orbit"};
        target.setLaunchDate(date1);
        target.setLaunchVehicle(vehicle1);
        target.setLaunchServiceProvider(serviceProvider1);
        target.setOrbit(orbits[0]);
        int i = 0;
        int j = 0;
        int k = 0;
        int m = 0;
        boolean unfinished = true;
        while (unfinished)
        {
            Launch anotherLaunch = new Launch();
            i = i + 1;
            if (i == 2) { i = 0; j = j + 1; }
            if (j == 2) { j = 0; k = k + 1; }
            if (k == 2) { k = 0; m = m + 1; }
            if (i == 0 && j == 1 && k == 1 & m == 1)
                unfinished = false;
            anotherLaunch.setLaunchDate(dates[i]);
            anotherLaunch.setLaunchVehicle(vehicles[j]);
            anotherLaunch.setLaunchServiceProvider(serviceProviders[k]);
            anotherLaunch.setOrbit(orbits[m]);
            assertFalse(target.equals(anotherLaunch));
        }

    }







}

