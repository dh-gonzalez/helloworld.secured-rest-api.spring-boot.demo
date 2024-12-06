Feature: Test actuator health is available

Background:
* url 'https://localhost:8443'

Scenario: Request acutator health
Given path 'actuator/health'
When method get
Then status 200
And match $ == {"status":"UP"}