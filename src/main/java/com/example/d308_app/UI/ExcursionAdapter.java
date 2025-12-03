package com.example.d308_app.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_app.R;
import com.example.d308_app.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;
        private final TextView excursionItemView2;
        private final TextView excursionItemView3;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);
            excursionItemView3 = itemView.findViewById(R.id.textView9);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionId());
                    intent.putExtra("name", current.getExcursionName());
                    intent.putExtra("date", current.getExcursionDate());
                    intent.putExtra("price", current.getPrice());
                    intent.putExtra("vacaId", current.getVacationId());
                    context.startActivity(intent);
                }
            });
        }
    }
    public ExcursionAdapter(Context context){
        mInflater= LayoutInflater.from(context);
        this.context= context;
    }
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        View excursionView= mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(excursionView);
    }
    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position){
        if(mExcursions!=null){
            Excursion current= mExcursions.get(position);
            String name= current.getExcursionName();
            int vacaId= current.getVacationId();
            String date= current.getExcursionDate();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(Integer.toString(vacaId));
            holder.excursionItemView3.setText(date);
        }
        else{
            holder.excursionItemView.setText("No excursion name");
            holder.excursionItemView2.setText("No vacation Id");
        }
    }
    public void setExcursions(List<Excursion> excursions){
        mExcursions= excursions;
        notifyDataSetChanged();
    }
    public int getItemCount(){
        if(mExcursions!=null) {
            return mExcursions.size();
        }
        else{
            return 0;
        }
    }
}
