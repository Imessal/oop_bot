import org.junit.Assert;
import org.junit.Test;


public class BotTest {
    @Test
    public void createKinomanOnRequestTest(){
        Kinoman kinoman = Kinoman.createKinomanOnRequest("покажи фильм ужас или приключения по годам");
        String correctLink = "https://www.kinopoisk.ru/top/lists/1/filtr/all/sort/year/genre[1]/1/genre[10]/10/page/1";
        Assert.assertEquals(kinoman.link, correctLink);
    }
}