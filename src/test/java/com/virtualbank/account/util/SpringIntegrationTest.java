package com.virtualbank.account.util;

import com.github.tomakehurst.wiremock.junit.Stubbing;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.virtualbank.account.AccountApplication;
import com.virtualbank.account.repository.AccountRepository;
import com.virtualbank.account.repository.TransactionRepository;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by SANJIT on 31/01/18.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@ConfigurationProperties("json.config")
public abstract class SpringIntegrationTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ResourceLoader resourceLoader;

    @ClassRule
    public static WireMockClassRule weatherApiService = new WireMockClassRule(
            WireMockSpring.options().dynamicPort()
    );

    TestRestTemplate template = new TestRestTemplate();


    @LocalServerPort
    private int port;

    private String req;
    private String res;

    static protected ResponseResults latestResponse = null;
    final Map<String, String> headers = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringIntegrationTest.class);


    @BeforeClass
    public void setUP() {


    }

    private void setupStubbing(Stubbing stubbing, String url, String response) {
        stubbing.stubFor(get(urlEqualTo("/test")).willReturn(aResponse().withBody(response)));
    }


    protected void executeGet(String url) throws IOException {
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
        latestResponse = template.execute(createURL(url), HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    public void setRequestPath(String requestPath) {
        this.req = requestPath;
    }

    public void setResponsePath(String responsePath) {
        this.res = responsePath;
    }

    protected void executePost(String url) throws IOException {
        setHeader();
        Function<String, String> requestPath = req -> "classpath:" + "/req/" + req + ".json";
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        requestCallback.setBody(readFile(req, requestPath));
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

//        template.setErrorHandler(errorHandler);
        latestResponse = template
                .execute(createURL(url), HttpMethod.POST, requestCallback, response -> {
                    if (errorHandler.hadError) {
                        return (errorHandler.getResults());
                    } else {
                        return (new ResponseResults(response));
                    }
                });
    }


    private class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getRawStatusCode() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            results = new ResponseResults(response);
        }
    }

    private String createURL(String contextPath) {
        return "http://localhost:" + port + contextPath;
    }

    private String readFile(String fileName, Function<String, String> filePath) {
        String fullPath = filePath.apply(fileName);
        Resource resource = resourceLoader.getResource(fullPath);
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        String content = null;
        try {
            inputStream = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            content = IOUtils.toString(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String readResponseFile() {
        Function<String, String> responsePath = (String path) -> "classpath:" + "/res/" + path + ".json";
        return this.readFile(this.res, responsePath);
    }

    private void setHeader() {
        headers.put("Content-Type", "application/json");
    }
}
