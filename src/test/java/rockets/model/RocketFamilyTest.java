package rockets.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RocketFamilyTest {
    private RocketFamily target;

    RocketFamilyTest() {
    }

    @BeforeEach
    public void setUp() {
        this.target = new RocketFamily("rocketFamily", "country", "development organization");
    }

    @Test
    public void shouldConstructTarget() {
        Assertions.assertNotNull(this.target);
    }

    @DisplayName("should throw exception when pass the null and empty to the setFamilyName")
    @ParameterizedTest
    @ValueSource(
            strings = {"", " ", "  "}
    )
    public void shouldExceptionNuWhenSetRocketEmptyOrNull(String fm) {
        IllegalArgumentException exception = (IllegalArgumentException)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.target.setFamilyName(fm);
        });
        Assertions.assertEquals("family name can not be null", exception.getMessage());
    }

    @DisplayName("should input the correct format when set a familyName")
    @ParameterizedTest
    @ValueSource(
            strings = {"familyNAme", "FAMILYNAME", "  FAMILYname  "}
    )
    public void shouldFormatFamilyname(String fm) {
        this.target.setFamilyName(fm);
        Assertions.assertEquals("Familyname", this.target.getFamilyName());
    }

    @DisplayName("setCountry function should throw a exception when pass the null")
    @ParameterizedTest
    @ValueSource(
            strings = {"", " ", "  "}
    )
    void setCountry(String country) {
        IllegalArgumentException exception = (IllegalArgumentException)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.target.setCountry(country);
        });
        Assertions.assertEquals("country name can not be null", exception.getMessage());
    }

    @DisplayName("assert exception when the passed value contain other content rather than letter")
    @ParameterizedTest
    @ValueSource(
            strings = {"sdf123", "123", "!@#", "中国"}
    )
    void testCountryNameContent(String country) {
        IllegalArgumentException exception = (IllegalArgumentException)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.target.setCountry(country);
        });
        Assertions.assertEquals("country name should only contain letter", exception.getMessage());
    }

    @DisplayName("the country name should no space")
    @ParameterizedTest
    @ValueSource(
            strings = {"cH  INa", "  CH INA", "  China ", "chi  na"}
    )
    public void noSpaceInCountryName(String country) {
        this.target.setCountry(country);
        String c1 = this.target.getCountry();
        Assertions.assertEquals("China", c1);
    }

    @DisplayName("setDevelop_OR should throw exception when is passed null or blank")
    @ParameterizedTest
    @ValueSource(
            strings = {"", " ", "  "}
    )
    void setDevelop_OR(String dp) {
        IllegalArgumentException exception = (IllegalArgumentException)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.target.setDevelop_OR(dp);
        });
        Assertions.assertEquals("development organization can not be null", exception.getMessage());
    }

    @DisplayName("setDevelop_OR should remove pass value fornt and behaind space")
    @ParameterizedTest
    @ValueSource(
            strings = {"  china", "china   ", " china "}
    )
    public void testRemoveSpaceOfSetDevelop_OR(String dp) {
        this.target.setDevelop_OR(dp);
        Assertions.assertEquals("china", this.target.getDevelop_OR());
    }
}
