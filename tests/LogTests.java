import junit.framework.TestCase;
import log.LogConst;
import log.LogWindowSource;
import log.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogTests extends TestCase {
    public int queueLength;
    public Logger log;
    public LogWindowSource logSource;

    @BeforeEach
    public void beforeEach(){
        queueLength = 0;
        log = new Logger();
        logSource = log.getDefaultLogSource();
    }

    @AfterEach
    public void afterEach(){
        logSource.reset();
    }


    @Test
    public void firstMessage(){
        Logger.debug("Тест");
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, 1);
    }

    @Test
    public void pushingInOverflow(){
        for (int i = LogConst.iQueueLength; i >= 0; i--){
            Logger.debug("Тест");
        }
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, LogConst.iQueueLength);
    }

    @Test
    public void averagePushing(){
        for (int i = 3; i > 0; i--){
            Logger.debug("Тест");
        }
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, 3);
    }
}