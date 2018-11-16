import java.util.Scanner;
import java.util.logging.Logger;

public class Bot{
    static final Logger log = LoggerBot.getBotLoggerInFils(Bot.class.getName());

    public static void main(String[] args) {
        //TelegramBot.Start(System.getenv("TOKEN"));
        workOnConsole();
    }

    private static void workOnConsole(){
        log.config("Console start");
        User user = new User(0);
        user.username="console";
        user.first_name=null;
        user.FSM = new FiniteStateMachine();
        user.kinoman = new Kinoman(user,"фильм", "по годам", new String[0], new String[0]);

        Scanner sc = new Scanner(System.in);
        while (true){
            String request = sc.nextLine().toLowerCase();
            request = SpellChecker.check(request).trim();
            log.info("console: " + request);
            Answer answer = Selector.getAnswer(user, request);
            for (String curAnswer : answer.answer) {
                System.out.println(curAnswer);
                log.info("bot: " + curAnswer);
            }
        }
    }
}
