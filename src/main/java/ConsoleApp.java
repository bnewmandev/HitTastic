import com.bndev.ood.hittastic.api.User;
import com.bndev.ood.hittastic.console.App;

public class ConsoleApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            runner();
        } else if ((args.length == 1)) {
            if (args[0].equals("seed")) {
                Seeder.run();
                runner();
            } else if (args[0].equals("seedonly")) {
                Seeder.run();
            } else {
                System.out.println("Invalid arguments");
            }
        } else {
            System.out.println("Invalid arguments");
        }
    }

    private static void runner() {
        App app = new App();
        System.out.println(User.all.getStoreFile());
        app.run();
    }
}
