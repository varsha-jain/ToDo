package com.android.easytask.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.android.easytask.pojo.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by varsha on 8/1/2017.
 */

public class ToDoItemsDBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "tasklist";
    // table name
    private static final String TABLE_TASK = "Task";

    private static final String KEY_TASKID = "task_id";
    private static final String KEY_TASK_NAME = "taskName";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_DATE = "date";

    public ToDoItemsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "(" + KEY_TASKID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TASK_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_DATE + " TEXT,"
                + KEY_STATUS + " TEXT," + KEY_PRIORITY + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_TASK);
        // Drop older table if existed
        onCreate(sqLiteDatabase);
    }

    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, task.getTaskName());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_DATE, task.getDate());
        values.put(KEY_STATUS, task.getStatus());
        values.put(KEY_PRIORITY, task.getPriority());
        db.insert(TABLE_TASK, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TASK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTaskID(Integer.parseInt(cursor.getString(0)));
                task.setTaskName(cursor.getString(1));
                task.setDescription((cursor.getString(2)));
                String dateFromDB = cursor.getString(3);
                String[] d = dateFromDB.split("/");
                int m = Integer.parseInt(d[1]);
                m = m+1;
                String newMonth = String.valueOf(m);
                String finalDateToBeDispayed = d[0]+"/"+newMonth+"/"+d[2];
                task.setDate(finalDateToBeDispayed);
                task.setStatus((cursor.getString(4)));
                task.setPriority((cursor.getString(5)));

                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return task list
        return taskList;
    }

    public Task getTask(String taskName, String date, String priority){
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        Task task = new Task();
        try{
            String query = "SELECT * FROM "+ TABLE_TASK
                    + " WHERE "+ KEY_TASK_NAME  + " like " + "'%" + taskName + "%'"
                    + " and " + KEY_DATE +  " like " + "'%" + date + "%'"
                    + " and " + KEY_PRIORITY + " like " + "'%" + priority + "%'";
            cursor = db.rawQuery(query, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                System.out.println("****INSIDE DBHELPER CLASS GETUSER METHOD*****");
                task.setTaskID(Integer.parseInt(cursor.getString(0)));
                task.setTaskName(cursor.getString(1));
                task.setDescription((cursor.getString(2)));
                task.setDate((cursor.getString(3)));
                task.setStatus((cursor.getString(4)));
                task.setPriority((cursor.getString(5)));
            }

        }catch(Exception e){
            System.out.println(e);
        }finally{
            cursor.close();
        }
        return task;
    }

    public boolean updateTask(Task task, int taskID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_TASK_NAME, task.getTaskName());
        args.put(KEY_DESCRIPTION, task.getDescription());
        args.put(KEY_DATE, task.getDate());
        args.put(KEY_STATUS, task.getStatus());
        args.put(KEY_PRIORITY, task.getPriority());
        return db.update(TABLE_TASK, args, KEY_TASKID + "=" + taskID, null) > 0;
    }

    public boolean deleteTask(int taskID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TASK, KEY_TASKID + "=" + taskID, null) > 0;
    }

}
