import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseMockingTest {

    @Mock
    private User user;

    @Mock
    private Kinoman kinoman = new Kinoman(user, "покажи рандомный фильм");

    @Mock
    private DatabaseRepository databaseRepository;

    @Test
    void shouldInjectMocks(){
        Assertions.assertNotNull(kinoman);
        Assertions.assertNotNull(databaseRepository);
    }

    @Test
    void shouldReturnMovie(){
        Movie movie = new Movie("test", 10, "test.com");
        Mockito.when(kinoman.getCurrentMovie()).thenReturn(movie);
        Mockito.when(databaseRepository.checkMovie(user.getId(), 10)).thenReturn(false);
        Assertions.assertEquals(movie, kinoman.getCurrentMovie());
        Assertions.assertFalse(databaseRepository.checkMovie(user.getId(), 10));
    }

    @Test
    void shouldBan(){
    }
}