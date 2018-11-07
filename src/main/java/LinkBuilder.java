import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.logging.Logger;

class LinkBuilder {
    private static final Logger log = Bot.log;

    static String getLink(String movie, String sortingType, int page, String... genres){
        movie = getTypeOfMovieDict().get(movie);
        sortingType = getSortingTypeDict().get(sortingType);

        StringBuilder genresBuild = new StringBuilder();
        if (genres.length != 0) {
            HashMap<String, String> genreDict = getGenreDict();
            for (String cGenre : genres){
                genresBuild.append('/');
                genresBuild.append(genreDict.get(cGenre));
            }
        }

        String link = "https://www.kinopoisk.ru/top/lists/" +
                movie + "/filtr/all/sort/" +
                sortingType+genresBuild.toString() +
                "/page/" + page;
        log.config("link - " + link);
        return link;
    }

    static String getLink(String movieName){
        try {
            movieName = URLEncoder.encode(movieName, "windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "https://www.kinopoisk.ru/s/type/film/list/1/find/"+movieName+"/order/relevant/page/1";
    }

    static String getNextPage(String link){
        int curPageNumber = Integer.parseInt(link.substring(link.length() - 1));
        link = getPageIndexOf(link, ++curPageNumber);
        log.config("linkToNextPage - " + link);
        return link;
    }

    static String getPageIndexOf(String link, int pageNumber){
        link = link.substring(0, link.lastIndexOf("/") + 1) + pageNumber;
        log.config("linkToPageIndexOf " + pageNumber+ " - " + link);
        return link;
    }

    static String getAlikePageLink(String link){
        link = link + "like/";
        log.config("linkAlikePageLink - " + link);
        return link;
    }

    static HashMap<String, String> getTypeOfMovieDict(){
        HashMap<String, String> movieDict = new HashMap<>();
        movieDict.put("фильм", "1");
        movieDict.put("сериал", "45");
        movieDict.put("мультфильм", "13");
        movieDict.put("мультсериал", "6");
        movieDict.put("аниме", "240");
        movieDict.put("аниме-сериал", "241");
        return movieDict;
    }

    static HashMap<String, String> getSortingTypeDict(){
        HashMap<String, String> sortingTypeDict = new HashMap<>();
        sortingTypeDict.put("по годам", "year");
        sortingTypeDict.put("по рейтингу", "rating_imdb");
        sortingTypeDict.put("по времени", "runtime");
        return sortingTypeDict;
    }

    static HashMap<String, String> getGenreDict(){
        HashMap<String, String> genreDict = new HashMap<>();
        genreDict.put("комедия", "genre[6]/6");
        genreDict.put("боевик", "genre[3]/3");
        genreDict.put("приключения", "genre[10]/10");
        genreDict.put("ужас", "genre[1]/1");
        genreDict.put("фантастика", "genre[2]/2");
        genreDict.put("фэнтези", "genre[5]/5");
        return genreDict;
    }
}
