package rockets.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;

public class Launch extends Entity {
    public enum LaunchOutcome {
        FAILED, SUCCESSFUL
    }

    private LocalDate launchDate;

    private Rocket launchVehicle;

    private LaunchServiceProvider launchServiceProvider;

    private Set<Payload> payloads;

    private String launchSite;

    private String orbit;

    private String function;

    private BigDecimal price;

    private LaunchOutcome launchOutcome;

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        if (launchDate == null)
            throw new NullPointerException("launch date cannot be null");
        if (launchDate.getYear() <= 1900)
            throw new  IllegalArgumentException("launch date cannot equals or less than 1900");
        LocalDate date = LocalDate.now();
        if (date.compareTo(launchDate) < 0)
            throw new IllegalArgumentException("launch date cannot greater than current date");
        this.launchDate = launchDate;
    }

    public Rocket getLaunchVehicle() {
        return launchVehicle;
    }

    public void setLaunchVehicle(Rocket launchVehicle) {
        if (launchVehicle == null)
            throw new NullPointerException("launch vehicle cannot be null");
        else
            this.launchVehicle = launchVehicle;
    }

    public LaunchServiceProvider getLaunchServiceProvider() {
        return launchServiceProvider;
    }

    public void setLaunchServiceProvider(LaunchServiceProvider launchServiceProvider) {
        if (launchServiceProvider == null)
            throw new NullPointerException("launch service provider cannot be null");
        else
            this.launchServiceProvider = launchServiceProvider;
    }

    public Set<Payload> getPayload() {
        return payloads;
    }

    public void setPayload(Set<Payload> payloads) {
        if (payloads == null)
            throw new NullPointerException("payloads cannot be null");
        for ( Payload payload : payloads) {
            if (payload == null)
                throw new IllegalArgumentException("payloads cannot contain null object");
        }
        this.payloads = payloads;
    }

    public String getLaunchSite() {
        return launchSite;
    }

    public void setLaunchSite(String launchSite) {
        notBlank(launchSite, "launch site cannot be null or empty");
        this.launchSite = launchSite;
    }

    public String getOrbit() {
        return orbit;
    }

    public void setOrbit(String orbit) {
        notBlank(orbit, "orbit cannot be null or empty");
        this.orbit = orbit;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        notBlank(function, "function cannot be null or empty");
        this.function = function;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null)
            throw new NullPointerException("price cannot be null");
        else if (price.floatValue() < 0)
            throw new IllegalArgumentException("price cannot be negative");
        else
            this.price = price;
    }

    public LaunchOutcome getLaunchOutcome() {
        return launchOutcome;
    }

    public void setLaunchOutcome(LaunchOutcome launchOutcome) {
        if (launchOutcome == null)
            throw new NullPointerException("launch outcome cannot be null");
        this.launchOutcome = launchOutcome;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(launchDate, launch.launchDate) &&
                Objects.equals(launchVehicle, launch.launchVehicle) &&
                Objects.equals(launchServiceProvider, launch.launchServiceProvider) &&
                Objects.equals(orbit, launch.orbit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(launchDate, launchVehicle, launchServiceProvider, orbit);
    }
}
