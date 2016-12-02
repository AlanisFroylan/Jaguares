package com.example.froylan.jaguares;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class NoticiasActivity extends AppCompatActivity {
    String carrera="";
    Button cerrar;
    Spinner spinner;
    String opcion;
    TextView text;
    List<String> tipoNoticias= new ArrayList<>();
    Firebase mDatabase;
    ArrayList<String> items = new ArrayList();
    ListView recyclerView;
    ArrayAdapter <NoticiasModel.News> modelsAdapter;
    ArrayList <NoticiasModel.News> listaNoticias= new ArrayList<>();
    final private String FIREBASE_URL_G="https://jaguares-f50cd.firebaseio.com/General";
    final private String FIREBASE_URL_SE="https://jaguares-f50cd.firebaseio.com/ServiciosEscolares";
    final private String FIREBASE_URL_I="https://jaguares-f50cd.firebaseio.com/Idiomas";
    final private String FIREBASE_URL_CD="https://jaguares-f50cd.firebaseio.com/Cultura";
    final private String FIREBASE_URL_T="https://jaguares-f50cd.firebaseio.com/Tutorias";
    final private String FIREBASE_URL_B="https://jaguares-f50cd.firebaseio.com/Becas";
    final private String FIREBASE_URL_SS="https://jaguares-f50cd.firebaseio.com/ServicioSocial";
    final private String FIREBASE_URL_RE="https://jaguares-f50cd.firebaseio.com/Residencias";
    final private String FIREBASE_URL_CN="https://jaguares-f50cd.firebaseio.com/CentroNegocios";
    private boolean tutorias,social,idiomas,negocios,escolares,residencias,cultura,becas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        text=(TextView)findViewById(R.id.textcarera);
        cerrar=(Button)findViewById(R.id.cerrar);
        spinner=(Spinner)findViewById(R.id.spiner_Noticias);
        recyclerView=(ListView)findViewById(R.id.list_noticias);
        cargarpreferencias();
        llenarAdapter();
        Firebase.setAndroidContext(this);

        modelsAdapter=new ArrayAdapter<NoticiasModel.News>(this,android.R.layout.simple_list_item_1,listaNoticias);
        recyclerView.setAdapter(modelsAdapter);


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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){

                    mDatabase=new Firebase(FIREBASE_URL_G);
                    Consultanoticias();
                }
                if(i==1){
                    modelsAdapter.clear();
                    final String FIREBASE_URL_CA="https://jaguares-f50cd.firebaseio.com/"+carrera;
                    mDatabase=new Firebase(FIREBASE_URL_CA);

                    Consultanoticias();
                }
                opcion=spinner.getSelectedItem().toString();
                if(opcion.equals("tutorias")){
                    modelsAdapter.clear();
                    mDatabase= new Firebase(FIREBASE_URL_T);
                    Consultanoticias();
                }else if(opcion.equals("Servicio Social")){
                    modelsAdapter.clear();
                    mDatabase= new Firebase(FIREBASE_URL_SS);
                    Consultanoticias();
                }else if(opcion.equals("Idiomas")) {
                    modelsAdapter.clear();
                    mDatabase = new Firebase(FIREBASE_URL_I);
                    Consultanoticias();
                }else if(opcion.equals("Centro de Negocios")) {
                    modelsAdapter.clear();
                    mDatabase = new Firebase(FIREBASE_URL_CN);
                    Consultanoticias();
                }else if(opcion.equals("Servicios Escolares")) {
                    modelsAdapter.clear();
                    mDatabase = new Firebase(FIREBASE_URL_SE);
                    Consultanoticias();
                }else if(opcion.equals("Residencias")) {
                    modelsAdapter.clear();
                    mDatabase = new Firebase(FIREBASE_URL_RE);
                    Consultanoticias();
                }else if(opcion.equals("Cultura y Deportes")) {
                    modelsAdapter.clear();
                    mDatabase = new Firebase(FIREBASE_URL_CD);
                    Consultanoticias();
                }else if(opcion.equals("Becas")) {
                    modelsAdapter.clear();
                    mDatabase = new Firebase(FIREBASE_URL_B);
                    Consultanoticias();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               dialog(i);


           }
       });

    } @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.action_settings){
            goPreferences();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void cargarpreferencias(){
        SharedPreferences preferencias=getSharedPreferences("PreferenciaUsuario",Context.MODE_PRIVATE);
        carrera=preferencias.getString("academia","");
        tipoNoticias.add("General");
        tipoNoticias.add(carrera);
        tutorias=preferencias.getBoolean("tutorias",false);
        if(tutorias){
            tipoNoticias.add("tutorias");
        }
        social=preferencias.getBoolean("social",false);
        if(social){
            tipoNoticias.add("Servicio Social");
        }
        idiomas=preferencias.getBoolean("idiomas",false);
        if(idiomas){
            tipoNoticias.add("Idiomas");
        }
        negocios=preferencias.getBoolean("negocios",false);
        if(negocios){
            tipoNoticias.add("Centro de Negocios");
        }
        escolares=preferencias.getBoolean("escolares",false);
        if(escolares){
            tipoNoticias.add("Servicios Escolares");
        }
        residencias=preferencias.getBoolean("residencias",false);
        if(residencias){
            tipoNoticias.add("Residencias");
        }
        cultura=preferencias.getBoolean("cultura",false);
        if(cultura){
            tipoNoticias.add("Cultura y Deportes");
        }
        becas=preferencias.getBoolean("becas",false);
        if(becas){
            tipoNoticias.add("Becas");

        }

        text.setText(carrera);

    }
    public void goLoginScreen(){
        Intent i = new Intent(this,LoginActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public void goPreferences(){
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
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public void llenarAdapter(){

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tipoNoticias);
        spinner.setAdapter(adapter);
    }
    public void Consultanoticias(){
       mDatabase.addValueEventListener(new com.firebase.client.ValueEventListener() {
           @Override
           public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
               Map<String,String> map= dataSnapshot.getValue(Map.class);
               if(map!=null) {

                   Iterator it = map.entrySet().iterator();
                   items.clear();
                   while (it.hasNext()) {
                       Map.Entry e = (Map.Entry) it.next();
                       String val = e.getValue().toString();

                       String datos = e.getKey() + " " + e.getValue();

                       String datos1 = datos.replace("{", ",");
                       String datos2 = datos1.replace("}", "");

                       items.add(datos2);

                   }
                   String key;
                   String noticia;
                   String desc, tit, dat, url;
                   listaNoticias.clear();
                   for (int y = 0; y < items.size(); y++) {
                       noticia = items.get(y);
                       StringTokenizer st = new StringTokenizer(noticia, ",");
                       NoticiasModel.News n = new NoticiasModel.News();

                       while (st.hasMoreTokens()) {

                           key = st.nextToken();

                           desc = st.nextToken();
                           desc = desc.substring(12, desc.length());
                           n.setDescripcion(desc);
                           dat = st.nextToken();
                           dat = dat.substring(6, dat.length());
                           n.setDate(dat);
                           url = st.nextToken();
                           url = url.substring(5, url.length());
                           n.setUrl(url);
                           tit = st.nextToken();
                           tit = tit.substring(8, tit.length());
                           n.setTitulo(tit);



                           listaNoticias.add(n);
                           modelsAdapter.notifyDataSetChanged();

                       }
                   }
               }else{
                   Toast.makeText(NoticiasActivity.this, "No hay noticias diponibles en esta area: "+opcion, Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onCancelled(FirebaseError firebaseError) {

           }
       });
    }
    public void dialog(int x){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        alertDialogBuilder.setTitle(listaNoticias.get(x).getTitulo());
        final String url=listaNoticias.get(x).getUrl();
        alertDialogBuilder.setMessage(listaNoticias.get(x).getDescripcion());
        if(!url.equals("")) {
            alertDialogBuilder.setPositiveButton("Leer Mas", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = null;
                    intent = new Intent(intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            });
        }
        alertDialogBuilder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
