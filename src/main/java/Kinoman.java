import java.util.*;


class Kinoman{
    String link;
    private int pageCount;
    private int movieCount;
    private int currentPage;
    private int currentMovieListed;
    private String sortingType;
    private Map<Integer, ArrayList<Movie>> pageList = new HashMap<>();
    private TreeMap<Integer, ArrayList<Integer>> shownMovieDict = new TreeMap<>();
    private int shownMovie = 0;

    Kinoman(String typeOfMovie, String sortingType, String... genres){
        currentPage = 1;
        currentMovieListed = -1;
        link = LinkBuilder.getLink(typeOfMovie, sortingType, currentPage, genres);
        movieCount = KinopoiskParser.getMoviesCount(link);
        pageCount = KinopoiskParser.getPageCount(movieCount);
        this.sortingType = sortingType;
    }

    Movie getNext(){
        if (sortingType.equals("рандомный")){
            return getRandomly();
        } else {
            return getInOrder();
        }
    }

    private Movie getInOrder(){
        if (currentPage > pageCount) {
            return null;
        }
        ArrayList<Movie> movies = getMovieList(currentPage);
        if (currentMovieListed + 1 == movies.size()) {
            currentPage++;
            link = LinkBuilder.getNextPage(link);
            currentMovieListed = -1;
            movies = getMovieList(currentPage);
        }
        currentMovieListed += 1;
        return movies.get(currentMovieListed);
    }

    private Movie getRandomly(){
        if (shownMovie == movieCount){
            return null;
        }
        Random rn = new Random();
        while (true){
            currentPage = rn.nextInt(pageCount) + 1;
            link = LinkBuilder.getPageIndexOf(link, currentPage);
            ArrayList<Movie> movies = getMovieList(currentPage);
            currentMovieListed = rn.nextInt(movies.size());
            if (checkShownMovie(currentPage, currentMovieListed)) {
                Movie movie = movies.get(currentMovieListed);
                addToShownMovie(currentPage, currentMovieListed);
                return movie;
            }
        }
    }

    List<Movie> showSimilar(){
        ArrayList<Movie> similarMovies = KinopoiskParser.getSimilarMoviesList(getCurrentMovie().link);
        if (similarMovies.size() > 2) {
            return similarMovies.subList(0, 3);
        } else {
            return null;
        }
    }

    private ArrayList<Movie> getMovieList(int page){
        if (pageList.containsKey(page)){
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

    Movie getCurrentMovie(){
        return pageList.get(currentPage).get(currentMovieListed);
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

        st.append("\n*Выводить могу по*: ");
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

    static Kinoman createKinomanOnRequest(String request){
        String typeOfMovie = "фильм";
        String sortingType = "рандомный";
        ArrayList<String> genre = new ArrayList<>();
        String[] words = request.split(" ");
        for (String word : words){
            if (LinkBuilder.getTypeOfMovieDict().containsKey(word)){
                typeOfMovie = word;
                break;
            }
        }
        for (String word : words){
            if (LinkBuilder.getSortingTypeDict().containsKey(word) || word.startsWith("рандомный")){
                sortingType = word;
                break;
            }
        }
        for (String word : words){
            if (LinkBuilder.getGenreDict().containsKey(word)){
                genre.add(word);
            }
        }
        return new Kinoman(typeOfMovie, sortingType, genre.toArray(new String[0]));
    }
}