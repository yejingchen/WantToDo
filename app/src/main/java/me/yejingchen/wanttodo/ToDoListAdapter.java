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

public class ToDoListAdapter extends CursorAdapter implements CheckBox.OnCheckedChangeListener {
    private final static String TAG = "ToDoList";

    private LayoutInflater inflater = null;
    private ListView listView = null;

    private SQLiteDatabase db = null;
    private ContentValues values = null;

    private boolean isCompleted = false;

    public ToDoListAdapter(Context context, Cursor c, ListView listView, SQLiteDatabase db) {
        // 我也不知道0是什么玩意儿，反正要一个 int flag
        super(context, c, 0);

        this.listView = listView;
        this.db = db;

        inflater = LayoutInflater.from(context);
    }

    // 这里是重点，根据checkbox 设置数据库
    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        // final int newValue;
        final int position = listView.getPositionForView(checkBox);

        if (position != ListView.INVALID_POSITION) {
            checkBox.setChecked(isChecked);

            // 获得要更变的数据库 ID
            View parent = (View) checkBox.getParent();
            TextView idtextview = (TextView) parent.findViewById(R.id.ToDoItemDBID);
            String dbIDstr = idtextview.getText().toString();

            // newValue = isChecked ? 1 : 0;

            // 准备要更新的内容
            values = new ContentValues();
            values.put(ToDoListContract.ToDoList.COLUMN_NAME_IS_FINISHED, isChecked);

            // 找到要变更的条目并更新
            String selection = ToDoListContract.ToDoList._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(dbIDstr) };
            db.update(
                    ToDoListContract.ToDoList.TABLE_NAME, values,
                    selection, selectionArgs);
        }

        // refreshUI
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.i(TAG, "Inside ToDoListAdapter.newView()");
        // Create and inflate the listView item view here
        View view = inflater.inflate(R.layout.to_do_list_item_layout, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.checkBox = (CheckBox) view.findViewById(R.id.ToDoItemFinished);
        holder.textView = (TextView) view.findViewById(R.id.ToDoItemText);
        holder.button = (Button) view.findViewById(R.id.ToDoItemDelete);

        // holder.checkBox.setOnCheckedChangeListener(this);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // final int newValue;
                final int position = listView.getPositionForView(buttonView);
                Log.i(TAG, "Inside onCheckedChanged()");
                Log.i(TAG, "checkbox position=" + position + ", ListView.INVALID_POSITION=" + ListView.INVALID_POSITION);

                if (position != ListView.INVALID_POSITION) {
                    Log.i(TAG, "checkbox position=" + position + ", ListView.INVALID_POSITION=" + ListView.INVALID_POSITION);
                    buttonView.setChecked(isChecked);

                    // 获得要更变的数据库 ID
                    View parent = (View) buttonView.getParent();
                    TextView idtextview = (TextView) parent.findViewById(R.id.ToDoItemDBID);
                    String dbIDstr = idtextview.getText().toString();

                    // newValue = isChecked ? 1 : 0;

                    // 准备要更新的内容
                    values = new ContentValues();
                    values.put(ToDoListContract.ToDoList.COLUMN_NAME_IS_FINISHED, isChecked);

                    // 找到要变更的条目并更新
                    String selection = ToDoListContract.ToDoList._ID + " LIKE ?";
                    String[] selectionArgs = { String.valueOf(dbIDstr) };
                    db.update(
                            ToDoListContract.ToDoList.TABLE_NAME, values,
                            selection, selectionArgs);
                }

                // refreshUI
            }
        });

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int tmp;

        // 将 cursor 中的数据绑定到返回的 view 中
        holder.checkBox.setOnCheckedChangeListener(null);
        tmp = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoList.COLUMN_NAME_IS_FINISHED));
        isCompleted = (tmp > 0);
        holder.checkBox.setChecked(isCompleted);
        holder.checkBox.setOnCheckedChangeListener(this);

        holder.textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToDoList.COLUMN_NAME_WHAT_TO_DO)));

        //Log.e(TAG, cursor.)
    }

    private static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
        Button button;
    }
}
