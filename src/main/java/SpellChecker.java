import java.util.ArrayList;
import java.util.HashMap;

class SpellChecker {
    static String check(String request){
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
        return st.toString();
    }
    private static HashMap<String, ArrayList<String>> getDict(){
        HashMap<String, ArrayList<String>> dict = new HashMap<>();

        dict.put("фильм", new ArrayList<>());
        dict.get("фильм").add("фильмы");

        dict.put("сериал", new ArrayList<>());
        dict.get("сериал").add("сериалы");

        dict.put("мультфильм", new ArrayList<>());
        dict.get("мультфильм").add("мультфильмы");
        dict.get("мультфильм").add("мультик");
        dict.get("мультфильм").add("мультики");
        dict.get("мультфильм").add("мульт");
        dict.get("мультфильм").add("мульты");

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

        dict.put("рандомный", new ArrayList<>());
        dict.get("рандомный").add("рандомные");
        dict.get("рандомный").add("рандомно");
        dict.get("рандомный").add("рандомное");
        dict.get("рандомный").add("случайный");
        dict.get("рандомный").add("случайные");

        dict.put("годам", new ArrayList<>());
        dict.get("годам").add("году");
        dict.get("годам").add("новые");
        dict.get("годам").add("новый");
        dict.get("годам").add("новенькое");
        dict.get("годам").add("новенький");
        dict.get("годам").add("свежие");
        dict.get("годам").add("свежий");

        /*
        dict.put("рейтингу", new ArrayList<>());
        dict.get("рейтингу").add("");
        dict.put("времени", new ArrayList<>());
        dict.get("времени").add("");
        */

        dict.put("комедия", new ArrayList<>());
        dict.get("комедия").add("комедию");
        dict.get("комедия").add("комедии");
        dict.get("комедия").add("смешное");
        dict.get("комедия").add("смешные");
        dict.get("комедия").add("ржаку"); // не ну мало ли


        /*
        dict.put("боевик", new ArrayList<>());
        dict.get("боевик").add("");
        */
        dict.put("приключения", new ArrayList<>());
        dict.get("приключения").add("приключение");


        dict.put("ужас", new ArrayList<>());
        dict.get("ужас").add("ужастик");
        dict.get("ужас").add("ужастики");
        dict.get("ужас").add("страшный");
        dict.get("ужас").add("страшные");

        dict.put("фантастика", new ArrayList<>());
        dict.get("фантастика").add("фантастику");
        dict.get("фантастика").add("фантастический");

        dict.put("фэнтези", new ArrayList<>());
        dict.get("фэнтези").add("фентези");
        dict.get("фэнтези").add("фентэзи");

        return dict;
    }
}
