package com.android.easytask.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.easytask.database.ToDoItemsDBHelper;
import com.android.easytask.pojo.Task;
import java.util.List;

import todo.android.com.todo.R;


public class DisplayTaskListActivity extends Activity {

    ListView listView;
    private String taskName = "";
    private String taskDsec = "";
    private String status = "";
    private String date = "";
    private String priority = "";
    private int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        ToDoItemsDBHelper db = new ToDoItemsDBHelper(this);
        List<Task> items = db.getAllTasks();
        if(items.size() == 0){
            Toast.makeText(this, "No tasks added yet!", Toast.LENGTH_LONG).show();
        }else {
            ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
                    android.R.layout.simple_list_item_1, items){
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View itemView = super.getView(position, convertView, parent);
                    String selectedItem = getItem(position).toString();
                    Task task = getTask(selectedItem);
                    String taskStatus = task.getStatus();
                    if (taskStatus.contains("Completed"))
                        itemView.setBackgroundColor(Color.GREEN);
                    else
                        itemView.setBackgroundColor(Color.LTGRAY);
                    return itemView;
                }
            };
            listView = (ListView)findViewById(R.id.list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String selectedItem = listView.getItemAtPosition(position).toString();
                    Task task = getTask(selectedItem);
                    taskID = task.getTaskID();
                    taskName = task.getTaskName();
                    taskDsec = task.getDescription();
                    status = task.getStatus();
                    date = task.getDate();
                    priority = task.getPriority();
                    showDialog();

                }
                @SuppressWarnings("unused")
                public void onClick(View v){
                };
            });
        }
    }

    public Task getTask(String selectedItem){
        String[] array = selectedItem.split("\n");
        String[] name = array[0].split(":");
        String[] da = array[1].split(":");
        String[] pr = array[2].split(":");
        int month = getMonth(da[1].trim());
        month = month - 1;
        String newMonth = String.valueOf(month);
        ToDoItemsDBHelper db = new ToDoItemsDBHelper(DisplayTaskListActivity.this);
        Task task = db.getTask(name[1].trim(), newMonth.trim(), pr[1].trim() );
        return task;
    }

    public int getMonth(String date){
        String[] d = date.split("/");
        int month = Integer.parseInt(d[1]);
        return month;
    }
    public void showDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(DisplayTaskListActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayTaskListActivity.this);
        alertDialogBuilder.setView(promptView);
        final EditText tn = (EditText)promptView.findViewById(R.id.taskNameET);
        final EditText desc = (EditText)promptView.findViewById(R.id.descriptionET);
        final Spinner st = (Spinner)promptView.findViewById(R.id.spinnerStatusET);
        final Spinner pr = (Spinner)promptView.findViewById(R.id.spinnerET);
        final DatePicker dp = (DatePicker)promptView.findViewById(R.id.datePickerET);
        tn.setText(taskName);
        desc.setText(taskDsec);
        st.setSelection(((ArrayAdapter) st.getAdapter()).getPosition(status));
        pr.setSelection(((ArrayAdapter) pr.getAdapter()).getPosition(priority));
        String[] dateSplit = date.split("/");
        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);
        dp.init(year, month, day, null);
        // setup a dialog window

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        taskName = tn.getText().toString();
                        taskDsec = desc.getText().toString();
                        status = String.valueOf(st.getSelectedItem());
                        priority = String.valueOf(pr.getSelectedItem());
                        //DatePicker dp = (DatePicker)findViewById(R.id.datePickerET);
                        String day = String.valueOf(dp.getDayOfMonth());
                        String month = String.valueOf(dp.getMonth());
                        String year = String.valueOf(dp.getYear());
                        date = day + "/" + month + "/" + year;
                        saveTask(tn, desc);

                    }
                })
                .setNegativeButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayTaskListActivity.this);

                                builder .setMessage("Are you sure you want to delete this Task?")
                                        .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                ToDoItemsDBHelper db = new ToDoItemsDBHelper(DisplayTaskListActivity.this);
                                                if(db.deleteTask(taskID)){
                                                    Toast.makeText(DisplayTaskListActivity.this, "Task deleted successfully!!!", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(DisplayTaskListActivity.this, DisplayTaskListActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }else{
                                                    Toast.makeText(DisplayTaskListActivity.this, "Task couldn't be deleted successfully!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void addTask(View view){
        Intent i = new Intent(this, AddTasksActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void saveTask(EditText tn, EditText desc)
    {
        if (taskName.trim().equals("") || taskDsec.trim().equals("")) {
            Toast.makeText(DisplayTaskListActivity.this, "Cannot add empty task!", Toast.LENGTH_LONG).show();
        } else {
            ToDoItemsDBHelper db = new ToDoItemsDBHelper(DisplayTaskListActivity.this);
            Task task = new Task();
            task.setPriority(priority);
            task.setStatus(status);
            task.setDate(date);
            task.setDescription(taskDsec);
            task.setTaskName(taskName);
            task.setTaskID(taskID);
            if(db.updateTask(task, taskID)){
                Toast.makeText(DisplayTaskListActivity.this, "Task updated successfully!!!", Toast.LENGTH_LONG).show();
                tn.setText("");
                desc.setText("");
                Intent i = new Intent(DisplayTaskListActivity.this, DisplayTaskListActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(DisplayTaskListActivity.this, "Task couldn't be updated successfully!!!", Toast.LENGTH_LONG).show();
            }
        }
    }



}
