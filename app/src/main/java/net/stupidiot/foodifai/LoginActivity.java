package net.stupidiot.foodifai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

    public static Map<String, Integer> map = new HashMap<>();
    EditText email;
    EditText password;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.LoginBtn);

        load();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();

                ParseUser.logInInBackground(emailTxt, passwordTxt, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            // If user exist and authenticated, send user to Welcome.class
                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    HomePage.class);
                            startActivity(intent);

                            finish();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Invalid credentials, please try again signup if you're a new user",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        TextView signUp = (TextView)findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void load()
    {
        try {
            InputStream is = getAssets().open("food.dat");

            Scanner scanner = new Scanner(is);

            while (scanner.hasNextLine()) {
                String[] s = scanner.nextLine().split(":");
                String key = s[0].toLowerCase();
                Log.d(this.getClass().getSimpleName().toString(), key);
                int value = Integer.parseInt(s[1].trim());
                map.put(key, value);
            }
        }
        catch (IOException e)
        {
            Log.e("Error while reading from food.dat", e.getMessage());
        }
    }
}
