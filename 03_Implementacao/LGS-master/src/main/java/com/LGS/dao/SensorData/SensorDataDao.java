package com.LGS.dao.SensorData;

import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.View.Sensory.SensorDataBundleView;

import java.util.Optional;
import java.util.UUID;

public interface SensorDataDao {

    int insertDataUnprocessed(UUID id, SensorDataBundle data);

    default int insertDataUnprocessed(SensorDataBundle data) {
        UUID id = UUID.randomUUID();
        return insertDataUnprocessed(id, data);
    }

    /**
     * @param id   - packetId
     * @param data - sensorData packet
     * @return value
     */
    int insertDataProcessed(UUID id, SensorDataBundle data, int riskLevel, String threatLevel);

    /**
     * Insert a SensorData to db with a generated ID
     *
     * @param data - sensorData packet
     * @return value
     */
    default int insertDataProcessed(SensorDataBundle data, int riskLevel, String riskDescription) {
        UUID id = UUID.randomUUID();
        return insertDataProcessed(id, data, riskLevel, riskDescription);
    }


    /**
     * Returns all SensorData for a user with given id
     *
     * @param userId - UserId
     * @return value
     */
    Optional<SensorDataBundleView> selectSensorDataByUserId(UUID userId);

    /**
     * Delete data existing in given bundle from database
     * empty fields are ignored
     *
     * @param sensorDataBundle View object
     * @return 0 - ERROR ; >N - OK(N -> Number of lines updated/deleted)
     */
    int deleteSensorDataBundle(SensorDataBundle sensorDataBundle);

    Optional<SensorDataBundleView> selectUnprocessedSensorDataByUserId(UUID userId, String riskMeasurement);

    int updateSensorDataByUserId(UUID userID, SensorDataBundleView packetsToUpdate, String riskMeasurement);

}
