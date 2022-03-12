package com.example.london_property_market.Loader;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;


public class AirbnbDataLoader {

    private final String CSV_FILE_PATH = "src/main/resources/propertyData/airbnb-london.csv";

    public List<AirbnbListing> ListDataLoader() {

        //https://stackoverflow.com/questions/33026193/supercsv-unable-to-find-method-exception Author: Jeronimo Backes Date: Oct 10, 2015
        BeanListProcessor<AirbnbListing> beanListProcessor = new BeanListProcessor<>(AirbnbListing.class);
        CsvParserSettings csvParserSettings = new CsvParserSettings();
        csvParserSettings.setHeaderExtractionEnabled(true);
        csvParserSettings.setProcessor(beanListProcessor);

        CsvParser csvParser = new CsvParser(csvParserSettings);
        try {
            csvParser.parse(new FileReader(CSV_FILE_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return beanListProcessor.getBeans();
    }

}
