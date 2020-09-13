package com.LGS.service;

import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.View.Sensory.SensorDataBundleView;

import java.util.Optional;
import java.util.UUID;


public interface SensorDataService {

    int insertDataUnprocessed(SensorDataBundle sensorData);

    int insertDataProcessed(SensorDataBundle sensorData, int riskLevel, String threatLevel);

    Optional<SensorDataBundleView> selectSensorDataByUserId(UUID userId);

    int deleteSensorDataBundle(SensorDataBundle sensorDataBundle);


}
