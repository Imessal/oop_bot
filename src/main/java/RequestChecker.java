import java.util.ArrayList;
import java.util.HashMap;

class RequestChecker {
    static String validate(String request) {
        return spellCheck(request.trim().toLowerCase());
    }

    private static String spellCheck(String request){
        if (request.startsWith("привет")){
            return "/start";
        }
        String[] words = request.split(" ");
        if (words.length < 2){
            return request;
        }
        HashMap<String, ArrayList<String>> dict = getDict();
        StringBuilder st = new StringBuilder();
        label:
        for (String word: words){
            for (String key: dict.keySet()){
                if (word.equals(key)){
                    st.append(word);
                    st.append(" ");
                    continue label;
                }
                if (dict.get(key).contains(word)){
                    word = key;
                    st.append(word);
                    st.append(" ");
                    continue label;
                }
            }
            st.append(word);
            st.append(" ");
        }
        return st.toString().trim();
    }

    private static HashMap<String, ArrayList<String>> getDict(){
        HashMap<String, ArrayList<String>> dict = new HashMap<>();

        //Фильм
        dict.put("фильм", new ArrayList<>());
        dict.get("фильм").add("фильмы");
        dict.get("фильм").add("кино");
        dict.put("сериал", new ArrayList<>());
        dict.get("сериал").add("сериалы");
        dict.put("мультфильм", new ArrayList<>());
        dict.get("мультфильм").add("мультфильмы");
        dict.get("мультфильм").add("мультик");
        dict.get("мультфильм").add("мультики");
        dict.get("мультфильм").add("мульт");
        dict.get("мультфильм").add("мульты");
        dict.get("мультфильм").add("мультов");
        dict.put("мультсериал", new ArrayList<>());
        dict.get("мультсериал").add("мультсериалы");
        dict.put("аниме", new ArrayList<>());
        dict.get("аниме").add("анимку");
        dict.get("аниме").add("анимки");
        dict.get("аниме").add("аниму");
        dict.get("аниме").add("анимэ");
        dict.put("аниме-сериал", new ArrayList<>());
        dict.get("аниме-сериал").add("анимесериал");
        dict.get("аниме-сериал").add("анимусериал");
        dict.get("аниме-сериал").add("анимэсериал");
        dict.get("аниме-сериал").add("анимесериалы");
        dict.get("аниме-сериал").add("анимусериалы");
        dict.get("аниме-сериал").add("анимэсериалы");


        //Сортировка
        dict.put("рандомный", new ArrayList<>());
        dict.get("рандомный").add("рандомные");
        dict.get("рандомный").add("рандомно");
        dict.get("рандомный").add("рандомное");
        dict.get("рандомный").add("случайный");
        dict.get("рандомный").add("случайные");
        dict.put("по годам", new ArrayList<>());
        dict.get("по годам").add("по году");
        dict.get("по годам").add("новые");
        dict.get("по годам").add("новый");
        dict.get("по годам").add("новенькое");
        dict.get("по годам").add("новенький");
        dict.get("по годам").add("свежие");
        dict.get("по годам").add("свежий");
        dict.put("по рейтингу", new ArrayList<>());
        dict.get("по рейтингу").add("топ");
        dict.get("по рейтингу").add("топовый");
        dict.get("по рейтингу").add("топовые");

        //Жанры
        dict.put("комедия", new ArrayList<>());
        dict.get("комедия").add("комедию");
        dict.get("комедия").add("комедии");
        dict.get("комедия").add("смешной");
        dict.get("комедия").add("смешное");
        dict.get("комедия").add("смешные");
        dict.get("комедия").add("смешных");
        dict.get("комедия").add("ржаку"); // не ну мало ли
        dict.put("приключения", new ArrayList<>());
        dict.get("приключения").add("приключение");
        dict.put("ужас", new ArrayList<>());
        dict.get("ужас").add("ужастик");
        dict.get("ужас").add("ужастики");
        dict.get("ужас").add("ужасов");
        dict.get("ужас").add("страшный");
        dict.get("ужас").add("страшные");
        dict.get("ужас").add("хоррор");
        dict.get("ужас").add("хорор");
        dict.get("ужас").add("хорроры");
        dict.get("ужас").add("хороры");
        dict.put("фантастика", new ArrayList<>());
        dict.get("фантастика").add("фантастику");
        dict.get("фантастика").add("фантастический");
        dict.put("фэнтези", new ArrayList<>());
        dict.get("фэнтези").add("фентези");
        dict.get("фэнтези").add("фентэзи");

        //Страны
        dict.put("великобритания", new ArrayList<>());
        dict.get("великобритания").add("великобританский");
        dict.get("великобритания").add("великобританские");
        dict.get("великобритания").add("великобританское");
        dict.get("великобритания").add("английский");
        dict.get("великобритания").add("английские");
        dict.get("великобритания").add("английское");
        dict.put("германия", new ArrayList<>());
        dict.get("германия").add("немецкий");
        dict.get("германия").add("немецкие");
        dict.get("германия").add("немецкое");
        dict.put("италия", new ArrayList<>());
        dict.get("италия").add("итальянский");
        dict.get("италия").add("итальянские");
        dict.get("италия").add("итальянское");
        dict.put("ссср", new ArrayList<>());
        dict.get("ссср").add("советских");
        dict.get("ссср").add("советский");
        dict.get("ссср").add("советские");
        dict.get("ссср").add("советское");
        dict.put("россия", new ArrayList<>());
        dict.get("россия").add("российский");
        dict.get("россия").add("российские");
        dict.get("россия").add("российское");
        dict.get("россия").add("русский");
        dict.get("россия").add("русские");
        dict.get("россия").add("русское");
        dict.put("сша", new ArrayList<>());
        dict.get("сша").add("американский");
        dict.get("сша").add("американские");
        dict.get("сша").add("американское");
        dict.put("франция", new ArrayList<>());
        dict.get("франция").add("француский");
        dict.get("франция").add("француские");
        dict.get("франция").add("француское");
        dict.put("япония", new ArrayList<>());
        dict.get("япония").add("японский");
        dict.get("япония").add("японские");
        dict.get("япония").add("японское");
        dict.put("китай", new ArrayList<>());
        dict.get("китай").add("китайский");
        dict.get("китай").add("китайские");
        dict.get("китай").add("китайское");

        return dict;
    }
}
