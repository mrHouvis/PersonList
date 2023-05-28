package com.example.personlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private Context context;
    private ArrayList<Model> arrayList;
    //dataBase Object
    DatabaseHelper databaseHelper;

    public Adapter(Context context, ArrayList<Model> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
        databaseHelper = new DatabaseHelper(context);

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        Model model = arrayList.get(position);

        //get for view
        final String id = model.getId();
        final byte[] image = model.getImage();
        final String name = model.getName();
        final String age = model.getAge();
        final String phone = model.getPhone();
        final String addTimeStamp = model.getAddTimeStamp();
        final String updateTimeStamp = model.getUpdateTimeStamp();

        //set views
        Bitmap bmp;
        bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.profileIv.setImageBitmap(bmp);

        holder.name.setText(name);
        holder.age.setText(age);
        holder.phone.setText(phone);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog(
                        "" + position,
                        "" + id,
                        "" + name,
                        "" + age,
                        "" + phone,
                        image,
                        "" + addTimeStamp,
                        "" + updateTimeStamp
                );
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog(id);
            }
        });

    }

    private void deleteDialog(final String id){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you want to delete?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_delete);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper.deleteInfo(id);
                ((MainActivity)context).onResume();
                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.create().show();

    }



    private void editDialog(String position, String id, String name, String age, String phone, byte[] image, String addTimeStamp, String updateTimeStamp) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update");
        builder.setMessage("Are you want update?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_edit);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(context, EditRecordActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("AGE", age);
                intent.putExtra("PHONE", phone);
                intent.putExtra("IMAGE", image);
                intent.putExtra("ADD_TIMESTAMP", addTimeStamp);
                intent.putExtra("UPDATE_TIMESTAMP", updateTimeStamp);
                context.startActivity(intent);

            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }

        });

        builder.create().show();

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        ImageView profileIv;
        TextView name, age, phone;
        ImageButton editButton, deleteButton;

        public Holder(@NonNull View itemView) {

            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            phone = itemView.findViewById(R.id.phone);
            editButton = itemView.findViewById(R.id.editBtn);
            deleteButton = itemView.findViewById(R.id.deleteBtn);

        }

    }

}
