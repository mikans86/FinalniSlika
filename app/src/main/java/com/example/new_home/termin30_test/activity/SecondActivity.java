package com.example.new_home.termin30_test.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.new_home.termin30_test.R;
import com.example.new_home.termin30_test.model.Filmovi;
import com.example.new_home.termin30_test.model.Glumci;
import com.example.new_home.termin30_test.model.ORMliteHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by New_home on 12.3.2017.
 */

public class SecondActivity extends AppCompatActivity{

    private SharedPreferences preferences;
    private ORMliteHelper databaseHelper;
    private Glumci glumci;
    private ImageView slika;
    private String imagePath;
    private FirstActivity firstActivity;
    public static String TOAST = "notif_toast";
    public static String NOTIFICATION = "notif_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int kljuc = getIntent().getExtras().getInt(FirstActivity.ACTOR_KEY);

        try {
            glumci = getDatabaseHelper().getGlumciDao().queryForId(kljuc);

            TextView ime = (TextView) findViewById(R.id.tv_ime);
            TextView bio = (TextView) findViewById(R.id.tv_bio);
            TextView ocena = (TextView) findViewById(R.id.tv_ocena);
            TextView datum = (TextView) findViewById(R.id.tv_datum);

            ime.setText(glumci.getImeGlumci());
            bio.setText(glumci.getBioGlumcmi());
            ocena.setText(glumci.getOcenaGlumci());
            datum.setText(glumci.getDatum());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            final ListView listView = (ListView) findViewById(R.id.lv_spisakFilmova);
            final List<Filmovi> list = getDatabaseHelper().getFilmoviDao().queryBuilder()
                    .where()
                    .eq(Filmovi.FIELD_NAME_ACTOR, glumci.getIdGlumci())
                    .query();
            final ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            //duzim klikom na bilo koji item, tj spisak filmova, se brise cela lista nakon potvrde u dialogu

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SecondActivity.this);
                    dialog.setTitle("Da li zelite da obrisete ovaj film ?");
                    dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                getDatabaseHelper().getFilmoviDao().delete(list);
                                refresh();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
            //implementacija klika na item, ispisuje toast poruku u kojoj su detalji unetog filma
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Filmovi f = (Filmovi)listView.getItemAtPosition(position);
                    Toast.makeText(SecondActivity.this, "Ime filma: "+f.getImeFilma()+"\nZanr filma: "+f.getZanrFilma()+"\nGodina proizvodnje filma: "+f.getGodinaFilma(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //jedno dugme koje sluzi za izbor slike

        Button imgButton = (Button)findViewById(R.id.btn_slika);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slika = (ImageView)findViewById(R.id.iv_slika);
                selectPicture();
            }
        });
    }

    //metoda koja radi to sto radi :D

    private void selectPicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    //Ovo cudo mora da se napravi da bi se selektovana slika smestila u ImageView koji je definisan

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri selectedImageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    imagePath = selectedImageUri.toString();

                    if (slika != null){
                        slika.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_filmovi);

                Button ok  = (Button)dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText imeFilma = (EditText)dialog.findViewById(R.id.et_ime);
                        EditText zanrFilma = (EditText)dialog.findViewById(R.id.et_zanr);
                        EditText datumFilma = (EditText)dialog.findViewById(R.id.et_datum);

                        Filmovi f = new Filmovi();
                        f.setImeFilma(imeFilma.getText().toString());
                        if (f.getImeFilma().equals("")){
                            f.setImeFilma("No name film");
                            Toast.makeText(SecondActivity.this, "Niste uneli ime filma", Toast.LENGTH_SHORT).show();
                        }
                        f.setZanrFilma(zanrFilma.getText().toString());
                        f.setGodinaFilma(datumFilma.getText().toString());
                        f.setGlumci(glumci);

                        try {
                            getDatabaseHelper().getFilmoviDao().create(f);
                            showMessage("Dodat film u listu");
                            refresh();
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                final Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.action_delete:
                try {
                    getDatabaseHelper().getGlumciDao().delete(glumci);
                    showMessage("Glumac obrisan");
                } catch (SQLException e){
                    e.printStackTrace();
                }
                firstActivity.refresh();
                finish();
                break;
            case R.id.action_edit:

                // ovo sam ja malo komplikovao pa sam u dialogu citao trenutne vredenosti detalja glumca i onda ih menjao po potrebi

                final Dialog dialog1 = new Dialog(this);
                dialog1.setContentView(R.layout.dialog_glumci);

                final EditText imeGlumca = (EditText)dialog1.findViewById(R.id.et_ime);
                final EditText bioGlumca = (EditText)dialog1.findViewById(R.id.et_bio);
                final EditText ocenaGlumca = (EditText)dialog1.findViewById(R.id.et_ocena);
                final EditText datumGlumca = (EditText)dialog1.findViewById(R.id.et_datum);

                imeGlumca.setText(glumci.getImeGlumci());
                bioGlumca.setText(glumci.getBioGlumcmi());
                ocenaGlumca.setText(glumci.getOcenaGlumci());
                datumGlumca.setText(glumci.getDatum());

                Button oks = (Button)dialog1.findViewById(R.id.ok);
                oks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            glumci.setImeGlumci(imeGlumca.getText().toString());
                            glumci.setBioGlumcmi(bioGlumca.getText().toString());
                            glumci.setOcenaGlumci(ocenaGlumca.getText().toString());
                            glumci.setDatum(datumGlumca.getText().toString());

                            getDatabaseHelper().getGlumciDao().update(glumci);
                            showMessage("Izmenjeni podaci");
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        //nisam pravio nov refresh za izmene, pa sam koristio onaj za glumce iz prvog aktivitija
                        firstActivity.refresh();
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
            // ovo sam radio jer nikako drugacije nisam mogao da implmentiram back dugme u SecondActivity
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metoda koja komunicira sa bazom podataka
    public ORMliteHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMliteHelper.class);
        }
        return databaseHelper;
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.lv_spisakFilmova);

        if (listview != null){
            ArrayAdapter<Filmovi> adapter = (ArrayAdapter<Filmovi>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Filmovi> list = getDatabaseHelper().getFilmoviDao().queryBuilder()
                            .where()
                            .eq(Filmovi.FIELD_NAME_ACTOR, glumci.getIdGlumci())
                            .query();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void statusMessage(String message){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_buy);
        builder.setContentTitle("pripremni test");
        builder.setContentText(message);
        notificationManager.notify(1, builder.build());

    }

    public void  showMessage(String message){
        boolean toast = preferences.getBoolean(TOAST, false);
        boolean notification = preferences.getBoolean(NOTIFICATION, false);

        if (toast){
            Toast.makeText(SecondActivity.this, message, Toast.LENGTH_SHORT).show();
        }
        if (notification){
            statusMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
