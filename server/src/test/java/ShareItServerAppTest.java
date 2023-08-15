import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.ShareItServer;

public class ShareItServerAppTest {

    @Test
    void testMain() {
        Assertions.assertDoesNotThrow(ShareItServer::new);
        Assertions.assertDoesNotThrow(() -> ShareItServer.main(new String[]{}));
    }
}
