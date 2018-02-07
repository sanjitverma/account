package com.virtualbank.account.stepdef;

import com.virtualbank.account.util.SpringIntegrationTest;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import sun.security.util.PendingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by SANJIT on 05/02/18.
 */
public class GetTransactionsStepDef extends SpringIntegrationTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(GetTransactionsStepDef.class);

    @Given("^the customer account is available in system$")
    public void the_customer_account_is_available_in_system() throws Throwable {
        // Write code here that turns the phrase above into concrete actions

    }

    @When("^the client calls getTransactions \"([^\"]*)\"$")
    public void the_client_calls_getTransactions(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        executeGet(arg1);
    }

    @Then("^the client receives status code of (\\d+) and transactions respone as \"([^\"]*)\"$")
    public void the_client_receives_status_code_of_and_transactions_respone_as(int arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        LOGGER.debug(latestResponse.getTheResponse().toString());
        assertEquals(HttpStatus.OK, latestResponse.getTheResponse().getStatusCode());
    }
}
