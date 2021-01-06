package ca.uwaterloo.sentimo.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;

import ca.uwaterloo.sentimo.R;

public class CalendarFragment extends Fragment {

    CustomCalendar customCalendar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Assign variable
        customCalendar = view.findViewById(R.id.custom_calendar);

        //Initialize description hash map
        HashMap<Object, Property> descHashMap = new HashMap<>();
        //Initialize default property
        Property defaultProperty = new Property();
        //Initialize default resource
        defaultProperty.layoutResource = R.layout.default_view;
        //Initialize and assign variable
        defaultProperty.dateTextViewResource = R.id.text_view;
        //Put object and property
        descHashMap.put("default", defaultProperty);

        //For current date
        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("current", currentProperty);

        //For Present date
        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("present", presentProperty);

        //For absent
        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("absent", absentProperty);

        //Set desc hash map on custom calendar
        customCalendar.setMapDescToProp(descHashMap);

        //Initialize date hash map
        HashMap<Integer, Object> dateHashMap = new HashMap<>();
        //Initialize calendar
        Calendar calendar = Calendar.getInstance();
        //Put values
        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");
        dateHashMap.put(1, "present");
        dateHashMap.put(2, "absent");
        dateHashMap.put(3, "present");
        dateHashMap.put(4, "absent");
        dateHashMap.put(20, "present");
        dateHashMap.put(30, "absent");
        //Set date
        customCalendar.setDate(calendar, dateHashMap);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                //Get string date
                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                        + "/" + (selectedDate.get(Calendar.MONTH) + 1 )
                        + "/" + selectedDate.get(Calendar.YEAR);
                //Display date in toast
                Toast.makeText(getActivity().getApplicationContext()
                        , sDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        return root;

    }
}