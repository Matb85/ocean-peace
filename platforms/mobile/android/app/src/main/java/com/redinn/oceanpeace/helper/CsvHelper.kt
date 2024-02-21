package com.redinn.oceanpeace.helper

import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVWriter
import com.opencsv.CSVWriterBuilder
import com.redinn.oceanpeace.model.UsageRecord
import java.io.File
import java.io.FileReader
import java.io.FileWriter


@Suppress("MemberVisibilityCanBePrivate")
object CsvHelper {
    val usageHeader = arrayOf("packageName", "startTime", "duration")

    fun write(file: File, records: List<UsageRecord>): Boolean {
        val alreadyExist = File(file.path).exists()
        return try {
            val writer = CSVWriterBuilder(FileWriter(file.path, true))
                    .withSeparator(',')
                    .build() as CSVWriter

            if (!alreadyExist) {
                writer.writeNext(usageHeader)
            }
            for (record in records) {
                writer.writeNext(record.toArray())
            }
            writer.close()
            true
        } catch (e: Exception) {
            Logger.e("CsvHelper", e.message + ": " + e.localizedMessage)
            false
        }
    }

    fun read(file: File): List<UsageRecord> {
        try {
            if (!file.canRead()) {
                Logger.d("CsvHelper", "can't read file ${file.path}")
                return listOf()
            }
            val records = arrayListOf<UsageRecord>()
            val reader = CSVReaderBuilder(FileReader(file.path)).build();
            var nextLine: Array<String?> = reader.readNext()
            while (nextLine != null) {
                val record = UsageRecord(nextLine[0]!!, nextLine[1]!!.toLong(), nextLine[2]!!.toLong())
                records.add(record)
                nextLine = reader.readNext()
            }
            reader.close()
            return records
        } catch (e: Exception) {
            Logger.e("CsvHelper", e.message + ": " + e.localizedMessage)
            return listOf()
        }
    }
}
