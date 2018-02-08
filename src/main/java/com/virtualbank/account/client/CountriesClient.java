package com.virtualbank.account.client;

import com.virtualbank.countries.ws.access.*;

/**
 * Created by SANJIT on 07/02/18.
 */
public class CountriesClient {
    public static Country getCountries(String countryName){
        CountriesPortService service = new CountriesPortService();
        CountriesPort countriesPort = service.getCountriesPortSoap11();
        GetCountryResponse countryResponse = countriesPort.getCountry(getCountriesRequest(countryName));
        return countryResponse.getCountry();
    }


     static GetCountryRequest getCountriesRequest(String countryName){
        GetCountryRequest request = new GetCountryRequest();
        request.setName(countryName);
        return request;
    }
}
