package de.craxy.pitemp.handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A @{@link Handler} that responds to all requests regarding the temperature reading.
 */
public class TemperatureHandler implements Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureHandler.class);

    @Override
    public void handle(@NotNull Context ctx) {
        ctx.status(200);
        ctx.result("Working");
        LOGGER.info("Connection from: " + ctx.ip());
    }
}
