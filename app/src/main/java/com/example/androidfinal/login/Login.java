package com.example.androidfinal.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.ui.MainActivity;

public class Login extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button btnLogin;
    private Switch switchNightMode;
    private long lastBackPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // Inicializar os componentes da interface
        editTextUsername = findViewById(R.id.Login);
        editTextPassword = findViewById(R.id.Senha);
        btnLogin = findViewById(R.id.btnLogin);
        switchNightMode = findViewById(R.id.switchNightMode);
        View telaFundo = findViewById(R.id.tela);

        // Recuperar a preferência do modo noturno do SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean isNightMode = sharedPreferences.getBoolean("isNightMode", false);

        // Configurar o estado do switch com base na preferência(Shared)
        switchNightMode.setChecked(isNightMode);
        // Alterar o tema globalmente(switch)
        setNightMode(isNightMode);

        //Caso usuário clique fora dos objetos interativosw.
        telaFundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this,"Preencha os campos e clique 'ACESSAR'!", Toast.LENGTH_SHORT).show();
            }
        });

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



        switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Armazenar a preferência do modo noturno no SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isNightMode", isChecked);
            editor.apply();

            // Alterar o modo de tema globalmente
            setNightMode(isChecked);
        });
    }

    // Função de login
    private boolean login(String username, String password) {
        // Recuperar dados do SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("username", null);
        String storedPassword = sharedPreferences.getString("password", null);

        // Verificaras credenciais
        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Definir usuário e senha padrão no SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Verifique se já existem. Caso contrário será armazenado.
        if (sharedPreferences.getString("username", null) == null) {
            editor.putString("username", "admin");
            editor.putString("password", "senha");
            editor.apply();
        }

    }

    @Override
    public void onBackPressed() {
        // Checa se o tempo entre os cliques foi curto o suficiente
        if (lastBackPressedTime + 2000 > System.currentTimeMillis()) {
            //clicar duas vezes dentro de 2 segundos, sai do app
            super.onBackPressed();
        } else {
            // Se não, exibe a mensagem de aviso
            Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();
        }

        // Atualiza o tempo do último clique
        lastBackPressedTime = System.currentTimeMillis();
    }

    private void setNightMode(boolean isNightMode) {
        // Altera o modo noturno
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
}