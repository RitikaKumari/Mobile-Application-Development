package com.ritika.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private List<Official> officialLs;
    private MainActivity mainAct;

    public OfficialAdapter(List<Official> offList, MainActivity ma){
        this.officialLs = offList;
        mainAct = ma;
    }

    @Override
    public OfficialViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_row2, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int position){
        Official official = officialLs.get(position);
        holder.name.setText(String.format("%s (%s)",official.getName(),official.getParty()));
        holder.office.setText(official.getOffice());
    }

    @Override
    public int getItemCount(){ return officialLs.size();}
}
