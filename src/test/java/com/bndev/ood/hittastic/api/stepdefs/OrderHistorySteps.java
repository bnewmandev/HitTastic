package com.bndev.ood.hittastic.api.stepdefs;

import com.bndev.ood.hittastic.api.Order;
import com.bndev.ood.hittastic.api.Session;
import com.bndev.ood.hittastic.api.Song;
import com.bndev.ood.hittastic.api.User;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderHistorySteps {

    Session session = null;
    Song song = null;

    List<Order> orders = null;

    @BeforeAll
    public static void setup() {
        User.Reset();
        Song.reset();
    }

    @AfterAll
    public static void teardown() {
        User.Reset();
        Song.reset();
    }

    @Given("user data is loaded")
    public void userDataIsLoaded() {
        session = new Session(User.CreateUser("ben", "123"));
        song = Song.createSong("Song 1", "Artist 1");

        this.session.user.createOrder(song, 1);
        this.session.user.createOrder(song, 3);
        this.session.user.createOrder(song, 5);
    }

    @When("get order history is called")
    public void getOrderHistoryIsCalled() {
        orders = session.ViewOrderHistory();
    }

    @Then("order history size is {int}")
    public void orderHistorySizeIs(int size) {
        assertEquals(size, orders.size());
    }
}
