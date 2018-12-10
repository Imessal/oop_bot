import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.*;
import java.util.function.BiConsumer;


public class TelegramBot extends TelegramLongPollingBot {
    private HashMap<Integer, User> users = new HashMap<>();
    private DatabaseRepository repository = new DatabaseRepository();

    static void start() {
        ApiContextInitializer.init();
        TelegramBotsApi TBA = new TelegramBotsApi();
        try {
            TBA.registerBot(new TelegramBot());
            Bot.log.config("Telegram start");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = getUser(message);
        String request = message.getText();
        Bot.log.info("("+user.username+")"+user.first_name+": "+request);
        Bot.doRequest(user, request, reply);
    }

    private BiConsumer<Long, Answer> reply = (chatId, answer) -> {
        for (String message : answer.messages) {
            SendMessage sendMessage = new SendMessage(chatId, message);
            sendMessage.enableMarkdown(true);

            if (answer.f_buttons != null) {
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                replyKeyboardMarkup.setSelective(true);
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setOneTimeKeyboard(true);

                List<KeyboardRow> keyboard = new ArrayList<>(); // Создаем список строк клавиатуры
                KeyboardRow keyboardFirstRow = new KeyboardRow(); // Первая строчка клавиатуры
                Arrays.stream(answer.f_buttons).forEach(keyboardFirstRow::add); // Добавляем кнопки в первую строчку клавиатуры
                keyboard.add(keyboardFirstRow); // Добавляем строку клавиатуры в список
                if (answer.s_buttons != null) {
                    KeyboardRow keyboardSecondRow = new KeyboardRow(); // Вторая строчка клавиатуры
                    Arrays.stream(answer.s_buttons).forEach(keyboardSecondRow::add); // Добавляем кнопки во вторую строчку клавиатуры
                    keyboard.add(keyboardSecondRow);
                }
                replyKeyboardMarkup.setKeyboard(keyboard); // и устанваливаем этот список нашей клавиатуре
            }

            try {
                execute(sendMessage);
                Bot.log.info("Bot: " + message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    };

    private User getUser(Message message) {
        Integer userId = message.getFrom().getId();
        if (!users.containsKey(userId)) {
            User user = new User(userId);
            user.first_name = message.getFrom().getFirstName();
            user.username = message.getFrom().getUserName();
            user.FSM = new FiniteStateMachine();
            users.put(userId, user);
            repository.addUser(user);
            Bot.log.config("New user - " + user.username);
        }
        return users.get(userId);
    }

    @Override
    public String getBotUsername() {
        return "oop_KinomanBot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TOKEN");
    }
}
