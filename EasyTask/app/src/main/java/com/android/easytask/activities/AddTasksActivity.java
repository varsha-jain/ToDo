package com.android.easytask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.easytask.database.ToDoItemsDBHelper;
import com.android.easytask.pojo.Task;

import todo.android.com.todo.R;


public class AddTasksActivity extends AppCompatActivity {

    private String taskName = "";
    private String taskDsec = "";
    private String status = "";
    private String date = "";
    private String priority = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        Intent i = getIntent();
    }
    public void saveTask(View view){
        EditText tn = (EditText)findViewById(R.id.taskNameET);
        EditText desc = (EditText)findViewById(R.id.descriptionET);
        Spinner st = (Spinner)findViewById(R.id.spinnerStatus);
        Spinner pr = (Spinner)findViewById(R.id.spinnerET);

        taskName = tn.getText().toString();
        taskDsec = desc.getText().toString();
        status = String.valueOf(st.getSelectedItem());
        priority = String.valueOf(pr.getSelectedItem());
        DatePicker dp = (DatePicker)findViewById(R.id.datePickerET);
        String day = String.valueOf(dp.getDayOfMonth());
        String month = String.valueOf(dp.getMonth());

        String year = String.valueOf(dp.getYear());
        date = day + "/" + (month) + "/" + year;

        if(taskName.trim().equals("") || taskDsec.trim().equals("")){
            Toast.makeText(this,"Cannot add empty task!", Toast.LENGTH_LONG).show();
        }else{
            ToDoItemsDBHelper db = new ToDoItemsDBHelper(this);
            Task task = new Task();
            task.setPriority(priority);
            task.setStatus(status);
            task.setDate(date);
            task.setDescription(taskDsec);
            task.setTaskName(taskName);
            db.addTask(task);
            Toast.makeText(this,"Task added successfully!!!", Toast.LENGTH_LONG).show();

            tn.setText("");
            desc.setText("");
            Intent i = new Intent(this, DisplayTaskListActivity.class);
            startActivity(i);
            finish();

        }

    }
}
