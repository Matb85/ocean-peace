package com.oceanpeace.redinn;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class JSONManager {
    static public void writeFile(JSONObject data, String fileName) throws Exception {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName));
            outputStreamWriter.write(data.toString(0));
            outputStreamWriter.close();
        } catch (IOException | JSONException err) {
            throw new Exception(err.toString());
        }
    }

    static public JSONObject readFile( File file) throws Exception {
        try {
            String content = getFileContents(file);
            Log.d("PresetsPlugin", "file data: "+ content);
            return new JSONObject(content);
        } catch (IOException | JSONException err) {
            throw new Exception(err.toString());
        }
    }

    static  public  String getFileContents(final File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;

        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }

    static public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return file.delete();
    }
}
