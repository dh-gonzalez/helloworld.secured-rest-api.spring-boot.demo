Feature: Test the secured greeting endpoint

Background:
* url 'https://localhost:8443/api/v1'

Scenario: Request secured greeting
# Step to authenticate user and retrieve the token
Given path 'authenticate'
And request { username: 'admin', password: 'admin' }
When method post
Then status 200
And match $ == {token:'#string',type:'Bearer',expiresInSeconds:'#number? _ > 0'}
And def token = response.token
And def type = response.type
And print 'Token retrieved:', token
And print 'Type retrieved:', type

# Step to call the secured greeting endpoint with the token
Given path 'secured/greeting'
And param name = 'David'
And header Authorization = type + ' ' + token
When method get
Then status 200
And match $ == {greeting:'Hello, David!'}
