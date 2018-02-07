package com.virtualbank.account.stepdef;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualbank.account.CreateAccountResponse;
import com.virtualbank.account.util.SpringIntegrationTest;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

/**
 * Created by SANJIT on 31/01/18.
 */
public class AddAccountStepDef extends SpringIntegrationTest {

    @Given("^the request \"([^\"]*)\" is available$")
    public void the_request_is_available(String arg1) throws Throwable {
        setRequestPath(arg1);
    }

    @When("^the client calls \"([^\"]*)\"$")
    public void the_client_calls(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        executePost(arg1);
    }

    @Then("^the client receives status code of (\\d+) and respone as \"([^\"]*)\"$")
    public void the_client_receives_status_code_of_and_respone_as(int arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        setResponsePath(arg2);
        ObjectMapper mapper = new ObjectMapper();
        CreateAccountResponse expected = mapper.readValue(readResponseFile(),CreateAccountResponse.class);
        CreateAccountResponse actual = mapper.readValue(latestResponse.getBody(),CreateAccountResponse.class);
        assertEquals(201, latestResponse.getTheResponse().getStatusCode().value());
       // assertEquals(expected,actual);
    }
}
