package com.LGS.service;

import com.LGS.model.Sensory.SensorDataBundle;
import org.json.JSONObject;


public interface MonitorService {
    JSONObject instantMonitor(SensorDataBundle sensorData);
}
