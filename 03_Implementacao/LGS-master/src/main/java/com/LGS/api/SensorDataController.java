package com.LGS.api;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.View.Sensory.SensorDataBundleView;
import com.LGS.service.MonitorServiceImpl;
import com.LGS.service.SensorDataServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("lgs/v1/sensorData")
@RestController
public class SensorDataController {

    private final SensorDataServiceImpl sensorDataServiceImpl;
    private final MonitorServiceImpl monitorServiceImpl;

    @Autowired
    public SensorDataController(SensorDataServiceImpl sensorDataServiceImpl, MonitorServiceImpl monitorServiceImpl) {
        this.sensorDataServiceImpl = sensorDataServiceImpl;
        this.monitorServiceImpl = monitorServiceImpl;
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<String> insertDataProcessed(@Valid @NonNull @RequestBody SensorDataBundle sensorData) {
        JSONObject monitorResponse = monitorServiceImpl.instantMonitor(sensorData);
        if (monitorResponse.has("Risk Level") && monitorResponse.has("Risk Description")) {
            sensorDataServiceImpl.insertDataProcessed(sensorData, (int) monitorResponse.get("Risk Level"), (String) monitorResponse.get("Risk Description"));
            return new ResponseEntity<>(monitorResponse.toString(), HttpStatus.OK);
        }
        throw new ApiRequestException("Monitoring error");
    }


    @GetMapping(path = "{userId}")
    public Optional<SensorDataBundleView> selectSensorDataByUserId(@PathVariable("userId") UUID userId){
        Optional<SensorDataBundleView> sensorDataByUserId = sensorDataServiceImpl.selectSensorDataByUserId(userId);
        if(sensorDataByUserId.isEmpty())
            throw new ApiRequestException("SensorData for user not found");
        return sensorDataByUserId;
    }

    /**
     * Administrator functionality, deletes data from database in JSON REQUEST
     *
     * @param sensorDataBundle View
     */
    @DeleteMapping(path = "{userId}")
    public String deleteSensorDataBundle(@Valid @lombok.NonNull @RequestBody SensorDataBundle sensorDataBundle) {
        if (sensorDataServiceImpl.deleteSensorDataBundle(sensorDataBundle) == 1)
            return "Sensor data deleted";
        else
            throw new ApiRequestException("Couldn't delete data for user: " + sensorDataBundle.getUserId());
    }

    /**
     * Saves data that wont be processed
     *
     * @param sensorData sensordata
     * @return string informing
     */
    @PostMapping(path = "unprocessed")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity insertDataUnprocessed(@Valid @NonNull @RequestBody SensorDataBundle sensorData) {
        if (sensorDataServiceImpl.insertDataUnprocessed(sensorData) == 1)
            return new ResponseEntity(HttpStatus.OK);
        throw new ApiRequestException("Something went wrong");
    }
}
