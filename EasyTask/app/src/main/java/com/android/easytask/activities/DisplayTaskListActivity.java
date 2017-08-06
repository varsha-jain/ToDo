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
import android.support.annotation.RequiresApi;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        ToDoItemsDBHelper db = new ToDoItemsDBHelper(this);
        List<Task> items = db.getAllTasks();
        getNotification(items);
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
                    System.out.println("INSIDE ON CLICK FOR LIST ITEM");
                    String selectedItem = listView.getItemAtPosition(position).toString();
                    Task task = getTask(selectedItem);
                    taskID = task.getTaskID();
                    //System.out.println(taskID);
                    taskName = task.getTaskName();
                    taskDsec = task.getDescription();
                    status = task.getStatus();
                    date = task.getDate();
                    priority = task.getPriority();
                    System.out.println(taskID);
                    System.out.println(taskName);
                    System.out.println(taskDsec);
                    System.out.println(status);
                    System.out.println(date);
                    System.out.println(priority);
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
        System.out.println("****" + name[1].trim());
        System.out.println("****" + da[1].trim());
        int month = getMonth(da[1].trim());
        //int month = Integer.parseInt(da[1].trim());
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
        System.out.println("month shown is:" + month);
        //month = month - 1;
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
                i.putExtra("TaskName", taskName);
                i.putExtra("Description", taskDsec);
                i.putExtra("Date", date);
                i.putExtra("Status", status);
                i.putExtra("Priority", priority);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(DisplayTaskListActivity.this, "Task couldn't be updated successfully!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getNotification(List<Task> items){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        System.out.println("INSIDE GET NOTIFICATION METHOD**");

        String currentDate = getCurrentDate();
        String[] currDate = currentDate.split("/");
        int currDay = Integer.parseInt(currDate[0].replaceFirst("^0+(?!$)", ""));
        int currMonth = Integer.parseInt(currDate[1].replaceFirst("^0+(?!$)", ""));
        int currYear = Integer.parseInt(currDate[2].replaceFirst("^0+(?!$)", ""));
        for(Task task : items){
            Calendar cal = Calendar.getInstance();
            System.out.println("INSIDE FOR LOOP***");
            String taskDateRetrieved = task.getDate();
            System.out.println("RETRIEVED DATE OF TASK IS: ***" + taskDateRetrieved);
            String[] dateSplit = taskDateRetrieved.split("/");
            int day = Integer.parseInt(dateSplit[0]);
            int month = Integer.parseInt(dateSplit[1]);
            int year = Integer.parseInt(dateSplit[2]);
            month = month +1;
            if( (day == currDay) && ((month) == currMonth) && (year == currYear)){
                System.out.println("INSIDE IF LOOP");

                cal.set(year,month,day);
                cal.setTimeInMillis(System.currentTimeMillis());
                long time = 2*60*60*1000; //in mili seconds
                System.out.println("YEAR*****"+ cal.YEAR);
                notificationIntent.putExtra("taskName", task.getTaskName());
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), time, broadcast);
            }

        }

    }
    public String getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("CURRENT DATE: " + sdf.format(date));
        return sdf.format(date);
    }


}
