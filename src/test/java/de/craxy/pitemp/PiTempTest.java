package de.craxy.pitemp;

import de.craxy.pitemp.temperature.TemperatureFileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

public class PiTempTest {

    private PiTemp piTemp;

    @Before
    public void setup() throws IOException {
        TemperatureFileUtil.resetTemperatureFile();
        this.piTemp = new PiTemp(42069, TemperatureFileUtil.TEMP_FILE_PATH);
    }

    @After
    public void shutDown() {
        this.piTemp.stop();
    }

    @Test
    public void testTemperatures() throws IOException, InterruptedException {
        assertTemp("25.63");
        TemperatureFileUtil.writeTemperatureFile("30600");
        Thread.sleep(1000);
        assertTemp("30.6");
        TemperatureFileUtil.writeTemperatureFile("31500");
        Thread.sleep(1000);
        assertTemp("31.5");
        assertTemp("31.5");
    }

    private void assertTemp(String temp) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://127.0.0.1:42069/temperature").openConnection();
        assertEquals(200, con.getResponseCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        assertEquals("{\"temperature\":" + temp + ",\"unit\":\"CELSIUS\"}", reader.readLine());

        reader.close();
        con.disconnect();
    }

}
