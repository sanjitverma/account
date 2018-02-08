Feature: add account feature test

  Scenario Outline: client makes GET call to get country details
    Given the country detail is available
    When the client calls getCountry api with <COUNTRY_NAME>
    Then the client receives status code of <RESPONSE_CODE>


    Examples:
      |COUNTRY_NAME |RESPONSE_CODE|
      |"Spain"      |    200      |

