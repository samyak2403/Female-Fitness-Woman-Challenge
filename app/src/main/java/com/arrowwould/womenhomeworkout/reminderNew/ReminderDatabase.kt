
package com.arrowwould.womenhomeworkout.reminderNew

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.String
import kotlin.collections.ArrayList

class ReminderDatabase(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_REMINDERS_TABLE = ("CREATE TABLE " + TABLE_REMINDERS +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " INTEGER,"
                + KEY_REPEAT + " BOOLEAN,"
                + KEY_REPEAT_NO + " INTEGER,"
                + KEY_REPEAT_TYPE + " TEXT,"
                + KEY_ACTIVE + " BOOLEAN,"
                + KEY_DAYS + " TEXT" + ")")
        db.execSQL(CREATE_REMINDERS_TABLE)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        if (oldVersion >= newVersion) return
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REMINDERS")

        // Create tables again
        onCreate(db)
    }

    // Adding new Reminder
    fun addReminder(reminder: Reminder): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, reminder.title)
        values.put(KEY_DATE, reminder.date)
        values.put(KEY_TIME, reminder.time)
        values.put(KEY_REPEAT, reminder.repeat)
        values.put(KEY_REPEAT_NO, reminder.repeatNo)
        values.put(KEY_REPEAT_TYPE, reminder.repeatType)
        values.put(KEY_ACTIVE, reminder.active)
        values.put(KEY_DAYS, reminder.days)

        // Inserting Row
        val ID = db.insert(TABLE_REMINDERS, null, values)
        db.close()
        return ID.toInt()
    }

    // Getting single Reminder
    fun getReminder(id: Int): Reminder? {
        try {
            val db = this.readableDatabase
            val cursor = db.query(
                TABLE_REMINDERS, arrayOf(
                    KEY_ID,
                    KEY_TITLE,
                    KEY_DATE,
                    KEY_TIME,
                    KEY_REPEAT,
                    KEY_REPEAT_NO,
                    KEY_REPEAT_TYPE,
                    KEY_ACTIVE,
                    KEY_DAYS
                ), "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null
            )
            cursor?.moveToFirst()
            return Reminder(
                cursor!!.getString(cursor.getColumnIndexOrThrow(KEY_ID)).toInt(),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_REPEAT)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_REPEAT_NO)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_REPEAT_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACTIVE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_DAYS))
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    val allReminders: ArrayList<Reminder>
        get() {
            val reminderList: ArrayList<Reminder> = ArrayList()

            // Select all Query
            val selectQuery = "SELECT * FROM $TABLE_REMINDERS"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // Looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    val reminder = Reminder()
                    reminder.iD = cursor.getString(0).toInt()
                    reminder.title = cursor.getString(1)
                    reminder.date = cursor.getString(2)
                    reminder.time = cursor.getString(3)
                    reminder.repeat = cursor.getString(4)
                    reminder.repeatNo = cursor.getString(5)
                    reminder.repeatType = cursor.getString(6)
                    reminder.active = cursor.getString(7)
                    reminder.days = cursor.getString(8)

                    // Adding Reminders to list
                    reminderList.add(reminder)
                } while (cursor.moveToNext())
            }
            return reminderList
        }

    // Getting Reminders Count
    val remindersCount: Int
        get() {
            val countQuery = "SELECT * FROM $TABLE_REMINDERS"
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            cursor.close()
            return cursor.count
        }

    // Updating single Reminder
    fun updateReminderActive(id:kotlin.String,active:kotlin.String): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_ACTIVE, active)

        return db.update(
            TABLE_REMINDERS,
            values,
            "$KEY_ID=?",
            arrayOf(String.valueOf(id))
        )
    }

    fun updateReminderDays(id:kotlin.String,days:kotlin.String): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_DAYS, days)

        return db.update(
            TABLE_REMINDERS,
            values,
            "$KEY_ID=?",
            arrayOf(String.valueOf(id))
        )
    }

    fun updateReminderTime(id:kotlin.String,time:kotlin.String): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_TIME, time)

        return db.update(
            TABLE_REMINDERS,
            values,
            "$KEY_ID=?",
            arrayOf(String.valueOf(id))
        )
    }

    // Deleting single Reminder
    fun deleteReminder(id:kotlin.String) {
        val db = this.writableDatabase
        db.delete(
            TABLE_REMINDERS,
            "$KEY_ID=?",
            arrayOf(String.valueOf(id))
        )
        db.close()
    }

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 2

        // Database Name
        private const val DATABASE_NAME = "ReminderDatabase"

        // Table name
        private const val TABLE_REMINDERS = "ReminderTable"

        // Table Columns names
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DATE = "date"
        private const val KEY_TIME = "time"
        private const val KEY_REPEAT = "repeat"
        private const val KEY_REPEAT_NO = "repeat_no"
        private const val KEY_REPEAT_TYPE = "repeat_type"
        private const val KEY_ACTIVE = "active"
        private const val KEY_DAYS = "days"
    }
}