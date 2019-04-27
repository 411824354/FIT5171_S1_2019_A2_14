package rockets.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class RocketUnitTest {



    @AfterEach
    public void tearDown() {
    }

    @DisplayName("should create rocket successfully when given right parameters to constructor")
    @Test
    public void shouldConstructRocketObject() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertNotNull(bfr);
    }

    @DisplayName("should throw exception when given null manufacturer to constructor")
    @Test
    public void shouldThrowExceptionWhenNoManufacturerGiven() {
        String name = "BFR";
        String country = "USA";
        assertThrows(NullPointerException.class, () -> new Rocket(name, country, null));
    }

    @DisplayName("should set rocket massToLEO value")
    @ValueSource(strings = {"10000", "15000"})
    public void shouldSetMassToLEOWhenGivenCorrectValue(String massToLEO) throws Exception {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");

        Rocket bfr = new Rocket(name, country, manufacturer);

        bfr.setMassToLEO(massToLEO);
        assertEquals(massToLEO, bfr.getMassToLEO());
    }

    @DisplayName("should throw exception when set massToLEO to null")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertThrows(NullPointerException.class, () -> bfr.setMassToLEO(null));
    }
    //-----------------------------------------
    private Rocket target;
    @BeforeEach
    public void setUp(){

        LaunchServiceProvider MF = new LaunchServiceProvider("tom", 2019, "China");
        target = new Rocket("Tom","China",MF);}

    @Test
    public void shouldConstructTarget(){ assertNotNull(target);}

    /*
     *accepts a non-null
     */
    /*
     *----------------------------------------------------------------------------------------------------------------------------------------
     */










    /*
     *----------------------------------------------------------------------------------------------------------------------------------------
     */



    @DisplayName("should throw exception when pass null to setMassToLEO function")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull1() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToLEO(null));
        assertEquals("massToLEO cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty massToLEO value to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToLEOToEmpty(String mass) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToLEO(mass));
        assertEquals("massToLEO cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a not digit input to massToLEO value to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"sss","///**","测试"})
    public void shouldTrowExceptionWhenPassNonDigitInputToSetMassToLEO(String mass){
        Exception exception = assertThrows(Exception.class, () -> target.setMassToLEO(mass));
        assertEquals("massToLEO should only be digit",exception.getMessage());
    }

    @DisplayName("should success assignment the value which involve the blank to setToLEO")
    @ParameterizedTest
    @ValueSource(strings = {"  1234 ","12 25"})
    public void shouldSuccessAssignmentValueInvolveBlankToSetMassLEO(String mass) throws Exception {
        target.setMassToLEO(mass);
        assertEquals(mass.replace(" ",""),target.getMassToLEO());
    }




    /*
     *----------------------------------------------------------------------------------------------------------------------------------------
     */
    @DisplayName("should throw exception when pass null to setMassToGTO function")
    @Test
    public void shouldThrowExceptionWhenSetMassToGTOToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToGTO(null));
        assertEquals("massToGTO cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty massToGTO value to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToGTOToEmpty(String mass) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToGTO(mass));
        assertEquals("massToGTO cannot be null or empty", exception.getMessage());

    }

    @DisplayName("should throw exception when pass a not digit input to massToGTO value to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"sss","///**","测试"})
    public void shouldTrowExceptionWhenPassNonDigitInputToSetMassToGTO(String mass){
        Exception exception = assertThrows(Exception.class, () -> target.setMassToGTO(mass));
        assertEquals("massToGTO should only be digit",exception.getMessage());
    }

    @DisplayName("should success assignment the value which involve the blank to massToGTO")
    @ParameterizedTest
    @ValueSource(strings = {"  1234 ","12 25"})
    public void shouldSuccessAssignmentValueInvolveBlankToSetMassGTO(String mass) throws Exception {
        target.setMassToLEO(mass);
        assertEquals(mass.replace(" ",""),target.getMassToLEO());
    }
    /*
     *----------------------------------------------------------------------------------------------------------------------------------------
     */


    @DisplayName("should throw exception when pass null to setMassToOther function")
    @Test
    public void shouldThrowExceptionWhenSetMassToOtherLEOToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToOther(null));
        assertEquals("massToOther cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty massToOther value to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void shouldThrowExceptionWhenSetMassToOtherToEmpty(String mass) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToOther(mass));
        assertEquals("massToOther cannot be null or empty", exception.getMessage());
    }
    @DisplayName("should throw exception when pass a not digit input to massToOther value to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"sss","///**","测试","12#df"})
    public void shouldTrowExceptionWhenPassNonDigitInputToSetMassToOther(String mass){
        Exception exception = assertThrows(Exception.class, () -> target.setMassToOther(mass));
        assertEquals("massToOther should only be digit",exception.getMessage());
    }

    @DisplayName("should success assignment the value which involve the blank to massToOther")
    @ParameterizedTest
    @ValueSource(strings = {"  1234 ","12 25"})
    public void shouldSuccessAssignmentValueInvolveBlankToSetMassOther(String mass) throws Exception {
        target.setMassToOther(mass);
        assertEquals(mass.replace(" ",""),target.getMassToOther());
    }
}