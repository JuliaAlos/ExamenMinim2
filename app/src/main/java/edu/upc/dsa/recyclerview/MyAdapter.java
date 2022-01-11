package edu.upc.dsa.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<GitRepos> repos;
    Context context;

    public MyAdapter(Context context,List<GitRepos> gitRepos){
        repos=gitRepos;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GitRepos repo=repos.get(position);
        holder.name.setText(repo.getName());
        holder.lenguage.setText(repo.getLanguage());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override//Numero de items que tenim
    public int getItemCount() {
        return repos.size();
    }

    /*****************************************************************
            Representa un sola fila del recycler view
     *****************************************************************/
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView lenguage;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.firstLine);
            lenguage = itemView.findViewById(R.id.secondLine);
            mainLayout=itemView.findViewById(R.id.myLayout);
        }
    }
}
