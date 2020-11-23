package com.moritz.templogger.resource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moritz.templogger.db.TemperatureEntity;
import com.moritz.templogger.db.TemperatureRepository;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;

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
    
    @Scheduled(initialDelay = 500, fixedRate = 10000) //poll pi every 10sec to display the graph
    private void runPiTempCheck() {
    	W1Master w1Master = new W1Master();
    	for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
            Double d = device.getTemperature(TemperatureScale.FARENHEIT);

            TemperatureEntity temperatureEntity = new TemperatureEntity();
            temperatureEntity.setTemperature(d);
            temperatureEntity.setTime(Timestamp.valueOf(LocalDateTime.now()));

            temperatureRepository.save(temperatureEntity);
            System.out.println("Logged temp: "+temperatureEntity);
        }
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