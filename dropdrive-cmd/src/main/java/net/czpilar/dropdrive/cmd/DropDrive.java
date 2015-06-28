package net.czpilar.dropdrive.cmd;

import net.czpilar.dropdrive.cmd.runner.IDropDriveCmdRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class for running dropDrive from command line.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DropDrive {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:dropdrive-cmd-applicationContext.xml");
        IDropDriveCmdRunner runner = context.getBean(IDropDriveCmdRunner.class);
        runner.run(args);
    }
}
