import java.util.*;


class Kinoman{
    String link;
    private int pageCount;
    private int movieCount;
    private int currentPage;
    private int currentMovie;
    private String sortingType;
    private Map<Integer, ArrayList<Movie>> pageList = new HashMap<>();
    private TreeMap<Integer, ArrayList<Integer>> shownMovie = new TreeMap<>();

    Kinoman(String typeOfMovie, String sortingType, String... genres){
        currentPage = 1;
        currentMovie = -1;
        link = LinkBuilder.getLink(typeOfMovie, sortingType, currentPage, genres);
        movieCount = KinopoiskParser.getMoviesCount(link);
        pageCount = KinopoiskParser.getPageCount(movieCount);
        this.sortingType = sortingType;
    }

    void showNext(){
        if (sortingType.equals("рандомный")){
            showRandomly();
        } else {
            showInOrder();
        }
    }

    private void showInOrder(){
        if (currentPage > pageCount) {
            System.out.println("Это был последний(");
            return;
        }
        ArrayList<Movie> movies = getMovieList(currentPage, link);
        currentMovie += 1;
        Movie movie = movies.get(currentMovie);
        movie.addAnnotation();
        System.out.println("\n" + movie.toStringMovie() + "\n");
        if (currentMovie == movies.size()) {
            currentPage++;
            link = LinkBuilder.getNextPage(link);
            currentMovie = 0;
        }
    }

    private void showRandomly(){
        if (shownMovie.size() == movieCount){
            System.out.println("Это был последний(");
            return;
        }
        Random rn = new Random();
        while (true){
            currentPage = rn.nextInt(pageCount) + 1;
            link = LinkBuilder.getPageIndexOf(link, currentPage);
            ArrayList<Movie> movies = getMovieList(currentPage, link);
            currentMovie = rn.nextInt(movies.size());
            if (checkShownMovie(currentPage, currentMovie)) {
                Movie movie = movies.get(currentMovie);
                movie.addAnnotation();
                System.out.println("\n" + movie.toStringMovie() + "\n");
                addToShownMovie(currentPage, currentMovie);
                break;
            }
        }
    }

    void showSimilar(){
        ArrayList<Movie> similarMovies = KinopoiskParser.getSimilarMoviesList(getCurrentMovie().link);
        System.out.println();
        if (similarMovies.size() > 2) {
            for (int i = 0; i <= 2; i++) {
                similarMovies.get(i).addAnnotation();
                System.out.println(similarMovies.get(i).toStringMovie() + "\n");
            }
        } else {
            System.out.println("Похожих не оказалось(");
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
        if (shownMovie.containsKey(curPage)){
            return !shownMovie.get(curPage).contains(curMovie);
        }
        return true;
    }

    private void addToShownMovie(int curPage, int curMovie){
        if (shownMovie.containsKey(curPage)){
            shownMovie.get(curPage).add(curMovie);
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(curMovie);
            shownMovie.put(curPage, list);
        }
    }

    private Movie getCurrentMovie(){
        return pageList.get(currentPage).get(currentMovie);
    }

    static void printHelp(){
        System.out.println("Просто напиши - \"Покажи\" + что хочешь посмотреть. Так же можешь указать жанр и способ сортировки");
        System.out.println("\nПример запроса:");
        System.out.println("Покажи мне мультфильм, и выводи их по годам, пожалуйста\n");
        System.out.println("После вывода фильма, можно попросить показать \"похожие\"");
        System.out.println("Чтобы продолжить вывод, напиши \"следующий\" или введи новый запрос");
        System.out.println("Чтобы узнать знакомые мне жанры, сортировки вывода и т.п, напиши \"возможные запросы\"");
    }

    static void printValidRequest(){
        System.out.print("Могу найти: ");
        StringBuilder strBuild = new StringBuilder();
        for (String movie : LinkBuilder.getTypeOfMovieDict().keySet()){
            strBuild.append(movie);
            strBuild.append(", ");
        }
        System.out.println(strBuild.deleteCharAt(strBuild.length() - 2).toString());

        System.out.print("Выводить могу по: ");
        strBuild.delete(0, strBuild.length());
        for (String sortingType : LinkBuilder.getSortingTypeDict().keySet()){
            strBuild.append(sortingType);
            strBuild.append(", ");
        }
        System.out.println(strBuild.deleteCharAt(strBuild.length() - 2).toString());

        System.out.print("Знаю такие жанры, как: ");
        strBuild.delete(0, strBuild.length());
        for (String genre : LinkBuilder.getGenreDict().keySet()){
            strBuild.append(genre);
            strBuild.append(", ");
        }
        System.out.println(strBuild.deleteCharAt(strBuild.length() - 2).toString());
    }

    static Kinoman createKinomanOnRequest(String request){
        String typeOfMovie = "фильм";
        String sortingType = "годам";
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