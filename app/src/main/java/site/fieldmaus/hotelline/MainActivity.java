package site.fieldmaus.hotelline;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new Airline(); // 첫 화면을 항공 검색으로 하기 위한 부분
        Bundle bundle = new Bundle(4);
        bundle.putString("id", getIntent().getExtras().getString("id"));
        bundle.putString("email", getIntent().getExtras().getString("email"));
        bundle.putString("name", getIntent().getExtras().getString("name"));
        bundle.putString("type", getIntent().getExtras().getString("type"));
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header_view = navigationView.getHeaderView(0);

        TextView nav_nameview = (TextView) nav_header_view.findViewById(R.id.nav_nameview);
        TextView nav_emailview = (TextView) nav_header_view.findViewById(R.id.nav_emailview);
        nav_nameview.setText(getIntent().getExtras().getString("name"));
        nav_emailview.setText(getIntent().getExtras().getString("email"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new Fragment();

        if (id == R.id.nav_airline) {
            // Handle the camera action
            fragment = new Airline();
        } else if (id == R.id.nav_hotel) {
            fragment = new Hotel();
        } else if (id == R.id.nav_profile) {
            fragment = new Profile();
        } else if (id == R.id.nav_settings) {
            fragment = new Settings();
        }
        if (fragment != null) {
            Bundle bundle = new Bundle(4);
            bundle.putString("id", getIntent().getExtras().getString("id"));
            bundle.putString("email", getIntent().getExtras().getString("email"));
            bundle.putString("name", getIntent().getExtras().getString("name"));
            bundle.putString("type", getIntent().getExtras().getString("type"));
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}