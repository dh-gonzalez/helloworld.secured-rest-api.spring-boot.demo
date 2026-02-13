Feature: Test swagger api-docs is available

Background:
* url 'https://localhost:8443'

Scenario: Request swagger api-docs
Given path 'v3/api-docs'
When method get
Then status 200
