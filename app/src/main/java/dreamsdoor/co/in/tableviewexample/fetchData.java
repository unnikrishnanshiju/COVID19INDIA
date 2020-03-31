package dreamsdoor.co.in.tableviewexample;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void,Void,Void> {
    String data = "";
    FetchDataCallbackInterface fetchDataCallbackInterface;
    MainActivity m = new MainActivity();
    String singleParsed;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.rootnet.in/covid19-in/stats/latest");
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;

            }
            JSONObject root = new JSONObject(data);
            JSONObject resultData = root.getJSONObject("data");
            JSONArray dataRegional = resultData.getJSONArray("regional");
            for(int i = 0 ;i<dataRegional.length();i++){
                JSONObject jo = (JSONObject) dataRegional.get(i);
                singleParsed = jo.get("loc")+"\n"
                            +jo.get("confirmedCasesIndian")+"\n";
            }
            //m.tv.setText(singleParsed);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
       /* try {
            m.tabData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }
}
