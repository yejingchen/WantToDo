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
    ToDoListContract.ToDoListDBHelper mDbHelper;//  = new ToDoListContract.ToDoListDBHelper(MainActivity.this);
    SQLiteDatabase db;// = mDbHelper.getWritableDatabase();
    // SQLiteDatabase db = new ToDoListContract.ToDoListDBHelper(this).getWritableDatabase();
    Cursor cursor;
    long last_id_inserted;

    // 旧版 ArrayAdapter 时用的数组
    // String[] todolist;

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

    /**
     * 将 todoitem 从数据库移除
     *
     * @param view 调用此回调的 view
     */
    public void removeToDoItem(View view) {
        // 获得要删除的数据库 ID
        View parent = (View) view.getParent();
        TextView idtextview = (TextView) parent.findViewById(R.id.ToDoItemDBID);
        String dbIDstr = idtextview.getText().toString();
        // long dbID = Integer.getInteger(dbIDstr);

        // 选中要删除行的条件
        String selection = ToDoList._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(dbIDstr) };

        db.delete(ToDoList.TABLE_NAME, selection, selectionArgs);

        refreshToDoListView();
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

/*
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.to_do_list_item_layout, c,
                ToDoItemAdapter.fromColumns, ToDoItemAdapter.toViews, 0);

        ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
        toDoListView.setAdapter(adapter);
*/

        ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
        ToDoListAdapter adapter = new ToDoListAdapter(MainActivity.this, cursor, toDoListView, db);
        toDoListView.setAdapter(adapter);

        /* 旧版的 ArrayAdapter 方式
        int i = 0;

        // todolist 的大小与 todoitems 的数目相同
        // 因为取出的数据按逆序排列，所以 moveToFirst 后 ID 是最大的
        if (c.moveToFirst()) {
            int tmp = c.getInt(c.getColumnIndexOrThrow(ToDoList._ID));
            todolist = new String[tmp];
        } else {
            todolist = new String[20];
        }

        // 逐条将数据库里的 todoitem 填到 todolist[] 里
        if (c.moveToFirst()) {
            do {
                todolist[i++] = c.getString(c.getColumnIndexOrThrow(ToDoList.COLUMN_NAME_WHAT_TO_DO));
            } while (c.moveToNext());
        }
        c.close();

        // 设置 adapter 将 todolist 显示在 toDoListView 里
        if (todolist[0] != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, todolist);

            ListView toDoListView = (ListView) findViewById(R.id.toDoListView);
            toDoListView.setAdapter(adapter);
        }
        */
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

    // 貌似不可用的 checkbox 回调
    /* public void onCheckBoxChange(View view) {
        // 获取 checkbox 状态
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.ToDoItemFinished);
        boolean finished = checkBox.isChecked();
        // 貌似 checkbox 没有改变自己的状态，手动改变一下
        // checkBox.setChecked(!finished);

        // 获得要更变的数据库 ID
        View parent = (View) view.getParent();
        TextView idtextview = (TextView) parent.findViewById(R.id.ToDoItemDBID);
        String dbIDstr = idtextview.getText().toString();

        // 准备要变更的数据
        ContentValues values = new ContentValues();
        values.put(ToDoList.COLUMN_NAME_IS_FINISHED, finished);

        // 找到要变更的条目并更新
        String selection = ToDoList._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(dbIDstr) };
        db.update(ToDoList.TABLE_NAME, values, selection, selectionArgs);

        refreshToDoListView();
    }
    */
}