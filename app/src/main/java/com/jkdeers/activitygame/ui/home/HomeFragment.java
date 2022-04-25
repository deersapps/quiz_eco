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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
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
        description.setText("");
        chart.setDescription(description);

        return view;
       // return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}