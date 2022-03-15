package com.example.testing;

import static java.lang.String.valueOf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class pictureFolderAdapter extends RecyclerView.Adapter<FolderHolder>{

    Activity activity;
    private ArrayList<imageFolder> folders;
    MainViewModel mainViewModel;

    SharedPreferences sharedPreferences;
    private Context folderContx;
    private itemClickListener listenToClick;
    boolean isSelected =false;
    boolean isenable =false;

    private ArrayList<String> sa;
    public ArrayList<String> selectlist = new ArrayList<>();




    public pictureFolderAdapter(ArrayList<imageFolder> folders, Context folderContx, itemClickListener listen) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.picture_folder_item, parent, false);
        return new FolderHolder(cell);

      //  mainViewModel= ViewModelProviders.of(FragmentActivity) activity).folders.get();

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final imageFolder folder = folders.get(position);
//        Glide.with(folderContx)
//                .load(folder.getFirstPic())
//                .apply(new RequestOptions().centerCrop())
//                .into(holder.folderPic);
        //setting the number of images
        String text = ""+folder.getFolderName();
        String folderSizeString=""+folder.getNumberOfPics()+" Images";


        holder.folderSize.setText(folderSizeString);
        holder.folderName.setText(text);

        holder.folderPic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                listenToClick.onPicClicked(folder.getPath(),folder.getFolderName());
            }
        });
        holder.folderPic.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view)
            {
                if(!isenable)
                {
                    ActionMode.Callback callback = new ActionMode.Callback()
                    {

                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                            MenuInflater menuInflater=actionMode.getMenuInflater();
                            menuInflater.inflate(R.menu.foldermenu,menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu)
                        {
                            isenable=true;
                            Clickitems(holder);
//                         //  mainViewModel.getText().observe((LifecycleOwner) activity,new Observer<String>()
//                           {
//                               public void onChanged(String s)
//                               {
//                                   actionMode.setTitle(String.format("%s Selected ","12"));
//                               }
//                           });
                           return false;
                        }

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem)
                        {
                            int id =menuItem.getItemId();
                            Dialog dialogimg = new Dialog(view.getContext());
                            dialogimg.setContentView(R.layout.deleteimagepopup);
                            dialogimg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialogimg.setCancelable(false);
                            dialogimg.getWindow().getAttributes().windowAnimations= R.style.Theme_Testing;
                            TextView cancl = dialogimg.findViewById(R.id.canclimgbtn);
                            TextView del = dialogimg.findViewById(R.id.delimgbtn);

                            switch(id)
                            {
                                case R.id.menu_delete:

                                    dialogimg.show();
                                    cancl.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogimg.hide();
                                            actionMode.finish();
                                        }
                                    });

                                    del.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view)
                                        {
//                                            signal="ok";
                                            for(int i=0; i < selectlist.size();i++ )
                                            {
                                                selectlist.remove(i);
                                            }
                                            if(folders.size()== 0)
                                            {
                                                Toast.makeText(folderContx,"avv",Toast.LENGTH_LONG).show();
                                            }
                                            actionMode.finish();
                                        }
                                    });

//                                    for (int i=0;i<selectedpicturelist.size();i++)
//                                    {
//                                        File file = new File(selectedpicturelist.get(i));
//                                        deleteimgRecursive(file);
//                                    }
//                                    if(selectedpicturelist.size() == 0)
//                                    {
//                                    }
                                    actionMode.finish();
                                    break;

                                case R.id.selctall:
                                    if(selectlist.size()==folders.size())
                                    {
                                        isSelected=false;
                                        selectlist.clear();
                                    }
                                    else
                                    {
                                        isSelected=true;
                                        selectlist.clear();
                                        //   selectlist.addAll(folders.t);

                                    }
                                  //  mainViewModel.setText(valueOf(selectlist.size()));
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode)
                        {
                            isenable =false;
                            isSelected= false;
                            selectlist.clear();
                            notifyDataSetChanged();
                        }
                    };

                    ((AppCompatActivity)view.getContext()).startActionMode(callback);
                }
                else
                {
                  Clickitems(holder);
                }
                return true;
            }

            private ArrayList Clickitems(FolderHolder holder)
            {
                String s= folders.get(holder.getAdapterPosition()).getPath();
                if(holder.ivcheckbox.getVisibility()== View.GONE)
                {
                    holder.ivcheckbox.setVisibility(View.VISIBLE);
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    selectlist.add(s);
                }
                else
                {
                    holder.ivcheckbox.setVisibility(View.GONE);
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    selectlist.remove(s);
                }
                return selectlist;
//        mainViewModel.setText(valueOf(selectlist.size()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


}
