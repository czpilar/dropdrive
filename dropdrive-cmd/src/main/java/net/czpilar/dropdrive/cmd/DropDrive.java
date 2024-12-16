package net.czpilar.dropdrive.cmd;

import net.czpilar.dropdrive.cmd.context.DropDriveCmdContext;
import net.czpilar.dropdrive.cmd.runner.IDropDriveCmdRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main class for running dropDrive from command line.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DropDrive {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(DropDriveCmdContext.class)
                .getBean(IDropDriveCmdRunner.class)
                .run(args);
    }
}
