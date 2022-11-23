package com.bndev.ood.hittastic.api.stepdefs;

import com.bndev.ood.hittastic.api.Session;
import com.bndev.ood.hittastic.api.User;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.*;

import javax.security.auth.login.FailedLoginException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    Session session = null;

    @BeforeAll
    public static void setup() {
        User.CreateUser("ben", "123");
        User.CreateAdmin("admin", "password");
    }

    @AfterAll
    public static void teardown() {
        User.Reset();
    }

    @Given("Test users have been loaded into program")
    public void testUsersHaveBeenLoadedIntoProgram() {
        User.CreateUser("ben", "123");
        User.CreateAdmin("admin", "password");
    }

    @Given("No user is logged in")
    public void noUserIsLoggedIn() {
        session = null;
    }

    @When("Login is attempted with username: {word} and password {word}")
    public void loginIsAttemptedWithUsernameBenAndPassword(String username, String password) {
        try {
            session = User.Login(username, password);
        } catch (FailedLoginException ignored) {

        }
    }

    @Then("session user has username {word}")
    public void sessionUserHasUsernameBen(String username) {
        assertEquals(username, session.user.username);
    }

    @But("session user is an admin")
    public void sessionUserIsAnAdmin() {
    }

    @Then("session is null")
    public void sessionIsNull() {
        assertNull(session);
    }

    @And("session user is not an admin")
    public void sessionUserIsNotAnAdmin() {
        assertFalse(session.user.isAdmin);
    }


}
