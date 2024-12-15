package net.czpilar.dropdrive.cmd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author David Pilar (david@czpilar.net)
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/test-applicationContext.xml"})
public class ApplicationContextTest {

    @Test
    public void test() {
    }
}
