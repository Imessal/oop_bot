import java.util.*;


class Kinoman{
    private User user;
    private String link = null;
    private int pageCount = 0;
    private int movieCount = 0;
    private int currentPage = 1;
    private int currentMovieNumber = 0;
    private String movieTitle = null;
    private String sortingType = null;
    private Movie currentMovie = null;
    private Map<Integer, ArrayList<Movie>> pageList = new HashMap<>();
    private ArrayList<Integer> shownMovieList = new ArrayList<>();

    Kinoman(User user, String request){
        this.user = user;
        setFields(request);
    }

    Movie getNext(){
        if (movieTitle != null) {
            return getMovieCalled();
        }else if (sortingType.equals("рандомный")){
            return getRandomly();
        } else {
            return getInOrder();
        }
    }

    private Movie getMovieCalled() {
        Bot.log.config("Начал парсить по названию");
        ArrayList<Movie> movies = getMoviesListOnPage(currentPage);
        Bot.log.config("Закончил");
        if (currentMovieNumber == movies.size()) {
            return null;
        }
        Movie movie = movies.get(currentMovieNumber);
        currentMovie = movie;
        currentMovieNumber ++;
        return movie;
    }

    private Movie getInOrder(){
        while (true) {
            if (currentPage > pageCount) {
                return null;
            }
            ArrayList<Movie> movies = getMoviesListOnPage(currentPage);
            Movie movie = movies.get(currentMovieNumber);
            currentMovie = movie;
            currentMovieNumber ++;
            if (currentMovieNumber == movies.size()) {
                currentPage++;
                link = LinkBuilder.getNextPage(link);
                currentMovieNumber = 0;
            }
            if (Bot.repository.checkMovie(user.getId(), movie.getId())) {
                return movie;
            }
        }
    }

    private Movie getRandomly(){
        Random rn = new Random();
        while (true){
            if (shownMovieList.size() == movieCount){
                return null;
            }
            currentPage = rn.nextInt(pageCount) + 1;
            link = LinkBuilder.getPageIndexOf(link, currentPage);
            Bot.log.config("Начал парсить рандомно");
            ArrayList<Movie> movies = getMoviesListOnPage(currentPage);
            Bot.log.config("закончил");
            currentMovieNumber = rn.nextInt(movies.size());
            Movie movie = movies.get(currentMovieNumber);
            if (!shownMovieList.contains(movie.getId())) {
                shownMovieList.add(movie.getId());
                currentMovie = movie;
                if (Bot.repository.checkMovie(user.getId(), movie.getId())) {
                    return movie;
                }
            }
        }
    }

    Movie getCurrentMovie(){
        return currentMovie;
    }

    List<Movie> showSimilar(){
        ArrayList<Movie> similarMovies = KinopoiskParser.getSimilarMoviesList(currentMovie.getLink());
        if (similarMovies != null && similarMovies.size() > 2) {
            return similarMovies.subList(0, 3);
        } else {
            return null;
        }
    }

    private ArrayList<Movie> getMoviesListOnPage(int page){
        if (pageList.containsKey(page)){
            return pageList.get(page);
        } else if (movieTitle != null) {
            Bot.log.config("Начал парсить");
            pageList.put(page, KinopoiskParser.getMoviesListCalled(link));
            Bot.log.config("Закончил");
            return pageList.get(page);
        } else {
            Bot.log.config("Начал парсить");
            pageList.put(page, KinopoiskParser.getMoviesList(link));
            Bot.log.config("Закончил");
            return pageList.get(page);
        }
    }

    private void setFields(String request){
        link = getLink(request);
        movieCount = KinopoiskParser.getMoviesCount(link);
        pageCount = KinopoiskParser.getPageCount(movieCount);
/*------movieTitle и sortingType задаются в getLink(), нужно исправить------*/
    }

    private String getLink(String request){
        StringBuilder movieTitle = new StringBuilder();
        String typeOfMovie = "фильм";
        sortingType = "рандомный";
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
            if (LinkBuilder.getSortingTypeDict().containsKey(word)) {
                this.sortingType = word;
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
            return LinkBuilder.getLink(typeOfMovie, sortingType, 1, genres.toArray(new String[0]), countries.toArray(new String[0]));
        }else {
            this.movieTitle = movieTitle.toString().trim();
            return LinkBuilder.getLink(movieTitle.toString());
        }
    }

    static ArrayList<String> splitRequest(String request){
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