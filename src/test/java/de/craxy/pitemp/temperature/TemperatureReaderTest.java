package de.craxy.pitemp.temperature;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TemperatureReaderTest {

    private TemperatureReader temperatureReader;

    @Before
    public void setup() throws IOException {
        TemperatureFileUtil.resetTemperatureFile();
        this.temperatureReader = new TemperatureReader(TemperatureFileUtil.TEMP_FILE_PATH);
    }

    @Test
    public void testReading() throws InterruptedException {
        this.temperatureReader.startReading(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        assertEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertEquals(78.134, this.temperatureReader.getLatestTemp(TemperatureUnit.FAHRENHEIT), 0);
        assertEquals(298.78, this.temperatureReader.getLatestTemp(TemperatureUnit.KELVIN), 0);
        assertNotEquals(30, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        this.temperatureReader.stopReading();
    }

    @Test
    public void testReReading() throws InterruptedException, IOException {
        this.temperatureReader.startReading(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        assertEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(30, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(0, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);

        TemperatureFileUtil.writeTemperatureFile("30000");

        Thread.sleep(2000);
        assertEquals(30, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(0, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
    }

    @Test
    public void testRestart() throws InterruptedException, IOException {
        this.temperatureReader.startReading(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        assertEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(30, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(0, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        this.temperatureReader.stopReading();

        TemperatureFileUtil.writeTemperatureFile("30000");

        this.temperatureReader.startReading(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        assertEquals(30, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(0, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
    }

    @Test
    public void testDelay() throws InterruptedException, IOException {
        this.temperatureReader.startReading(5, TimeUnit.SECONDS);
        Thread.sleep(1000);
        assertEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(31, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(0, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);

        TemperatureFileUtil.writeTemperatureFile("31000");

        assertEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(31, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(0, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);

        Thread.sleep(5000);
        assertEquals(31, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(25.63, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
        assertNotEquals(0, this.temperatureReader.getLatestTemp(TemperatureUnit.CELSIUS), 0);
    }

}
