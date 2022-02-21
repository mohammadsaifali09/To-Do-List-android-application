package com.mohammadsaifali.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mohammadsaifali.todolist.Model.ToDoListModel;

import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG="ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;

    private DatabaseHandler databaseHandler;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@NonNull ViewGroup container,@NonNull Bundle saveInstanceState){
        View view=inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,@NonNull Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        // problem
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.newtask);
        newTaskSaveButton = getView().findViewById(R.id.newTaskSave);

        boolean isUpdate=false;
        final Bundle bundle=getArguments();
        if(bundle!=null){
            isUpdate=true;
            String task=bundle.getString("task");
            newTaskText.setText(task);
            assert task != null;
            if (task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.app));
        }

        databaseHandler=new DatabaseHandler(getActivity());
        databaseHandler.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.app));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate=isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=newTaskText.getText().toString();
                if (finalIsUpdate){
                    databaseHandler.updateTask(bundle.getInt("id"),text);
                }
                else {
                    ToDoListModel task=new ToDoListModel();
                    task.setTask(text);
                    task.setStatus(0);
                    databaseHandler.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Log.d("save button", "onClick: saved");
        Activity activity=getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
