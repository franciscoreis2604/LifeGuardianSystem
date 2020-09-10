package com.lgs.lgsclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

public class SensorReaderGenerator {

    public static JSONObject GenerateRandomSensorReading(Session session) {
        int number = ThreadLocalRandom.current().nextInt(0, 2 + 1);
        switch (number) {
            case 1:
                return GenerateTemperatureOnlyPayload(session);
            case 2:
                return GeneratePulseOnlyPayload(session);
            default:
                return GenerateBothSensorsPayload(session);
        }
    }

    public static JSONObject GenerateRandomSensorReadingWithMoreThan80PercentHigh(Session session) {
        int number = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        if (number >= 20) return GenerateBothSensorPayloadHigh(session);
        else return GenerateBothSensorPayloadSafe(session);
    }

    private static JSONObject GenerateTemperatureOnlyPayload(Session session) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId", session.getUserID());
            JSONArray sensorDataArray = new JSONArray();
            JSONObject[] innerObject = new JSONObject[1];
            for (int i = 0; i < 1; i++) {
                innerObject[i] = new JSONObject();
                innerObject[i].put("name", "Temperature");
                innerObject[i].put("value", ThreadLocalRandom.current().nextInt(30, 45 + 1));
                sensorDataArray.put(innerObject[i]);
            }
            payload.put("sensorData", sensorDataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    private static JSONObject GeneratePulseOnlyPayload(Session session) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId", session.getUserID());
            JSONArray sensorDataArray = new JSONArray();
            JSONObject[] innerObject = new JSONObject[1];
            for (int i = 0; i < 1; i++) {
                innerObject[i] = new JSONObject();
                innerObject[i].put("name", "Pulse");
                innerObject[i].put("value", ThreadLocalRandom.current().nextInt(30, 160 + 1));
                sensorDataArray.put(innerObject[i]);
            }
            payload.put("sensorData", sensorDataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    private static JSONObject GenerateBothSensorsPayload(Session session) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId", session.getUserID());
            JSONArray sensorDataArray = new JSONArray();
            JSONObject[] innerObject = new JSONObject[2];
            for (int i = 0; i < 2; i++) {
                innerObject[i] = new JSONObject();
                if (i == 0) {
                    innerObject[i].put("name", "Temperature");
                    innerObject[i].put("value", ThreadLocalRandom.current().nextInt(30, 45 + 1));
                } else {
                    innerObject[i].put("name", "Pulse");
                    innerObject[i].put("value", ThreadLocalRandom.current().nextInt(30, 160 + 1));
                }
                sensorDataArray.put(innerObject[i]);
            }
            payload.put("sensorData", sensorDataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    private static JSONObject GenerateBothSensorPayloadHigh(Session session) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId", session.getUserID());
            JSONArray sensorDataArray = new JSONArray();
            JSONObject[] innerObject = new JSONObject[2];
            for (int i = 0; i < 2; i++) {
                innerObject[i] = new JSONObject();
                if (i == 0) {
                    innerObject[i].put("name", "Temperature");
                    innerObject[i].put("value", 33.9);
                } else {
                    innerObject[i].put("name", "Pulse");
                    innerObject[i].put("value", 150);
                }
                sensorDataArray.put(innerObject[i]);
            }
            payload.put("sensorData", sensorDataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }


    private static JSONObject GenerateBothSensorPayloadSafe(Session session) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId", session.getUserID());
            JSONArray sensorDataArray = new JSONArray();
            JSONObject[] innerObject = new JSONObject[2];
            for (int i = 0; i < 2; i++) {
                innerObject[i] = new JSONObject();
                if (i == 0) {
                    innerObject[i].put("name", "Temperature");
                    innerObject[i].put("value", 36.5);
                } else {
                    innerObject[i].put("name", "Pulse");
                    innerObject[i].put("value", 80);
                }
                sensorDataArray.put(innerObject[i]);
            }
            payload.put("sensorData", sensorDataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }
}
