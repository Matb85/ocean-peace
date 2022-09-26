package com.redinn.oceanpeace.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesManager {


    private final String path;

    public PropertiesManager(String differentLocalization, String fileName) {
        path = differentLocalization + "/" + fileName;
    }


    public void Create() throws IOException {
        new File(path).createNewFile();
    }

    //Write config file
    public void Write(String key, String value) {
        try {

            FileInputStream IN = new FileInputStream(path);
            Properties prop = new Properties();
            prop.load(IN);
            IN.close();

            OutputStream output = new FileOutputStream(path);
            // set the properties value
            prop.setProperty(key, value);

            // save properties to project root folder
            prop.store(output, null);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Write(String[] keys, String[] values) {
        try {

            FileInputStream IN = new FileInputStream(path);
            Properties prop = new Properties();
            prop.load(IN);
            IN.close();

            OutputStream output = new FileOutputStream(path);

            // set the properties values
            for (int i = 0; i < keys.length && i < values.length; i++) {
                prop.setProperty(keys[i], values[i]);
            }

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    //Read config file
    public String Read(String key) {
        String reading = "";
        try {
            InputStream input = new FileInputStream(path);
            Properties prop = new Properties();

            // Load the config file
            prop.load(input);
            input.close();

            // get the properties value
            return prop.getProperty(key);

        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return reading;
    }

    public String[] Read(String[] keys) {
        String[] readings = new String[keys.length];
        try {

            InputStream input = new FileInputStream(path);
            Properties prop = new Properties();

            // Load the config file
            prop.load(input);
            input.close();

            // set the properties values
            for (int i = 0; i < keys.length; i++) {
                readings[i] += prop.getProperty(keys[i]);
            }

        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return readings;
    }
}
