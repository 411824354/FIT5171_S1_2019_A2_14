package rockets.model;

import java.lang.reflect.Array;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Rocket extends Entity {
    private String name;

    private String country;

    private String manufacturer;

    private String massToLEO;

    private String massToGTO;

    private String massToOther;

    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param country
     * @param manufacturer
     */
    public Rocket(String name, String country, String manufacturer) {

        notNull(name);
        notNull(country);
        notNull(manufacturer);



        this.name = name;
        this.country = country;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getMassToLEO() {
        return massToLEO;
    }

    public String getMassToGTO() {
        return massToGTO;
    }

    public String getMassToOther() {
        return massToOther;
    }

    public boolean isNumber(String str) {
        String regex = "^[0-9]+$";
        return str.matches(regex);
    }

    public void setMassToLEO(String massToLEO) throws Exception {

        notBlank(massToLEO, "massToLEO cannot be null or empty");
        massToLEO = massToLEO.replace(" ","");
        if (isNumber(massToLEO)){
            this.massToLEO = massToLEO;
        }
        else {

            throw new Exception("massToLEO should only be digit");
        }

    }

    public void setMassToGTO(String massToGTO) throws Exception {
        notBlank(massToGTO, "massToGTO cannot be null or empty");
        massToGTO = massToGTO.replace(" ","");
        if (isNumber(massToGTO)) {
            this.massToGTO = massToGTO;
        } else {

            throw new Exception("massToGTO should only be digit");
        }
    }

    public void setMassToOther (String massToOther) throws Exception {
        notBlank(massToOther,"massToOther cannot be null or empty");
        massToOther = massToOther.replace(" ","");
        if (isNumber(massToOther)){
            this.massToOther = massToOther;
        }
        else{
            throw new Exception("massToOther should only be digit");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rocket rocket = (Rocket) o;
        return Objects.equals(name, rocket.name) &&
                Objects.equals(country, rocket.country) &&
                Objects.equals(manufacturer, rocket.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, manufacturer);
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", massToLEO='" + massToLEO + '\'' +
                ", massToGTO='" + massToGTO + '\'' +
                ", massToOther='" + massToOther + '\'' +
                '}';
    }
}
