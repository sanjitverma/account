package com.virtualbank.account.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by SANJIT on 06/02/18.
 */

@Service
public class WeatherApiService {


    RestTemplate template = new RestTemplate();
    private String uri = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";

    public String getWeatherByCity(){
        ResponseEntity<String> weatherData = template.getForEntity(uri, String.class);
        return weatherData.getBody();
    }

}
