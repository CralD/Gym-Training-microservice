Feature: Trainee
  Scenario: Register a new trainee
    Given Juliana wants to register as a trainee in the gym
    When I introduce Juliana information in the gym system
    And trainee is authenticated
    Then The trainee profile should be created successfully

  Scenario: Delete a existing trainee
    Given "Juliana.Hernandez" exist in the gym system
    When I select "Juliana.Hernandez" profile in the system
    Then The trainee "Juliana.Hernandez" profile should be deleted successfully

  Scenario: Register a trainee with valid data
    Given the trainee registration request is valid
    When I send a POST request to "/api/trainees"
    And the response includes username "Juliana.Juarez"
    Then the response should be 200

  Scenario: Register a trainee with invalid data
    Given the trainee registration request is invalid
    When I send a POST request to "/api/trainees" with invalid data
    Then the response should be 403

  Scenario: Delete a existing trainee and failed
    Given "Rodrigo.Perez" does not exist in the gym system
    When I look for "Rodrigo.Perez" profile in the system
    Then The trainee Delete profile should be skipped


  Scenario: Failed authentication due to incorrect password
    Given a trainee with username "Juliana.Juarez" exists in the system
    And the password for "Juliana.Juarez" is "correctPassword"
    When I attempt to authenticate with username "Juliana.Juarez" and password "wrongPassword"
    Then the authentication should fail

  Scenario: failed post Login
    Given a registered user with username "Juliana.Juarez"
    When I send a POST request to "/api/auth/login" with username "Juliana.Juarez"
    Then the response should be code 403




