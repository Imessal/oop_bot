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


public class TelegramBot extends TelegramLongPollingBot {
    private static String TOKEN;
    private HashMap<Long, User> users = new HashMap<>();

    static void Start(String token) {
        TOKEN = token;
        ApiContextInitializer.init();
        TelegramBotsApi TBA = new TelegramBotsApi();
        try {
            TBA.registerBot(new TelegramBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }}

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String request = message.getText().toLowerCase();
        request = SpellChecker.check(request);
        checkUser(message); //Если пользователь новенький, кидает его в users
        User user = users.get(chatId);
        Answer answer = Selector.getAnswer(user, request);
        for (String curAnswer : answer.answer) {
            sendMsg(curAnswer, chatId, answer.buttons);
        }
    }

    private void sendMsg(String text, Long chatId, String... commands){
        if (chatId == null){
            System.out.println(text);
        }else {
            SendMessage sendMessage = new SendMessage(chatId, text);
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);

            if (commands.length != 0) {
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                replyKeyboardMarkup.setSelective(true);
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setOneTimeKeyboard(true);

                List<KeyboardRow> keyboard = new ArrayList<>(); // Создаем список строк клавиатуры
                KeyboardRow keyboardFirstRow = new KeyboardRow(); // Первая строчка клавиатуры
                Arrays.stream(commands).forEach(keyboardFirstRow::add); // Добавляем кнопки в первую строчку клавиатуры
                keyboard.add(keyboardFirstRow); // Добавляем все строчки клавиатуры в список
                replyKeyboardMarkup.setKeyboard(keyboard); // и устанваливаем этот список нашей клавиатуре
            }

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkUser(Message message){
        Long chatId = message.getChatId();
        if(!users.containsKey(chatId)){
            users.put(chatId, new User());
            User user = users.get(chatId);
            user.FSM = new FiniteStateMachine();
            user.kinoman = new Kinoman("фильм", "по годам");
            user.first_name = message.getFrom().getFirstName();
            user.username = message.getFrom().getUserName();
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
