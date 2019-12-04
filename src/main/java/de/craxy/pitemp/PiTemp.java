package de.craxy.pitemp;

import de.craxy.pitemp.handler.TemperatureHandler;
import de.craxy.pitemp.temperature.TemperatureReader;
import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * The main class of the program. Initializes Javalin and starts the server.
 *
 * @author Craxy
 */
public class PiTemp {

    private static final Logger LOGGER = LoggerFactory.getLogger(PiTemp.class);

    private Javalin javalin;

    /**
     * Creates a new PiTemp server with partial default settings.
     *
     * @param httpPort            the desired http port.
     * @param temperatureFilePath the path to the temperature file. The temperature file should contain the temperature in 1000°C in the first line.
     */
    public PiTemp(int httpPort, @NotNull String temperatureFilePath) {
        new PiTemp(httpPort, false, 0, null, null, null, temperatureFilePath);
    }

    /**
     * Creates a new PiTemp server with the default settings, which starts a new http server on port 80 without ssl.
     */
    public PiTemp() {
        new PiTemp(80, false, 0, null, null, null, null);
    }

    /**
     * Create a new PiTemp server with the given parameters and starts the temperature reader.
     *
     * @param httpPort            the http port to run on.
     * @param ssl                 whether or not ssl should be enabled.
     * @param sslPort             required if ssl is true. Sets the port for https.
     * @param context             the context of the server.
     * @param sslCertLocation     required if ssl is true. Sets the location of the ssl certificate.
     * @param keystorePassword    required if ssl true. Sets the password for the ssl certificate.
     * @param temperatureFilePath The temperature file should contain the temperature in 1000°C in the first line.
     */
    public PiTemp(int httpPort, boolean ssl, int sslPort, @Nullable String context, @Nullable String sslCertLocation, @Nullable String keystorePassword, @Nullable String temperatureFilePath) {
        if (ssl && (sslCertLocation == null || keystorePassword == null)) {
            throw new IllegalStateException("sslCertLocation and keystorePassword are required when enabling ssl!");
        }

        TemperatureReader temperatureReader = temperatureFilePath == null ? new TemperatureReader() : new TemperatureReader(temperatureFilePath);
        temperatureReader.startReading(1, TimeUnit.SECONDS);

        this.javalin = Javalin.create(javalinConfig -> {
            javalinConfig.showJavalinBanner = false;
            javalinConfig.contextPath = context == null ? "/" : context;
            javalinConfig.enforceSsl = ssl;
            if (ssl) {
                if (!(new File(sslCertLocation)).exists()) {
                    throw new IllegalArgumentException("SSL cert file does not exist.");
                }
                javalinConfig.server(() -> {
                    Server server = new Server();
                    SslContextFactory sslContextFactory = new SslContextFactory();
                    sslContextFactory.setKeyStorePath(sslCertLocation);
                    sslContextFactory.setKeyStorePassword(keystorePassword);
                    ServerConnector sslConnector = new ServerConnector(server, sslContextFactory);
                    sslConnector.setPort(sslPort);
                    ServerConnector connector = new ServerConnector(server);
                    connector.setPort(httpPort);
                    server.setConnectors(new Connector[]{sslConnector, connector});
                    return server;
                });
            }
        });

        this.javalin.get("/temperature", new TemperatureHandler(temperatureReader));

        this.javalin.start(httpPort);
        Runtime.getRuntime().addShutdownHook(new Thread(this.javalin::stop));

        LOGGER.info("Successfully started server!");
    }

    /**
     * Stops the server, if online.
     */
    public void stop() {
        if(this.javalin != null) {
            this.javalin.stop();
            this.javalin = null;
        }
    }

}
