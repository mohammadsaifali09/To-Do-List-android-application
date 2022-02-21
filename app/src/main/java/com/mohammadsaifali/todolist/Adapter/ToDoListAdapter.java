package com.mohammadsaifali.todolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammadsaifali.todolist.AddNewTask;
import com.mohammadsaifali.todolist.DatabaseHandler;
import com.mohammadsaifali.todolist.MainActivity;
import com.mohammadsaifali.todolist.Model.ToDoListModel;
import com.mohammadsaifali.todolist.R;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private List<ToDoListModel> toDoListModels;
    private MainActivity activity;
    private DatabaseHandler databaseHandler;

    public ToDoListAdapter(DatabaseHandler databaseHandler,MainActivity activity){
        this.databaseHandler=databaseHandler;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
      public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        databaseHandler.openDatabase();
        final ToDoListModel item=toDoListModels.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    databaseHandler.updateStatus(item.getId(), 1);
                } else {
                    databaseHandler.updateStatus(item.getId(), 0);
                }
            }
        });
        }

    private boolean toBoolean(int n) {
        return n != 0;
    }

        @Override
        public int getItemCount(){
          return toDoListModels.size();
        }

        public Context getContext(){
        return activity;
        }

        public void setTasks(List<ToDoListModel> todoList){
        this.toDoListModels=todoList;
        notifyDataSetChanged();
        }

        public void deleteItem(int position){
        ToDoListModel item=toDoListModels.get(position);
        databaseHandler.deleteTask(item.getId());
        toDoListModels.remove(position);
        notifyItemRemoved(position);
        }

        public void editItem(int position){
        ToDoListModel item=toDoListModels.get(position);
            Bundle bundle=new Bundle();
            bundle.putInt("id",item.getId());
            bundle.putString("task",item.getTask());
            AddNewTask fragment=new AddNewTask();
            fragment.setArguments(bundle);
            fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
        }
        public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task=view.findViewById(R.id.checkBoxTask);
        }
    }
}
