package dreamsdoor.co.in.tableviewexample;

import org.json.JSONException;

interface FetchDataCallbackInterface {
    public abstract void  tabData(String result) throws JSONException;
}
