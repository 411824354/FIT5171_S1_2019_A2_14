package rockets.dataaccess.neo4j;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import rockets.dataaccess.DAO;
import rockets.model.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Neo4jDAOUnitTest {
    private DAO dao;
    private Session session;
    private SessionFactory sessionFactory;

    private LaunchServiceProvider esa;
    private LaunchServiceProvider spacex;
    private Rocket rocket;

    @BeforeAll
    public void initializeNeo4j() {
        ServerControls embeddedDatabaseServer = TestServerBuilders.newInProcessBuilder().newServer();
        GraphDatabaseService dbService = embeddedDatabaseServer.graph();
        EmbeddedDriver driver = new EmbeddedDriver(dbService);
        sessionFactory = new SessionFactory(driver, User.class.getPackage().getName());
        session = sessionFactory.openSession();
        dao = new Neo4jDAO(session);
    }

    @BeforeEach
    public void setup() {
        esa = new LaunchServiceProvider("ESA", 1970, "Europe");
        spacex = new LaunchServiceProvider("SpaceX", 2002, "USA");
        rocket = new Rocket("F9", "USA", spacex);
    }

    @Test
    public void shouldCreateNeo4jDAOSuccessfully() {
        assertNotNull(dao);
    }

    @Test
    public void shouldCreateARocketSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Falcon_9");
        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);
        LaunchServiceProvider manufacturer = graphRocket.getManufacturer();
        assertNotNull(manufacturer.getId());
        assertEquals(rocket.getWikilink(), graphRocket.getWikilink());
        assertEquals(spacex, manufacturer);
    }

    @Test
    public void shouldUpdateRocketAttributeSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Falcon_9");

        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);

        String newLink = "http://adifferentlink.com";
        rocket.setWikilink(newLink);
        dao.createOrUpdate(rocket);
        graphRocket = dao.load(Rocket.class, rocket.getId());
        assertEquals(newLink, graphRocket.getWikilink());
    }

    @Test
    public void shouldNotSaveTwoSameRockets() {
        assertNull(spacex.getId());

        Rocket rocket1 = new Rocket("F9", "USA", spacex);
        Rocket rocket2 = new Rocket("F9", "USA", spacex);
        assertEquals(rocket1, rocket2);
        dao.createOrUpdate(rocket1);
        assertNotNull(spacex.getId());
        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());
        Collection<LaunchServiceProvider> manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());
        dao.createOrUpdate(rocket2);
        manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());
        rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());
    }

    @Test
    public void shouldLoadAllRockets() {
        Set<Rocket> rockets = Sets.newHashSet(
                new Rocket("Ariane4", "France", esa),
                new Rocket("F5", "USA", spacex),
                new Rocket("BFR", "USA", spacex)
        );

        for (Rocket r : rockets) {
            dao.createOrUpdate(r);
        }

        Collection<Rocket> loadedRockets = dao.loadAll(Rocket.class);
        assertEquals(rockets.size(), loadedRockets.size());
        for (Rocket r : rockets) {
            assertTrue(rockets.contains(r));
        }
    }

    @Test
    public void shouldCreateALaunchSuccessfully() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2017, 1, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);
        assertFalse(launches.isEmpty());
        assertTrue(launches.contains(launch));
    }


    @Test
    public void shouldUpdateLaunchAttributesSuccessfully() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2017, 1, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);

        Launch loadedLaunch = launches.iterator().next();
        assertNull(loadedLaunch.getFunction());

        launch.setFunction("experimental");
        dao.createOrUpdate(launch);
        launches = dao.loadAll(Launch.class);
        assertEquals(1, launches.size());
        loadedLaunch = launches.iterator().next();
        assertEquals("experimental", loadedLaunch.getFunction());
    }

    @Test
    public void shouldLoadAllLaunches() {
        Set<Launch> launches = Sets.newHashSet(

        );

        for (Launch l : launches) {
            dao.createOrUpdate(l);
        }

        Collection<Launch> loadedLaunches = dao.loadAll(Launch.class);
        assertEquals(launches.size(), loadedLaunches.size());
        for (Launch l : launches) {
            assertTrue(launches.contains(l));
        }
    }

    @Test
    public void shouldDeleteRocketWithoutDeleteLSP() {
        dao.createOrUpdate(rocket);
        assertNotNull(rocket.getId());
        assertNotNull(rocket.getManufacturer().getId());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(rocket);
        assertTrue(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    @Test
    public void shouldDeleteLaunchWithoutDeleteLSP() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2017, 1, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchServiceProvider(spacex);
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);
        assertFalse(dao.loadAll(Launch.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(launch);
        assertTrue(dao.loadAll(Launch.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    @Test
    public void shouldCreateALaunchServiceProviderSuccessfully() {
        LaunchServiceProvider launchServiceProvider = new LaunchServiceProvider();
        launchServiceProvider.setName("SpaceX");
        launchServiceProvider.setCountry("USA");
        launchServiceProvider.setYearFounded(2002);
        dao.createOrUpdate(launchServiceProvider);

        Collection<LaunchServiceProvider> launchServiceProviders = dao.loadAll(LaunchServiceProvider.class);
        assertFalse(launchServiceProviders.isEmpty());
        assertTrue(launchServiceProviders.contains(launchServiceProvider));
    }

    @Test
    public void shouldUpdateLaunchServiceProviderAttributesSuccessfully() {
        LaunchServiceProvider launchServiceProvider = new LaunchServiceProvider();
        launchServiceProvider.setName("SpaceX");
        launchServiceProvider.setCountry("USA");
        launchServiceProvider.setYearFounded(2002);
        dao.createOrUpdate(launchServiceProvider);

        Collection<LaunchServiceProvider> lsp = dao.loadAll(LaunchServiceProvider.class);

        LaunchServiceProvider loadedLSP = lsp.iterator().next();
        assertNull(loadedLSP.getHeadquarters());

        launchServiceProvider.setHeadquarters("SpaceX USA");
        dao.createOrUpdate(launchServiceProvider);
        lsp = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, lsp.size());
        loadedLSP = lsp.iterator().next();
        assertEquals("SpaceX USA", loadedLSP.getHeadquarters());
    }

    @Test
    public void shouldLoadAllLSP() {
        Set<LaunchServiceProvider> lsp = Sets.newHashSet(
                esa, spacex
        );

        for (LaunchServiceProvider l : lsp) {
            dao.createOrUpdate(l);
        }

        Collection<LaunchServiceProvider> loadedLSP = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(lsp.size(), loadedLSP.size());
        for (LaunchServiceProvider l : lsp) {
            assertTrue(lsp.contains(l));
        }
    }

    @Test
    public void shouldDeleteLaunchServiceProviderSuccessfully(){
        dao.createOrUpdate(esa);
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(esa);
        assertTrue(dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    @Test
    public void shouldCreateARocketFamilySuccessfully() {
        RocketFamily rocketFamily = new RocketFamily("Falcon9", "USA", "SpaceX");
        dao.createOrUpdate(rocketFamily);
        Collection<RocketFamily> rocketFamilies = dao.loadAll(RocketFamily.class);
        assertFalse(rocketFamilies.isEmpty());
        assertTrue(rocketFamilies.contains(rocketFamily));
    }

    @Test
    public void shouldUpdateRocketFamilyAttributesSuccessfully() {
        RocketFamily rocketFamily = new RocketFamily("Falcon9", "USA", "SpaceX");
        rocketFamily.setWikilink("https://rocketfamilytest.com");

        RocketFamily graphRocketFamily = dao.createOrUpdate(rocketFamily);
        assertNotNull(graphRocketFamily.getId());
        assertEquals(rocketFamily, graphRocketFamily);

        String newLink = "https://anotherrocketfamilytest.com";
        rocketFamily.setWikilink(newLink);
        dao.createOrUpdate(rocketFamily);
        graphRocketFamily = dao.load(RocketFamily.class, rocketFamily.getId());
        assertEquals(newLink, graphRocketFamily.getWikilink());
    }

    @Test
    public void shouldLoadAllRocketFamilies() {
        Set<RocketFamily> rocketFamilies = Sets.newHashSet(
                new RocketFamily("Falcon9", "USA", "SpaceX"),
                new RocketFamily("RocketFamily", "Europe", "ESA")
        );

        for (RocketFamily r : rocketFamilies) {
            dao.createOrUpdate(r);
        }

        Collection<RocketFamily> loadedRocketFamilies = dao.loadAll(RocketFamily.class);
        assertEquals(rocketFamilies.size(), loadedRocketFamilies.size());
        for (RocketFamily r : rocketFamilies) {
            assertTrue(rocketFamilies.contains(r));
        }
    }

    @Test
    public void shouldDeleteRocketFamilySuccessfully(){
        RocketFamily rocketFamily = new RocketFamily("Falcon9", "USA", "SpaceX");
        dao.createOrUpdate(rocketFamily);
        assertFalse(dao.loadAll(RocketFamily.class).isEmpty());
        dao.delete(rocketFamily);
        assertTrue(dao.loadAll(RocketFamily.class).isEmpty());
    }

    @Test
    public void shouldCreateAPayloadSuccessfully() {
        Payload payload = new Payload("Payload", "v1.1");
        dao.createOrUpdate(payload);
        Collection<Payload> payloads = dao.loadAll(Payload.class);
        assertFalse(payloads.isEmpty());
        assertTrue(payloads.contains(payload));
    }

    @Test
    public void shouldUpdatePayloadAttributesSuccessfully() {
        Payload payload = new Payload("Payload", "v1.1");
        dao.createOrUpdate(payload);

        Collection<Payload> payloads = dao.loadAll(Payload.class);

        Payload loadedPayload = payloads.iterator().next();
        assertNull(loadedPayload.getMission());
        assertNull(loadedPayload.getMass());

        payload.setMission("to LEO");
        payload.setMass("13150");
        dao.createOrUpdate(payload);
        payloads = dao.loadAll(Payload.class);
        assertEquals(1, payloads.size());
        loadedPayload = payloads.iterator().next();
        assertEquals("to LEO", loadedPayload.getMission());
        assertEquals("13150", loadedPayload.getMass());
    }

    @Test
    public void shouldLoadAllPayloads() {
        Set<Payload> payloads = Sets.newHashSet(
                new Payload("Falcon Payload", "v1.0"),
                new Payload("Falcon Payload", "v1.1")
        );

        for (Payload p : payloads) {
            dao.createOrUpdate(p);
        }

        Collection<Payload> loadedPayloads = dao.loadAll(Payload.class);
        assertEquals(payloads.size(), loadedPayloads.size());
        for (Payload p : payloads) {
            assertTrue(payloads.contains(p));
        }
    }

    @Test
    public void shouldDeletePayloadSuccessfully(){
        Payload payload = new Payload("Payload", "v1.1");
        dao.createOrUpdate(payload);
        assertFalse(dao.loadAll(Payload.class).isEmpty());
        dao.delete(payload);
        assertTrue(dao.loadAll(Payload.class).isEmpty());
    }

    @AfterEach
    public void tearDown() {
        session.purgeDatabase();
    }

    @AfterAll
    public void closeNeo4jSession() {
        session.clear();
        sessionFactory.close();
    }
}