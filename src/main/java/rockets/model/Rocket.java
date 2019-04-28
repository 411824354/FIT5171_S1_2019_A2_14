package rockets.model;



import java.util.Objects;

import static jdk.nashorn.internal.runtime.JSType.isNumber;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;


public class Rocket extends Entity {
    private String name;

    private String country;

    private LaunchServiceProvider manufacturer;

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
    public Rocket(String name, String country, LaunchServiceProvider manufacturer) {
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

    public LaunchServiceProvider getManufacturer() {
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

    public boolean isNumber(String input){
        for (int i = input.length();--i>=0;){
            if (!Character.isDigit(input.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public void setName(String name){
        notNull(name,"the name should not empty or null");
        notBlank(name,"the name should not empty or null");
        name = name.trim();
        if (name.length() < 40){
            this.name = name;
        }
        else
        {
            throw new IllegalArgumentException("the name should less than 40 letters");
        }
    }

    public void setCountry(String country){

        notNull(country,"the country should not be null or empty");
        notBlank(country,"the country should not be null or empty");
        this.country = country;
    }

    public void setMassToLEO(String massToLEO) throws Exception{
        notBlank(massToLEO, "massToLEO cannot be null or empty");
        massToLEO = massToLEO.replace(" ","");
        if (isNumber(massToLEO)){
            this.massToLEO = massToLEO;
        }
        else {

            throw new Exception("massToLEO should only be digit");
        }    }

    public void setMassToGTO(String massToGTO) throws Exception{
        notNull(massToGTO,"massToGTO cannot be null or empty");
        notBlank(massToGTO, "massToGTO cannot be null or empty");

        massToGTO = massToGTO.replace(" ","");
        if (isNumber(massToGTO)) {
            this.massToGTO = massToGTO;
        }
        else {

            throw new Exception("massToGTO should only be digit");
        }
    }

    public void setMassToOther (String massToOther) throws Exception {
        notNull(massToOther,"massToOther cannot be null or empty");
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
