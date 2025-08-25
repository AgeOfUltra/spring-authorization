package com.authorization.springauthorization.service;

import com.authorization.springauthorization.entity.Weather;
import com.authorization.springauthorization.repo.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepo repo;
    @Autowired
    WeatherService(WeatherRepo repo) {
        this.repo = repo;
    }

    @Cacheable(value = "weather",key = "#city")
    public String getWeatherByCity(String city){
        System.out.println("fetching data from DB for city : "+city);
        Optional<Weather> weather = repo.getWeatherByCity(city);
        return weather.map(Weather::getForecast).orElse("no force cast available");
    }

    public Weather save(Weather weather) {
       return repo.save(weather);
    }

//    @Cacheable(value = "weather",key = "'allWeather'") // still some issues
    public List<Weather> getWeather() {
        return repo.findAll();
    }

    @CachePut( value = "weather",key = "#city") // it is to update the cache with deleted value.

//    @Caching(
//            put = @CachePut( value = "weather",key = "#city"),
//            evict = @CacheEvict(value = "weather",key = "'allWeather'")
//    ) // it is to update the updated value in for the key and allWeather key as well.
    public String updateWeather(String city, String foreCast) {
//        repo.getWeatherByCity(city).ifPresent(weather -> {
//            weather.setForecast(foreCast);
//            repo.save(weather);
//        });
         Optional<Weather> current = repo.getWeatherByCity(city);
         if(current.isPresent()){
             Weather weather= current.get();
             weather.setForecast(foreCast);
             repo.save(weather);
             return weather.getForecast()+"saved successfully";
         }

        return "no city found";
    }

    @Transactional
    @CacheEvict(value = "weather",key = "#city") // this will only update the cache with city key

//    @Caching(
//            evict = {
//                    @CacheEvict(value = "weather", key = "#city"),
//                    @CacheEvict(value = "weather", key = "'allWeather'")
//            }
//    )// this will update both city and allWeather
    public String deleteCity(String city) {
        Optional<Weather> weather = repo.getWeatherByCity(city);
        if(weather.isPresent()){
            repo.delete(weather.get());
            return "success";
        }else{
            return "fail";
        }
    }
}
