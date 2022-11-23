package com.bndev.ood.hittastic.api.stepdefs;

import com.bndev.ood.hittastic.api.Order;
import com.bndev.ood.hittastic.api.Session;
import com.bndev.ood.hittastic.api.Song;
import com.bndev.ood.hittastic.api.User;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.security.auth.login.FailedLoginException;

import static org.junit.jupiter.api.Assertions.*;


public class BuySongSteps {

    Session session = null;
    Song selectedSong = null;
    Order order = null;

    @BeforeAll
    public static void setup() throws FailedLoginException {
        User.Reset();
        Song.reset();
    }

    @AfterAll
    public static void teardown() {
        User.Reset();
        Song.reset();
    }


    @And("the current user has no orders")
    public void theCurrentUserHasNoOrders() {
        session.user.orders.clear();
    }

    @When("Buy is run with a quantity of {int}")
    public void buyIsRunWithAQuantityOfQty(int qty) {
        order = session.BuySong(selectedSong, qty);
    }

    @Then("song sales is {int}")
    public void songSalesIsQty(int sales) {
        assertEquals(sales, selectedSong.sales);
    }

    @And("orders size is {int}")
    public void ordersSizeIs(int ordersSize) {
        assertEquals(ordersSize, session.user.orders.size());
    }

    @Given("the selected song has no sales")
    public void theSelectedSongHasNoSales() {
        selectedSong.sales = 0;
    }

    @Given("user is logged in")
    public void userIsLoggedIn() throws FailedLoginException {
        session = User.Login("ben", "123");
    }

    @And("order is not null")
    public void orderIsNotNull() {
        assertNotNull(order);
    }

    @And("order is null")
    public void orderIsNull() {
        assertNull(order);
    }

    @Given("song is selected")
    public void songIsSelected() {
        selectedSong = Song.createSong("Song 1", "Artist 1");
    }
}