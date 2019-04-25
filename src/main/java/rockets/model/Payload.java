package rockets.model;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Payload extends Entity {
    private String name;
    private  String type;
    private  String mass;
    private  String mission;

    public Payload(String name, String type){
        notNull(name);
        notNull(type);

        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        if (mass == null)
            throw new NullPointerException("mass cannot be null");
        else if (!isNumeric(mass))
            throw new IllegalArgumentException("mass must be numeric and cannot be negative");
        else
            this.mass = mass;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        notBlank(mission, "mission cannot be null or empty");
        this.mission = mission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload payload = (Payload) o;
        return Objects.equals(name, payload.name) &&
                Objects.equals(type, payload.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    public String toString() {
        return "Payload{" +
                "name='" + name + '\'' +
                ", type'" + type + '\'' +
                ", mass='" + mass + '\'' +
                ", mission='" + mission + '\'' +
                '}';
    }

    public boolean isNumeric(String input)
    {
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
        boolean matches = pattern.matcher(input).matches();
        return matches;
    }
}
