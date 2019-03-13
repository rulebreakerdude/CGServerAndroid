package dev.n.d.cgserverandroid;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class CallerListAdapter extends RecyclerView.Adapter<CallerListAdapter.CallerViewHolder> {

    class CallerViewHolder extends RecyclerView.ViewHolder {
        private final TextView callerTextView;
        private final TextView datetimeOfCallTextView;
        private final TextView apiDatetimeTextView;
        private final TextView responseCGNetTextView;
        private final TextView responseIMITextView;
        private final TextView successfulCallTextView;
        private final CheckBox fromCGNetCheckBox;
        private final CheckBox fromIMICheckBox;

        private CallerViewHolder(View itemView) {
            super(itemView);
            callerTextView = itemView.findViewById(R.id.callerTextView);
            datetimeOfCallTextView=itemView.findViewById(R.id.datetimeOfCallTextView);
            apiDatetimeTextView =itemView.findViewById(R.id.apiDatetimeTextView);
            responseCGNetTextView=itemView.findViewById(R.id.responseCGNetTextView);
            responseIMITextView=itemView.findViewById(R.id.responseIMITextView);
            successfulCallTextView=itemView.findViewById(R.id.successfulCallTextView);
            fromCGNetCheckBox=itemView.findViewById(R.id.fromCGNetCheckBox);
            fromIMICheckBox=itemView.findViewById(R.id.fromIMICheckBox);
        }
    }

    private final LayoutInflater mInflater;
    private List<Caller> mCallers; // Cached copy of words

    CallerListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public CallerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CallerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallerViewHolder holder, int position) {
        if (mCallers != null) {
            Caller current = mCallers.get(position);
            holder.callerTextView.setText(current.getCallerNumber());
            holder.datetimeOfCallTextView.setText(current.getCallDatetime());
            holder.apiDatetimeTextView.setText(current.getApiDatetime());
            holder.responseCGNetTextView.setText(current.getResponseCGNet());
            holder.responseIMITextView.setText(current.getResponseIMI());
            holder.successfulCallTextView.setText(current.getSuccessfulCallbackDatetime());
            if(current.getCallFromCGNet()==1){
                holder.fromCGNetCheckBox.setChecked(true);
            }
            else{
                holder.fromCGNetCheckBox.setChecked(false);
            }
            if(current.getCallFromIMI()==1){
                holder.fromIMICheckBox.setChecked(true);
            }
            else{
                holder.fromIMICheckBox.setChecked(false);
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.callerTextView.setText("No Caller Yet");

        }
    }

    void setCallers(List<Caller> callers){
        mCallers = callers;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mCallers != null)
            return mCallers.size();
        else return 0;
    }
}
