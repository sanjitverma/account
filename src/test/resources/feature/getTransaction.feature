Feature: get transaction feature test

  Scenario Outline: client does get call to get transaction details
    Given the customer account is available in system
    When the client calls getTransactions <RESOURCE_URI>
    Then the client receives status code of <RESPONSE_CODE> and transactions respone as <RESPONSE_FILE>


    Examples:
      |RESPONSE_FILE                   |RESOURCE_URI                      |RESPONSE_CODE|
      |"get_transactions_response"     |"/api/accounts/1/transactions"    |    200      |

