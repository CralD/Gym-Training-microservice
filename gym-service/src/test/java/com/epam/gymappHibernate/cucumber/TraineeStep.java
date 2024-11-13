package com.epam.gymappHibernate.cucumber;

import com.epam.gymappHibernate.GymAppHibernateApplication;
import com.epam.gymappHibernate.GymAppHibernateApplicationTests;
import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dto.CredentialsDto;
import com.epam.gymappHibernate.dto.TraineeDto;
import com.epam.gymappHibernate.dto.UserDto;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.User;
import com.epam.gymappHibernate.services.TraineeService;
import com.epam.gymappHibernate.util.JwtUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@CucumberContextConfiguration
public class TraineeStep extends GymAppHibernateApplicationTests {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TraineeRepository traineeRepository;

    private TraineeDto request;
    private CredentialsDto credentialsResponse;
    private ResponseEntity response;
    private Trainee trainee;
    String token;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;


    @Given("Juliana wants to register as a trainee in the gym")
    public void julianaWantsToRegisterAsATraineeInTheGym() {
        User user = new User();
        user.setUserName("Juliana.Hernandez");
        user.setFirstName("Juliana");
        user.setLastName("Hernandez");
        user.setPassword("1234567890");
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("carrera 35 #57");
        trainee.setDateOfBirth(new Date());
        request = new TraineeDto();
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setAddress(trainee.getAddress());
        request.setDateOfBirth(trainee.getDateOfBirth());
    }

    @When("I introduce Juliana information in the gym system")
    public void iIntroduceJulianaInformationInTheGymSystem() {
        credentialsResponse = traineeService.createTrainee(request);
    }

    @Then("The trainee profile should be created successfully")
    public void theTraineeProfileShouldBeCreatedSuccessfully() {
        assertNotNull(credentialsResponse);
    }

    @And("trainee is authenticated")
    public void traineeIsAuthenticated() {
        assertNotNull(credentialsResponse.getUsername());
        assertNotNull(credentialsResponse.getPassword());
    }


    @Given("the trainee registration request is valid")
    public void theTraineeRegistrationRequestIsValid() {
        User user = new User();
        user.setUserName("Juliana.Juarez");
        user.setFirstName("Juliana");
        user.setLastName("Juarez");
        user.setPassword("1234567890");
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("carrera 35 #57");
        trainee.setDateOfBirth(new Date());
        request = new TraineeDto();
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setAddress(trainee.getAddress());
        request.setDateOfBirth(trainee.getDateOfBirth());
    }

    @When("I send a POST request to {string}")
    public void iSendAPOSTRequestTo(String endpoint) {

        response = testRestTemplate.postForEntity("/api/trainees", request, UserDto.class);
    }


    @Then("the response should be {int}")
    public void theResponseShouldBe(int statusCode) {
        int actualStatusCode = response.getStatusCodeValue();
        assertEquals(statusCode, actualStatusCode);
    }

    @And("the response includes username {string}")
    public void theResponseIncludesUsername(String username) {
        UserDto responseBody = (UserDto) response.getBody();
        assertNotNull(responseBody);
        assertEquals(username, responseBody.getUsername());

    }

    @Given("{string} exist in the gym system")
    public void julianaWantsToEndHerGymMembership(String username) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        token = jwtUtil.generateToken(userDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/trainees/{username}",
                HttpMethod.GET,
                entity,
                UserDto.class,
                username
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Trainee does not exist in the system.");
    }

    @When("I select {string} profile in the system")
    public void iSelectJulianaProfileInTheSystem(String username) {

        trainee = traineeRepository.getTraineeByUsername(username);
        System.out.println(trainee.getUser().getUserName());
    }


    @Then("The trainee {string} profile should be deleted successfully")
    public void theTraineeProfileShouldBeDeletedSuccessfully(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        token = jwtUtil.generateToken(userDetails);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = testRestTemplate.exchange("/api/trainees/{username}", HttpMethod.DELETE, entity, Void.class, username);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to delete trainee profile");
    }

    @Given("the trainee registration request is invalid")
    public void theTraineeRegistrationRequestIsInvalid() {
        User user = new User();
        user.setUserName("Juliana.Juarez");
        user.setFirstName(null);
        user.setLastName("Juarez");
        user.setPassword("1234567890");
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("carrera 35 #57");
        trainee.setDateOfBirth(new Date());
        request = new TraineeDto();
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setAddress(trainee.getAddress());
        request.setDateOfBirth(trainee.getDateOfBirth());
    }

    @When("I send a POST request to {string} with invalid data")
    public void iSendAPOSTRequestToWithInvalidData(String endpoint) {
        response = testRestTemplate.postForEntity("/api/trainees", request, UserDto.class);

    }

    @Then("the response should be {string}")
    public void theResponseShouldBe1(int statusCode1) {
        int actualStatusCode = response.getStatusCodeValue();
        assertEquals(statusCode1, actualStatusCode);
    }


    @Given("{string} does not exist in the gym system")
    public void doesNotExistInTheGymSystem(String username) {
        User user = new User();
        user.setUserName("Juliana.Juarez");
        user.setFirstName("Juliana");
        user.setLastName("Juarez");
        user.setPassword("1234567890");
        String securityUsername = user.getUserName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(securityUsername);
        token = jwtUtil.generateToken(userDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/trainees/{username}",
                HttpMethod.GET,
                entity,
                UserDto.class,
                securityUsername
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Trainee does not exist in the system.");
    }

    @When("I look for {string} profile in the system")
    public void iLookForProfileInTheSystem(String username) {
        username = null;

    }

 @Then("The trainee Delete profile should be skipped")
    public void theTraineeDeleteProfileShouldBeSkipped() {
     String username = null;

     if (username == null) {
         System.out.println("Deletion skipped as the username is null.");
         return;
     }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        token = jwtUtil.generateToken(userDetails);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = testRestTemplate.exchange("/api/trainees/{username}", HttpMethod.DELETE, entity, Void.class,username);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to delete trainee profile");
    }
}
