import org.junit.Assert;
import org.junit.Test;

public class RequestCheckerTest {
    @Test
    public void validateTest(){
        String request = "покажи топ смешных советских мультов";
        Assert.assertEquals("покажи по рейтингу комедия ссср мультфильм", RequestChecker.validate(request));
    }
}
