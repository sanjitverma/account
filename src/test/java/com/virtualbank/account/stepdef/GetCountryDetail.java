package com.virtualbank.account.stepdef;

import com.virtualbank.account.client.CountriesClient;
import com.virtualbank.countries.ws.access.Country;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

/**
 * Created by SANJIT on 08/02/18.
 */
public class GetCountryDetail {



    CountriesClient client = null;
    Country country = null;

    @Given("^the country detail is available$")
    public void the_country_detail_is_available() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
       client = new CountriesClient();
    }

    @When("^the client calls getCountry api with \"([^\"]*)\"$")
    public void the_client_calls_getCountry_api_with(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        country = client.getCountries(arg1);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertEquals("Madrid", country.getCapital());

    }

}
