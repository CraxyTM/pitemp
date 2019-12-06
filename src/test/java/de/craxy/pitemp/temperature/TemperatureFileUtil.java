package de.craxy.pitemp.temperature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class TemperatureFileUtil {

    public static final String TEMP_FILE_PATH = "src/test/resources/testtemperature.txt";

    public static void resetTemperatureFile() throws IOException {
        //Reset temp file
        File tempFile = new File(TEMP_FILE_PATH);
        if (!tempFile.exists()) {
            if (!tempFile.createNewFile()) {
                fail();
            }
        }

        //Write default temp
        writeTemperatureFile("25630");
    }

    public static void writeTemperatureFile(String temperature) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(TemperatureFileUtil.TEMP_FILE_PATH)));
        writer.write(temperature);
        writer.flush();
        writer.close();
    }

}
