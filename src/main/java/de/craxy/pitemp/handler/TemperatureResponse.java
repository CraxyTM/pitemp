package de.craxy.pitemp.handler;

import de.craxy.pitemp.temperature.TemperatureUnit;

/**
 * A POJO for creating a JSON response containing the current temperature.
 */
public class TemperatureResponse {

    private double temperature;
    private TemperatureUnit unit;

    /**
     * Creates a new TemperatureResponse with the given parameters.
     *
     * @param temperature the current system temperature.
     * @param unit        the unit of the temperature.
     */
    public TemperatureResponse(double temperature, TemperatureUnit unit) {
        this.temperature = temperature;
        this.unit = unit;
    }

    /**
     * Gives the current temperature.
     *
     * @return the temperature.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Gives the unit of the temperature.
     *
     * @return the unit.
     */
    public TemperatureUnit getUnit() {
        return unit;
    }
}
