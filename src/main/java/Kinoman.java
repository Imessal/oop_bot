import java.util.*;


class Kinoman{
    String link;
    private int pageCount;
    private int movieCount;
    private int currentPage;
    private int currentMovie;
    String sortingType;
    private Map<Integer, ArrayList<Movie>> pageList = new HashMap<>();
    private TreeMap<Integer, ArrayList<Integer>> shownMovieDict = new TreeMap<>();
    private int shownMovie = 0;

    Kinoman(String typeOfMovie, String sortingType, String... genres){
        currentPage = 1;
        currentMovie = -1;
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
            System.out.println("Это был последний");
            return null;
        }
        ArrayList<Movie> movies = getMovieList(currentPage, link);
        if (currentMovie == movies.size()) {
            System.out.println("Секундочку...");
            currentPage++;
            link = LinkBuilder.getNextPage(link);
            currentMovie = 0;
        }
        currentMovie += 1;
        return movies.get(currentMovie);
    }

    private Movie getRandomly(){
        if (shownMovie == movieCount){
            System.out.println("Это был последний");
            return null;
        }
        Random rn = new Random();
        while (true){
            currentPage = rn.nextInt(pageCount) + 1;
            link = LinkBuilder.getPageIndexOf(link, currentPage);
            ArrayList<Movie> movies = getMovieList(currentPage, link);
            currentMovie = rn.nextInt(movies.size());
            if (checkShownMovie(currentPage, currentMovie)) {
                Movie movie = movies.get(currentMovie);
                //movie.addAnnotation();
                addToShownMovie(currentPage, currentMovie);
                return movie;
            }
        }
    }

    List<Movie> showSimilar(){
        ArrayList<Movie> similarMovies = KinopoiskParser.getSimilarMoviesList(getCurrentMovie().link);
        if (similarMovies.size() > 2) {
            return similarMovies.subList(0, 3);
        } else {
            System.out.println("Похожих не оказалось");
            return null;
        }
    }

    private ArrayList<Movie> getMovieList(int page, String link){
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

    private Movie getCurrentMovie(){
        return pageList.get(currentPage).get(currentMovie);
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