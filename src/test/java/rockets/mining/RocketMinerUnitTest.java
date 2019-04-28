package rockets.mining;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Payload;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();
        lsps = Arrays.asList(
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe ")
        );
        List<Launch> launchList1 = Lists.newArrayList();
        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 0, 0, 1, 1};
        int[] countryIndex = new int[]{0,0,0,1,1};
        String[] countries = new String[]{"CHINA","USA"};
        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, countries[countryIndex[i]], lsps.get(lspIndex[i])));
        }
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};

        // 10 launches
        launchList1 = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            l.setLaunchDate(LocalDate.of(2017, months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchSite("VAFB");
            l.setOrbit("LEO");
            spy(l);
            return l;
        }).collect(Collectors.toList());

        int[] setSize = new int[]{3,3,3,3,2,2,2,1,1,1};
        String[] type = new String[]{"satellite","spaceStation","spaceProbe"};
        List<Launch> launchList = new ArrayList<>();
        int[] outComes = new int[]{1,1,1,0,1,1,0,1,0,0};
        int[] lspIndex1 = new int[]{0,0,0,0,1,1,1,2,2,2};
        int[] price = new int[]{4000,5000,6000,1000,1500,8000,3000,9000,10000,2000};
        // 5 rockets
        for (int k = 0; k < launchList1.size(); k++) {
            List<Set<Payload>> setList = new ArrayList<>();
            Set<Payload> payloads = new HashSet<>();
            Launch.LaunchOutcome outCome = null;
            int j = setSize[k];
            for (int i = 0; i < j; i++) {
                Payload payload = new Payload("Payload" + j + "" + i, type[j-1]);
                payloads.add(payload);

            }
            setList.add(payloads);

            Launch launch = launchList1.get(k);
            launch.setPayload(payloads);
            launch.setLaunchVehicle(rockets.get(rocketIndex[k]));
            if (outComes[k] == 1)
                outCome = Launch.LaunchOutcome.SUCCESSFUL;
            else
                outCome = Launch.LaunchOutcome.FAILED;
            launch.setLaunchOutcome(outCome);
            launch.setLaunchServiceProvider(lsps.get(lspIndex1[k]));
            BigDecimal value = new BigDecimal(price[k]);
            launch.setPrice(value);
            launchList.add(launch);
        }
        int i = 0;
        launches = launchList;
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 9})
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate()));
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3})
    public void shouldReturnTopMostLaunchedRocket(int k){
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Rocket> rocketList = new ArrayList<>(rockets);
        List<Rocket> launchedRockets = miner.mostLaunchedRockets(k);
        assertEquals(k,launchedRockets.size());
        assertEquals(rocketList.subList(0,k),launchedRockets);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnMostReliableLaunchServiceProviders(int k){
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<LaunchServiceProvider> launchServiceProviders1 = new ArrayList<>(lsps);
        List<LaunchServiceProvider> launchServiceProviders = miner.mostReliableLaunchServiceProviders(k);
        assertEquals(k,launchServiceProviders.size());
        assertEquals(launchServiceProviders1.subList(0,k),launchServiceProviders);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void shouldReturnMostExpensiveLaunches(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getPrice().compareTo(b.getPrice()));
        List<Launch> launchList = miner.mostExpensiveLaunches(k);
        assertEquals(k,launchList.size());
        assertEquals(sortedLaunches.subList(0,k),launchList);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2})
    public void ShouldReturnHighestRevenueLaunchServiceProviders(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<LaunchServiceProvider> launchServiceProviderList = miner.highestRevenueLaunchServiceProviders(k,2017);
        List<LaunchServiceProvider> realList = new ArrayList<>();
        if (k == 1)
            realList.add(lsps.get(0));
        if (k == 2)
        {
            realList.add(lsps.get(0));
            realList.add(lsps.get(1));
        }
        assertEquals(k,launchServiceProviderList.size());
        assertEquals(realList.subList(0,k),launchServiceProviderList);
    }

    @Test
    public void ShouldReturnDominantCountry()
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        String realCountry = "CHINA";
        String testCountry = miner.dominantCountry("LEO");
        assertEquals(realCountry,testCountry);
    }

    @ParameterizedTest
    @ValueSource(ints = {5,10,-1,-5})
    public void boundaryTestKInMostLaunchedRockets(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.mostLaunchedRockets(k));
        assertEquals("k beyond the data boundary",exception.getMessage());

    }

    @ParameterizedTest
    @ValueSource(ints = {5,10,2,3,4,-1,-5})
    public void shouldThrowExceptionWHenNothingLoadInMostLaunchedRockets(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> miner.mostLaunchedRockets(k));
        assertEquals("no launches in database",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {4,10,-1,-5})
    public void boundaryTestKInMostReliableLaunchServiceProvider(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.mostReliableLaunchServiceProviders(k));
        assertEquals("k beyond the data boundary",exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(ints = {4,10,1,2,3,-1,-5})
    public void shouldThrowExceptionWHenNothingLoadInMostReliableLaunchServiceProvider(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> miner.mostReliableLaunchServiceProviders(k));
        assertEquals("no launches in database",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {11,15,-1,-5})
    public void boundaryTestKInMostRecentLaunches(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.mostRecentLaunches(k));
        assertEquals("k beyond the data boundary",exception.getMessage());

    }

    @ParameterizedTest
    @ValueSource(ints = {11,15,5,9,10,-1,-5})
    public void shouldThrowExceptionWhenNothingLoadInMostRecentLaunches(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> miner.mostRecentLaunches(k));
        assertEquals("no launches in database",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {""," ","  "})
    public void ShouldThrowExceptionWhenPassEmptyOrbit(String orbit)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.dominantCountry(orbit));
        assertEquals("orbit cannot be null or empty",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"LEO","aaaaa"})
    public void shouldThrowExceptionWhenNothingLoadInDominantCountry(String orbit)
    {
        when(dao.loadAll(Launch.class)).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> miner.dominantCountry(orbit));
        assertEquals("no launches in database",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa","bbb"})
    public void shouldThrowExceptionWhenPassedOrbitNotExit(String orbit)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.dominantCountry(orbit));
        assertEquals("passed orbit not found",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {11,15,-1,-5})
    public void boundaryTestKInMostExpensiveLaunches(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.mostExpensiveLaunches(k));
        assertEquals("k beyond the data boundary",exception.getMessage());

    }

    @ParameterizedTest
    @ValueSource(ints = {11,15,5,9,10,-1,-5})
    public void shouldThrowExceptionWhenNothingLoadInMostExpensiveLaunches(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> miner.mostExpensiveLaunches(k));
        assertEquals("no launches in database",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 10,-1,-5})
    public void boundaryTestKInHighestRevenueLaunchServiceProviders(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.highestRevenueLaunchServiceProviders(k,2017));
        assertEquals("k beyond the data boundary",exception.getMessage());

    }

    @ParameterizedTest
    @ValueSource(ints = {4,10,1,2,3,-1,-5})
    public void shouldThrowExceptionWhenNothingLoadInHighestRevenueLaunchServiceProviders(int k)
    {
        when(dao.loadAll(Launch.class)).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> miner.highestRevenueLaunchServiceProviders(k,2017));
        assertEquals("no launches in database",exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {2016,2018,1999})
    public void shouldThrowExceptionWhenNoProviderInThisYearInHighestRevenueLaunchServiceProviders(int year)
    {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.highestRevenueLaunchServiceProviders(3,year));
        assertEquals("Launch service provide not found in this year",exception.getMessage());
    }
}