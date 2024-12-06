Feature: Test the secured greeting endpoint

Background:
* url 'https://localhost/api/v1'

Scenario: Request secured greeting
Given path 'secured/greeting'
And param name = 'David'
When method get
Then status 200
And match $ == {greeting:'Hello, David!'}
