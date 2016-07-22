package ru.pooch.myapplication;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends AppCompatActivity {
    private EditText text;
    private TextView mTranslatedText;
    private TextView rusText;
    private TextView engText;
    private boolean language = true;
    Map<String, String> mapJson = new HashMap<>();
    private ImageButton changeBtn;
    private final String KEY = "trnsl.1.1.20160530T163721Z.46212086c960f3a8.b52ad3378730521c12680e290eba0d085ba50181";
    private final String URL = "https://translate.yandex.net";

    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();
    private Link intf = retrofit.create(Link.class);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.editText);
        rusText = (TextView) findViewById(R.id.textRu);
        engText = (TextView) findViewById(R.id.textEn);
        mTranslatedText = (TextView) findViewById(R.id.textView);
        changeBtn = (ImageButton) findViewById(R.id.buttonChange);
        mapJson.put("lang", "ru-en");

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text != null) {
                    text.getText().clear();
                }
            }
        });
        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mapJson.put("key", KEY);
                    String request = text.getText().toString();
                    request = request.replace(" ", "+");
                    mapJson.put("text", request);
                    Call<Object> call = intf.translate(mapJson);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        Response<Object> response = call.execute();
                        Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);
                        for (Map.Entry e : map.entrySet()) {
                            if (e.getKey().equals("text")) {
                                mTranslatedText.setText(e.getValue().toString().replace("+", " ").replace("[", "").replace("]", "").replace("\n", ""));
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            }
                        }
                    } catch (MalformedJsonException e) {
                        Toast.makeText(getApplicationContext(), "Multiple request is not implemented yet", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!language) {
                    rusText.setText(R.string.en);
                    engText.setText(R.string.ru);
                    mapJson.put("lang", "en-ru");
                    language = true;
                } else {
                    rusText.setText(R.string.ru);
                    engText.setText(R.string.en);
                    mapJson.put("lang", "ru-en");
                    language = false;
                }
            }
        });

    }
}
