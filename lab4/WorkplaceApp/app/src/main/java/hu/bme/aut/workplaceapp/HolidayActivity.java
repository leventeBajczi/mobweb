package hu.bme.aut.workplaceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import data.DataManager;
import fragments.DatePickerDialogFragment;

public class HolidayActivity extends AppCompatActivity implements DatePickerDialogFragment.OnDateSelectedListener{
    private PieChart chartHoliday;
    private Button btnTakeHoliday;

    private View root;

    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);

        dataManager = DataManager.getInstance();

        chartHoliday = findViewById(R.id.chartHoliday);

        btnTakeHoliday = findViewById(R.id.btnTakeHoliday);

        root = findViewById(R.id.root);

        btnTakeHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialogFragment().show(getSupportFragmentManager(), "DATE_TAG");

            }
        });

        loadHolidays();
    }

    private void loadHolidays() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(dataManager.getHolidays(), "Taken"));
        entries.add(new PieEntry(dataManager.getRemainingHolidays(), "Remaining"));

        PieDataSet dataSet = new PieDataSet(entries, "Holidays");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        chartHoliday.setData(data);
        chartHoliday.invalidate();
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        int numHolidays = dataManager.getHolidays();
        Calendar calendar= Calendar.getInstance();
        calendar.set(year,month,day);
        boolean isValid = calendar.compareTo(Calendar.getInstance()) > 0;
        if (isValid) {
            dataManager.setHolidays(numHolidays + 1);
        }
        else {
            Snackbar.make(root, getString(R.string.error_past_holiday), Snackbar.LENGTH_LONG).show();
        }


        if(dataManager.getRemainingHolidays() <= 0){
            btnTakeHoliday.setEnabled(false);
        }

        // Update chart
        loadHolidays();
    }
}