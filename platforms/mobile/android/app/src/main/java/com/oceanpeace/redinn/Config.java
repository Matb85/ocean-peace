package com.oceanpeace.redinn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {
    private static Config instance = null;
    public static Config getInstance()
    {
        if (instance == null)
            instance = new Config();
        return instance;
    }



    //Write config file
    public void Write(String key, String value) {
        try (OutputStream output = new FileOutputStream("config.properties")) {

            Properties prop = new Properties();

            // set the properties value
            prop.setProperty(key, value);

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Write(String[] keys, String[] values) {
        try (OutputStream output = new FileOutputStream("config.properties")) {

            Properties prop = new Properties();

            // set the properties values
            for(int i=0; i<keys.length && i<values.length; i++) {
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
        String reading=null;
        try (InputStream input = new FileInputStream("config.properties")) {

            Properties prop = new Properties();

            // Load the config file
            prop.load(input);

            // get the properties value
            reading = prop.getProperty(key);

        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return reading;
    }

    public String[] Read(String[] keys) {
        String[] readings = new String[keys.length];
        try (InputStream input = new FileInputStream("config.properties")) {

            Properties prop = new Properties();

            // Load the config file
            prop.load(input);

            // set the properties values
            for(int i=0; i<keys.length; i++) {
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
