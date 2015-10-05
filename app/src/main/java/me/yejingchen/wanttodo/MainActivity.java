package me.yejingchen.wanttodo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static me.yejingchen.wanttodo.ToDoListContract.*;

public class MainActivity extends Activity {
    // 创建一个helper以便管理数据库
    ToDoListContract.ToDoListDBHelper mDbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    long last_id_inserted;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshToDoListView();
    }

    @Override
    public void onDestroy() {
        db.close();
        cursor.close();
        super.onDestroy();
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

    /**
     * 将文本框的内容添加到数据库中
     *
     * @param view : 调用此回调的 add 按钮
     */
    public void addToDoItem(View view) {
        EditText toDoItemText = (EditText) findViewById(R.id.toDoItemText);
        String text = toDoItemText.getText().toString();
        last_id_inserted = insertToDoItem(text);

        refreshToDoListView();

        toDoItemText.setText("");
    }

    public void refreshToDoListView() {
        // 尝试从数据库读取信息
        mDbHelper = new ToDoListContract.ToDoListDBHelper(MainActivity.this);
        db = mDbHelper.getWritableDatabase();
        String sortOrder = "_id DESC";

        String[] projection = {
                ToDoList._ID,
                ToDoList.COLUMN_NAME_WHAT_TO_DO,
                ToDoList.COLUMN_NAME_IS_FINISHED
        };
        cursor = db.query(
                ToDoList.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
        ToDoListAdapter adapter = new ToDoListAdapter(MainActivity.this, cursor, toDoListView, db);
        toDoListView.setAdapter(adapter);
    }

    /**
     * 将 toDoItemText 插入到默认数据库
     *
     * @param toDoItemText 代办事项的文字
     * @return 新增的条目在数据库的编号
     */
    long insertToDoItem(String toDoItemText) {
        ContentValues values = new ContentValues();
        values.put(ToDoList.COLUMN_NAME_WHAT_TO_DO, toDoItemText);
        values.put(ToDoList.COLUMN_NAME_IS_FINISHED, false);

        return db.insert(ToDoList.TABLE_NAME, null, values);
    }
}