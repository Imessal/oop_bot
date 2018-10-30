import java.util.List;

class Elector {
    static Answer getAnswer(User user, String request){
        FiniteStateMachine.State state = user.FSM.workWithRequest(request);
        switch (state) {
            case SayHello:
                return new Answer("Привет, я Киноман!\nМогу помочь с выбором фильма, сериала и т.д.\n" +
                        "Для справки введи \"помощь\"",
                        "Помощь", "Возможные запросы");
            case Requesting:
                request = SpellChecker.check(request); //Заменит "случайный" на "рандомый" и т.п.
                user.kinoman = Kinoman.createKinomanOnRequest(request);
                Movie movie = user.kinoman.getNext();
                if (movie == null){
                    return new Answer("Ты пытался найди хоррор мультик или что-то вроде?" +
                            "\nВ любом случае я ничего не нашел.");
                }
                return new Answer(movie.toStringMovie(), "Похожие", "Аннотация", "Следующий");
            case ShowingAnnotation:
                return new Answer(user.kinoman.getCurrentMovie().getAnnotation(), "Похожие", "Следующий");
            case ShowingSimilar:
                List<Movie> similarMovie = user.kinoman.showSimilar();
                if(similarMovie != null) {
                    return new Answer(user.kinoman.showSimilar().get(0).toStringMovie(), "Следующий");
                } else {
                    return new Answer("Я не нашел похожих, сорян", "Следующий");
                }
            case ShowingNext:
                movie = user.kinoman.getNext();
                if (movie == null){
                    return new Answer("Это был последний");
                }
                return new Answer(movie.toStringMovie(), "Похожие", "Аннотация", "Следующий");
            case ShowingHelp:
                return new Answer(Kinoman.printHelp(), "Возможные запросы");
            case ShowingValidRequests:
                return new Answer(Kinoman.printValidRequest());
            default:
                return new Answer("Некорректная команда" +
                        "\nМожет ты что-то не правильно написал?", "Помощь", "Возможные запросы");
        }
    }
}
