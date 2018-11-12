import java.util.*;
import java.util.logging.Logger;

import static org.apache.commons.lang3.ArrayUtils.toArray;


class Kinoman{
    private static Logger log = Bot.log;
    private static DatabaseController controller = TelegramBot.controller;
    private User user;
    String link;
    private int pageCount;
    private int movieCount;
    private int currentPage;
    private int currentMovieListed;
    private String movieTitle;
    private String sortingType;
    Movie currentMovie;
    private Map<Integer, ArrayList<Movie>> pageList = new HashMap<>();
    private TreeMap<Integer, ArrayList<Integer>> shownMovieDict = new TreeMap<>();
    private int shownMovie = 0;

    Kinoman(User user, String typeOfMovie, String sortingType, String[] genres, String[] countries){
        this.user = user;
        currentPage = 1;
        currentMovieListed = -1;
        link = LinkBuilder.getLink(typeOfMovie, sortingType, currentPage, genres, countries);
        movieCount = KinopoiskParser.getMoviesCount(link);
        pageCount = KinopoiskParser.getPageCount(movieCount);
        this.sortingType = sortingType;
    }

    private Kinoman(User user, String movieTitle){
        this.user = user;
        this.movieTitle = movieTitle;
        link = LinkBuilder.getLink(movieTitle);
        currentMovieListed = -1;
        currentPage = 1;
    }

    Movie getNext(){
        log.config("Search next");
        if (movieTitle != null) {
            return getMovieCalled();
        }else if (sortingType.equals("рандомный")){
            return getRandomly();
        } else {
            return getInOrder();
        }
    }

    private Movie getMovieCalled() {
        log.config("Начал парсить");
        ArrayList<Movie> movies = getMovieList(currentPage);
        log.config("Закончил");
        if (currentMovieListed + 1 == movies.size()) {
            return null;
        }
        currentMovieListed += 1;
        Movie movie = movies.get(currentMovieListed);
        currentMovie = movie;
        return movie;
    }

    private Movie getInOrder(){
        if (currentPage > pageCount) {
            return null;
        }
        log.config("Начал парсить");
        ArrayList<Movie> movies = getMovieList(currentPage);
        log.config("закончил");
        if (currentMovieListed + 1 == movies.size()) {
            currentPage++;
            link = LinkBuilder.getNextPage(link);
            currentMovieListed = -1;
            movies = getMovieList(currentPage);
        }
        currentMovieListed += 1;
        Movie movie = movies.get(currentMovieListed);
        currentMovie = movie;
        if (controller.checkMovie(user, movie)) {
            return movie;
        }else {
            return getInOrder();
        }
    }

    private Movie getRandomly(){
        if (shownMovie == movieCount){
            return null;
        }
        Random rn = new Random();
        while (true){
            currentPage = rn.nextInt(pageCount) + 1;
            link = LinkBuilder.getPageIndexOf(link, currentPage);
            log.config("Начал парсить");
            ArrayList<Movie> movies = getMovieList(currentPage);
            log.config("закончил");
            currentMovieListed = rn.nextInt(movies.size());
            if (checkShownMovie(currentPage, currentMovieListed)) {
                Movie movie = movies.get(currentMovieListed);
                addToShownMovie(currentPage, currentMovieListed);
                currentMovie = movie;
                if (controller.checkMovie(user, movie)) {
                    return movie;
                }else {
                    return getRandomly();
                }
            }
        }
    }

    List<Movie> showSimilar(){
        ArrayList<Movie> similarMovies = KinopoiskParser.getSimilarMoviesList(currentMovie.link);
        if (similarMovies != null && similarMovies.size() > 2) {
            return similarMovies.subList(0, 3);
        } else {
            return null;
        }
    }

    private ArrayList<Movie> getMovieList(int page){
        if (pageList.containsKey(page)){
            return pageList.get(page);
        } else if (movieTitle != null) {
            pageList.put(page, KinopoiskParser.getMoviesListCalled(link));
            return pageList.get(page);
        } else {
            pageList.put(page, KinopoiskParser.getMoviesList(link));
            return pageList.get(page);
        }
    }

    private boolean checkShownMovie(int curPage, int curMovie){
        if (shownMovieDict.containsKey(curPage)){
            return !shownMovieDict.get(curPage).contains(curMovie);
        }
        return true;
    }

    private void addToShownMovie(int curPage, int curMovie){
        shownMovie++;
        if (shownMovieDict.containsKey(curPage)){
            shownMovieDict.get(curPage).add(curMovie);
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(curMovie);
            shownMovieDict.put(curPage, list);
        }
    }

    static String printHelp(){
        return "Просто напиши - \"Покажи\" + что хочешь посмотреть. Так же можешь указать жанр и способ сортировки\n" +
                "\nПример запроса:\n" +
                "Покажи мне мультфильм, и выводи их по годам, пожалуйста\n\n" +
                "После вывода фильма, можно попросить показать \"похожие\"\n" +
                "Чтобы продолжить вывод, напиши \"следующий\" или введи новый запрос\n" +
                "\nЯ еще не умею искать фильмы по названиям или по актерам, но скоро научусь)\n"+
                "Чтобы узнать знакомые мне жанры, сортировки вывода и т.п, напиши \"возможные запросы\"\n";
    }

    static String printValidRequest(){
        StringBuilder st = new StringBuilder();
        st.append("*Могу найти*: ");
        for (String movie : LinkBuilder.getTypeOfMovieDict().keySet()){
            st.append(movie);
            st.append(", ");
        }
        st.deleteCharAt(st.length() - 2);

        st.append("\n*Выводить могу*: ");
        for (String sortingType : LinkBuilder.getSortingTypeDict().keySet()){
            st.append(sortingType);
            st.append(", ");
        }
        st.deleteCharAt(st.length() - 2);

        st.append("\n*Знаю такие жанры, как*: ");
        for (String genre : LinkBuilder.getGenreDict().keySet()){
            st.append(genre);
            st.append(", ");
        }
        return st.deleteCharAt(st.length() - 2).toString();
    }

    static Kinoman createKinomanOnRequest(User user, String request){
        StringBuilder movieTitle = new StringBuilder();
        String typeOfMovie = "фильм";
        String sortingType = "рандомный";
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<String> countries = new ArrayList<>();

        for (String word : splitRequest(request)) {
            if (word.equals("покажи")){
                continue;
            }
            if (LinkBuilder.getTypeOfMovieDict().containsKey(word)) {
                typeOfMovie = word;
                continue;
            }
            if (LinkBuilder.getSortingTypeDict().containsKey(word) || word.startsWith("рандомный")) {
                sortingType = word;
                continue;
            }
            if (LinkBuilder.getGenreDict().containsKey(word)) {
                genres.add(word);
                continue;
            }
            if (LinkBuilder.getCountriesDict().containsKey(word)){
                countries.add(word);
                continue;
            }
            movieTitle.append(word).append(" ");
        }

        if (movieTitle.toString().length() == 0){
            log.config("Вывожу: movie - " + typeOfMovie
                    + ", sorting - " + sortingType
                    + ", genres - " + genres
                    + ", countries - " + countries);
            return new Kinoman(user, typeOfMovie, sortingType, genres.toArray(new String[0]), countries.toArray(new String[0]));
        }else {
            log.config("Вывожу по названию - " + movieTitle);
            return new Kinoman(user, movieTitle.toString());
        }
    }

    private static ArrayList<String> splitRequest(String request){
        char[] chars = request.toCharArray();
        StringBuilder word = new StringBuilder();
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < chars.length; i++){
            if(i+1 == chars.length){
                word.append(chars[i]);
                words.add(word.toString());
            }else if (chars[i] == 32 && !word.toString().equals("по")){
                words.add(word.toString());
                word.delete(0, word.length());
            }else {
                word.append(chars[i]);
            }
        }
        return words;
    }
}