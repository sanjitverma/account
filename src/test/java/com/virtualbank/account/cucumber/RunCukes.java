package com.virtualbank.account.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

/**
 * Created by SANJIT on 31/01/18.
 */

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:feature/",
        format = "pretty",
        glue = {"com.virtualbank.account.stepdef"})
public class RunCukes {

}
