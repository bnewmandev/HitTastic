package com.bndev.ood.hittastic.api.stepdefs;

import com.bndev.ood.hittastic.api.Song;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSteps {
    List<Song> selectedSongs = null;

    @BeforeAll
    public static void setup() {
        Song.createSong("Faded And Heaven", "Yassin Bates");
        Song.createSong("Longing For Past", "Christy Weber");
        Song.createSong("Wild And Vibes", "Tamar Marks");
        Song.createSong("We Can Dance", "Todd French");
        Song.createSong("Picture Of Fame", "Julie Knapp");
    }

    @AfterAll
    public static void teardown() {
        Song.reset();
    }

    @Given("No songs are selected")
    public void noSongIsSelected() {
        selectedSongs = null;
    }


    @Then("results has a length of {int}")
    public void resultsHasALengthOfLength(int size) {
        assertEquals(size, selectedSongs.size());
    }


    @When("Title search is run with {string}")
    public void titleSearchIsRunWithPhrase(String phrase) {
        selectedSongs = Song.findByTitle(phrase);
    }

    @When("Artist search is run with {string}")
    public void artistSearchIsRunWithPhrase(String phrase) {
        selectedSongs = Song.findByArtist(phrase);
    }
}
