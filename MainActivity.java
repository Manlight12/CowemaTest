package com.sml.cowematest.activitie.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.sml.cowematest.activitie.main.adapter.RestoAdapter;
import com.sml.cowematest.databinding.ActivityMainBinding;
import com.sml.cowematest.model.RestaurantItem;
import com.sml.cowematest.util.Fkey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="MAIN_ACTIVITY";
    ActivityMainBinding binding;
    ArrayList<RestaurantItem> resItems=new ArrayList<>();
    RestoAdapter adapter;
    RecyclerView recyclerView;
    static ConnectivityManager cManager;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        cManager= (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    void initView(){
        adapter=new RestoAdapter(this,resItems);
        recyclerView=binding.recyclerView;
        recyclerView.setAdapter(adapter);
        binding.btnRefresh.setOnClickListener(v->{
            loadData();
        });
    }
    void loadData(){
        new DownloadJsonItems().execute();
    }
    boolean getConState(){
        NetworkInfo ninfo = cManager.getActiveNetworkInfo();
        if(ninfo!=null && ninfo.isConnected() && ninfo.isAvailable())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    class DownloadJsonItems extends AsyncTask<Void,Void,String>{
        OkHttpClient client;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            client=new OkHttpClient();
            int size=resItems.size();
            if(size==0 || !getConState()){
                manageState(LOADING_STATE);
            }else{
                if(getConState()){
                    manageState(OFFLINE_STATE);
                }else{
                    manageState(CONTENT_STATE);
                }
            }


        }

        @Override
        protected String doInBackground(Void... voids) {
            Request request=new Request.Builder()
                    .url("https://jsonplaceholder.typicode.com/photos")
                    .build();
            try (Response response=client.newCall(request).execute()){
                return response.body().string();
            } catch (IOException e) {
                showError(e.getMessage());
                return null;
            }

        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String sJson) {
            if(sJson==null) {
                if(resItems.size()==0 ){
                    if(!getConState()){
                        manageState(OFFLINE_STATE);
                    }
                }else{
                    /*Snackbar.make(binding.getRoot(),"Impossible d'acceder à internet,certaines fonctinnalités pouraient pas marchées!",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ressayer",v->{
                               this.execute();
                            });*/
                    showError("Impossible d'obtenir les données\nverifier votre Connexion");
                    manageState(LOADING_STATE);
                }
            }
            else{
                new Thread(()->{
                    try {
                        updateSetRestoData(sJson);
                    } catch (JSONException e) {
                        showError(e.getMessage());
                    }
                }).start();

            }
        }
    }
    void updateSetRestoData(String sJson) throws JSONException{
        ArrayList<RestaurantItem> backupList=resItems;
        resItems.clear();
        backupList=resItems;
        JSONArray jsonArray= new JSONArray(sJson);

        for (int i=0;i<jsonArray.length() ;i++){
            JSONObject jObject=jsonArray.getJSONObject(i);
            RestaurantItem item=getResFromJson(jObject);
            int oldSize=resItems.size()-1;
            resItems.add(item);
            setupAdapter(oldSize,oldSize);
        }
        ArrayList<RestaurantItem> finalBackupList = backupList;
        handler.post(()->{
            if(resItems.size()==0){
                resItems= finalBackupList;
                manageState(CONTENT_STATE);
            }else{
                manageState(CONTENT_STATE);
            }
        });

    }

    private RestaurantItem getResFromJson(JSONObject object) throws JSONException {
        String id=object.getString(Fkey.ID);
        String title=object.getString(Fkey.TITLE);;
        String placeholder=object.getString(Fkey.PH_URL);;
        String url=object.getString(Fkey.URL);;
        return new RestaurantItem(id,title,placeholder,url);
    }
    private void manageState(String state){
        if(state.equals(LOADING_STATE)){
            binding.progressCircular.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
            binding.offlineLyt.setVisibility(View.GONE);
        }else if(state.equals(CONTENT_STATE)){

            binding.progressCircular.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.offlineLyt.setVisibility(View.GONE);

        }else if(state.equals(OFFLINE_STATE)){

            binding.progressCircular.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);
            binding.offlineLyt.setVisibility(View.VISIBLE);

        }
    }
    private void setupAdapter(int position,int oldSize){
        if(adapter!=null){
            handler.postDelayed(()->{
                if(position>oldSize){
                    adapter.notifyItemInserted(position);
                }else{
                    adapter.notifyItemChanged(position);
                }
            },100);
        }else{
            initView();
            adapter.notifyDataSetChanged();
        }
    }
    void showError(String errorText){
        Log.i(this.TAG,errorText);
    }

    public static final String LOADING_STATE ="LOADING";
    public static final String OFFLINE_STATE ="PROGRESS";
    public static final String CONTENT_STATE ="CONTENT";


}