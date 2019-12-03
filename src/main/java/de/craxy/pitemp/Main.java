package de.craxy.pitemp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * Main class for PiTemp.
 *
 * @author Craxy
 */
public class Main {

    /**
     * Entry point for PiTemp. Processes arguments
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);

        if (parser.hasArgument("debug") && parser.getStringValue("debug").get().equalsIgnoreCase("true")) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("de.craxy.pitemp");
            rootLogger.setLevel(Level.DEBUG);
        }

        new PiTemp(parser.getIntValue("port").orElse(80),
                parser.getBooleanValue("ssl").orElse(false),
                parser.getIntValue("sslport").orElse(443),
                parser.getStringValue("context").orElse("/"),
                parser.getStringValue("sslcertlocation").orElse(null),
                parser.getStringValue("keystorepassword").orElse(null),
                parser.getStringValue("temperaturefilepath").orElse(null));
    }

}
