package com.example.energieverbrauch;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, com.example.energieverbrauch.StartFragment.StartFragmentListener, com.example.energieverbrauch.AddCounterFragment.AddCounterFragmentListener {

    public StartFragment StartFragment;
    public MyConsumptionFragment MyConsumptionFragment;
    public MyCountersFragment MyCountersFragment;
    public SavingTipsFragment SavingTipsFragment;
    public SettingsFragment SettingsFragment;

    public DrawerLayout drawer;

    int progress = 0;
    float MaxVerbrauch = 0;
    float aktuellerVerbrauch = 10;
    int anzahlZaehler = 0;
    boolean buttonErstelltenZaehlerHinzufuegenClicked = false;

    ArrayList<String> zaehlername;
    ArrayList<Float> standBeginn;
    ArrayList<Float> preisProEinheit;

    Bundle dataToMyCountersFrag = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StartFragment = new StartFragment();
        MyCountersFragment = new MyCountersFragment();
        MyConsumptionFragment = new MyConsumptionFragment();
        SavingTipsFragment = new SavingTipsFragment();
        SettingsFragment = new SettingsFragment();

        zaehlername = new ArrayList<>();
        standBeginn = new ArrayList<>();
        preisProEinheit = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close); //ermöglicht Blinden App zu nutzen, durch Vorlesefunktion
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) { //only switch to Start if app is started initially. Rotating the screen wont cause jumping back to start.
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_start);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { //öffnet verschiedene Fragments, je nach Klick im NavDrawer
        switch (item.getItemId()) {
            case R.id.nav_start:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartFragment()).commit();
                break;
            case R.id.nav_MyConsumption:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyConsumptionFragment()).commit();
                break;
            case R.id. nav_MyCounters:
                dataToMyCountersFrag.putStringArrayList("zaehlername", zaehlername);
                dataToMyCountersFrag.putInt("anzahlZaehler", anzahlZaehler);
                MyCountersFragment.setArguments(dataToMyCountersFrag);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MyCountersFragment).commit();
                break;
            case R.id. nav_SavingTips:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SavingTipsFragment()).commit();
                break;
            case R.id. nav_Settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START); //nachdem ein Menüpunkt geklickt wurde, schließt sich das Menü nach links(START)
        return true;
    }

    @Override
    public void dataFromStartFragmentToMainActivity(int progressSF, float MaxVerbrauchSF) { //liest Wert aus EditText_StartFragment ab
        progress = progressSF;
        MaxVerbrauch = MaxVerbrauchSF;
    }

    @Override
    public void dataFromAddCounterFragmentToMainActivity(String zaehlernameACF, float standBeginnACF, float preisProEinheitACF, boolean buttonErstelltenZaehlerHinzufuegenClickedACF) {
        anzahlZaehler++;
        zaehlername.add(zaehlernameACF);
        standBeginn.add(standBeginnACF);
        preisProEinheit.add(preisProEinheitACF);
        buttonErstelltenZaehlerHinzufuegenClicked = buttonErstelltenZaehlerHinzufuegenClickedACF;
        if (buttonErstelltenZaehlerHinzufuegenClicked) {
            buttonErstelltenZaehlerHinzufuegenClicked = false;
            dataToMyCountersFrag.putStringArrayList("zaehlername", zaehlername);
            dataToMyCountersFrag.putInt("anzahlZaehler", anzahlZaehler);
            MyCountersFragment.setArguments(dataToMyCountersFrag);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MyCountersFragment).commit();
        }
    }

    public float updateHint() {
        return MaxVerbrauch;
    }

    public int sendProgressData() {
        return progress;
    }

    public float sendAktuelleVerbrauchsData() {
        return aktuellerVerbrauch;
    }


    @Override
    public void onBackPressed() { //sorgt dafür, dass bei klicken auf zurück bei geöffnetem Menü nicht die App, sondern das Menü geschlossen wird
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }
}