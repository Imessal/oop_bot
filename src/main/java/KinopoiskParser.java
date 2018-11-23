import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.logging.Logger;

class KinopoiskParser {
    static ArrayList<Movie> getMoviesList(String link) {
        ArrayList<Movie> movies = new ArrayList<>();
        try(BufferedReader br = WebsiteOpener.getWebsiteContent(link))
        {
            assert br != null : "Не удалось открыть страницу - " + link;
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.startsWith("<tr id=")) { //Нашли блок с инофой о фильме
                    String name = null;
                    int id = 0;
                    String m_link = null;
                    String year = null;
                    String rating = null;

                    while (!inputLine.startsWith("</tr>")) {
                        if (inputLine.startsWith("        <div style=\"margin-bottom: 9px\"><a style=\"font-size: 13px; font-weight: bold\"")) {
                            m_link = "https://www.kinopoisk.ru" + inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class"));
                            name = StringEscapeUtils.unescapeHtml4(inputLine.substring(inputLine.indexOf("\"all\">") + 6, inputLine.indexOf("</a>")));
                            try {
                                year = StringEscapeUtils.unescapeHtml4(
                                        inputLine.substring(inputLine.indexOf("(") + 1, inputLine.indexOf(")")));
                            } catch (java.lang.StringIndexOutOfBoundsException E) {
                                Bot.log.config("У фильма нет года");
                            }
                        }
                        if (inputLine.startsWith("             data-film-rating")) {
                            rating = inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1);
                        }
                        if (inputLine.startsWith("    data-kp-film-id")) {
                            id = Integer.parseInt(inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1));
                        }
                        inputLine = br.readLine();
                    }
                    if (name!=null && id>0){
                        Movie movie = new Movie(name, id, m_link);
                        movie.setRating(rating);
                        movie.setYear(year);
                        movies.add(movie);
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    static ArrayList<Movie> getMoviesListCalled(String link){
        ArrayList<Movie> movies = new ArrayList<>();
        try(BufferedReader br = WebsiteOpener.getWebsiteContent(link))
        {
            assert br != null : "Не удалось открыть страницу - " + link;
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.startsWith("     <p class=\"name\">")) {
                    String name = inputLine.substring(inputLine.indexOf("data-type="), inputLine.indexOf("</a>"));
                    name = StringEscapeUtils.unescapeHtml4(name.substring(name.indexOf(">") + 1));
                    String m_link = "https://www.kinopoisk.ru" +
                            inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class") - 5);
                    String s_id = inputLine.substring(inputLine.indexOf("data-id=\"")+9);
                    int id = Integer.parseInt(s_id.substring(0, s_id.indexOf("\"")));
                    String year = null;
                    try {
                        year = StringEscapeUtils.unescapeHtml4(
                                inputLine.substring(inputLine.indexOf("\"year\">") + 7, inputLine.indexOf("</span>")));
                    }catch (java.lang.StringIndexOutOfBoundsException E){
                        Bot.log.config("У фильма нет года");
                    }
                    Movie movie = new Movie(name, id, m_link);
                    movie.setYear(year);
                    movies.add(movie);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    static ArrayList<Movie> getSimilarMoviesList(String link) {
        link = LinkBuilder.getAlikePageLink(link);
        ArrayList<Movie> movies = new ArrayList<>();
        try (BufferedReader br = WebsiteOpener.getWebsiteContent(link)) {
            assert br != null : "Не удалось открыть страницу - " + link;
            String inputLine;
            int counter = 0;
            while ((inputLine = br.readLine()) != null && counter < 3) {
                if (inputLine.startsWith("<tr id=")) { //Нашли блок с инофой о фильме
                    String name = null;
                    String m_link = null;
                    String year = null;
                    String rating = null;
                    int id = Integer.parseInt(inputLine.substring(inputLine.indexOf("\"")+4, inputLine.indexOf("\" class")));

                    while (!inputLine.startsWith("</tr>")) {
                        if (inputLine.startsWith("            <div style=\"margin-bottom:")) {
                            m_link = "https://www.kinopoisk.ru" +
                                    inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class"));
                            name = StringEscapeUtils.unescapeHtml4(
                                    inputLine.substring(inputLine.indexOf("\" class=\"all\">") + 14, inputLine.indexOf("</a><span style=")));
                        }
                        if (inputLine.startsWith("               <span class=\"all\" style=\"color: #fff\">")) {
                            int indexForSubstring = inputLine.indexOf(">") + 1;
                            rating = inputLine.substring(indexForSubstring, inputLine.indexOf(">") + 6);
                        }
                        inputLine = br.readLine();
                    }
                    if(name!=null){
                        Movie movie = new Movie(name, id, m_link);
                        movie.setRating(rating);
                        movies.add(movie);
                        counter++;
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    static String getAnnotation(String link) {
        Bot.log.config("Ищу аннотацию");
        String inputLine;
        try (BufferedReader br = WebsiteOpener.getWebsiteContent(link)) {
            assert br != null : "Не удалось открыть страницу - " + link;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.startsWith("    <span class=\"_reachbanner_\"><div class=\"brand_words film-synopsys\" itemprop=\"description\">")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(inputLine);
                    while (!inputLine.contains("</div>")) {
                        inputLine = br.readLine();
                        sb.append(inputLine);
                    }
                    String annotation = sb.toString()
                            .substring(94, inputLine.indexOf("</div>"))
                            .replaceAll("<br>", "")
                            .replaceAll("<br />", "")
                            .replaceAll("`", "\\`");
                    Bot.log.config("Нашел - " + annotation);
                    return StringEscapeUtils.unescapeHtml4(annotation);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        Bot.log.config("Не нашел");
        return "У этого фильма нет аннотации";
    }

    static int getMoviesCount(String link) {
        String inputLine;
        try (BufferedReader br = WebsiteOpener.getWebsiteContent(link)) {
            assert br != null : "Не удалось открыть страницу - " + link;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.contains("script")) {
                    continue;
                }
                if (inputLine.startsWith("    <div class=\"pagesFromTo")) {
                    return Integer.parseInt(inputLine.substring(inputLine.indexOf("из ") + 3, inputLine.indexOf("</div>")));
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static int getPageCount(int moviesCount) {
        if (moviesCount < 25){
            return 1;
        }
        return moviesCount / 25;
    }
}
