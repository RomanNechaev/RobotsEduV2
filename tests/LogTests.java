import junit.framework.TestCase;
import log.LogConst;
import log.LogWindowSource;
import log.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.SocketOption;
import java.security.spec.RSAOtherPrimeInfo;

public class LogTests extends TestCase {
    public int queueLength;
    public LogWindowSource logSource;


    @BeforeEach
    public void beforeEach(){
        logSource = Logger.getDefaultLogSource();
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
    public void anotherMessage(){
        Logger.debug("Другой текст");
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, 1);
    }

    @Test
    public void pushingLastPossible(){
        for (int i = LogConst.iQueueLength; i > 0; i--){
            Logger.debug("Тест");
        }
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, LogConst.iQueueLength);
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
    public void pushAndPop(){
        Logger.debug("Тест");
        Logger.debugDelete();
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, 0);
    }

    @Test
    public void averagePushing(){
        for (int i = 3; i > 0; i--){
            Logger.debug("Тест");
        }
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, 3);
    }

    @Test
    public void specialCharacters(){
        Logger.debug("\n\t\s");
        queueLength = logSource.size();
        Assertions.assertEquals(queueLength, 1);
    }
}