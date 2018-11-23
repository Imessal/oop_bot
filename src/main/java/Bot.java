import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class Bot{
    static final Logger log = LoggerBot.getBotLoggerInFils(Bot.class.getName());
    static DatabaseRepository repository = new DatabaseRepository();

    public static void main(String[] args) {
        //TelegramBot.start();
        ConsoleBot.start();
    }

    static void doRequest(User user, String request, BiConsumer<Long, Answer> sender){
        request = RequestChecker.validate(request);
        sender.accept((long) user.getId(), Selector.getAnswer(user, request));
    }
}
