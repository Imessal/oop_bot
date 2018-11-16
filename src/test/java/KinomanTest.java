import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class KinomanTest {
    @Test
    public void splitRequestTest() {
        String request = "покажи бегущий по лезвию";
        ArrayList<String> words = new ArrayList<>();
        words.add("покажи");
        words.add("бегущий");
        words.add("по лезвию");
        Assert.assertEquals(words, Kinoman.splitRequest(request));

        request = "покажи сериалы по руйтингу";
        words.clear();
        words.add("покажи");
        words.add("сериалы");
        words.add("по руйтингу");
        Assert.assertEquals(words, Kinoman.splitRequest(request));

        request = "покажи фильмы По";
        words.clear();
        words.add("покажи");
        words.add("фильмы");
        words.add("По");
        Assert.assertEquals(words, Kinoman.splitRequest(request));
    }
}
