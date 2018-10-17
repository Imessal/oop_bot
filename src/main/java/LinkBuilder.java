import java.util.HashMap;
import java.util.Map;

class LinkBuilder {

    static String getLink(String movie, String sortingType, int page, String... genres){
        movie = getTypeOfMovieDict().get(movie);
        sortingType = getSortingTypeDict().get(sortingType);

        String genre = "";
        if (genres.length != 0) {
            StringBuilder strBuild = new StringBuilder();
            HashMap<String, String> genreDict = getGenreDict();
            for (String cGenre : genres){
                strBuild.append('/');
                strBuild.append(genreDict.get(cGenre));
            }
            genre = strBuild.toString();
        }

        return "https://www.kinopoisk.ru/top/lists/"+movie+"/filtr/all/sort/"+sortingType+genre+"/page/"+page;
    }

    static String getNextPage(String link){
        int curPageNumber = Integer.parseInt(link.substring(link.length() - 1));
        return getPageIndexOf(link, ++curPageNumber);
    }

    static String getPageIndexOf(String link, int pageNumber){
        return link.substring(0, link.length() - 1) + pageNumber;
    }

    static String getAlikePageLink(String link){
        return link + "like/";
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
        sortingTypeDict.put("годам", "year");
        sortingTypeDict.put("рейтингу", "rating_imdb");
        sortingTypeDict.put("времени", "runtime");
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
