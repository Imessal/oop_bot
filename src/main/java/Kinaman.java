import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


class Kinoman {
    private String m_link;
    private Map<Integer, ArrayList<Animation>> pageList = new HashMap<>();
    private int m_pageCount;
    private int m_currentPage;
    private int m_currentAnim;
    private String m_sortingType;
    private TreeMap<Integer, ArrayList<Integer>> m_shownAnim = new TreeMap<>();

    Kinoman(String typeOfAnim, String sortingType){
        m_currentPage = 1;
        m_currentAnim = -1;
        switch (typeOfAnim) {
            case "фильм":
                m_link = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/rating_imdb/page/1/";
                break;
            case "сериал":
                m_link = "https://www.kinopoisk.ru/top/lists/45/filtr/all/sort/rating_imdb/page/1/";
                break;
            default:
                System.out.println("Я могу показать только фильм или сериал");
                return;
        }
        m_pageCount = getPageCount(m_link);
        switch (sortingType) {
            case "рейтинг":
                m_sortingType = "рейтинг";
                break;
            case "рандомно":
                m_sortingType = "рандом";
                break;
            default:
                System.out.println("Я могу выводить только по рейтингу или рандомно");
                break;
        }
    }

    void showNext(){
        if (m_sortingType.equals("рейтинг")){
            showByRating();
        } else {
            showRandomly();
        }
    }

    private void showByRating(){
        if (m_currentPage > m_pageCount){
            return;
        }
        ArrayList<Animation> anims = getAnimList(m_currentPage, m_link);
        m_currentAnim += 1;
        if (m_currentAnim == anims.size()){
            m_currentPage += 1;
            m_link = m_link.substring(0, m_link.indexOf("page/") + 5);
            m_link = m_link + m_currentPage;
            m_currentAnim = 0;
        }
        Animation anim = anims.get(m_currentAnim);
        addAnnotation();
        anim.showInfo();
    }

    private void showRandomly() {
        while (true) {
            Random rn = new Random();
            m_currentPage = rn.nextInt(m_pageCount) + 1;
            m_link = m_link.substring(0, m_link.indexOf("page/") + 5);
            m_link = m_link + m_currentPage;
            ArrayList<Animation> anims = getAnimList(m_currentPage, m_link);
            m_currentAnim = rn.nextInt(anims.size());
            if(checkShownMap(m_currentPage, m_currentAnim)) {
                addAnnotation();
                anims.get(m_currentAnim).showInfo();
                break;
            }
        }
    }

    private ArrayList<Animation> parse(String link) {
        ArrayList<Animation> anims = new ArrayList<>();
        BufferedReader br = getWebSiteContent(link);
        if (br != null) {
            try {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.startsWith("        <div style=\"margin-bottom: 9px\"><a style=\"font-size: 13px; font-weight: bold\"")) {
                        Animation anim = new Animation();
                        anims.add(anim);
                        anim.url = new URL("https://www.kinopoisk.ru" + inputLine.substring(inputLine.indexOf("/film/"), inputLine.indexOf("\" class")));
                    }
                    if (inputLine.startsWith("             data-film-title")) {
                        anims.get(anims.size() - 1).name = inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1);
                    }
                    if (inputLine.startsWith("             data-film-rating")) {
                        anims.get(anims.size() - 1).rating = inputLine.substring(inputLine.indexOf("\"") + 1, inputLine.length() - 1);
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return anims;
    }

    private int getPageCount(String link){
        try {
            BufferedReader br = getWebSiteContent(link);
            if (br != null) {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.startsWith("    <div class=\"pagesFromTo\">")) {
                        String pageCount = inputLine.substring(inputLine.indexOf("из ") + 3, inputLine.indexOf("</div>"));
                        return Integer.parseInt(pageCount) / 25;
                    }
                }
            }
        }catch (IOException | NullPointerException e){
            e.printStackTrace();
        }
        return -1;
    }

    private BufferedReader getWebSiteContent(String link){
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            return new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Animation> getAnimList(int page, String link) {
        if (pageList.containsKey(page)) {
            return pageList.get(page);
        } else {
            pageList.put(page, parse(link));
            return pageList.get(page);
        }
    }

    private void addAnnotation() {
        Animation anim = pageList.get(m_currentPage).get(m_currentAnim);
        try {
            URLConnection connect = anim.url.openConnection();
            BufferedReader bufRead = new BufferedReader(new InputStreamReader(connect.getInputStream(), "windows-1251"));
            String inputLine;
            while ((inputLine = bufRead.readLine()) != null) {
                if (inputLine.startsWith("    <meta itemprop=\"description\" content=\"")) {
                    anim.annotation = inputLine.substring(42, inputLine.indexOf("\" />"));
                    break;
                }
            }
            bufRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkShownMap(int curPage, int curAnim){
        if (m_shownAnim.containsKey(curPage)){
            return !m_shownAnim.get(curPage).contains(curAnim);
        }
        else {
            m_shownAnim.put(curPage, new ArrayList<>(curAnim));
            return true;
        }
    }
}