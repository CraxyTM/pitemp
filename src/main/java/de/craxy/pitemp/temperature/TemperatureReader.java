package de.craxy.pitemp.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Reads the temperature from a system file in a given delay.
 *
 * @author Craxy
 */
public class TemperatureReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureReader.class);

    private ScheduledExecutorService executorService;
    private File tempFile;
    private double latestTemp = Double.MIN_VALUE;

    /**
     * Instantiates a new temperature reader with a given temperature file.
     *
     * @param temperatureFilePath the path to the temperature file.
     */
    public TemperatureReader(String temperatureFilePath) {
        tempFile = new File(temperatureFilePath);
        if (!tempFile.exists()) {
            throw new RuntimeException("Cannot find custom temperature file.");
        }
    }

    /**
     * Instantiates a new temperature reader. Tries to use unix system files for reading.
     */
    public TemperatureReader() {
        tempFile = new File("/sys/class/thermal/thermal_zone0/temp");
        if (!tempFile.exists()) {
            tempFile = new File("/sys/class/hwmon/hwmon0/temp1_input");
            if (!tempFile.exists()) {
                throw new RuntimeException("Cannot find temperature file.");
            }
        }
        LOGGER.info("Initialized temperature file!");
    }

    /**
     * Starts the scheduler to read the temperature.
     *
     * @param delay    the delay to wait after each temperature read.
     * @param timeUnit the time unit of the delay.
     */
    public void startReading(long delay, TimeUnit timeUnit) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleWithFixedDelay(this::readTemperature, 0, delay, timeUnit);
        LOGGER.info("Started temperature scheduler!");
    }

    /**
     * Stops the reading of the temperature.
     */
    public void stopReading() {
        this.executorService.shutdown();
    }

    /**
     * Reads the temperature using the tempFile.
     */
    private void readTemperature() {
        if (!tempFile.exists()) {
            throw new IllegalStateException("Temperature file does not exist.");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            latestTemp = Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            LOGGER.error("Error while reading temperature.", e);
        }
    }

    /**
     * Gives the latest temperature that was read.
     *
     * @param temperatureUnit the desired unit.
     * @return the latest temperature in the given unit.
     */
    public double getLatestTemp(TemperatureUnit temperatureUnit) {
        if (temperatureUnit == null) {
            throw new IllegalArgumentException("Unit cannot be null.");
        }
        if (latestTemp <= Double.MIN_VALUE) {
            throw new IllegalStateException("Cannot access temperature before reading.");
        }

        switch (temperatureUnit) {
            case CELSIUS:
                return latestTemp / 1000;
            case FAHRENHEIT:
                return (latestTemp / 1000) * 9.0d / 5.0d + 32.0d;
            case KELVIN:
                return (latestTemp / 1000) + 273.15d;
            default:
                return -1;
        }
    }

}
