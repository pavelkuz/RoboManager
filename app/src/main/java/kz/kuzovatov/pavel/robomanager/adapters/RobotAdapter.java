package kz.kuzovatov.pavel.robomanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import kz.kuzovatov.pavel.robomanager.R;
import kz.kuzovatov.pavel.robomanager.RobotDetailsActivity;
import kz.kuzovatov.pavel.robomanager.models.Robot;

public class RobotAdapter extends RecyclerView.Adapter<RobotAdapter.ViewHolder> {
    private List<Robot> robotList;
    private Context context;
    private RecyclerView rv;
    private int previousPosition;
    private static ViewHolder viewHolder;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView typeTextView;
        public TextView yearTextView;
        public RadioButton radioButton;

        public ViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.robotName);
            typeTextView = (TextView) v.findViewById(R.id.robotType);
            yearTextView = (TextView) v.findViewById(R.id.robotYear);
            radioButton = (RadioButton) v.findViewById(R.id.radioButton);
            v.setTag(v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = rv.getChildAdapterPosition(v);
            Bundle basket = new Bundle();
            basket.putInt("robotId", (int) RobotAdapter.this.getItemId(position));
            Intent intent = new Intent(context, RobotDetailsActivity.class);
            intent.putExtras(basket);
            context.startActivity(intent);
        }
    }

    public RobotAdapter(List<Robot> robotList, Context context, RecyclerView rv) {
        this.context = context;
        this.robotList = robotList;
        this.rv = rv;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.robot_item, parent, false);
        //Created for set the view's size, margin, padding and layout parameters, without them yet
        ViewHolder vh = new ViewHolder(v);
        viewHolder = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nameTextView.setText(String.format(context.getString(R.string.robot_name), robotList.get(position).getName()));
        holder.typeTextView.setText(String.format(context.getString(R.string.robot_type), robotList.get(position).getType()));
        holder.yearTextView.setText(String.format(context.getString(R.string.robot_year), robotList.get(position).getYear()));

        holder.radioButton.setChecked(robotList.get(position).isChecked());

        holder.radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                robotList.get(position).setIsChecked(true);

                if (previousPosition != position){
                    robotList.get(previousPosition).setIsChecked(false);
                }

                previousPosition = position;

                notifyItemRangeChanged(0, robotList.size());

                Intent intent = new Intent();
                Bundle basket = new Bundle();
                basket.putInt("selectedId", robotList.get(position).getId());
                intent.putExtras(basket);
            }
        });
    }

    public static ViewHolder getViewHolder(){
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return robotList.size();
    }

    @Override
    public long getItemId(int position) {
        return robotList.get(position).getId();
    }
}
