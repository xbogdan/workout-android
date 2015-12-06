package com.boamfa.workout.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.DrawerAdapter;
import com.boamfa.workout.utils.AppService;
import com.boamfa.workout.utils.User;
import com.boamfa.workout.utils.UserLocalStore;

/**
 * Created by bogdan on 25/11/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerListener;
    protected ListView listView;
    protected DrawerAdapter drawerAdapter;
    protected AppService service;
    protected User currentUser;
    protected UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        listView = (ListView) findViewById( R.id.drawerList );
        drawerAdapter = new DrawerAdapter(this);
        listView.setAdapter(drawerAdapter);

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );

        drawerListener = new ActionBarDrawerToggle( this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.drawer_opened);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.drawer_closed);
                }
            }
        };

        drawerLayout.setDrawerListener(drawerListener);

        service = new AppService();
        userLocalStore = new UserLocalStore(this);
        currentUser = userLocalStore.getLoggedInUser();

    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerListener.onOptionsItemSelected( item )) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void alertMessage(String title, String message) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
