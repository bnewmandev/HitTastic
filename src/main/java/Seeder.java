import com.bndev.ood.hittastic.api.Song;
import com.bndev.ood.hittastic.api.User;

public class Seeder {

    public static void run() {
        Song.reset();
        User.Reset();

        Song.Save();
        User.Save();

        songs();
        users();
    }

    private static void users() {
        User.CreateUser("ben", "123");
        User.CreateAdmin("admin", "123");
    }

    private static void songs() {
        Song.createSong("Faded And Heaven", "Yassin Bates");
        Song.createSong("Longing For Past", "Christy Weber");
        Song.createSong("Wild And Vibes", "Tamar Marks");
        Song.createSong("We Can Dance", "Todd French");
        Song.createSong("Picture Of Fame", "Julie Knapp");
    }
}
