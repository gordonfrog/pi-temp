package com.moritz.templogger.resource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moritz.templogger.db.TemperatureEntity;
import com.moritz.templogger.db.TemperatureRepository;

@RestController
public class TemperatureResource {

	@Autowired
    private TemperatureRepository temperatureRepository;

    @GetMapping("/temp")
    public List<TemperatureEntity> retrieveAllTempearatures() {
    	List<TemperatureEntity> result = StreamSupport.stream(temperatureRepository.findAll().spliterator(), false)
    			    .collect(Collectors.toList());
        return result;
    }

//    @PostMapping("/measurement")
//    public ResponseEntity createMeasurement(
//            @RequestParam long sensorId,
//            @RequestParam String key,
//            @RequestParam double value) {
//
//        SensorEntity sensorEntity = sensorRepository.findById(sensorId);
//
//        if (sensorEntity == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("No sensor defined with the ID: " + sensorId);
//        }
//
//        MeasurementEntity measurementEntity = new MeasurementEntity(
//                sensorEntity, System.currentTimeMillis(), key, value);
//        measurementRepository.save(measurementEntity);
//
//        return ResponseEntity.ok().build();
//    }
}