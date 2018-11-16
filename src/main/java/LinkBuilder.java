import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.logging.Logger;

class LinkBuilder {
    private static final Logger log = Bot.log;

    static String getLink(String movie, String sortingType, int page, String[] genres, String[] countries){
        StringBuilder link = new StringBuilder();
        link.append("https://www.kinopoisk.ru/top/lists/");
        link.append(getTypeOfMovieDict().get(movie));
        link.append("/filtr/all/sort/");
        link.append(getSortingTypeDict().get(sortingType));

        if (genres.length != 0) {
            HashMap<String, String> genreDict = getGenreDict();
            for (String cGenre : genres){
                link.append('/');
                link.append(genreDict.get(cGenre));
            }
        }

        if (countries.length != 0) {
            HashMap<String, String> countriesDict = getCountriesDict();
            for (String cCountry : countries){
                link.append('/');
                link.append(countriesDict.get(cCountry));
            }
        }

        link.append("/page/");
        link.append(page);

        log.config("link - " + link);
        return link.toString();
    }

    static String getLink(String movieName){
        try {
            movieName = URLEncoder.encode(movieName, "windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "https://www.kinopoisk.ru/s/type/film/list/1/find/"+movieName+"/order/relevant/page/1";
        //      https://www.kinopoisk.ru/index.php?kp_query=%EC%E0%F2%F0%E8%F6%E0&what=
        //      https://www.kinopoisk.ru/index.php?kp_query=%E4%E6%E8%EC+%EA%E5%F0%F0%E8&what=
    }

    static String getNextPage(String link){
        int curPageNumber = Integer.parseInt(link.substring(link.lastIndexOf("/")+1));
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

    static HashMap<String, String> getCountriesDict(){
        HashMap<String, String> movieDict = new HashMap<>();
        movieDict.put("великобритания", "country[11]/11");
        movieDict.put("германия", "country[3]/3");
        movieDict.put("италия", "country[14]/14");
        movieDict.put("ссср", "country[13]/13");
        movieDict.put("россия", "country[2]/2");
        movieDict.put("сша", "country[1]/1");
        movieDict.put("франция", "country[8]/8");
        movieDict.put("япония", "country[9]/9");
        movieDict.put("китай", "country[31]/31");
        return movieDict;
    }
}
