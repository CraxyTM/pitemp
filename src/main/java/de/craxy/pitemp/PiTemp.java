package de.craxy.pitemp;

import de.craxy.pitemp.handler.TemperatureHandler;
import io.javalin.Javalin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PiTemp {

    private static final Logger LOGGER = LoggerFactory.getLogger(PiTemp.class);

    public PiTemp(int port, boolean ssl, @NotNull String context) {
        Javalin javalin = Javalin.create(javalinConfig -> {
            javalinConfig.showJavalinBanner = false;
            javalinConfig.contextPath = context;
            javalinConfig.enforceSsl = ssl;
        });

        javalin.get("/temperature", new TemperatureHandler());

        javalin.start(port);
        Runtime.getRuntime().addShutdownHook(new Thread(javalin::stop));

        LOGGER.info("Successfully started server!");
    }

}
