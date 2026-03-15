package net.czpilar.dropdrive.cmd;

import net.czpilar.dropdrive.cmd.context.DropDriveCmdContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author David Pilar (david@czpilar.net)
 */
@ExtendWith(SpringExtension.class)
@Import(DropDriveCmdContext.class)
class ApplicationContextTest {

    @Test
    void test() {
    }
}
