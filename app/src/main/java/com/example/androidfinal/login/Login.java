package com.example.androidfinal.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.ui.MainActivity;

public class Login extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // Inicializar os componentes da interface
        editTextUsername = findViewById(R.id.Login);
        editTextPassword = findViewById(R.id.Senha);
        btnLogin = findViewById(R.id.btnLogin);
        // Configurar o evento de clique no botão de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (login(username, password)) {
                    Toast.makeText(Login.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(Login.this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Função de login
    private boolean login(String username, String password) {
        // Recuperar dados do SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("username", null);
        String storedPassword = sharedPreferences.getString("password", null);
        // Verificar se as credenciais coincidem
        return username.equals(storedUsername) && password.equals(storedPassword);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Definir usuário e senha padrão no SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Verifique se já existem credenciais armazenadas. Caso contrário, armazene algumas padrões.
        if (sharedPreferences.getString("username", null) == null) {
            editor.putString("username", "admin");
            editor.putString("password", "senha");
            editor.apply();
        }
    }
}