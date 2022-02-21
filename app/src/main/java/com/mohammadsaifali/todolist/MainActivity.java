package com.mohammadsaifali.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mohammadsaifali.todolist.Adapter.ToDoListAdapter;
import com.mohammadsaifali.todolist.Model.ToDoListModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView recyclerView;
    private ToDoListAdapter adapter;
    private FloatingActionButton floatingActionButton;

    private List<ToDoListModel> taskList;

    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        databaseHandler=new DatabaseHandler(this);
        databaseHandler.openDatabase();


        recyclerView=findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ToDoListAdapter(databaseHandler,MainActivity.this);
        recyclerView.setAdapter(adapter);

        /* taskList=new ArrayList<>();

        ToDoListModel task =new ToDoListModel();
        task.setTask("This is a Test task");
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        adapter.setTasks(taskList);*/

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        floatingActionButton=findViewById(R.id.addButton);

        taskList=databaseHandler.getAllTasks();
        Collections.reverse(taskList);
        adapter.setTasks(taskList);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList=databaseHandler.getAllTasks();
        Collections.reverse(taskList);
        adapter.setTasks(taskList);
        adapter.notifyDataSetChanged();
    }
}