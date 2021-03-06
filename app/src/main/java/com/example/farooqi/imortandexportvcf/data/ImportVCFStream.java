package com.example.farooqi.imortandexportvcf.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAMSUNG on 2/14/2018.
 */

public class ImportVCFStream {
    InputStream inputStream;

    public ImportVCFStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }


    public List<String> readData() {
        List<String> result = new ArrayList();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line = "";
            String cvfLine;
            while ((cvfLine = reader.readLine()) != null) {
                line = cvfLine + "\n";
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
