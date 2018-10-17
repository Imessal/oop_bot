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
        if (currentPage > pageCount){
            System.out.println("Это был последний(");
            return;
        }
        ArrayList<Movie> movies = getMovieList(currentPage, link);
        currentMovie += 1;
        Movie movie = movies.get(currentMovie);
        movie.addAnnotation();
        System.out.println("\n"+movie.toStringMovie()+"\n");
        if (currentMovie == movies.size()){
            currentPage++;
            link = LinkBuilder.getNextPage(link);
            currentMovie = 0;
        }
    }

    private void showRandomly() {
        if (shownMovie.size() == movieCount){
            System.out.println("Это был последний(");
            return;
        }
        Random rn = new Random();
        while (true) {
            currentPage = rn.nextInt(pageCount) + 1;
            link = LinkBuilder.getPageIndexOf(link, currentPage);
            ArrayList<Movie> movies = getMovieList(currentPage, link);
            currentMovie = rn.nextInt(movies.size());
            if(checkShownMovie(currentPage, currentMovie)) {
                Movie movie = movies.get(currentMovie);
                movie.addAnnotation();
                System.out.println("\n"+movie.toStringMovie()+"\n");
                addToShownMovie(currentPage, currentMovie);
                break;
            }
        }
    }

    void showSimilar(){
        ArrayList<Movie> similarMovies = KinopoiskParser.getSimilarMoviesList(getCurrentMovie().link);
        System.out.println();
        if (similarMovies.size() != 0){
            for (int i = 0; i <= 2; i++){
                similarMovies.get(i).addAnnotation();
                System.out.println(similarMovies.get(i).toStringMovie()+"\n");
            }
        } else {
            System.out.println("Похожих не оказалось(");
        }
    }

    private ArrayList<Movie> getMovieList(int page, String link) {
        if (pageList.containsKey(page)) {
            return pageList.get(page);
        } else {
            pageList.put(page, KinopoiskParser.getMoviesList(link));
            return pageList.get(page);
        }
    }

    private boolean checkShownMovie(int curPage, int curMovie) {
        if (shownMovie.containsKey(curPage)) {
            return !shownMovie.get(curPage).contains(curMovie);
        }
        return true;
    }

    private void addToShownMovie(int curPage, int curMovie){
        if (shownMovie.containsKey(curPage)){
            shownMovie.get(curPage).add(curMovie);
        }else {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(curMovie);
            shownMovie.put(curPage, list);
        }
    }

    private Movie getCurrentMovie(){
        return pageList.get(currentPage).get(currentMovie);
    }
}