package me.yejingchen.wanttodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import me.yejingchen.wanttodo.ToDoListContract;
import me.yejingchen.wanttodo.ToDoListContract.*;

public class ToDoListAdapter extends CursorAdapter implements  CheckBox.OnCheckedChangeListener {
    private final static String TAG = "ToDoList";

    private LayoutInflater inflater = null;
    private ListView listView = null;

    private Context context = null;

    private SQLiteDatabase db = null; // 在构造器处得到

    public ToDoListAdapter(Context context, Cursor c, ListView listView, SQLiteDatabase db) {
        // 我也不知道0是什么玩意儿，反正要一个 int flag
        super(context, c, 0);

        this.context = context;

        this.listView = listView;
        this.db = db;

        inflater = LayoutInflater.from(context);
    }

    // 这里是重点，根据checkbox 设置数据库
    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        Log.i(TAG, "它喵的终于来到了 onCheckedChanged()");
        // final int newValue;
        final int position = listView.getPositionForView(checkBox);

        if (position != ListView.INVALID_POSITION) {
            checkBox.setChecked(isChecked);

            // 获得要更变的数据库条目 ID
            View parent = (View) checkBox.getParent();
            TextView idtextview = (TextView) parent.findViewById(R.id.ToDoItemDBID);
            String dbIDstr = idtextview.getText().toString();

            // newValue = isChecked ? 1 : 0;

            // 准备要更新的内容
            ContentValues values = new ContentValues();
            values.put(ToDoListContract.ToDoList.COLUMN_NAME_IS_FINISHED, isChecked);

            // 找到要变更的条目并更新
            String selection = ToDoListContract.ToDoList._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(dbIDstr) };
            db.update(
                    ToDoListContract.ToDoList.TABLE_NAME, values,
                    selection, selectionArgs);
        }
    }

    /**
     * deletion 操作完成后刷新列表
     */
    private void updateUI() {
        String sortOrder = "_id DESC";

        String[] projection = {
                ToDoList._ID,
                ToDoList.COLUMN_NAME_WHAT_TO_DO,
                ToDoList.COLUMN_NAME_IS_FINISHED
        };
        Cursor cursor = db.query(
                ToDoList.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        ToDoListAdapter adapter = new ToDoListAdapter(context, cursor, listView, db);
        listView.setAdapter(adapter);

        // cursor.close();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.i(TAG, "Inside ToDoListAdapter.newView()");
        // Create and inflate the listView item view here
        View view = inflater.inflate(R.layout.to_do_list_item_layout, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.checkBox = (CheckBox) view.findViewById(R.id.ToDoItemFinished);
        holder.idTextView = (TextView) view.findViewById(R.id.ToDoItemDBID);
        holder.textView = (TextView) view.findViewById(R.id.ToDoItemText);
        holder.button = (Button) view.findViewById(R.id.ToDoItemDelete);

        holder.checkBox.setOnCheckedChangeListener(this);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获得要删除的数据库 ID
                View parent = (View) v.getParent();
                TextView idtextview = (TextView) parent.findViewById(R.id.ToDoItemDBID);
                String dbIDstr = idtextview.getText().toString();
                // long dbID = Integer.getInteger(dbIDstr);

                // 选中要删除行的条件
                String selection = ToDoList._ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(dbIDstr)};

                db.delete(ToDoList.TABLE_NAME, selection, selectionArgs);

                // TODO: 15-10-5 deletion 完成后刷新界面
                updateUI();
            }
        });
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int tmp;

        holder.idTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoList._ID)));;

        // 将 cursor 中的数据绑定到返回的 view 中
        holder.checkBox.setOnCheckedChangeListener(null);
        tmp = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoList.COLUMN_NAME_IS_FINISHED));
        boolean isCompleted = (tmp > 0);
        holder.checkBox.setChecked(isCompleted);
        holder.checkBox.setOnCheckedChangeListener(this);

        holder.textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoList.COLUMN_NAME_WHAT_TO_DO)));

        //Log.e(TAG, cursor.)
    }

    private static class ViewHolder {
        CheckBox checkBox;
        TextView idTextView;
        TextView textView;
        Button button;
    }
}
