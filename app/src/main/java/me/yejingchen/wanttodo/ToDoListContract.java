package me.yejingchen.wanttodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class ToDoListContract {
    public ToDoListContract() {}

    // define table contents
    public static abstract class ToDoList implements BaseColumns {
        public static final String TABLE_NAME = "todolist";
        public static final String COLUMN_NAME_WHAT_TO_DO = "whattodo";
        public static final String COLUMN_NAME_IS_FINISHED = "finished";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ToDoList.TABLE_NAME + " (" +
                    ToDoList._ID + " INTEGER PRIMARY KEY," +
                    ToDoList.COLUMN_NAME_WHAT_TO_DO + TEXT_TYPE + COMMA_SEP +
                    ToDoList.COLUMN_NAME_IS_FINISHED + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + ToDoList.TABLE_NAME;

    public static class ToDoListDBHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "ToDoList.db";

        public ToDoListDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_TABLE);
            onCreate(db);
        }
    }
}
