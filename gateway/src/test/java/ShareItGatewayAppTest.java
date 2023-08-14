import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.ShareItGateway;

public class ShareItGatewayAppTest {

    @Test
    void testMain() {
        Assertions.assertDoesNotThrow(ShareItGateway::new);
        Assertions.assertDoesNotThrow(() -> ShareItGateway.main(new String[]{}));
    }
}
