package com.LGS.dao.SensorData;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.Sensory.SensorPacket;
import com.LGS.model.View.Sensory.SensorDataBundleView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository("postgres_SensorData")
public class SensorDataAccessService implements SensorDataDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SensorDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertDataUnprocessed(UUID id, SensorDataBundle data) {
        try {
            StringBuilder query = new StringBuilder("INSERT INTO SENSORDATAUNPROCESSED (pId,uId,sId,sValue) VALUES ");
            for (SensorPacket packet : data.getSensorData()) {
                query.append(String.format("('%s','%s',(SELECT sId FROM SENSORS WHERE sName= '%s'), %f),",
                        id, data.getUserId(), packet.getSensorType(), packet.getSensorReading()));
            }
            query = new StringBuilder(query.substring(0, query.length() - 1));
            jdbcTemplate.update(query.toString());
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ApiRequestException("Couldn't add values to Database");
        }
    }

    @Override
    public int insertDataProcessed(UUID id, SensorDataBundle data, int riskLevel, String riskDescription) {
        try {
            StringBuilder query = new StringBuilder("INSERT INTO SENSORDATA (pId,uId,sId,sValue,sTimestamp," +
                    "sRisk,sRiskDescription,sProcessedDelayedHigh,sProcessedDelayedMedium,sProcessedDelayedLow) VALUES ");
            for (SensorPacket packet : data.getSensorData()) {
                query.append(String.format("('%s','%s',(SELECT sId FROM SENSORS WHERE sName= '%s'), %f, " +
                                "'%s', '%d','%s', '%s', '%s', '%s'),",
                        id, data.getUserId(), packet.getSensorType(), packet.getSensorReading(),
                        new Timestamp(System.currentTimeMillis()), riskLevel, riskDescription, '0', '0', '0'));
            }
            query = new StringBuilder(query.substring(0, query.length() - 1));
            jdbcTemplate.update(query.toString());
            return 1;
        } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new ApiRequestException("Couldn't add values to Database");
            }
    }

    @Override
    public Optional<SensorDataBundleView> selectSensorDataByUserId(UUID userId) {
        try {
            List<SensorPacket> sensorPackets = getUserSensorPackets(userId);
            SensorDataBundleView sensorData = new SensorDataBundleView(sensorPackets);
            return Optional.of(sensorData);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public int deleteSensorDataBundle(SensorDataBundle sensorDataBundle) {
        if (sensorDataBundle.getSensorData().size() == 0) throw new ApiRequestException("Sensordata cannot be empty");
        try {
            for (SensorPacket packet : sensorDataBundle.getSensorData()) {
                if (!packet.getSensorType().equals("")) {
                    int value = deleteSensorDataPacket(sensorDataBundle.getUserId(), packet);
                    if (value == 0) {
                        throw new ApiRequestException("Couldn't delete packet");
                    }
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiRequestException("Couldn't delete");
        }
    }

    @Override
    public Optional<SensorDataBundleView> selectUnprocessedSensorDataByUserId(UUID userId, String riskMeasurement) {
        try {
            List<SensorPacket> SensorPackets = getUserUnprocessedPackets(userId, riskMeasurement);
            SensorDataBundleView sensorData = new SensorDataBundleView(SensorPackets);
            return Optional.of(sensorData);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int updateSensorDataByUserId(UUID userID, SensorDataBundleView packetsToUpdate, String riskMeasurement) {
        try {
            for (SensorPacket packet : packetsToUpdate.getSensorData()) {
                int value = updateSensorDataByUserId(userID, packet, riskMeasurement);
                if (value == 0) {
                    throw new ApiRequestException("Couldn't update packet");
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiRequestException("Couldn't update");
        }
    }


    //-- Auxiliary METHODS --//

    /**
     * Auxiliary method to reduce redundancy
     *
     * @param sensorType - "TEMPERATURE,PULSE,MOVEMENT"
     * @param userId     - userID
     * @return - List of packets with user info, returns all sensor data for a given user
     */
    private List<SensorPacket> getUserSensorPackets(UUID userId, String sensorType) {
        String query = "SELECT * FROM SENSORDATA AS SD JOIN SENSORS AS S ON SD.sId = S.sId WHERE uId= ? AND sType = ?";
        return jdbcTemplate.query(query, new Object[]{userId, sensorType}, (resultSet, i) -> {
            float value = resultSet.getFloat("sValue");
            Timestamp timestamp = resultSet.getTimestamp("sTimestamp");
            return new SensorPacket(sensorType, String.valueOf(value), timestamp);
        });
    }

    /**
     * Auxiliary method to reduce redundancy
     *
     * @param userId - userId
     * @return - List of packets with user info, returns all sensor data for a given user
     */
    private List<SensorPacket> getUserSensorPackets(UUID userId) {
        String query = "SELECT * FROM SENSORDATA AS SD JOIN SENSORS AS S ON SD.sId = S.sId WHERE uId= ?";
        return jdbcTemplate.query(query, new Object[]{userId}, (resultSet, i) -> {
            String sType = resultSet.getString("sName");
            float value = resultSet.getFloat("sValue");
            Timestamp timestamp = resultSet.getTimestamp("sTimestamp");
            int risk = resultSet.getInt("srisk");
            String description = resultSet.getString("sriskdescription");
            return new SensorPacket(sType, String.valueOf(value), timestamp, risk, description);
        });
    }


    private List<SensorPacket> getUserUnprocessedPackets(UUID userId, String riskMeasurement) {
        String query = "SELECT * FROM SENSORDATA AS SD JOIN SENSORS AS S ON SD.sId = S.sId WHERE uId= '" + userId + "' AND " + riskMeasurement + " = 'f' ORDER BY stimestamp";
        return jdbcTemplate.query(query, (resultSet, i) -> {
            String sType = resultSet.getString("sName");
            float value = resultSet.getFloat("sValue");
            Timestamp timestamp = resultSet.getTimestamp("sTimestamp");
            int riskLevel = resultSet.getInt("sRisk");
            String riskDescription = resultSet.getString("sriskdescription");
            return new SensorPacket(sType, String.valueOf(value), timestamp, riskLevel, riskDescription);
        });
    }


    /**
     * Auxiliary method used in deleteSensorDataBundle
     *
     * @param uId    user id
     * @param packet sensorPacket
     * @return number of affected rows
     */
    private int deleteSensorDataPacket(UUID uId, SensorPacket packet) {
        String sql = "DELETE FROM SENSORDATA " +
                "WHERE uId= '" + uId + "' AND sId =(SELECT sId FROM SENSORS WHERE sName='" + packet.getSensorType() + "') AND svalue = '" + packet.getSensorReading()
                + "' AND stimestamp = '" + packet.getTimestamp() + "'";
        return jdbcTemplate.update(sql);
    }

    /**
     * Update flag for packet processed for high risk measurement
     *
     * @param userId user id
     * @param packet packet
     * @return 1 if updated 0 if not
     */
    private int updateSensorDataByUserId(UUID userId, SensorPacket packet, String riskMeasurement) {
        String sql = "" +
                "UPDATE SENSORDATA " +
                "SET " + riskMeasurement + " = '1'" +
                "WHERE uId= '" + userId + "' AND sId =(SELECT sId FROM SENSORS WHERE sName='" + packet.getSensorType() + "') AND sValue = '" + packet.getSensorReading() +
                "' AND sTimestamp = '" + packet.getTimestamp() + "' AND sRisk = " + packet.getRiskLevel() + " AND sRiskDescription = '" + packet.getRiskDescription() + "'";
        return jdbcTemplate.update(sql);
    }

}
