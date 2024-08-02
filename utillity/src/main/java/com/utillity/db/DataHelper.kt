package com.utillity.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import com.utillity.objects.CommonUtility
import com.utillity.pojo.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

private const val DBName = "FemaleFitness.db"

private const val PlanTable = "PlanTable"
private const val PlanId = "PlanId"
private const val PlanName = "PlanName"
private const val PlanProgress = "PlanProgress"
private const val PlanText = "PlanText"
private const val PlanLvl = "PlanLvl"
private const val PlanImage = "PlanImage"
private const val PlanDays = "PlanDays"
private const val PlanType = "PlanType"
private const val PlanKcal = "PlanKcal"
private const val PlanMinutes = "PlanMinutes"
private const val PlanSort = "sort"

private const val PlanDaysTable = "PlanDaysTable"
private const val DayId = "DayId"
private const val DayName = "DayName"
private const val WeekName = "WeekName"
private const val IsCompleted = "IsCompleted"
private const val DayProgress = "DayProgress"

private const val DayExTable = "DayExTable"
private const val DayExSingleTable = "DayExSingleTable"
private const val DayExId = "DayExId"
private const val ExId = "ExId"
private const val ExTime = "ExTime"
private const val UpdatedExTime = "UpdatedExTime"
private const val ReplaceExId = "ReplaceExId"

private const val ExerciseTable = "ExerciseTable"
private const val ExName = "ExName"
private const val ExUnit = "ExUnit"
private const val ExPath = "ExPath"
private const val ExDescription = "ExDescription"
private const val ExVideo = "ExVideo"
private const val ExReplaceTime = "ReplaceTime"

private const val ReminderTable = "ReminderTable"
private const val RId = "RId"
private const val RemindTime = "RemindTime"
private const val Days = "Days"
private const val IsActive = "IsActive"

private const val WeightTable = "WeightTable"
private const val WeightId = "WeightId"
private const val WeightKg = "WeightKg"
private const val WeightLb = "WeightLb"
private const val WeightDate = "WeightDate"
private const val CurrentTimeStamp = "CurrentTimeStamp"

private const val HistoryTable = "HistoryTable"
private const val HId = "HId"
private const val HPlanName = "HPlanName"
private const val HLvlName = "HLvlName"
private const val HDayName = "HDayName"
private const val HBurnKcal = "HBurnKcal"
private const val HTotalEx = "HTotalEx"
private const val HKg = "HKg"
private const val HFeet = "HFeet"
private const val HInch = "HInch"
private const val HFeelRate = "HFeelRate"
private const val HCompletionTime = "HCompletionTime"
private const val HDateTime = "HDateTime"

class DataHelper(private val mContext: Context) {

    fun checkDBExist(): Boolean {
        var isExist = false
        val dbFile = mContext.getDatabasePath(DBName)

        if (!dbFile.exists()) {
            try {
                if (copyDatabase(dbFile)) {
                    if (dbFile.exists()) {
                        isExist = true
                    }
                } else if (dbFile.delete()) {
                    if (copyDatabase(dbFile)) {
                        if (dbFile.exists()) {
                            isExist = true
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return isExist
    }

    private fun getReadWriteDB(): SQLiteDatabase {
        val dbFile = mContext.getDatabasePath(DBName)
        if (!dbFile.exists()) {
            try {
                val checkDB = mContext.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null)
                checkDB?.close()
                copyDatabase(dbFile)
            } catch (e: Exception) {
                throw RuntimeException("Error creating source database", e)
            }
        }

        val database = SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
        Log.e("TAG", "getReadWriteDB:Databse Version:::::OLD  ${database.version}" )
        if (database.version == 0){
//            database.execSQL("DROP TABLE IF EXISTS $PlanTable")
//            getReadWriteDB()
            updateTable(database)
            database.version = 1
            Log.e("TAG", "getReadWriteDB:Database Version New:::::  ${database.version}" )
        }
        return database
    }

    private fun updateTable(database: SQLiteDatabase?) {

        updateData(database,"271 kcal 10 minutes",2)
        updateData(database,"218 kcal 8 minutes",3)
        updateData(database,"189 kcal 7 minutes",4)
        updateData(database,"186 kcal 7 minutes",5)
        updateData(database,"166 kcal 6 minutes",6)
        updateData(database,"161 kcal 6 minutes",7)
        updateData(database,"155 kcal 6 minutes",8)
        updateData(database,"152 kcal 5 minutes",9)
        updateData(database,"133 kcal 5 minutes",10)
        updateData(database,"113 kcal 4 minutes",11)
        updateData(database,"110 kcal 4 minutes",12)
    }

    private fun updateData(database: SQLiteDatabase?, planText: String, id: Int) {
        try {
            database!!.execSQL("UPDATE $PlanTable SET $PlanText='$planText' WHERE $PlanId='$id'")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun copyDatabase(dbFile: File): Boolean {
        var isSuccess = false
        var ins: InputStream? = null
        var os: FileOutputStream? = null

        try {
            ins = mContext.assets.open(DBName)
            os = FileOutputStream(dbFile)

            val buffer = ByteArray(1024)
            while (ins.read(buffer) > 0) {
                os.write(buffer)
            }

            isSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (os != null) {
                os.flush()
                os.close()
            }
            if (ins != null) {
                ins.close()
            }
        }

        return isSuccess
    }

    fun getPlanList(strPlanType: String): ArrayList<PlanTableClass> {

        val arrPlan: ArrayList<PlanTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select * From $PlanTable where $PlanType = '$strPlanType' order by $PlanSort"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = PlanTableClass()
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
                    aClass.planProgress = cursor.getString(cursor.getColumnIndexOrThrow(PlanProgress))
                    aClass.planText = cursor.getString(cursor.getColumnIndexOrThrow(PlanText))
                    aClass.planLvl = cursor.getString(cursor.getColumnIndexOrThrow(PlanLvl))
                    aClass.planImage = cursor.getString(cursor.getColumnIndexOrThrow(PlanImage))
                    aClass.planDays = cursor.getString(cursor.getColumnIndexOrThrow(PlanDays))
                    aClass.planType = cursor.getString(cursor.getColumnIndexOrThrow(PlanType))
                    aClass.planKcal = cursor.getString(cursor.getColumnIndexOrThrow(PlanKcal))
                    aClass.planMinutes = cursor.getString(cursor.getColumnIndexOrThrow(PlanMinutes))
                    arrPlan.add(aClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }
        return arrPlan
    }

    fun resetPlanDay(strDayId: String) {

        var db: SQLiteDatabase? = null

        val contentValues = ContentValues()
        contentValues.put(UpdatedExTime, "")
        contentValues.put(ReplaceExId, "")

        try {
            db = getReadWriteDB()
            db.update(DayExTable, contentValues, "$DayId = $strDayId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

    }

    fun getCompleteDayCountByPlanId(strId: String): Int {

        var completedCount = 0

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select $DayId From $PlanDaysTable where $PlanId = $strId And $IsCompleted = '1'"
            cursor = db.rawQuery(query, null)
            if (cursor != null) {
                completedCount = cursor.count
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return completedCount
    }

    fun getPlanNameByPlanId(strId: String): String {

        var planName = ""

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select $PlanName From $PlanTable where $PlanId = $strId"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }
        return planName
    }

    fun getPlanDaysList(strPlanId: String): ArrayList<PlanDaysTableClass> {

        val arrPlanDayClasses: ArrayList<PlanDaysTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select * From $PlanDaysTable where $PlanId = $strPlanId"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = PlanDaysTableClass()
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndexOrThrow(DayId))
                    aClass.dayName = cursor.getString(cursor.getColumnIndexOrThrow(DayName))
                    aClass.isCompleted = cursor.getString(cursor.getColumnIndexOrThrow(IsCompleted))
                    aClass.dayProgress = cursor.getString(cursor.getColumnIndexOrThrow(DayProgress))
                    arrPlanDayClasses.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrPlanDayClasses
    }

    fun getPlanDayNameByDayId(strId: String): String {

        var dayName = ""

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select $DayName From $PlanDaysTable where $DayId = $strId"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                dayName = cursor.getString(cursor.getColumnIndexOrThrow(DayName))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return dayName
    }

    fun updatePlanDayCompleteByDayId(strDayId: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(IsCompleted, "1")

        try {
            db = getReadWriteDB()
            mCount = db.update(PlanDaysTable, contentValues, "$DayId = $strDayId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun getDayExList(strDayId: String): ArrayList<DayExTableClass> {

        val arrDayExClass: ArrayList<DayExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "SELECT DX.$DayExId, DX.$PlanId," +
                    "       DX.$DayId," +
                    "       DX.$IsCompleted," +
                    "       CASE WHEN DX.$UpdatedExTime != ''" +
                    "       THEN DX.$UpdatedExTime" +
                    "       ELSE DX.$ExTime" +
                    "       END as $ExTime, " +
                    "       CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END as $ExId, " +
                    "EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit FROM $DayExTable as DX " +
                    "INNER JOIN $ExerciseTable as EX ON " +
                    "(CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END)" +
                    "= EX.$ExId WHERE DX.$DayId = $strDayId"


            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = DayExTableClass()
                    aClass.dayExId = cursor.getString(cursor.getColumnIndexOrThrow(DayExId))
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndexOrThrow(DayId))
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    aClass.isCompleted = cursor.getString(cursor.getColumnIndexOrThrow(IsCompleted))
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exDescription = cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    arrDayExClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrDayExClass
    }

    fun getSingleDayExList(strPlanId: String): ArrayList<DayExTableClass> {

        val arrDayExClass: ArrayList<DayExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "SELECT DX.$DayExId, DX.$PlanId," +
                    "       DX.$DayId," +
                    "       DX.$IsCompleted," +
                    "       CASE WHEN DX.$UpdatedExTime != ''" +
                    "       THEN DX.$UpdatedExTime" +
                    "       ELSE DX.$ExTime" +
                    "       END as $ExTime, " +
                    "       CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END as $ExId, " +
                    "EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit FROM $DayExSingleTable as DX " +
                    "INNER JOIN $ExerciseTable as EX ON " +
                    "(CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END)" +
                    "= EX.$ExId WHERE DX.$PlanId = $strPlanId"


            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = DayExTableClass()
                    aClass.dayExId = cursor.getString(cursor.getColumnIndexOrThrow(DayExId))
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndexOrThrow(DayId))
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    aClass.isCompleted = cursor.getString(cursor.getColumnIndexOrThrow(IsCompleted))
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exDescription = cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    arrDayExClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrDayExClass
    }



    fun updateCompleteExByDayExId(strDayExId: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(IsCompleted, "1")

        try {
            db = getReadWriteDB()
            mCount = db.update(DayExTable, contentValues, "$DayExId = $strDayExId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun getRemindersList(): ArrayList<ReminderTableClass> {
        val arrReminder = ArrayList<ReminderTableClass>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select * From $ReminderTable order by $RId DESC"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val reminderClass = ReminderTableClass()
                    reminderClass.rId = cursor.getString(cursor.getColumnIndexOrThrow(RId))
                    reminderClass.remindTime = cursor.getString(cursor.getColumnIndexOrThrow(RemindTime))
                    reminderClass.days = cursor.getString(cursor.getColumnIndexOrThrow(Days))
                    reminderClass.isActive = cursor.getString(cursor.getColumnIndexOrThrow(IsActive))
                    arrReminder.add(reminderClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrReminder
    }

    fun getReminderById(mid: String): ReminderTableClass {
        val reminderClass = ReminderTableClass()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select * From $ReminderTable where $RId=$mid"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    reminderClass.rId = cursor.getString(cursor.getColumnIndexOrThrow(RId))
                    reminderClass.remindTime = cursor.getString(cursor.getColumnIndexOrThrow(RemindTime))
                    reminderClass.days = cursor.getString(cursor.getColumnIndexOrThrow(Days))
                    reminderClass.isActive = cursor.getString(cursor.getColumnIndexOrThrow(IsActive))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return reminderClass
    }

    fun addReminder(reminderClass: ReminderTableClass): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(RemindTime, reminderClass.remindTime)
        contentValues.put(Days, reminderClass.days)
        contentValues.put(IsActive, reminderClass.isActive)

        try {
            db = getReadWriteDB()
            mCount = db.insert(ReminderTable, null, contentValues).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun deleteReminder(id: String): Boolean {
        var isSuccess = false
        var db: SQLiteDatabase? = null

        try {
            db = getReadWriteDB()
            val mCount = db.delete(ReminderTable, "$RId=?", arrayOf(id))
            if (mCount > 0) {
                isSuccess = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return isSuccess
    }

    fun updateReminder(strReminderId: String, strIsActive: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(IsActive, strIsActive)

        try {
            db = getReadWriteDB()
            mCount = db.update(ReminderTable, contentValues, "$RId = $strReminderId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun updateReminderDays(strReminderId: String, strDays: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(Days, strDays)

        try {
            db = getReadWriteDB()
            mCount = db.update(ReminderTable, contentValues, "$RId = $strReminderId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun updateReminderTimes(strReminderId: String, strTime: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(RemindTime, strTime)

        try {
            db = getReadWriteDB()
            mCount = db.update(ReminderTable, contentValues, "$RId = $strReminderId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun getWorkoutWeeklyData(strCategoryName: String): ArrayList<pWeeklyDayData> {

        val arrPWeeklyDayData = ArrayList<pWeeklyDayData>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            var query = ""

            query =
//                "SELECT $PlanId, group_concat(DISTINCT(CAST($DayName as INTEGER))) as $DayName, $WeekName, $DayId, $IsCompleted from $PlanDaysTable GROUP BY CAST($WeekName as INTEGER)"
                "SELECT  max($DayId) as DayId, $PlanId, group_concat(DISTINCT(CAST($DayName as INTEGER))) as $DayName, $WeekName, $IsCompleted from $PlanDaysTable GROUP BY CAST($WeekName as INTEGER)"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    val aClass = pWeeklyDayData()
                    aClass.Workout_id = cursor.getString(cursor.getColumnIndex(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndex(DayId))
                    aClass.Day_name = cursor.getString(cursor.getColumnIndex(DayName))
                    aClass.Week_name = cursor.getString(cursor.getColumnIndex(WeekName))
                    aClass.Is_completed = cursor.getString(cursor.getColumnIndex(IsCompleted))
                    aClass.categoryName = strCategoryName

                    aClass.arrWeekDayData = getWeekDaysData(aClass.Week_name)

                    val aClass1 = pWeekDayData()
                    aClass1.Day_name = "Cup"

                    if (aClass.Is_completed == "1") {
                        aClass1.Is_completed = "1"
                    } else {
                        aClass1.Is_completed = "0"
                    }

                    aClass.arrWeekDayData.add(aClass1)
                    arrPWeeklyDayData.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
            }
        }
        return arrPWeeklyDayData
    }

    private fun getWeekDaysData(strWeekName: String): ArrayList<pWeekDayData> {

        val arrWeekDayData = ArrayList<pWeekDayData>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {

            db = getReadWriteDB()
            var query = ""

            query = "select $DayName, $DayId ,$IsCompleted FROM $PlanDaysTable " +
                    "WHERE $DayName IN ('1','2','3','4','5','6','7') " +
                    "AND $WeekName = '$strWeekName' GROUP by $DayName"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    val aClass = pWeekDayData()
                    aClass.Day_id = cursor.getString(cursor.getColumnIndex(DayId))
                    aClass.Day_name = cursor.getString(cursor.getColumnIndex(DayName))
                    aClass.Is_completed = cursor.getString(cursor.getColumnIndex(IsCompleted))
                    arrWeekDayData.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
            }
        }

        return arrWeekDayData
    }


    fun getMaxWeight(): String {

        var strMaxWeight = "0"
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val maxkg = "maxkg"
        try {
            db = getReadWriteDB()

            val query = "SELECT MAX(CAST($WeightKg as INTEGER)) as $maxkg from $WeightTable"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    strMaxWeight = cursor.getString(cursor.getColumnIndex(maxkg))

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return strMaxWeight
    }

    fun getMinWeight(): String {

        var strMinWeight = "0"
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val minkg = "minkg"
        try {
            db = getReadWriteDB()

            val query = "SELECT MIN(CAST($WeightKg as INTEGER)) as $minkg from $WeightTable"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    strMinWeight = cursor.getString(cursor.getColumnIndex(minkg))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return strMinWeight
    }

    fun getUserWeightData(): ArrayList<HashMap<String, String>> {

        val arrDateChange = ArrayList<HashMap<String, String>>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()

            val query = "Select * from $WeightTable where $WeightKg != '0' group by $WeightDate order by $WeightDate"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    val hashMap = HashMap<String, String>()
                    hashMap.put("KG", cursor.getString(cursor.getColumnIndexOrThrow(WeightKg)))
                    hashMap.put(
                        "DT",
                        cursor.getString(cursor.getColumnIndexOrThrow(WeightDate))
                    )
                    arrDateChange.add(hashMap)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrDateChange
    }

    fun updateWeight(strDate: String, strWeightKG: String, strWeightLB: String): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(WeightKg, strWeightKG)
        cv.put(WeightLb, strWeightLB)

        try {
            db = getReadWriteDB()

            count = db.update(WeightTable, cv, "$WeightDate = ?", arrayOf(strDate))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    fun addUserWeight(strWeightKG: String, strDate: String, strweightLB: String): Int {
        var count = 0
        var db: SQLiteDatabase? = null

        val row = ContentValues()
        row.put(WeightKg, strWeightKG)
        row.put(WeightDate, strDate)
        row.put(WeightLb, strweightLB)
        row.put(CurrentTimeStamp, CommonUtility.getCurrentTimeStamp())

        try {
            db = getReadWriteDB()
            count = db.insert(WeightTable, null, row).toInt()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return count
    }

    fun weightExistOrNot(strDate: String): Boolean {

        var boolResult = false
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()

            val query = "Select * From $WeightTable Where $WeightDate = '$strDate'"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                boolResult = true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }

            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return boolResult
    }



    fun getHistoryTotalMinutes(): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totMinutesSum = 0

        try {
            db = getReadWriteDB()
            val query = "SELECT SUM(CAST($HCompletionTime as INTEGER)) as $HCompletionTime FROM $HistoryTable"

            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totMinutesSum = cursor.getInt(cursor.getColumnIndex(HCompletionTime))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return totMinutesSum
    }

    fun getHistoryTotalWorkout(): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val totWorkout = "totCompletionTime"
        var totWorkoutSum = 0

        try {
            db = getReadWriteDB()
            val query = "SELECT SUM(CAST($HTotalEx as INTEGER)) as $totWorkout FROM $HistoryTable"

            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totWorkoutSum = cursor.getInt(cursor.getColumnIndex(totWorkout))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return totWorkoutSum
    }

    fun getHistoryTotalKCal(): Float {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totKcalSum = 0f

        try {
            db = getReadWriteDB()
            val query = "SELECT SUM(CAST($HBurnKcal as Float)) as $HBurnKcal FROM $HistoryTable"

            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totKcalSum = cursor.getFloat(cursor.getColumnIndex(HBurnKcal))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return totKcalSum
    }

    fun getCompleteExerciseDate(): ArrayList<String> {

        val arrDt = ArrayList<String>()
        val arrDtTemp = ArrayList<String>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()

            val query = "Select * from $HistoryTable"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (!arrDtTemp.contains
                            (CommonUtility.convertFullDateToDate(cursor.getString(cursor.getColumnIndexOrThrow(HDateTime))))
                    ) {
                        arrDtTemp.add(CommonUtility.convertFullDateToDate(cursor.getString(cursor.getColumnIndexOrThrow(HDateTime))))
                        arrDt.add(cursor.getString(cursor.getColumnIndexOrThrow(HDateTime)))
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrDt
    }

    fun getWeekDayOfHistory(): ArrayList<HistoryWeekDataClass> {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val weekStart = "WeekStart"
        val WeekEnd = "WeekEnd"
        val WeekNumber = "WeekNumber"
        val arrHistoryData = ArrayList<HistoryWeekDataClass>()

        try {
            db = getReadWriteDB()

            val query = "select strftime('%W', ${HDateTime}) $WeekNumber," +
                    "    max(date($HDateTime, 'weekday 0' ,'-6 day')) $weekStart," +
                    "    max(date($HDateTime, 'weekday 0', '-0 day')) $WeekEnd " +
                    "from $HistoryTable " +
                    "group by $WeekNumber"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    val historyWeekDataClass = HistoryWeekDataClass()

                    historyWeekDataClass.weekNumber = cursor.getString(cursor.getColumnIndexOrThrow(WeekNumber))
                    historyWeekDataClass.weekStart = cursor.getString(cursor.getColumnIndexOrThrow(weekStart))
                    historyWeekDataClass.weekEnd = cursor.getString(cursor.getColumnIndexOrThrow(WeekEnd))
                    historyWeekDataClass.totKcal = getTotBurnWeekKcal(historyWeekDataClass.weekStart, historyWeekDataClass.weekEnd)
                    historyWeekDataClass.totTime = getTotWeekWorkoutTime(historyWeekDataClass.weekStart, historyWeekDataClass.weekEnd)

                    historyWeekDataClass.arrhistoryDetail = getWeekHistoryData(historyWeekDataClass.weekStart, historyWeekDataClass.weekEnd)

                    historyWeekDataClass.totWorkout = historyWeekDataClass.arrhistoryDetail.size
                    arrHistoryData.add(historyWeekDataClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrHistoryData
    }

    fun getTotBurnWeekKcal(strWeekStart: String, strWeekEnd: String): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totKcal = 0
        try {
            db = getReadWriteDB()

            val query =
                "SELECT sum($HBurnKcal) as ${HBurnKcal} from $HistoryTable WHERE date('$strWeekStart') <= date($HDateTime) AND date('$strWeekEnd') >= date($HDateTime)"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst()
                totKcal = cursor.getInt(cursor.getColumnIndexOrThrow(HBurnKcal))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return totKcal
    }

    fun getTotWeekWorkoutTime(strWeekStart: String, strWeekEnd: String): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totCompletionTime = 0
        try {
            db = getReadWriteDB()

            val query =
                "SELECT sum($HCompletionTime) as $HCompletionTime from $HistoryTable WHERE date('$strWeekStart') <= date($HDateTime) AND date('$strWeekEnd') >= date($HDateTime)"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst()
                totCompletionTime = cursor.getInt(cursor.getColumnIndexOrThrow(HCompletionTime))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return totCompletionTime
    }

    fun getWeekHistoryData(strWeekStart: String, strWeekEnd: String): ArrayList<HistoryDetailsClass> {

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val arrHistoryWeekDetails = ArrayList<HistoryDetailsClass>()
        try {
            db = getReadWriteDB()
            val query =
                "SELECT * FROM $HistoryTable WHERE date('$strWeekStart') <= date(${HDateTime}) AND date('$strWeekEnd') >= date(${HDateTime}) Order by $HId Desc "

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val historyDetailsClass = HistoryDetailsClass()
                    historyDetailsClass.LevelName = cursor.getString(cursor.getColumnIndexOrThrow(HLvlName))
                    historyDetailsClass.PlanName = cursor.getString(cursor.getColumnIndexOrThrow(HPlanName))
                    historyDetailsClass.DateTime = cursor.getString(cursor.getColumnIndexOrThrow(HDateTime))
                    historyDetailsClass.CompletionTime = cursor.getString(cursor.getColumnIndexOrThrow(HCompletionTime))
                    historyDetailsClass.BurnKcal = cursor.getString(cursor.getColumnIndexOrThrow(HBurnKcal))
                    historyDetailsClass.TotalWorkout = cursor.getString(cursor.getColumnIndexOrThrow(HTotalEx))
                    historyDetailsClass.Kg = cursor.getString(cursor.getColumnIndexOrThrow(HKg))
                    historyDetailsClass.Feet = cursor.getString(cursor.getColumnIndexOrThrow(HFeet))
                    historyDetailsClass.Inch = cursor.getString(cursor.getColumnIndexOrThrow(HInch))
                    historyDetailsClass.FeelRate = cursor.getString(cursor.getColumnIndexOrThrow(HFeelRate))
                    historyDetailsClass.DayName = cursor.getString(cursor.getColumnIndexOrThrow(HDayName))

                    arrHistoryWeekDetails.add(historyDetailsClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return arrHistoryWeekDetails
    }

    // Todo Check User history available or not
    fun isHistoryAvailable(strDate: String): Boolean {
        var dtIsAvailable = false
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()

            val query = "Select $HId From $HistoryTable " +
                    "Where " +
                    "DateTime(strftime('%Y-%m-%d', DateTime($HDateTime)))" +
                    "= " +
                    "DateTime(strftime('%Y-%m-%d', DateTime('$strDate')));"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                dtIsAvailable = true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return dtIsAvailable
    }

    fun addHistory(
        strLevelName: String, strPlanName: String, strDateTime: String, strCompletionTime: String, strBurnKcal: String,
        strTotalWorkout: String, strKg: String, strFeet: String, strInch: String, strFeelRate: String, strDayName: String
    ): Int {

        var db: SQLiteDatabase? = null
        var count = 0

        val cv = ContentValues()
        cv.put(HLvlName, strLevelName)
        cv.put(HPlanName, strPlanName)
        cv.put(HDateTime, strDateTime)
        cv.put(HCompletionTime, strCompletionTime)
        cv.put(HBurnKcal, strBurnKcal)
        cv.put(HTotalEx, strTotalWorkout)
        cv.put(HKg, strKg)
        cv.put(HFeet, strFeet)
        cv.put(HInch, strInch)
        cv.put(HFeelRate, strFeelRate)
        cv.put(HDayName, strDayName)

        try {
            db = getReadWriteDB()
            count = db.insert(HistoryTable, null, cv).toInt()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

        return count
    }


    fun restartProgress() {

        var db: SQLiteDatabase? = null

        val contentValues = ContentValues()
        contentValues.put(IsCompleted, "0")

        try {
            db = getReadWriteDB()
            db.update(DayExSingleTable, contentValues, null, null)

            db.update(DayExTable, contentValues, null, null)

            db.update(PlanDaysTable, contentValues, null, null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                db.close()
                db.releaseReference()
            }
        }

    }

}
