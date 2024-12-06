Feature: Test the anonymous greeting endpoint

Background:
* url 'https://localhost/api/v1'

Scenario: Request anonymous greeting
Given path 'anonymous/greeting'
When method get
Then status 200
And match $ == {greeting:'Hello, World!'}
