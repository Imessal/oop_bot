import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

class ConsoleBot {
    private static final Logger log = Bot.log;
    private static final User user = getConsoleUser();

    private static BiConsumer<Long, Answer> reply = (chatId, answer) -> {
        for (String message: answer.messages) {
            System.out.println(message);
            log.info("bot: " + message);
        }
    };

    static void start(){
        log.config("Console start");
        Scanner scanner = new Scanner(System.in);
        while (true){
            String request = scanner.nextLine();
            log.info("console: " + request);
            Bot.doRequest(user, request, reply);
        }
    }

    private static User getConsoleUser(){
        User user = new User(0);
        user.username = "console";
        user.first_name = null;
        user.FSM = new FiniteStateMachine();
        return user;
    }
}
