package de.craxy.pitemp;

import de.craxy.pitemp.handler.TemperatureHandler;
import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the program. Initializes Javalin and starts the server.
 *
 * @author Craxy
 */
public class PiTemp {

    private static final Logger LOGGER = LoggerFactory.getLogger(PiTemp.class);

    /**
     * Create a new PiTemp Server with the given parameters.
     * @param httpPort the http port to run on.
     * @param ssl whether or not ssl should be enabled.
     * @param sslPort required if ssl is true. Sets the port for https.
     * @param context the context of the server.
     * @param sslCertLocation required if ssl is true. Sets the location of the ssl certificate.
     * @param keystorePassword required if ssl true. Sets the password for the ssl certificate.
     */
    public PiTemp(int httpPort, boolean ssl, int sslPort, @Nullable String context, @Nullable String sslCertLocation, @Nullable String keystorePassword) {
        if (ssl && (sslCertLocation == null || keystorePassword == null)) {
            throw new IllegalStateException("sslCertLocation and keystorePassword are required when enabling ssl!");
        }

        Javalin javalin = Javalin.create(javalinConfig -> {
            javalinConfig.showJavalinBanner = false;
            javalinConfig.contextPath = context == null ? "/" : context;
            javalinConfig.enforceSsl = ssl;
            if (ssl) {
                javalinConfig.server(() -> {
                    Server server = new Server();
                    SslContextFactory sslContextFactory = new SslContextFactory();
                    sslContextFactory.setKeyStorePath(PiTemp.class.getResource(sslCertLocation).toExternalForm());
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

        javalin.get("/temperature", new TemperatureHandler());

        javalin.start(httpPort);
        Runtime.getRuntime().addShutdownHook(new Thread(javalin::stop));

        LOGGER.info("Successfully started server!");
    }

}
