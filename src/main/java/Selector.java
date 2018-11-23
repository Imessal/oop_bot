import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class Selector {
    static Answer getAnswer(User user, String request){
        FiniteStateMachine.State state = user.FSM.workWithRequest(request);
        switch (state) {
            case SayHello:
                Bot.log.config("FSM - SayHello");
                return sayHello();

            case Requesting:
                Bot.log.config("FSM - Requesting");
                return requesting(user, request);

            case ShowingAnnotation:
                Bot.log.config("FSM - ShowingAnnotation");
                return showingAnnotation(user);

            case ShowingSimilar:
                Bot.log.config("FSM - ShowingSimilar");
                return showingSimilar(user);

            case ShowingNext:
                Bot.log.config("FSM - ShowingNext");
                return showingNext(user);

            case BanningCurrentRequest:
                Bot.log.config("FSM - BanningRequest");
                return banningCurrentRequest(user);

            case ShowingHelp:
                Bot.log.config("FSM - ShowingHelp");
                return showingHelp();

            case ShowingValidRequests:
                Bot.log.config("FSM - ShowingValidRequests");
                return showingValidRequests();

            default:
                return new Answer(new String[]{"Некорректная команда" +
                        "\nМожет ты что-то не правильно написал?"},
                        new String[]{"Помощь", "Возможные запросы"});
        }
    }

    private static Answer sayHello(){
        return new Answer(new String[]{"Привет, я Киноман!\nМогу помочь с выбором фильма, сериала и т.д.\n" +
                "Для справки введи \"помощь\""},
                new String[]{"Помощь", "Возможные запросы"});
    }
    private static Answer requesting(User user, String request){
        user.kinoman = new Kinoman(user, request);
        Movie movie = user.kinoman.getNext();
        if (movie == null){
            return new Answer(new String[]{"Ты пытался найди хоррор мультик или что-то вроде?" +
                    "\nВ любом случае я ничего не нашел."});
        }
        return new Answer(new String[]{movie.toStringMovie()},
                new String[]{"Похожие", "Следующий"}, new String[]{"Скрыть", "Аннотация"});
    }
    private static Answer showingAnnotation(User user){
        return new Answer(new String[]{user.kinoman.getCurrentMovie().getAnnotation()},
                new String[]{"Скрыть", "Следующий"},  new String[]{"Похожие"});
    }
    private static Answer showingSimilar(User user){
        List<Movie> similarMovie = user.kinoman.showSimilar();
        if(similarMovie != null) {
            ArrayList<String> answer = new ArrayList<String>() {
            };
            for (Movie curMovie: similarMovie){
                answer.add(curMovie.toStringMovie());
            }
            return new Answer(answer.toArray(new String[0]),
                    new String[]{"Следующий"});
        } else {
            return new Answer(new String[]{"Я не нашел похожих, сорян"},
                    new String[]{"Следующий"});
        }
    }
    private static Answer showingNext(User user){
        Movie movie = user.kinoman.getNext();
        if (movie == null){
            return new Answer(new String[]{"Это был последний"});
        }
        return new Answer(new String[]{movie.toStringMovie()},
                new String[]{"Похожие", "Следующий"}, new String[]{"Скрыть", "Аннотация"});
    }
    private static Answer banningCurrentRequest(User user){
        Movie c_movie = user.kinoman.getCurrentMovie();
        Bot.repository.addMovieToBlackList(user, c_movie);
        return new Answer(new String[]{"Больше я не буду показывать вам - " + c_movie.getName()},
                new String[]{"Похожие", "Следующий"});
    }
    private static Answer showingHelp(){
        return new Answer(new String[]{"Просто напиши - \"Покажи\" + что хочешь посмотреть\n" +
                "\nПример запроса:\n" +
                "Покажи Назад в будущее\n" +
                "Покажи новые мультфильмы\n" +
                "Покажи американские сериалы по рейтингу\n\n" +
                "Команды:\n" +
                "Помощь - выводит справку\n" +
                "Возможные запросы - выводит возможные фильтры и сортировки\n" +
                "Покажи ... - выводит фильмы в соответствии с запросом\n" +
                "Следующий - выводит следующий фильм в соответствии с запросом\n" +
                "Аннотация - выводит аннотацию к фильму\n" +
                "Похожие - выводит 3 фильма, похожих на последний\n" +
                "Скрыть - добавляет фильм в черный список\n"},
                new String[]{"Возможные запросы"});
    }
    private static Answer showingValidRequests(){
        StringBuilder st = new StringBuilder();
        st.append("*Могу найти фильм по названию*\n");
        st.append("*Могу найти*: ");
        for (String movieType : LinkBuilder.getTypeOfMovieDict().keySet()){
            st.append(movieType).append(", ");
        }
        st.deleteCharAt(st.length() - 2);

        st.append("\n*Выводить могу*: ");
        for (String sortingType : LinkBuilder.getSortingTypeDict().keySet()){
            st.append(sortingType).append(", ");
        }
        st.deleteCharAt(st.length() - 2);

        st.append("\n*Знаю такие жанры, как*: ");
        for (String genre : LinkBuilder.getGenreDict().keySet()){
            st.append(genre).append(", ");
        }
        st.deleteCharAt(st.length() - 2);

        st.append("\n*Знаю такие страны, как*: ");
        for (String country : LinkBuilder.getCountriesDict().keySet()){
            st.append(country).append(", ");
        }
        return new Answer(new String[]{st.deleteCharAt(st.length() - 2).toString()});
    }
}
