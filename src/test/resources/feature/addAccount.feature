Feature: add account feature test

  Scenario Outline: client makes POST call to add account
    Given the request <REQUEST_FILE> is available
    When the client calls <RESOURCE_URI>
    Then the client receives status code of <RESPONSE_CODE> and respone as <RESPONSE_FILE>


  Examples:
  |REQUEST_FILE|RESPONSE_FILE|RESOURCE_URI   |RESPONSE_CODE|
  |"create_account_request"      |"create_account_response"       |"/api/accounts"|    201      |

