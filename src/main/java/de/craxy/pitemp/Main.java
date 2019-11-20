package de.craxy.pitemp;

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
        new PiTemp(parser.getIntValue("port").orElse(80),
                parser.getBooleanValue("ssl").orElse(false),
                parser.getIntValue("sslport").orElse(443),
                parser.getStringValue("context").orElse("/"),
                parser.getStringValue("sslcertlocation").orElse(null),
                parser.getStringValue("keystorepassword").orElse(null));
    }

}
