package edu.bluejack22_2.agocar.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Notification;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.PreferencesViewHolder> {
    private ArrayList<Notification> notifications = new ArrayList<>();

    @NonNull
    @Override
    public PreferencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_notification_card, parent, false);
        return new PreferencesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferencesViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.tvNotificationContent.setText(notification.getContent());

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class PreferencesViewHolder extends RecyclerView.ViewHolder{
        TextView tvNotificationContent;

        public PreferencesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotificationContent = itemView.findViewById(R.id.tvNotificationContent);


        }


    }

    public void setNotifications(ArrayList<Notification> newNotifications) {
        notifications.clear();
        notifications.addAll(newNotifications);
        notifyDataSetChanged();
    }


}
