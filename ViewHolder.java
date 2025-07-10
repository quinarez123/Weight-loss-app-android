package com.zybooks.projecttwo;

import com.zybooks.projecttwo.MyAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// A view holder to display each view instance.
public class ViewHolder extends RecyclerView.ViewHolder {

    TextView dateView,weightView,commentView;
    Button deleteButton;
    public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        deleteButton = (Button) itemView.findViewById(R.id.deleteButton);


// A listener for when the user taps on a view list item. This prompt a screen to edit their logs
itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (recyclerViewInterface != null) {
            int position = getAbsoluteAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                recyclerViewInterface.onClickForUpdate(position);
            }
        }
    }
});


// Delete button listener used for when the user taps the delete button on a specific row
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View view) {
                if (recyclerViewInterface != null) {
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onDeleteButtonClick(position);
                    }
                }
            }
        });


        dateView = (TextView) itemView.findViewById(R.id.dateView);
        weightView = (TextView) itemView.findViewById(R.id.weightView);
        commentView = (TextView) itemView.findViewById(R.id.commentView);


    }
}
