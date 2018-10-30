import java.util.ArrayList;
import java.util.List;

class Selector {
    static Answer getAnswer(User user, String request){
        FiniteStateMachine.State state = user.FSM.workWithRequest(request);
        switch (state) {
            case SayHello:
                return new Answer(new String[]{"Привет, я Киноман!\nМогу помочь с выбором фильма, сериала и т.д.\n" +
                        "Для справки введи \"помощь\""},
                        "Помощь", "Возможные запросы");
            case Requesting:
                request = SpellChecker.check(request); //Заменит "случайный" на "рандомый" и т.п.
                user.kinoman = Kinoman.createKinomanOnRequest(request);
                Movie movie = user.kinoman.getNext();
                if (movie == null){
                    return new Answer(new String[]{"Ты пытался найди хоррор мультик или что-то вроде?" +
                            "\nВ любом случае я ничего не нашел."});
                }
                return new Answer(new String[]{movie.toStringMovie()}, "Похожие", "Аннотация", "Следующий");
            case ShowingAnnotation:
                return new Answer(new String[]{user.kinoman.getCurrentMovie().getAnnotation()}, "Похожие", "Следующий");
            case ShowingSimilar:
                List<Movie> similarMovie = user.kinoman.showSimilar();
                if(similarMovie != null) {
                    ArrayList<String> answer = new ArrayList<String>() {
                    };
                    for (Movie curMovie: similarMovie){
                        answer.add(curMovie.toStringMovie());
                    }
                    return new Answer(answer.toArray(new String[0]), "Следующий");
                } else {
                    return new Answer(new String[]{"Я не нашел похожих, сорян"}, "Следующий");
                }
            case ShowingNext:
                movie = user.kinoman.getNext();
                if (movie == null){
                    return new Answer(new String[]{"Это был последний"});
                }
                return new Answer(new String[]{movie.toStringMovie()}, "Похожие", "Аннотация", "Следующий");
            case ShowingHelp:
                return new Answer(new String[]{Kinoman.printHelp()}, "Возможные запросы");
            case ShowingValidRequests:
                return new Answer(new String[]{Kinoman.printValidRequest()});
            default:
                return new Answer(new String[]{"Некорректная команда" +
                        "\nМожет ты что-то не правильно написал?"}, "Помощь", "Возможные запросы");
        }
    }
}
