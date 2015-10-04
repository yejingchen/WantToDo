package me.yejingchen.wanttodo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.List;

import static me.yejingchen.wanttodo.ToDoListContract.*;

public class MainActivity extends Activity {
    /**
     * Created by yejingchen on 15-10-3.
     */

    // 创建一个helper以便管理数据库
    // ToDoListContract.ToDoListDBHelper mDbHelper;//  = new ToDoListContract.ToDoListDBHelper(MainActivity.this);
    // SQLiteDatabase db;// = mDbHelper.getWritableDatabase();
    // SQLiteDatabase db = new ToDoListContract.ToDoListDBHelper(this).getWritableDatabase();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ========== RECOVERY BEGIN
        ToDoListContract.ToDoListDBHelper mDbHelper = new ToDoListDBHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoList.COLUMN_NAME_WHAT_TO_DO, "开通支付宝");
        values.put(ToDoList.COLUMN_NAME_IS_FINISHED, false);
        db.insert(ToDoList.TABLE_NAME, null, values);

        values.put(ToDoList.COLUMN_NAME_WHAT_TO_DO, "买草稿");
        values.put(ToDoList.COLUMN_NAME_IS_FINISHED, false);
        db.insert(ToDoList.TABLE_NAME, null, values);

        String[] projection = {
                ToDoList._ID,
                ToDoList.COLUMN_NAME_WHAT_TO_DO,
                ToDoList.COLUMN_NAME_IS_FINISHED
        };

        String sortOrder = ToDoList._ID + " DESC";

        Cursor c = db.query(
                ToDoList.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        String[] todolist = new String[20];
        int i = 0;

        // 逐条将数据库里的 todoitem 填到 todolist[] 里
        String todoEntry;
        if (c.moveToFirst()) {
            Log.d("AAAAAA", "进入if (c.moveToFirst())");
            do {
                todoEntry = c.getString(c.getColumnIndexOrThrow(ToDoList.COLUMN_NAME_WHAT_TO_DO));
                Log.i("aPP", todoEntry);
                todolist[i++] = todoEntry;
            } while (c.moveToNext() && i < 20);
            Log.d("AAAAAA", "离开if (c.moveToFirst())");
        }
        c.close();

        // 设置 adapter 将 todolist 显示在 toDoListView 里
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, todolist);

        ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
        toDoListView.setAdapter(adapter);
        Log.d("AAAAAAAAA", "adapter 已设置");


        // ========== RECOVERY END
        //refreshToDoListView();

        Log.i("app", "View refreshed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                // openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String str = data.getStringExtra("todoitemtext");
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        }
    }
    */

    /**
     * 将文本框的内容添加到数据库中
     *
     * @return 是否成功完成
     * @view: 调用此回调的 add 按钮
     */
    /*
    public void addToDoItem(View view) {
        EditText toDoItemText = (EditText) findViewById(R.id.toDoItemText);
        String text = toDoItemText.getText().toString();
        insertToDoItem(text);

        // ============
        // refreshToDoListView();
    }
    */
// ==============
/*
    void refreshToDoListView() {
        // 尝试从数据库读取信息
        mDbHelper = new ToDoListContract.ToDoListDBHelper(MainActivity.this);
        db = mDbHelper.getWritableDatabase();

        String[] projection = {
                ToDoList._ID,
                ToDoList.COLUMN_NAME_WHAT_TO_DO,
                ToDoList.COLUMN_NAME_IS_FINISHED
        };

        String sortOrder = "_id DESC";

        Cursor c = db.query(
                ToDoList.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        int i = 0;

        // 逐条将数据库里的 todoitem 填到 todolist[] 里
        String todoEntry;
        if (c.moveToFirst()) {
            do {
                todoEntry = c.getString(c.getColumnIndexOrThrow(ToDoList.COLUMN_NAME_WHAT_TO_DO));
                Log.i("aPP", todoEntry);
                todolist[i++] = todoEntry;
            } while (c.moveToNext() && i < 20);
        }
        c.close();

        // 设置 adapter 将 todolist 显示在 toDoListView 里
        if (todolist[0] != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, todolist);

            ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
            toDoListView.setAdapter(adapter);
        }
    }
    */

    /**
     * 将 toDoItemText 插入到默认数据库
     * @param toDoItemText
     * @return
     */
    /*
    long insertToDoItem(String toDoItemText) {
        ContentValues values = new ContentValues();
        values.put(ToDoList.COLUMN_NAME_WHAT_TO_DO, toDoItemText);
        values.put(ToDoList.COLUMN_NAME_IS_FINISHED, false);

        return db.insert(ToDoList.TABLE_NAME, null, values);
    }
    */

}

