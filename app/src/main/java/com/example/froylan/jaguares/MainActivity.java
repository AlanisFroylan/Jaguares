package com.example.froylan.jaguares;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    RadioButton r_tutorias,r_servivcio,r_idiomas,r_centro,r_becas,r_cultura,r_residencias,r_escolares;
    Button cerrar,ingresar;
    String preferen="";

    TextView textView;
    Spinner spinner;
    String academia;
    private boolean carrera= false;
    String careras[]={"Seleciona tu carrera","SISTEMAS","BIOQUIMICA","GESTION","INDUSTRIAL","MECATRONICA","TICS","NANOTECNOLOGIA"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cerrar=(Button)findViewById(R.id.cerrar);
        textView=(TextView)findViewById(R.id.text);
        spinner=(Spinner)findViewById(R.id.spinerCarreras);
        ingresar=(Button)findViewById(R.id.ingresar);
        r_tutorias=(RadioButton)findViewById(R.id.radio_Tutorias);
        r_servivcio=(RadioButton)findViewById(R.id.radio_SS);
        r_idiomas=(RadioButton)findViewById(R.id.radio_I);
        r_centro=(RadioButton)findViewById(R.id.radio_CN);
        r_becas=(RadioButton)findViewById(R.id.radio_B);
        r_cultura=(RadioButton)findViewById(R.id.radio_CD);
        r_residencias=(RadioButton)findViewById(R.id.radio_R);
        r_escolares=(RadioButton)findViewById(R.id.radio_SE);




        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,careras);
        spinner.setAdapter(adapter);
        cargarpreferencias();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(!preferen.equals("")){
                goNoticiasScreen();
            }
            textView.setText("Elige a que carrera perteneces ");
        }else{
            goLoginScreen();
        }

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferencias=getSharedPreferences("PreferenciaUsuario",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferencias.edit();
                editor.putString("academia","");
                editor.putBoolean("tutorias",false);
                editor.putBoolean("social",false);
                editor.putBoolean("idiomas",false);
                editor.putBoolean("negocios",false);
                editor.putBoolean("escolares",false);
                editor.putBoolean("residencias",false);
                editor.putBoolean("cultura",false);
                editor.putBoolean("becas",false);
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
        });
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carrera) {
                    guardarPreferencias();
                    goNoticiasScreen();

                }else{
                    Toast.makeText(MainActivity.this, "No has elegido carerra!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    carrera=false;

                }
                if(i==1){
                    carrera=true;
                    academia="SISTEMAS";
                }
                if(i==2){
                    carrera=true;
                    academia="BIOQUIMICA";
                }
                if(i==3){
                    carrera=true;
                    academia="GESTION";
                }
                if(i==4){
                    carrera=true;
                    academia="INDUSTRIAL";
                }
                if(i==5){
                    carrera=true;
                    academia="MECATRONICA";
                }
                if(i==6){
                    carrera=true;
                    academia="TICS";

                }
                if(i==7){
                    carrera=true;
                    academia="NANOTECNOLOGIA";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    public void goLoginScreen(){
        Intent i = new Intent(this,LoginActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public void goNoticiasScreen(){
        Intent i = new Intent(this,NoticiasActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public void guardarPreferencias(){
        SharedPreferences preferencias=getSharedPreferences("PreferenciaUsuario",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("academia",academia);
        editor.putBoolean("tutorias",r_tutorias.isChecked());
        editor.putBoolean("social",r_servivcio.isChecked());
        editor.putBoolean("idiomas",r_idiomas.isChecked());
        editor.putBoolean("negocios",r_centro.isChecked());
        editor.putBoolean("escolares",r_escolares.isChecked());
        editor.putBoolean("residencias",r_residencias.isChecked());
        editor.putBoolean("cultura",r_cultura.isChecked());
        editor.putBoolean("becas",r_becas.isChecked());
        editor.commit();
    }
    public void cargarpreferencias(){
        SharedPreferences preferencias=getSharedPreferences("PreferenciaUsuario",Context.MODE_PRIVATE);
        preferen=preferencias.getString("academia","");
        r_tutorias.setChecked(preferencias.getBoolean("tutorias",false));
        r_servivcio.setChecked(preferencias.getBoolean("social",false));
        r_idiomas.setChecked(preferencias.getBoolean("idiomas",false));
        r_centro.setChecked(preferencias.getBoolean("negocios",false));
        r_escolares.setChecked(preferencias.getBoolean("escolares",false));
        r_residencias.setChecked(preferencias.getBoolean("residencias",false));
        r_cultura.setChecked(preferencias.getBoolean("cultura",false));
        r_becas.setChecked(preferencias.getBoolean("becas",false));



    }
}
