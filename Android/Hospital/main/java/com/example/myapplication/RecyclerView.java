package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;

import com.example.myapplication.MainActivity;

import java.util.*;
import java.lang.*;


public class RecyclerView extends MainActivity {

    private android.support.v7.widget.RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private android.support.v7.widget.RecyclerView.LayoutManager layoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    List<Request> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        requestList = new ArrayList<>();
        recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        requestList.add(
                new Request(
                        "Pratik Rajani",
                        "20",
                        "Male",
                        "RaviKumar Tiwari",
                        "9534520125",
                        "GJ5 AJ 5463"
                )
        );

        requestList.add(
                new Request(
                        "Manish K Gupta",
                        "40",
                        "Male",
                        "RutuBen Parekh",
                        "1202154532",
                        "GJ1 RP 4523"
                )
        );

        requestList.add(
                new Request(
                        "Pratik Rajani",
                        "20",
                        "Male",
                        "RaviKumar Tiwari",
                        "9534520125",
                        "GJ5 AJ 5463"
                )
        );

        requestList.add(
                new Request(
                        "Manish K Gupta",
                        "40",
                        "Male",
                        "RutuBen Parekh",
                        "1202154532",
                        "GJ1 RP 4523"
                )
        );





        requestAdapter = new RequestAdapter(this, requestList);
        recyclerView.setAdapter(requestAdapter);
    }
}
