import org.junit.Assert;
import org.junit.Test;

public class LinkBuilderTest {

    @Test
    public void LinkBuilderWithoutGenreTest(){
        String correctLink;

        correctLink = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/year/page/1";
        Assert.assertEquals(LinkBuilder.getLink("фильм", "годам", 1), correctLink);

        correctLink = "https://www.kinopoisk.ru/top/lists/45/filtr/all/sort/rating_imdb/page/5";
        Assert.assertEquals(LinkBuilder.getLink("сериал", "рейтингу", 5), correctLink);
    }

    @Test
    public void LinkBuilderWithGenreTest(){
        String correctLink;

        correctLink = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/runtime/genre[6]/6/page/3";
        Assert.assertEquals(LinkBuilder.getLink("фильм", "времени", 3, "комедия"), correctLink);

        correctLink = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/runtime/genre[10]/10/genre[1]/1/page/5";
        Assert.assertEquals(LinkBuilder.getLink("фильм", "времени", 5, "приключения", "ужас"), correctLink);
    }

    @Test
    public void LinkBuilderGetPageIndexOfTest(){
        String correctLink;

        correctLink = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/year/page/5";
        Assert.assertEquals(LinkBuilder.getPageIndexOf("https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/year/page/1", 5), correctLink);

        correctLink = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/year/page/2";
        Assert.assertEquals(LinkBuilder.getNextPage("https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/year/page/1"), correctLink);

    }

    @Test
    public void LinkBuilderSameTest(){
        String correctLink;

        correctLink = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/rating_imdb/genre[1]/1/page/1";
        Assert.assertEquals(LinkBuilder.getLink("фильм", "рейтингу", 1,"ужас"), correctLink);
    }
}
