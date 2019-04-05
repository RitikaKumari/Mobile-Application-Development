package com.ritika.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


public class LoadDataAsyncTask extends AsyncTask<String, Void, String> {

    private MainActivity mainAct;
    private static final String KEY = "AIzaSyCA8cqAg1pGqc6PEISUpq631ECVi4F9JJk";
    private final String dataURL = "https://www.googleapis.com/civicinfo/v2/representatives?key="+ KEY +"&address=";

    private String city;
    private String state;
    private String zip;

    public LoadDataAsyncTask(MainActivity mact){ mainAct = mact;}

    @Override
    protected void onPreExecute(){
        Toast.makeText(mainAct, "Loading Official Data", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String str){


        if(str == null){
            Toast.makeText(mainAct,"Civic Info service is not found",Toast.LENGTH_SHORT).show();
            mainAct.setOfficialLs(null);
            return;
        }
        if(str.isEmpty()){
            Toast.makeText(mainAct,"No data is available for the specified location",Toast.LENGTH_SHORT).show();;
            mainAct.setOfficialLs(null);
            return;
        }

        ArrayList<Official> officialLs = parseJSON(str);
        Object [] results = new Object[2];
        if (city.equals(""))
        {
            results[0] = state + " " + zip;
        }
        else
        {
        results[0] = city + ", " + state + " " + zip;
        }

        results[1] = officialLs;
        mainAct.setOfficialLs(results);
        return;
    }

    @Override
    protected String doInBackground(String... params){

        String dataURL = this.dataURL + params[0];
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        catch (Exception e) {
            return null;
        }
        return sb.toString();
    }

    private ArrayList<Official> parseJSON(String s){

        ArrayList<Official> officialList = new ArrayList<>();
        try{
            JSONObject wholeThing = new JSONObject(s);
            JSONObject normalizedInput = wholeThing.getJSONObject("normalizedInput");
            JSONArray offices = wholeThing.getJSONArray("offices");
            JSONArray officials = wholeThing.getJSONArray("officials");
            city = normalizedInput.getString("city");
            state = normalizedInput.getString("state");
            zip = normalizedInput.getString("zip");

            for(int i = 0;i < offices.length(); i++){
                JSONObject obj = offices.getJSONObject(i);
                String officeName = obj.getString("name");
                String officialIndices = obj.getString("officialIndices");
                String temp = officialIndices.substring(1,officialIndices.length()-1);
                String [] temp2 = temp.split(",");
                int [] indices = new int [temp2.length];
                for(int j = 0; j < temp2.length; j++){
                    indices[j] = Integer.parseInt(temp2[j]);
                }


                for(int j = 0; j < indices.length; j++ ){
                    JSONObject innerObj = officials.getJSONObject(indices[j]);
                    String name = innerObj.getString("name");

                    String address = "";
                    if(! innerObj.has("address")){
                        address = "No Data Provided";
                    }
                    else {
                        JSONArray addressArray = innerObj.getJSONArray("address");
                        JSONObject addressObject = addressArray.getJSONObject(0);

                        if (addressObject.has("line1")) {
                            address += addressObject.getString("line1") + "\n";
                        }
                        if (addressObject.has("line2")) {
                            address += addressObject.getString("line2") + "\n";
                        }
                        if (addressObject.has("city")) {
                            address += addressObject.getString("city") + " ";
                        }
                        if (addressObject.has("state")) {
                            address += addressObject.getString("state") + ", ";
                        }
                        if (addressObject.has("zip")) {
                            address += addressObject.getString("zip");
                        }

                    }

                    String party = (innerObj.has("party") ? innerObj.getString("party") : "Unknown" );

                    String phones = ( innerObj.has("phones") ? innerObj.getJSONArray("phones").getString(0) : "No Data Provided");

                    String urls = ( innerObj.has("urls") ? innerObj.getJSONArray("urls").getString(0) : "No Data Provided");

                    String emails = (innerObj.has("emails") ? innerObj.getJSONArray("emails").getString(0) : "No Data Provided" );

                    String photoURL = (innerObj.has("photoUrl") ? innerObj.getString("photoUrl") : "No Data Provided");


                    JSONArray channels = ( innerObj.has("channels") ? innerObj.getJSONArray("channels") : null );
                    String googleplus = ""; String facebook = ""; String twitter = ""; String youtube = "";

                    if(channels != null){
                        for(int k = 0; k < channels.length(); k++ ){
                            String type = channels.getJSONObject(k).getString("type");
                            switch (type){
                                case "GooglePlus":
                                    googleplus = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Facebook":
                                    facebook = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Twitter":
                                    twitter = channels.getJSONObject(k).getString("id");
                                    break;
                                case "YouTube":
                                    youtube = channels.getJSONObject(k).getString("id");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    else{
                        googleplus = "No Data Provided";
                        facebook = "No Data Provided";
                        twitter = "No Data Provided";
                        youtube = "No Data Provided";
                    }


                    Official o = new Official(name, officeName, party,
                            address, phones, urls, emails, photoURL,
                            googleplus, facebook, twitter, youtube);
                    officialList.add(o);
                }
            }
            return officialList;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}