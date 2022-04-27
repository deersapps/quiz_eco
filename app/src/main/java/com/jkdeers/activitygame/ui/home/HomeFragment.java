package com.jkdeers.activitygame.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jkdeers.activitygame.R;
import com.jkdeers.activitygame.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    // chart2 variables
    // variable for our bar chartClass
    BarChart barChartClass;
    // variable for our bar data.
    BarData barDataClass;
    // variable for our bar data set.
    BarDataSet barDataSetClass;
    // array list for storing entries.
    ArrayList barEntriesArrayListClass = null;

    //chart3 variables
    // variable for our bar chartSchool
    BarChart barChartSchool;
    // variable for our bar data.
    BarData barDataSchool;
    // variable for our bar data set.
    BarDataSet barDataSetSchool;
    // array list for storing entries.
    ArrayList barEntriesArrayListSchool;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        PieChart chart = view.findViewById(R.id.chart1);
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        yvalues.add(new PieEntry(20f, "Plantation"));
        yvalues.add(new PieEntry(50f, "Recycle Waste"));
        yvalues.add(new PieEntry(30f, "Switching off Lights"));

        PieDataSet dataSet = new PieDataSet(yvalues, "(Monthly)");

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.WHITE);
        chart.animateXY(1,1);
        chart.setData(data);
        chart.setDrawHoleEnabled(true);
        chart.setTransparentCircleRadius(5f);
        chart.setHoleRadius(10f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Top 3 Activities %");
        chart.setDescription(description);



        // initializing variable for barchartClass
        barChartClass = view.findViewById(R.id.chart2);
        // calling method to get bar entries.
        getBarEntriesClass();
        // creating a new bar data set.
        barDataSetClass = new BarDataSet(barEntriesArrayListClass, "Ranking By Class");
        // creating a new bar data and 
        // passing our bar data set.
        barDataClass = new BarData(barDataSetClass);
        // below line is to set data 
        // to our bar chart.
        barChartClass.setData(barDataClass);
        // adding color to our bar data set.
        barDataSetClass.setColors(ColorTemplate.MATERIAL_COLORS);
        // setting text color.
        barDataSetClass.setValueTextColor(Color.BLACK);
        // setting text size
        barDataSetClass.setValueTextSize(16f);
        barChartClass.getDescription().setEnabled(false);


        // initializing variable for barchartSchool.
        barChartSchool = view.findViewById(R.id.chart3);
        // calling method to get bar entries.
        getBarEntriesSchool();
        // creating a new bar data set.
        barDataSetSchool = new BarDataSet(barEntriesArrayListSchool, "Ranks by school");
        // creating a new bar data and 
        // passing our bar data set.
        barDataSchool = new BarData(barDataSetSchool);
        // below line is to set data 
        // to our bar chart.
        barChartSchool.setData(barDataSchool);
        // adding color to our bar data set.
        barDataSetSchool.setColors(ColorTemplate.MATERIAL_COLORS);
        // setting text color.
        barDataSetSchool.setValueTextColor(Color.BLACK);
        // setting text size
        barDataSetSchool.setValueTextSize(16f);
        barChartSchool.getDescription().setEnabled(false);


        return view;
       // return root;
    }

    private void getBarEntriesSchool() {
        // creating a new array list
        barEntriesArrayListSchool = new ArrayList<>();
        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntriesArrayListSchool.add(new BarEntry(1f, 4));
        barEntriesArrayListSchool.add(new BarEntry(2f, 6));
        barEntriesArrayListSchool.add(new BarEntry(3f, 8));
        barEntriesArrayListSchool.add(new BarEntry(4f, 2));
        barEntriesArrayListSchool.add(new BarEntry(5f, 4));
        barEntriesArrayListSchool.add(new BarEntry(6f, 1));
    }

    private void getBarEntriesClass() {
        // creating a new array list
        barEntriesArrayListClass = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntriesArrayListClass.add(new BarEntry(1f, 4));
        barEntriesArrayListClass.add(new BarEntry(2f, 6));
        barEntriesArrayListClass.add(new BarEntry(3f, 8));
        barEntriesArrayListClass.add(new BarEntry(4f, 2));
        barEntriesArrayListClass.add(new BarEntry(5f, 4));
        barEntriesArrayListClass.add(new BarEntry(6f, 1));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}