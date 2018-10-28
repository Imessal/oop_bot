import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Bot extends TelegramLongPollingBot {
    private static HashMap<Long, User> users = new HashMap<>();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi TBA = new TelegramBotsApi();
        try {
            TBA.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }}

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String request = message.getText().toLowerCase();
        checkUser(message); //Если пользователь новенький, кидает его в users
        if (request.equals("/start") || request.equals("привет")){
            sendHello(chatId);
            System.out.println("Привет!");
            return;
        }
        User user = users.get(chatId);

        //Date date = new Date();
        //SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        System.out.print("\n" + "(" + user.username + ")" + user.first_name + ": "); // + format.format(date) + " "
        System.out.println(request);

        FiniteStateMachine.State state = user.FSM.workWithRequest(request);
        switch (state) {
            case Ready:
                break;
            case Requesting:
                sendMsg(chatId,"Минутку...");
                request = SpellChecker.check(request); //Заменит "случайный" на "рандомый" и т.п.
                user.kinoman = Kinoman.createKinomanOnRequest(request);
                if (user.kinoman.sortingType.equals("рандомный")){
                    sendMsg(chatId, "Я могу долго выбирать рандомный фильм, лучше указать метод сортировки.");
                }
                Movie movie = user.kinoman.getNext();
                if (movie == null){
                    sendMsg(chatId, "Ты пытался найди хоррор мультик или что-то вроде?" +
                            "\nВ любом случае я ничего не нашел.");
                    System.out.println("Ничего не нашел по данному запросу");
                    break;
                }
                System.out.println(movie.name);
                sendMsg(chatId, movie.toStringMovie(), "Возможные запросы","Похожие","Следующий");
                break;
            case ShowingSimilar:
                List<Movie> similarMovie = user.kinoman.showSimilar();
                if(similarMovie != null){
                    for (int i = 0; i < 3; ++i) {
                        movie = user.kinoman.showSimilar().get(i);
                        System.out.println(movie.name);
                        sendMsg(chatId, movie.toStringMovie(),"Следующий");
                    }
                } else {
                    sendMsg(chatId, "Я не нашел похожих, сорян");
                    System.out.println("Нет похожих");
                }
                break;
            case ShowingNext:
                movie = user.kinoman.getNext();
                if (movie == null){
                    sendMsg(chatId, "Больше нет");
                    System.out.println("Больше нет");
                    break;
                }
                System.out.println(movie.name);
                sendMsg(chatId, movie.toStringMovie(), "Похожие", "Следующий");
                break;
            case ShowingHelp:
                sendMsg(chatId, Kinoman.printHelp(), "Возможные запросы");
                System.out.println("Вывел помощь");
                break;
            case ShowingValidRequests:
                sendMsg(chatId, Kinoman.printValidRequest());
                System.out.println("Вывел возможные запросы");
                break;
            default:
                String text = "Ну, я ничего не понял(" +
                        "\nМожет ты что-то не правильно написал? Псмотреть как надо - \"помощь\"";
                sendMsg(chatId,text);
                System.out.println("Не корректный запрос");
        }
    }

    private void sendMsg(Long chatId, String text, String... commands){
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.enableMarkdown(true);
        //sendMessage.disableWebPagePreview(); //Отключает подгрузку страницы в чат телеги

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        if (commands.length != 0) {
            // Создаем список строк клавиатуры
            List<KeyboardRow> keyboard = new ArrayList<>();

            // Первая строчка клавиатуры
            KeyboardRow keyboardFirstRow = new KeyboardRow();
            // Добавляем кнопки в первую строчку клавиатуры
            keyboardFirstRow.add(commands[0]);
            if (commands.length > 1) {
                keyboardFirstRow.add(commands[1]);
            }

            // Добавляем все строчки клавиатуры в список
            keyboard.add(keyboardFirstRow);
            // и устанваливаем этот список нашей клавиатуре
            replyKeyboardMarkup.setKeyboard(keyboard);
        }

        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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

    private void sendHello(Long chatId){
        String st = "Привет, я Киноман!" +
                "\nМогу помочь с выбором фильма, сериала и т.д." +
                "\nДля справки введи \"помощь\"";
        sendMsg(chatId, st, "Помощь", "Возможные запросы");
    }

    @Override
    public String getBotUsername() {
        return "oop_KinomanBot";
    }

    @Override
    public String getBotToken() {
        return "664282063:AAGwegI5Tp2tT42RJKRD3v_RKCQ5LroEipk";
    }
}
