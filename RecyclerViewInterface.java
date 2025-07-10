package com.zybooks.projecttwo;


// A custom interface that declares two methods but are defined elsewhere (displayDatabaseInfo java file)
public interface RecyclerViewInterface {
    void onDeleteButtonClick(int position);
    void onClickForUpdate(int position);
}
