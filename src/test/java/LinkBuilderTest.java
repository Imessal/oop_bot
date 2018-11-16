import org.junit.Assert;
import org.junit.Test;

public class LinkBuilderTest {
    @Test
    public void getLinkTest(){
        String link = "https://www.kinopoisk.ru/top/lists/13/filtr/all/sort/rating_imdb/genre[6]/6/country[13]/13/page/1";
        String linkBuilder = LinkBuilder.getLink("мультфильм",
                "по рейтингу",
                1,
                new String[]{"комедия"},
                new String[]{"ссср"});
        Assert.assertEquals(link, linkBuilder);
    }
    @Test
    public void getNextPageTest(){
        String link = "https://www.kinopoisk.ru/top/lists/13/filtr/all/sort/rating_imdb/page/99";
        String linkBuilder = LinkBuilder.getNextPage(link);
        link = "https://www.kinopoisk.ru/top/lists/13/filtr/all/sort/rating_imdb/page/100";
        Assert.assertEquals(link, linkBuilder);
    }
}
