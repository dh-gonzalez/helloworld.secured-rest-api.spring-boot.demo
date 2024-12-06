Feature: Test swagger UI is available

Background:
* url 'https://localhost:8443'

Scenario: Request swagger UI
Given path '/swagger-ui/index.html'
When method get
Then status 200
