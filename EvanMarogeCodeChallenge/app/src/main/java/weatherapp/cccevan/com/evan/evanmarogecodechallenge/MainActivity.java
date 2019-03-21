package weatherapp.cccevan.com.evan.evanmarogecodechallenge;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weatherapp.cccevan.com.evan.evanmarogecodechallenge.models.Font;
import weatherapp.cccevan.com.evan.evanmarogecodechallenge.models.Information;

public class MainActivity extends AppCompatActivity {

    final static String URLN = "https://eulerity-hackathon.appspot.com";
    private TextView textView;
    private MaterialSpinner spinnerForFonts;
    private JsonHolderApi jsonHolderApi;
    private Retrofit retrofit;
    private ArrayList<String> arrayOfFontStrings;
    private ArrayList<Font> arrayOfFontsObjects;
    private String mainUrlOfFont;
    private EditText UserEmail;
    private int number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //create an instance of retrofit with a converter
        retrofit = new Retrofit.Builder()
                .baseUrl("https://eulerity-hackathon.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //with the retrofit, you can create the jsonHolderApi
        jsonHolderApi = retrofit.create(JsonHolderApi.class);


        //initialize the arraylist and other UI objects
        arrayOfFontStrings = new ArrayList<String>();
        arrayOfFontsObjects = new ArrayList<Font>();
        textView = (TextView) findViewById(R.id.textRetro);
        UserEmail = (EditText) findViewById(R.id.email);
        spinnerForFonts = (MaterialSpinner) findViewById(R.id.spinner_for_font);


        //set a button click listener.
        findViewById(R.id.change_font).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = "http://eulerity-hackathon.appspot.com/fonts/RobotoCondensed-Bold.ttf";
                Log.d("MAIN", "onClick: " + "you clicked");
                //create a instance of asyncTask and execute it.
                if (mainUrlOfFont != null) {
                    new MyTask().execute(mainUrlOfFont);

                } else {
                    Toast.makeText(getApplicationContext(), "Please Choose Font", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //post request button
        findViewById(R.id.submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainUrlOfFont != null) {
                    String emailAddress = UserEmail.getText().toString();
                    if (emailAddress == null) {
                        Toast.makeText(getApplicationContext(), "Please type your UserEmail", Toast.LENGTH_SHORT).show();
                    } else {
                        EditText typed = (EditText) findViewById(R.id.user_input);
                        String userInput = typed.getText().toString() + "";
                        createPostRequest(
                                emailAddress,
                                arrayOfFontsObjects.get(number).getFamily(),
                                arrayOfFontsObjects.get(number).isBoldOrNot(),
                                arrayOfFontsObjects.get(number).isItalicOrNot(),
                                userInput,
                                arrayOfFontsObjects.get(number).getUrl());
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Choose Font", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //@GET request
        retrofitGet();
        //add content to the spinner
        addContnetToSpinnder(arrayOfFontStrings);
    }


    //get request

    private void retrofitGet() {

        //get a list of Font objects
        Call<List<Font>> call = jsonHolderApi.getFonts();

        //create a new thread to execute the code needed
        call.enqueue(new Callback<List<Font>>() {
            @Override
            public void onResponse(Call<List<Font>> call, Response<List<Font>> response) {
                //check if there is a 404 response.
                if (!response.isSuccessful()) {
//                    textView.setText("There was an error: " + response.body());
                    return;
                }

                List<Font> fonts = response.body();

                //iterate through the objects and add each one to an arraylist of Font objects

                for (Font font : fonts) {
                    arrayOfFontsObjects.add(font);
                }
                for (Font font : fonts) {
                    //create a custom string for each Font that tells us: 1. the name. 2. If bold 3. if italic
                    String content = "";
                    content += "Font: " + font.getFamily() + "        ";
                    if (font.isBoldOrNot()) {
                        content += " Bold " + "      ";
                    }
                    if (font.isItalicOrNot()) {
                        content += "Italic ";

                    }
                    if (!font.isItalicOrNot() & !font.isBoldOrNot()) {
                        content += "Regular ";

                    }

//                    add the string to the array list of strings for the spinner
                    arrayOfFontStrings.add(content);

                }
//                for(int i=0;i<arrayOfFontStrings.size();i++){
//                    Log.d("I am king", "onResponse: " + arrayOfFontStrings.get(i));
//                }
            }

            @Override
            public void onFailure(Call<List<Font>> call, Throwable t) {
                Log.d("GET REQUEST FAILED", "onFailure: get request failed " + t.getMessage());
            }
        });
    }

    //    //method to add fonts to spinner.

    private void addContnetToSpinnder(ArrayList<String> arrayOfFonts) {

        //create an arrayAdapter for the spinner and set it
        ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayOfFonts);
        spinnerForFonts.setAdapter(adp);
        spinnerForFonts.setVisibility(View.VISIBLE);

        //set on selected listener.
        spinnerForFonts.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Toast.makeText(MainActivity.this, "Clicked " + arrayOfFontsObjects.get(position).getUrl(), Toast.LENGTH_LONG).show();
                Log.d("THE POSITION ", "onItemSelected: " + position);

                number = position;
                String n = URLN;

                mainUrlOfFont = arrayOfFontsObjects.get(position).getUrl();
                mainUrlOfFont = n + mainUrlOfFont;
//                Log.d("main font", "onItemSelected: "+ mainUrlOfFont);

//                new DownloadFileURL().execute(n);

            }
        });

    }


    //post request

    private void createPostRequest(String email, String fontFamily, boolean bold, boolean italic, String userInput, String url) {

        //send all the information of the user and create a java object from them  to send
        Information information = new Information(email, fontFamily, bold, italic, userInput, url);

        JsonHolderApi client = retrofit.create(JsonHolderApi.class);

        Call<Information> call = client.createPost(information);

        call.enqueue(new Callback<Information>() {
            @Override
            public void onResponse(Call<Information> call, Response<Information> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Failed: " + response.code());
                    return;
                }


                Toast.makeText(getApplicationContext(), response.code() + " _" + response.body().getAppid(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Information> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });

    }


    //asyncTask to download the font file

    public class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {


            DownloadFileNow(args[0]);
            String d = args[0].substring(args[0].lastIndexOf('/') + 1, args[0].length());
            //send the file name to onPostExecute
            return d;
        }

        @Override
        protected void onPostExecute(String string) {

            super.onPostExecute(string);

            Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//           String[] iles = file.list();
            File iles[] = file.listFiles();

            Log.d("PIC", ": " + iles[2]);

            int position = -1;
            for (int i = 0; i < iles.length; i++) {
                String filename2 = iles[i].toString().substring(iles[i].toString().lastIndexOf('/') + 1, iles[i].toString().length());
                Log.d("PIC", "TWOOOOO: " + filename2);
                if (filename2.equals(string)) {
                    Log.d("PIC", "The string is : " + string);
                    position = i;
                }


            }

            if (position != -1) {
//                File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),iles[position]);


//                Typeface tf = Typeface.createFromFile(file1.getPath());
                Log.d("Loser", "onPostExecute: " + iles[position].toString());
//                Typeface tf = Typeface.createFromFile(iles[position].getAbsolutePath());
                Typeface tf = Typeface.createFromFile(new File(iles[position].getPath()));

                textView.setTypeface(tf);
            }


        }
    }

    private void DownloadFileNow(String url1) {
//        String url =  "http://eulerity-hackathon.appspot.com/fonts/proxima_nova_reg_it.otf";
        String fileName = "proxima_nova_reg_it.otf";
        Log.d("FILE PASSED", "the name new: " + url1);

        String filename2 = url1.substring(url1.lastIndexOf('/') + 1, url1.length());
        Log.d("FILE NAME", "the name new: " + filename2);


        try {
            URL myurl = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) myurl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.connect();

            File rootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename2);
            if (!rootDirectory.getParentFile().exists()) {
                rootDirectory.getParentFile().mkdir();
//                Log.d("FILE NAME", "file made: "+rootDirectory.getParentFile());

            }
            if (!rootDirectory.exists()) {
                rootDirectory.createNewFile();
//                Log.d("FILE NAME", "child made: "+rootDirectory.getParentFile());

            }
            String nameOfFile = URLUtil.guessFileName(url1, null, MimeTypeMap.getFileExtensionFromUrl(url1));
            File file = new File(rootDirectory, nameOfFile);
            file.createNewFile();

            InputStream inputStream = connection.getInputStream();

            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = inputStream.read(buffer)) > 0) {
                output.write(buffer, 0, byteCount);
            }
            output.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            getApplicationContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
