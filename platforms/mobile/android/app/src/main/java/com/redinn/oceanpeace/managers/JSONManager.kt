package com.redinn.oceanpeace.managers

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object JSONManager {
    @JvmStatic
    @Throws(Exception::class)
    fun writeFile(data: JSONObject, fileName: String?) {
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(fileName))
            outputStreamWriter.write(data.toString(0))
            outputStreamWriter.close()
        } catch (err: IOException) {
            throw Exception(err.toString())
        } catch (err: JSONException) {
            throw Exception(err.toString())
        }
    }

    @JvmStatic
    @Throws(Exception::class)
    fun readFile(file: File?): JSONObject {
        return try {
            val content = getFileContents(file)
            Log.d("PresetsPlugin", "file data: $content")
            JSONObject(content)
        } catch (err: IOException) {
            throw Exception(err.toString())
        } catch (err: JSONException) {
            throw Exception(err.toString())
        }
    }

    @Throws(IOException::class)
    fun getFileContents(file: File?): String {
        val inputStream: InputStream = FileInputStream(file)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var done = false
        while (!done) {
            val line = reader.readLine()
            done = line == null
            if (line != null) {
                stringBuilder.append(line)
            }
        }
        reader.close()
        inputStream.close()
        return stringBuilder.toString()
    }

    fun deleteFile(fileName: String?): Boolean {
        val file = File(fileName.toString())
        return file.delete()
    }
}
