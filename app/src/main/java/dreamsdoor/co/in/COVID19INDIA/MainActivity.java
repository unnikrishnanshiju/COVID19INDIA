package dreamsdoor.co.in.COVID19INDIA;

import androidx.appcompat.app.AppCompatActivity;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import android.content.Intent;

import android.content.res.Configuration;
import android.graphics.Color;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity  {
    private FirebaseAnalytics mFirebaseAnalytics;
    String[][] dataFromTab;
    static String[] headerTabsPortrait = {"STATE/UT","C","A","R", "D"};
    static String[] headerTabsLandscape = {"STATE/UT","CONFIRMED","ACTIVE","RECOVERED", "DECEASED"};
    TableView<String[]> tabView;

     TextView cnf,lu,dis,dea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        cnf = findViewById(R.id.confirmed);
        lu = findViewById(R.id.lastUp);
        dis = findViewById(R.id.discharged);
        dea = findViewById(R.id.deceased);
        tabView = (TableView<String[]>) findViewById(R.id.tabView);
        tabView.setHeaderBackgroundColor(Color.parseColor("#808c8e"));
        tabView.setColumnWeight(0,2);
        tabView.setColumnCount(5);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tabView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,headerTabsLandscape));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tabView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,headerTabsPortrait));
        }

        fetchData process = new fetchData();
        process.execute();
        try {
            String maindata = process.get();
            remainingData(maindata);
            tabularData(maindata);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.about) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.refresh){
            fetchData process = new fetchData();
            process.execute();
            try {
                String data =  process.get();
                tabularData(data);
                remainingData(data);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(item.getItemId()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, String.valueOf(item.getTitle()));
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "REFRESHING DATA", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.helpful){
            Intent intent = new Intent(MainActivity.this, HelpfulLinks.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void tabularData(String data){

            try {

                JSONObject root = new JSONObject(data);
                JSONObject resultData = root.getJSONObject("data");
                JSONArray dataRegional = resultData.getJSONArray("regional");

                dataFromTab = new String[dataRegional.length()][5];

                for (int i = 0; i < dataRegional.length(); i++) {

                    JSONObject jo = (JSONObject) dataRegional.get(i);
                    Integer active = (jo.getInt("confirmedCasesIndian") + jo.getInt("confirmedCasesForeign")) - jo.getInt("discharged");
                    Integer confirmed = jo.getInt("confirmedCasesIndian") + jo.getInt("confirmedCasesForeign");

                    dataFromTab[i][0] = jo.getString("loc");
                    dataFromTab[i][1] = confirmed.toString();
                    dataFromTab[i][2] = active.toString();
                    dataFromTab[i][3] = jo.getString("discharged");
                    dataFromTab[i][4] = jo.getString("deaths");
                }
                tabView.setDataAdapter(new SimpleTableDataAdapter(this, dataFromTab));
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    public void remainingData(String data){


        try {
            JSONObject root = new JSONObject(data);
            JSONObject resultData = root.getJSONObject("data");
            JSONObject summaryData = resultData.getJSONObject("summary");
            Object total = summaryData.get("total");
            Object discharged = summaryData.get("discharged");
            Object death = summaryData.get("deaths");
            Object lastup = root.get("lastRefreshed");


            cnf.setText("CONFIRMED:(C)"+total);
            dis.setText("DISCHARGED:(R)" + discharged);
            dea.setText("DECEASED:(D)"+death);
            lu.setText("LAST UPDATED:"+lastup );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
