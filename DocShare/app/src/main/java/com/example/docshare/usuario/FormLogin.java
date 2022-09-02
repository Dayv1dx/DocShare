package com.example.docshare.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docshare.R;
import com.example.docshare.TelaUsuario2Activity;
import com.example.docshare.formularios.FormCadastro;
import com.example.docshare.metodos.FileGenerator;
import com.example.docshare.metodos.ImagePic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    private EditText edt_email, edt_senha;
    private ImageView loadingBg;
    private TextView edt_cadastre;
    private Button bt_entrar;
    private ProgressBar progressBar;
    private String mensagens[] = {"Insira seu email e senha"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();   // Esconder barra de ação
        IniciarComponentes();

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String senha = edt_senha.getText().toString();

                if(email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(getApplicationContext(), mensagens[0], Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    loadingBg.setVisibility(View.INVISIBLE);
                }
                else {
                    View view = getCurrentFocus();
                    HideKeyboard(view);
                    progressBar.setVisibility(View.VISIBLE);
                    loadingBg.setVisibility(View.VISIBLE);
                    AutenticarUsuario(email, senha);
                }
            }
        });

        edt_cadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFormCadastroActivity = new Intent(getApplicationContext(), FormCadastro.class);
                startActivity(goToFormCadastroActivity);
                finish();
            }
        });
    }



    private void HideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        abrirDialog();
    }

    public void abrirDialog (){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirmação");
        dialog.setMessage("Deseja sair do aplicativo?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FormLogin.super.onBackPressed();
            }
        });

        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog.create();
        dialog.show();
    }

    public void IniciarComponentes(){
        edt_email = findViewById(R.id.edit_email);
        edt_senha = findViewById(R.id.editTextPassword);
        bt_entrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progress_bar);
        edt_cadastre = findViewById(R.id.textViewCadastre);
        progressBar.setVisibility(View.INVISIBLE);
        loadingBg = findViewById(R.id.loagingBgLogin);
        loadingBg.setVisibility(View.INVISIBLE);
        edt_cadastre = findViewById(R.id.textViewCadastre);
    }

    private void AutenticarUsuario(String email, String senha){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToMainActivity();
                        }
                    }, 3000);
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (Exception e){
                        erro = "Erro ao logar usuário";
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    loadingBg.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /***
     * Manter usuário logado
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null)
            goToMainActivity();

    } 

    private void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), TelaUsuario2Activity.class);
        startActivity(intent);
        finish();
    }
}