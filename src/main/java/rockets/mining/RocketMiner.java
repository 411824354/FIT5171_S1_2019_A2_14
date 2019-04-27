package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;

    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k most active rockets, as measured by number of completed launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {
        logger.info("find most launched" + k + "rockets");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        ArrayList<Rocket> rockets = new ArrayList<>();
        for (Launch launch : launches) {
            rockets.add(launch.getLaunchVehicle());
        }
        Map<Rocket,Integer> rocketMap = new HashMap<>();
        for (Rocket rocket: rockets) {
            if (rocketMap.containsKey(rocket)) {
                int count = rocketMap.get(rocket) + 1;
                rocketMap.replace(rocket,count);
            }
            else
                rocketMap.put(rocket,1);
        }
        ArrayList<Rocket> rockets1 = new ArrayList<>();
        Set<Map.Entry<Rocket,Integer>> rocketMap2 =
                rocketMap.entrySet().stream().sorted(Map.Entry.<Rocket,Integer>comparingByValue().reversed()).limit(k).collect(Collectors.toSet());
        for (Map.Entry<Rocket,Integer> map : rocketMap2) {
            rockets1.add(map.getKey());
        }
        return rockets1;
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {
        logger.info("find most reliable " + k + " launch services providers");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        List<Launch> launches1 = launches.stream().collect(Collectors.toList());
        ArrayList<LaunchServiceProvider> launchServiceProviders = new ArrayList<>();
        Map<LaunchServiceProvider,Double> launchServiceProviderDoubleMap = new HashMap<>();
        for (int i = 0; i < launches1.size(); i++)
        {
            double success = 0;
            double failed = 0;
            double reliable = 0;
            if (launches1.get(i).getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL))
                success = 1;
            else if (launches1.get(i).getLaunchOutcome().equals(Launch.LaunchOutcome.FAILED))
                failed = 0;
            LaunchServiceProvider launchServiceProvider = launches1.get(i).getLaunchServiceProvider();
            int j = i + 1;
            while (j < launches1.size())
            {

                LaunchServiceProvider launchServiceProvider1 = launches1.get(i).getLaunchServiceProvider();
                LaunchServiceProvider launchServiceProvider2 = launches1.get(j).getLaunchServiceProvider();

                if (launchServiceProvider1.equals(launchServiceProvider2))
                {
                    if (launches1.get(j).getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL))
                    {
                        success = success + 1;
                    }
                    else if (launches1.get(j).getLaunchOutcome().equals(Launch.LaunchOutcome.FAILED))
                    {
                        failed = failed + 1;
                    }
                    launches1.remove(j);
                }
                else
                    j ++;
            }
            DecimalFormat df = new DecimalFormat("#.##");
            Double reliable1 = success / (success + failed);
            reliable = Double.parseDouble(df.format(reliable1));
            launchServiceProviderDoubleMap.put(launchServiceProvider,reliable);
        }
        Comparator<Map.Entry<LaunchServiceProvider,Double>> entryComparator = new Comparator<Map.Entry<LaunchServiceProvider, Double>>() {
            @Override
            public int compare(Map.Entry<LaunchServiceProvider, Double> o1, Map.Entry<LaunchServiceProvider, Double> o2) {
                if ((o2.getValue() - o1.getValue())>0)
                    return 1;
                else if((o2.getValue() - o1.getValue())==0)
                    return 0;
                else
                    return -1;
            }
        };
        List<Map.Entry<LaunchServiceProvider, Double>> entryList = new ArrayList<Map.Entry<LaunchServiceProvider, Double>>(launchServiceProviderDoubleMap.entrySet());
        List<Map.Entry<LaunchServiceProvider,Double>> entryList1 = entryList.stream().sorted(entryComparator).limit(k).collect(Collectors.toList());
        for (Map.Entry<LaunchServiceProvider,Double> lsp : entryList1)
        {
            launchServiceProviders.add(lsp.getKey());
        }
        return launchServiceProviders;
    }

    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
        logger.info("find most recent " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the dominant country who has the most launched rockets in an orbit.
     *
     * @param orbit the orbit
     * @return the country who sends the most payload to the orbit
     */
    public String dominantCountry(String orbit) { return null;}

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {
        return null;
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k the number of launch service provider.
     * @param year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */
    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year) {
        return null;
    }
}
