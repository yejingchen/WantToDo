package me.yejingchen.wanttodo;

public class ToDoItemAdapter {
    public static final String[] fromColumns = {
            ToDoListContract.ToDoList.COLUMN_NAME_IS_FINISHED,
            ToDoListContract.ToDoList._ID,
            ToDoListContract.ToDoList.COLUMN_NAME_WHAT_TO_DO
            //ToDoListContract.ToDoList.COLUMN_NAME_IS_FINISHED
    };

    public static final int[] toViews = {
            R.id.ToDoItemFinished,
            R.id.ToDoItemDBID,
            R.id.ToDoItemText
    };
}