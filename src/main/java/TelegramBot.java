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
import java.util.logging.Logger;


public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = Bot.log;
    static DatabaseRepository repository = new DatabaseRepository();
    private static String TOKEN;
    private HashMap<Integer, User> users = new HashMap<>();

    static void Start(String token) {
        TOKEN = token;
        ApiContextInitializer.init();
        TelegramBotsApi TBA = new TelegramBotsApi();
        try {
            TBA.registerBot(new TelegramBot());
            log.config("Telegram start");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        checkUser(message);
        User user = users.get(message.getFrom().getId());
        Long chatId = message.getChatId();
        String request = SpellChecker.check(message.getText().toLowerCase()).trim();
        log.info("("+user.username+")"+user.first_name+": "+request);
        Answer answer = Selector.getAnswer(user, request);
        for (String curAnswer : answer.answer) {
            sendMsg(curAnswer, chatId, answer.f_buttons, answer.s_buttons);
            log.info("bot: " + curAnswer);
        }
    }

    private void sendMsg(String text, Long chatId, String[] f_commands, String[] s_commands) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.enableMarkdown(true);

        if (f_commands != null) {
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(true);

            List<KeyboardRow> keyboard = new ArrayList<>(); // Создаем список строк клавиатуры
            KeyboardRow keyboardFirstRow = new KeyboardRow(); // Первая строчка клавиатуры
            Arrays.stream(f_commands).forEach(keyboardFirstRow::add); // Добавляем кнопки в первую строчку клавиатуры
            keyboard.add(keyboardFirstRow); // Добавляем строку клавиатуры в список
            if (s_commands != null) {
                KeyboardRow keyboardSecondRow = new KeyboardRow(); // Вторая строчка клавиатуры
                Arrays.stream(s_commands).forEach(keyboardSecondRow::add); // Добавляем кнопки во вторую строчку клавиатуры
                keyboard.add(keyboardSecondRow);
            }
            replyKeyboardMarkup.setKeyboard(keyboard); // и устанваливаем этот список нашей клавиатуре
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void checkUser(Message message){
        Integer userId = message.getFrom().getId();
        if(!users.containsKey(userId)){
            User newUser = new User(userId);
            users.put(userId, newUser);
            newUser.first_name = message.getFrom().getFirstName();
            newUser.username = message.getFrom().getUserName();
            repository.addUser(newUser);
            newUser.FSM = new FiniteStateMachine();
            newUser.kinoman = new Kinoman(newUser, "фильм", "по годам", new String[0], new String[0]);
            repository.addUser(newUser);
            log.config("new user - " + newUser.username);
        }
    }

    @Override
    public String getBotUsername() {
        return "oop_KinomanBot";
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
