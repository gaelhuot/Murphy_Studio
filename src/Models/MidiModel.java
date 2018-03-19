package Models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.*;
import jdk.nashorn.internal.parser.JSONParser;

public class MidiModel {
    // Contains all the midi instruments
    private ArrayList<String> instruments;

    public MidiModel()
    {
        instruments = new ArrayList<String>();
        initInstruments();
    }

    private void initInstruments() {
        Gson gson = new Gson();

        try {

            // Getting the json file
            Scanner scanner = new Scanner( new File("ressources/data.json") );
            String jsonString = scanner.useDelimiter("\\A").next();
            scanner.close(); // Put this call in a finally block

            JsonParser jsonParser = new JsonParser();

            //convert the json string back to object
            JsonObject instruments = jsonParser.parse(jsonString).getAsJsonObject();
            JsonArray jsonArray = instruments.get("midiInstruments").getAsJsonArray();

            for ( int i = 0; i < jsonArray.size(); i++ )
                this.instruments.add(jsonArray.get(i).toString());

            System.out.println(this.instruments);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
