import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.logging.Logger;

class KinopoiskParser {
    private static Logger log = Bot.log;

    static ArrayList<Movie> getMoviesList(String link) {
        ArrayList<Movie> movies = new ArrayList<>();
        try(BufferedReader br = WebsiteOpener.getWebsiteContent(link))
        {
            assert br != null : "Не удалось открыть страницу - " + link;
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.startsWith("        <div style=\"margin-bottom: 9px\"><a style=\"font-size: 13px; font-weight: bold\"")) {
                    Movie movie = new Movie();
                    movies.add(movie);
                    movie.link = "https://www.kinopoisk.ru" + inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class"));
                    String name = inputLine.substring(inputLine.indexOf("\"all\">") + 6, inputLine.indexOf("</a>"));
                    movie.name = StringEscapeUtils.unescapeHtml4(name);
                    try {
                        movie.year = StringEscapeUtils.unescapeHtml4(
                                inputLine.substring(inputLine.indexOf("(") + 1, inputLine.indexOf(")")));
                    }catch (java.lang.StringIndexOutOfBoundsException E){
                        log.config("у фильма - "+movie.name+", нет года");
                    }
                }
//                if (inputLine.startsWith("             data-film-title")) {
//                    String name = inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1);
//                    movies.get(movies.size() - 1).name = StringEscapeUtils.unescapeHtml4(name);
//                }
                if (inputLine.startsWith("             data-film-rating")) {
                    movies.get(movies.size() - 1).rating = inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1);
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
                    Movie movie = new Movie();
                    movies.add(movie);
                    movie.link = "https://www.kinopoisk.ru" +
                            inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class") - 5);
                    String name = inputLine.substring(inputLine.indexOf("data-type="), inputLine.indexOf("</a>"));
                    name = name.substring(name.indexOf(">") + 1);
                    movie.name = StringEscapeUtils.unescapeHtml4(name);
                    try {
                        movie.year = StringEscapeUtils.unescapeHtml4(
                                inputLine.substring(inputLine.indexOf("\"year\">") + 7, inputLine.indexOf("</span>")));
                    }catch (java.lang.StringIndexOutOfBoundsException E){
                        log.config("у фильма - "+movie.name+", нет года");
                    }
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
            if (br == null){
                return null;
            }
            String inputLine;
            int counter = 0;
            while ((inputLine = br.readLine()) != null && counter < 3) {
                if (inputLine.startsWith("            <div style=\"margin-bottom: 9px\"><a style=\"font-size: 13px; font-weight: bold\"")) {
                    Movie movie = new Movie();
                    movies.add(movie);
                    movie.link = "https://www.kinopoisk.ru" +
                            inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class"));
                    String name = inputLine.substring(inputLine.indexOf("\" class=\"all\">") + 14, inputLine.indexOf("</a><span style="));
                    movie.name = StringEscapeUtils.unescapeHtml4(name);
                }
                if (inputLine.startsWith("               <span class=\"all\" style=\"color: #fff\">")) {
                    int indexForSubstring = inputLine.indexOf(">") + 1;
                    movies.get(movies.size() - 1).rating = inputLine.substring(indexForSubstring, inputLine.indexOf(">") + 6);
                    counter++;
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    static String getAnnotation(String link) {
        log.config("ищу аннотацию");
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
                    log.config("нашел - " + annotation);
                    return StringEscapeUtils.unescapeHtml4(annotation);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        log.config("не нашел");
        return "У этого фильма нет аннотации";
    }

    //Возвращает ссылку на постер к фильму https://....jpg
    static String getImage(String link){
        String inputLine;
        try (BufferedReader br = WebsiteOpener.getWebsiteContent(link)) {
            assert br != null : "Не удалось открыть страницу - " + link;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.startsWith("                        <img width=\"205\"")) {
                    String result = inputLine.substring(inputLine.indexOf("src=") + 5, inputLine.indexOf("jpg") + 3);
                    result = result.replaceAll("_", "\\\\_");
                    return result;
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
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
