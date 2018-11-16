import org.junit.Assert;
import org.junit.Test;

public class SpelCheckerTest {
    @Test
    public void checkTest(){
        String request = "покажи топ смешных советских мультов";
        Assert.assertEquals("покажи по рейтингу комедия ссср мультфильм", SpellChecker.check(request));
    }
}
