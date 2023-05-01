package edu.bluejack22_2.agocar.other;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.models.Notification;

public interface RetrievedNotificationsListener {
    void retrievedNotifications(ArrayList<Notification> notifications);
}
