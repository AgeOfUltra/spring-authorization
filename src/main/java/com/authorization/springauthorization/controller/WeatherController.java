package com.authorization.springauthorization.controller;

import com.authorization.springauthorization.entity.Weather;
import com.authorization.springauthorization.service.CacheInspectionService;
import com.authorization.springauthorization.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService service;

    @Autowired
    private CacheInspectionService inspection;

    @GetMapping("/forCity")
    public ResponseEntity<String> getWeatherByCity(@RequestParam String city){
        return ResponseEntity.ok(service.getWeatherByCity(city));
    }

    @PostMapping("/save")
    public ResponseEntity<Weather> saveWeather(@RequestBody Weather weather){
        return ResponseEntity.ok(service.save(weather));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Weather>> getAll(){
        return ResponseEntity.ok(service.getWeather());
    }

    @GetMapping("/cacheData")
    public void getCacheData(){
        inspection.printCacheContents("weather");
    }

    @PutMapping("/{city}")
    public ResponseEntity<String> updateWeather(@PathVariable String city, @RequestParam String foreCast){
        return ResponseEntity.ok(service.updateWeather(city,foreCast));
    }

    @DeleteMapping("/deleteCity")
    public ResponseEntity<String> deleteCity(@RequestParam String city){
        return ResponseEntity.ok(service.deleteCity(city));
    }

    @GetMapping("/health")
    public ResponseEntity<String> getHealth(){
        return ResponseEntity.ok("Healthy");
    }
}

