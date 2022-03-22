package com.example.london_property_market;

import com.example.london_property_market.Loader.CsvLoader;
import com.example.london_property_market.UI.MainViewer;
import com.example.london_property_market.UI.Statistics.StatisticsModel;
import javafx.application.Application;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Application.launch(MainViewer.class,args);
//        List<String> boroughTest = new ArrayList<>();
//        boroughTest.add("Croydon");
//        StatisticsModel stats = new StatisticsModel(boroughTest);
//        try {
//            if (stats.avgReviewsPerProperty().next()){
//                System.out.println(stats.avgReviewsPerProperty().getObject(2));
//            }else{
//                System.out.println("Avgrevews.next came out false so there was nothing there");
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

}
