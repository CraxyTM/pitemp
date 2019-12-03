package de.craxy.pitemp.handler;

import de.craxy.pitemp.temperature.TemperatureReader;
import de.craxy.pitemp.temperature.TemperatureUnit;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

/**
 * A @{@link Handler} that responds to all requests regarding the temperature reading.
 */
public class TemperatureHandler implements Handler {

    private TemperatureReader temperatureReader;

    /**
     * Creates a new temperature handler.
     *
     * @param temperatureReader a temperature reader that provides the current system temperature.
     */
    public TemperatureHandler(TemperatureReader temperatureReader) {
        this.temperatureReader = temperatureReader;
    }

    @Override
    public void handle(@NotNull Context ctx) {
        ctx.status(200);
        //TODO: parameter for temperature unit
        //TODO: unit tests
        ctx.json(new TemperatureResponse(temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), TemperatureUnit.CELSIUS));
    }
}
