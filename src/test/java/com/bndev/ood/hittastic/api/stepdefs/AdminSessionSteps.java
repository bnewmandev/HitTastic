package com.bndev.ood.hittastic.api.stepdefs;

import com.bndev.ood.hittastic.api.Session;
import com.bndev.ood.hittastic.api.Song;
import com.bndev.ood.hittastic.api.User;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdminSessionSteps {

    Session session = null;
    AuthenticationException ex = null;

    List<User> userList = null;

    @Given("an admin is logged in")
    public void anAdminIsLoggedIn() {
        session = new Session(User.CreateAdmin("admin", "pass"));
    }

    @Given("there are no songs loaded")
    public void thereAreNoSongsLoaded() {
        Song.all.clear();
    }

    @When("a new song is added")
    public void aNewSongIsAdded() {
        try {
            session.AddSong("New Song", "New Artist");
        } catch (AuthenticationException e) {
            ex = e;
        }
    }

    @Then("the length of songs is {int}")
    public void theLengthOfSongsIs(int length) {
        assertEquals(length, Song.all.size());
    }

    @Given("a user is logged in")
    public void aUserIsLoggedIn() {
        session = new Session(User.CreateUser("ben", "123"));
    }

    @And("an authentication exception is thrown")
    public void anAuthenticationExceptionIsThrown() {
        assertNotNull(ex);
    }

    @Given("the following users are loaded")
    public void theFollowingUsersAreLoaded(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        System.out.println(data);
        data.forEach(x -> User.CreateUser(x.get("username"), x.get("password")));
    }

    @When("list users is called")
    public void listUsersIsCalled() throws AuthenticationException {
        userList = session.GetAllUsers();
    }

    @Then("size of user list is {int}")
    public void sizeOfUserListIs(int size) {
        assertNotNull(userList);
        assertEquals(size, userList.size());
    }

    @When("change user is called on {string} with details {string} {string} {string}")
    public void changeUserIsCalledOnUserWithDetailsNewuserPasswordAdmin(String oldUser, String newUser, String password, String adminState) throws AuthenticationException {
        boolean admin = Boolean.parseBoolean(adminState);
        session.UpdateUser(oldUser, newUser, password, admin);
    }

    @Then("{string} is not null")
    public void newuserIsNotNull(String username) {
        assertNotNull(User.all.get(username));
    }

    @Then("the password of {string} is {string}")
    public void thePasswordOfNewuserIsPassword(String username, String password) {
        assertEquals(password, User.all.get(username).password);
    }

    @Then("the admin state of {string} is {string}")
    public void theAdminStateOfNewuserIsAdmin(String username, String adminState) {
        boolean admin = Boolean.parseBoolean(adminState);
        assertEquals(admin, User.all.get(username).isAdmin);
    }

    @Given("there are no users loaded")
    public void thereAreNoUsersLoaded() {
        User.Reset();
        session = null;
    }
}
