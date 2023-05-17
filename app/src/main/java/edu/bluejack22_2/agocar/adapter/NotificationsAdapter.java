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

import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Notification;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedNotificationsListener;

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

        holder.ivDeleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.delete(new OnSuccessListener() {
                    @Override
                    public void onSuccess(boolean success) {
                        if(success){
                            Notification.getUserNotifications(new RetrievedNotificationsListener() {
                                @Override
                                public void retrievedNotifications(ArrayList<Notification> notifications) {
                                    if(notifications == null){
                                        setNotifications(new ArrayList<Notification>());
                                    }else{
                                        setNotifications(notifications);
                                    }

                                }
                            }, HomeActivity.user.getId());
                        }else{
                            setNotifications(notifications);
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class PreferencesViewHolder extends RecyclerView.ViewHolder{
        TextView tvNotificationContent;
        ImageView ivDeleteNotification;

        public PreferencesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotificationContent = itemView.findViewById(R.id.tvNotificationContent);
            ivDeleteNotification = itemView.findViewById(R.id.ivDeleteNotification);



        }


    }

    public void setNotifications(ArrayList<Notification> newNotifications) {
        notifications.clear();
        notifications.addAll(newNotifications);
        notifyDataSetChanged();
    }


}
