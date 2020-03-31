package dreamsdoor.co.in.tableviewexample;

import androidx.appcompat.app.AppCompatActivity;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements FetchDataCallbackInterface{
    String[][] dataFromTab;
    static String[] headerTabs = {"STATE/UT","CONFIRMED","ACTIVE","RECOVERED", "DECEASED"};
     TableView<String[]> tabView;
     String singleParsed;
     TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
       tabView = (TableView<String[]>) findViewById(R.id.tabView);
        tabView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tabView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,headerTabs));
        fetchData process = new fetchData();
        process.execute();
        //tabView.setDataAdapter(new SimpleTableDataAdapter(this,dataFromTab));
        tabView.setColumnCount(5);
    }

    @Override
    public void tabData(String result) throws JSONException {
        //Log.d("Result", "tabData: " + result);
        JSONObject root = new JSONObject(result);
       /* //JSONArray ja = root.getJSONArray("data");
        JSONObject resultData = root.getJSONObject("data");
        JSONArray dataRegional = resultData.getJSONArray("regional");
        //Log.d("regional", "tabData: "+dataRegional);
        // JSONObject jo = (JSONObject) dataRegional.get(0);

        // Log.d("loc", "tabData: "+ singleParsed);
        // tv.setText(singleParsed);
        dataFromTab = new String[dataRegional.length()][5];
        for (int i = 0; i < dataRegional.length(); i++) {
            JSONObject jo = (JSONObject) dataRegional.get(i);
            singleParsed = jo.getString("loc");

            //Log.d("loc", "tabData: " + singleParsed);
            //tv.setText(singleParsed);

            dataFromTab[i][0]=  "maharashtra";
            dataFromTab[i][1]= "10";
            dataFromTab[i][2]= "12";
            dataFromTab[i][3] ="10";
            dataFromTab[i][4] ="1";

           // Log.d("regional", "tabData: "+dataFromTab);
            //tabView.setDataAdapter(new SimpleTableDataAdapter(MainActivity.this, dataFromTab));

        }

           tabView.setDataAdapter(new SimpleTableDataAdapter(MainActivity.this, dataFromTab));
*/
    }
}
