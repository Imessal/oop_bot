import java.util.Scanner;
import java.util.logging.Logger;

public class Bot{
    static final Logger log = LoggerBot.getBotLogger(Bot.class.getName());

    public static void main(String[] args) {
        TelegramBot.Start(System.getenv("TOKEN"));
        workOnConsole();
    }

    private static void workOnConsole(){
        log.config("Console start");
        User user = new User();
        user.FSM = new FiniteStateMachine();
        user.kinoman = new Kinoman("фильм", "по годам");

        Scanner sc = new Scanner(System.in);
        while (true){
            String request = sc.nextLine().toLowerCase();
            request = SpellChecker.check(request);
            log.info("console: " + request);
            Answer answer = Selector.getAnswer(user, request);
            for (String curAnswer : answer.answer) {
                System.out.println(curAnswer);
                log.info("bot: " + curAnswer);
            }
        }
    }
}
