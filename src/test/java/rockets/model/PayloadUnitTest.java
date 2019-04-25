package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PayloadUnitTest {

    private Payload target;

    @BeforeEach
    public void setUp(){
        target = new Payload("GSAT-31","Spacecraft");
    }

    @DisplayName("should throw exception when pass null to setMass function")
    @Test
    public void shouldThrowExceptionWhenSetMassToNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMass(null));
        assertEquals("mass cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass non-numeric value to setMass function")
    @ParameterizedTest
    @ValueSource(strings = {"abc","a1b","1b","b1","1 1"})
    public void shouldThrowExceptionWhenSetMassToNonNumeric(String mass)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMass(mass));
        assertEquals("mass must be numeric and cannot be negative", exception.getMessage());
    }

    @DisplayName("should throw exception when pass negative value to setId function")
    @ParameterizedTest
    @ValueSource(strings = {"-1","-100"})
    public void shouldThrowExceptionWhenSetMassToNegative(String mass)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMass(mass));
        assertEquals("mass must be numeric and cannot be negative",exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setMission function")
    @Test
    public void shouldThrowExceptionWhenSetMissionToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMission(null));
        assertEquals("mission cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty mission to setMission function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMissionToEmpty(String mission) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMission(mission));
        assertEquals("mission cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should return true when two payloads have the same name and type")
    @Test
    public void shouldReturnTrueWhenUsersHaveSameNameANDType() {
        Payload anotherPayload = new Payload("GSAT-31", "Spacecraft");
        assertTrue(target.equals(anotherPayload));
    }

    @DisplayName("should return false when two payloads have different name and type")
    @ParameterizedTest
    @ValueSource(strings = {"IRNSS-1I,Spacecraft","SOYUZ MS-12,satellite","GSAT-31,satellite"})
    public void shouldReturnFalseWhenUsersHaveDifferentNameANDType(String payloadInfo) {
        String[] payloadInfos = payloadInfo.split(",");
        Payload anotherPayload = new Payload(	payloadInfos[0], payloadInfos[1]);
        assertFalse(target.equals(anotherPayload));
    }

}
