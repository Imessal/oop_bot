import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;

class KinopoiskParser {

    static ArrayList<Movie> getMoviesList(String link) {
        ArrayList<Movie> movies = new ArrayList<>();
        BufferedReader br = WebsiteOpener.getWebsiteContent(link);
        if (br != null) {
            try {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.startsWith("        <div style=\"margin-bottom: 9px\"><a style=\"font-size: 13px; font-weight: bold\"")) {
                        Movie anim = new Movie();
                        movies.add(anim);
                        anim.url = new URL("https://www.kinopoisk.ru" + inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class")));
                    }
                    if (inputLine.startsWith("             data-film-title")) {
                        movies.get(movies.size() - 1).name = inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1);
                    }
                    if (inputLine.startsWith("             data-film-rating")) {
                        movies.get(movies.size() - 1).rating = inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1);
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    static ArrayList<Movie> getSimilarMoviesList(URL url) {
        url = LinkBuilder.getAlikePageURL(url);
        ArrayList<Movie> movies = new ArrayList<>();
        BufferedReader br = WebsiteOpener.getWebsiteContent(url);
        if (br != null) {
            try {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.startsWith("            <div style=\"margin-bottom: 9px\"><a style=\"font-size: 13px; font-weight: bold\"")) {
                        Movie movie = new Movie();
                        movies.add(movie);
                        movie.url = new URL("https://www.kinopoisk.ru" +
                                inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class")));
                        movie.name = inputLine.substring(inputLine.indexOf("\" class=\"all\">") + 14, inputLine.indexOf("</a><span style="));
                    }
                    if (inputLine.startsWith("               <span class=\"all\" style=\"color: #fff\">")) {
                        int indexForSubstring = inputLine.indexOf(">") + 1;
                        movies.get(movies.size() - 1).rating = inputLine.substring(indexForSubstring, inputLine.indexOf(">") + 6);
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    static String getAnnotation(URL url) {
        BufferedReader br = WebsiteOpener.getWebsiteContent(url);
        if (br != null) {
            String inputLine;
            try {
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.startsWith("    <span class=\"_reachbanner_\"><div class=\"brand_words film-synopsys\" itemprop=\"description\">")) {
                        String annotation = inputLine.substring(94, inputLine.indexOf("</div>"));
                        //annotation = annotation.replaceAll("&#151;", "—");
                        annotation = StringEscapeUtils.unescapeHtml4(annotation);
                        return annotation;
                    }
                }
                br.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    static int getMoviesCount(String link) {
        URL url = WebsiteOpener.stringToURL(link);
        if (url != null) {
            return getMoviesCount(url);
        } else return -1;
    }
    private static int getMoviesCount(URL url) {
        BufferedReader br = WebsiteOpener.getWebsiteContent(url);
        if (br != null) {
            String inputLine;
            try {
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.contains("script")) {
                        continue;
                    }
                    if (inputLine.startsWith("    <div class=\"pagesFromTo")) {
                        //System.out.println("Нашел строку");
                        int a =Integer.parseInt(inputLine.substring(inputLine.indexOf("из ") + 3, inputLine.indexOf("</div>")));
                        //System.out.println(a);
                        return a;
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    static int getPageCount(int moviesCount) {
        if (moviesCount < 25){
            return 1;
        }
        return moviesCount / 25;
    }
    static int getPageCount(String link) {
        URL url = WebsiteOpener.stringToURL(link);
        return getMoviesCount(url) / 25;
    }
}
