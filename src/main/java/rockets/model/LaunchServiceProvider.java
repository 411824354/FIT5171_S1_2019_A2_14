package rockets.model;

import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;

public class LaunchServiceProvider extends Entity {
    private String name;

    private int yearFounded;

    private String country;

    private String headquarters;

    private Set<Rocket> rockets;

    public LaunchServiceProvider(){
        rockets = Sets.newLinkedHashSet();
    }

    public LaunchServiceProvider(String name, int yearFounded, String country) {
        this.name = name;
        this.yearFounded = yearFounded;
        this.country = country;

        rockets = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    public int getYearFounded() {

        return yearFounded;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Null input");
        if (name.trim().isEmpty())
            throw new IllegalArgumentException("Empty input");
        this.name = name.trim();
    }

    public void setYearFounded(int yearFounded) {
        if (yearFounded <= 0)
            throw new IllegalArgumentException("Zero or negative input");
        if (yearFounded <= 1900 || yearFounded >= 2020)
            throw new IllegalArgumentException("Not in range input");
        this.yearFounded = yearFounded;
    }

    public void setCountry(String country) {
        if (country == null)
            throw new IllegalArgumentException("Null input");
        if (country.trim().isEmpty())
            throw new IllegalArgumentException("Empty input");
        this.country = country.trim();
    }

    public void setHeadquarters(String headquarters) {
        if (headquarters == null)
            throw new IllegalArgumentException("Null input");
        if (headquarters.trim().isEmpty())
            throw new IllegalArgumentException("Empty input");
        this.headquarters = headquarters.trim();
    }

    public void setRockets(Set<Rocket> rockets) {
        if (rockets == null)
            throw new NullPointerException("Null input");
        this.rockets = rockets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchServiceProvider that = (LaunchServiceProvider) o;
        return yearFounded == that.yearFounded &&
                Objects.equals(name, that.name) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, yearFounded, country);
    }
}
