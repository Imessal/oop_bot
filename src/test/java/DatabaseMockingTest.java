import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class DatabaseMockingTest {

    @Mock
    private User user;

    @Mock
    private DatabaseRepository repository;

    @InjectMocks
    private Kinoman kinoman = new Kinoman(user, "покажи фильм по рейтингу");

    @Mock
    private ArrayList<Movie> movies;
    @Mock
    private Movie greenMile;
    @Mock
    private Movie forrestGump;
    @Mock
    private Movie theShawshankRedemption;

    private void makingMockList(){
        Mockito.lenient().
                when(movies.get(0)).thenReturn(greenMile);
        Mockito.lenient().
                when(movies.get(1)).thenReturn(forrestGump);
        Mockito.lenient().
                when(movies.get(2)).thenReturn(theShawshankRedemption);

    }

    @Test
    void testListOfMovies(){
        makingMockList();
        Movie first_movie = movies.get(0);
        Movie second_movie = movies.get(1);
        Movie third_movie = movies.get(2);
        Assertions.assertSame(greenMile, first_movie);
        Assertions.assertSame(forrestGump, second_movie);
        Assertions.assertSame(theShawshankRedemption, third_movie);
    }


    @Test
    void shouldInjectMocks(){
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(repository);
        Assertions.assertNotNull(kinoman);
        Assertions.assertSame(user, kinoman.getCurrentUser());
        Assertions.assertSame(repository, kinoman.getRepository());
    }

    @Test
    void shouldBan(){
        makingMockList();
        movies.add(kinoman.getNext());
    }
}