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

        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 0, 0, 1, 1};
        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, "USA", lsps.get(lspIndex[i])));
        }
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};

        // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            l.setLaunchDate(LocalDate.of(2017, months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchSite("VAFB");
            l.setOrbit("LEO");
            spy(l);
            return l;
        }).collect(Collectors.toList());
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate()));
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
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
        List<Launch> launches1 = Lists.newArrayList();

        int[] outComes = new int[]{1,1,1,0,1,1,0,1,0,0};
        int[] lspIndex = new int[]{0,0,0,0,1,1,1,2,2,2};
        for (int i = 0;i < launches.size();i ++)
        {
            Launch.LaunchOutcome outCome = null;
            if (outComes[i] == 1)
                outCome = Launch.LaunchOutcome.SUCCESSFUL;
            else
                outCome = Launch.LaunchOutcome.FAILED;
            Launch launch = launches.get(i);
            launch.setLaunchOutcome(outCome);
            launch.setLaunchServiceProvider(lsps.get(lspIndex[i]));
            launches1.add(launch);
        }
        when(dao.loadAll(Launch.class)).thenReturn(launches1);
        List<LaunchServiceProvider> launchServiceProviders1 = new ArrayList<>(lsps);
        List<LaunchServiceProvider> launchServiceProviders = miner.mostReliableLaunchServiceProviders(k);
        assertEquals(k,launchServiceProviders.size());
        assertEquals(launchServiceProviders1.subList(0,k),launchServiceProviders);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void shouldReturnMostExpensiveLaunches(int k)
    {
        int[] price = new int[]{4000,5000,6000,1000,1500,8000,3000,9000,10000,2000};
        List<Launch> launches1 = new ArrayList<>();
        for (int i = 0; i < launches.size();i++)
        {

            BigDecimal value = new BigDecimal(price[i]);
            Launch launch = launches.get(i);
            launch.setPrice(value);
            launches1.add(launch);
        }
        when(dao.loadAll(Launch.class)).thenReturn(launches1);
        List<Launch> sortedLaunches = new ArrayList<>(launches1);
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
        int[] setSize = new int[]{3,3,3,3,2,2,2,1,1,1};
        String[] type = new String[]{"satellite","spaceStation","spaceProbe"};
        List<Launch> launchList = new ArrayList<>();
        List<Rocket> rocketList = Lists.newArrayList();
        int[] lspIndex = new int[]{0, 0, 0, 1, 1};
        int[] countryIndex = new int[]{0,0,0,1,1};
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};
        String[] countries = new String[]{"CHINA","USA"};
        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rocketList.add(new Rocket("rocket_" + i, countries[countryIndex[i]], lsps.get(lspIndex[i])));
        }
        for (int k = 0; k < launches.size(); k++) {
            List<Set<Payload>> setList = new ArrayList<>();
            Set<Payload> payloads = new HashSet<>();
            int j = setSize[k];
            for (int i = 0; i < j; i++) {
                Payload payload = new Payload("Payload" + j + "" + i, type[j-1]);
                payloads.add(payload);

            }
            setList.add(payloads);

            Launch launch = launches.get(k);
            launch.setPayload(payloads);
            launch.setLaunchVehicle(rocketList.get(rocketIndex[k]));
            launchList.add(launch);
        }
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        String realCountry = "CHINA";
        String testCountry = miner.dominantCountry("LEO");
        assertEquals(realCountry,testCountry);
    }
}