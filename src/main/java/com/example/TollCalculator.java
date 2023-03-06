package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.json.JSONTokener;

public class TollCalculator {

    public final BigDecimal TOLL_RATE = new BigDecimal("0.25");
    private Map<String, Object> locationMap;
    private Map<String, String> nameMap;

    public TollCalculator() throws IOException {
        locationMap = new HashMap<>();
        nameMap = new HashMap<>();

        // init the calculator with the json file
        File file = new File("input/interchanges.json");
        InputStream is = new FileInputStream(file);
        JSONTokener resource = new JSONTokener(is);

        JSONObject jsonObject = new JSONObject(resource);
        JSONObject locations = (JSONObject) jsonObject.get("locations");
        locationMap = locations.toMap();

        //create the nameMap for Id searching
        for (Entry<String, Object> location : locationMap.entrySet()) {
            Map<String, Object> detail = (Map<String, Object>) location.getValue();
            String name = (String) detail.get("name");
            nameMap.put(name, location.getKey());
        }
    }

    public void costOfTrip(String origin, String destination) {
        String originId = nameMap.get(origin);
        String destinationId = nameMap.get(destination);

        BigDecimal distance = BigDecimal.ZERO;
        BigDecimal cost = BigDecimal.ZERO;

        if (originId == null || destinationId == null) {
            System.out.println("Undefined Interchange(s)");
            return;
        }

        if (!originId.equals(destinationId)) {
            
            Map<String, Object> current = (Map<String, Object>) locationMap.get(originId);
            BigDecimal totalDistance = calculateDistance(current, originId, null, destinationId);

            if (totalDistance == null) {
                System.out.println("Undefined Route");
                return;
            } else {
                distance = totalDistance.setScale(3, RoundingMode.UP);
                cost = calculateCost(totalDistance);
                cost = cost.setScale(3, RoundingMode.UP);
            }
            
        }

        System.out.println("Distance(km): " + distance);
        System.out.println("Cost: $" + cost);
    }

    private BigDecimal calculateDistance(Map<String, Object> currentInterchange, String currentId, String previousId,
            String destinationId) {

        List<Map<String, Object>> routes = (List<Map<String, Object>>) currentInterchange.get("routes");

        for (Map<String, Object> route : routes) {
            String toId = String.valueOf(route.get("toId"));
            String distStr = String.valueOf(route.get("distance"));
            BigDecimal dist = new BigDecimal(distStr);
            if (toId.equals(destinationId)) {
                return dist;
            } else if (!toId.equals(previousId)) {
                Map<String, Object> next = (Map<String, Object>) locationMap.get(toId);
                if (next != null) {
                    BigDecimal result = calculateDistance(next, toId, currentId, destinationId);
                    if (result != null) {
                        return dist.add(result);
                    }
                }
            }
        }

        return null;
    }

    private BigDecimal calculateCost(BigDecimal distance) {
        return distance.multiply(TOLL_RATE);
    }

    public static void main(String[] args) {
        try {
            TollCalculator cal = new TollCalculator();
            String origin, destination;

            try (Scanner reader = new Scanner(System.in)) {
                while (true) {
                    System.out.println("Enter the origin: ");
                    origin = reader.nextLine();

                    System.out.println("Enter the destination: ");
                    destination = reader.nextLine();
                    cal.costOfTrip(origin, destination);

                }
            }

        } catch (IOException e) {
            System.err.println("Failed to get the calculator, reason(s):" + e.getLocalizedMessage());
        }
    }
}
