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
    private Movie greenMile;
    @Mock
    private Movie forrestGump;
    @Mock
    private Movie theShawshankRedemption;

    private void configureMocks(ArrayList<Movie> movies){
        Mockito.lenient().
                when(user.getId()).thenReturn(0);

        Mockito.lenient().
                when(greenMile.getId()).thenReturn(1);
        Mockito.lenient().
                when(forrestGump.getId()).thenReturn(2);
        Mockito.lenient().
                when(theShawshankRedemption.getId()).thenReturn(3);

        Mockito.lenient().
                when(repository.checkMovie(0,1)).then((i) -> checkMovieMocking(1, movies));
        Mockito.lenient().
                when(repository.checkMovie(0, 2)).then((i)-> checkMovieMocking(2, movies));
        Mockito.lenient().
                when(repository.checkMovie(0, 3)).then((i) -> checkMovieMocking(3, movies));

        Mockito.lenient().doAnswer((i) -> {
            banMocking(greenMile, movies);
            return null;
        }).when(repository).addMovieToBlackList(user, greenMile);
        Mockito.lenient().doAnswer((i) -> {
            banMocking(forrestGump, movies);
            return null;
        }).when(repository).addMovieToBlackList(user, forrestGump);
        Mockito.lenient().doAnswer((i) -> {
            banMocking(theShawshankRedemption, movies);
            return null;
        }).when(repository).addMovieToBlackList(user, theShawshankRedemption);
    }

    private Boolean checkMovieMocking(int id, ArrayList<Movie> movies) {
        boolean flag = false;
        for(Movie m: movies){
            if(m.getId() != id) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void banMocking(Movie movie, ArrayList<Movie> movies){
        if (!movies.contains(movie)){
            movies.add(movie);
        }
    }

    private ArrayList<Movie> getMovies(){
        return new ArrayList<>();
    }



    @Test
    void testListOfMovies(){
        ArrayList<Movie> movies = getMovies();
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
    void shouldCheck(){
        configureMocks(getMovies());
        Assertions.assertTrue(repository.checkMovie(user.getId(), greenMile.getId()));
        Assertions.assertTrue(repository.checkMovie(user.getId(), forrestGump.getId()));
        Assertions.assertTrue(repository.checkMovie(user.getId(), theShawshankRedemption.getId()));
    }

    @Test
    void shouldBan(){
        configureMocks(getMovies());
        repository.addMovieToBlackList(user, greenMile);
        Assertions.assertFalse(repository.checkMovie(user.getId(), greenMile.getId()));
        Assertions.assertTrue(repository.checkMovie(user.getId(), theShawshankRedemption.getId()));
        Assertions.assertTrue(repository.checkMovie(user.getId(), forrestGump.getId()));
    }

    @Test
    void shouldWorkWithKinoman(){
        //????
    }
}