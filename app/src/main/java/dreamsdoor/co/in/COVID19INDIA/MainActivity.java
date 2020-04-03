package dreamsdoor.co.in.COVID19INDIA;

import androidx.appcompat.app.AppCompatActivity;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity  {
    String[][] dataFromTab;
    static String[] headerTabs = {"STATE/UT","CONFIRMED","ACTIVE","RECOVERED", "DECEASED"};
     TableView<String[]> tabView;
     String singleParsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchData process = new fetchData();
        process.execute();
        try {
            String  data = process.get();

            JSONObject root = new JSONObject(data);
            JSONObject resultData = root.getJSONObject("data");
            JSONArray dataRegional = resultData.getJSONArray("regional");

            dataFromTab = new String[dataRegional.length()][5];

            for (int i = 0; i < dataRegional.length(); i++) {

                JSONObject jo = (JSONObject) dataRegional.get(i);
                Integer active = (jo.getInt("confirmedCasesIndian") + jo.getInt("confirmedCasesForeign"))-jo.getInt("discharged");
                Integer confirmed = jo.getInt("confirmedCasesIndian")+ jo.getInt("confirmedCasesForeign");

                dataFromTab[i][0]=  jo.getString("loc");
                dataFromTab[i][1]= confirmed.toString();
                dataFromTab[i][2]= active.toString();
                dataFromTab[i][3] =jo.getString("discharged");
                dataFromTab[i][4] =jo.getString("deaths");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tabView = (TableView<String[]>) findViewById(R.id.tabView);
        tabView.setHeaderBackgroundColor(Color.parseColor("#6200EE"));
        tabView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,headerTabs));
        tabView.setDataAdapter(new SimpleTableDataAdapter(this,dataFromTab));
        tabView.setColumnCount(5);


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
        }
        else if(item.getItemId() == R.id.helpful){
            Intent intent = new Intent(MainActivity.this, HelpfulLinks.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
