package jp.mstssk.notification_tester.notification_tester;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {

    @InjectView(R.id.visibility)
    Spinner visibilitySpinner;

    @InjectView(R.id.priority)
    Spinner prioritySpinner;

    @InjectView(R.id.withPublicVer)
    CheckBox withPublicVerCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        visibilitySpinner.setAdapter(new ArrayAdapter<Visibility>(this, android.R.layout.simple_selectable_list_item, Visibility.values()));
        prioritySpinner.setAdapter(new ArrayAdapter<Priority>(this, android.R.layout.simple_selectable_list_item, Priority.values()));
        prioritySpinner.setSelection(Priority.DEFAULT.ordinal());
    }

    @OnClick(R.id.showNotification)
    void showNotification() {
        Visibility visibility = (Visibility) this.visibilitySpinner.getSelectedItem();
        Priority priority = (Priority) prioritySpinner.getSelectedItem();
        boolean withPublicVer = withPublicVerCheckbox.isChecked();

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(visibility.name() + "/" + priority.name())
                .setContentText(withPublicVer ? "with Public ver." : "")
                .setVisibility(visibility.value)
                .setPriority(priority.value);
        if (withPublicVer) {
            Notification publicVer = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Alternate notification")
                    .setContentText(visibility.name() + "/" + priority.name())
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .build();
            builder.setPublicVersion(publicVer);
        }

        int id = visibility.value * 100 + priority.value * 10 + (withPublicVer ? 1 : 0);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(id, builder.build());
    }

    public enum Visibility {
        PUBLIC(Notification.VISIBILITY_PUBLIC),
        PRIVATE(Notification.VISIBILITY_PRIVATE),
        SECRET(Notification.VISIBILITY_SECRET);
        public final int value;

        private Visibility(final int value) {
            this.value = value;
        }
    }

    public enum Priority {
        MAX(Notification.PRIORITY_MAX),
        HIGH(Notification.PRIORITY_HIGH),
        DEFAULT(Notification.PRIORITY_DEFAULT),
        LOW(Notification.PRIORITY_LOW),
        MIN(Notification.PRIORITY_MIN);
        public final int value;

        private Priority(final int value) {
            this.value = value;
        }
    }

}
