package com.artto.instagramunfollowers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import dev.niekirk.com.instagram4android.Instagram4Android;
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowersRequest;
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowingRequest;
import dev.niekirk.com.instagram4android.requests.InstagramUnfollowRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramGetUserFollowersResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary;

public class MainActivity extends AppCompatActivity {

    Instagram4Android instagram;
    Button bUnfollowAll;
    DelayedProgressDialog spinner;
    RecyclerView recycler;
    Adapter adapter;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        login();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(unfollowReceiver, new IntentFilter("com.artto.instagramunfollowers.UNFOLLOW"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(unfollowReceiver);
    }

    BroadcastReceiver unfollowReceiver = new BroadcastReceiver() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onReceive(Context context, Intent intent) {
            new AsyncTask<Long, Integer, Void>() {
                @Override
                protected Void doInBackground(Long... longs) {
                    try {
                        instagram.sendRequest(new InstagramUnfollowRequest(longs[0]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    bUnfollowAll.setText(getString(R.string.bUnfollowAllCount,
                            adapter.getItemCount()));
                }
            }.execute(intent.getLongExtra("username", 0));
        }
    };

    private void initialize() {
        bUnfollowAll = findViewById(R.id.bUnfollowAll);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        recycler.setAdapter(adapter);
        spinner = new DelayedProgressDialog();
    }

    void login() {
        startActivityForResult(new Intent(this, LoginActivity.class), 0);
    }

    @SuppressLint("StaticFieldLeak")
    void unfollowAll() {
        new AsyncTask<long[], Void, Void>() {
            @Override
            protected Void doInBackground(long[]... longs) {
                for (long user : longs[0]) {
                    try {
                        Thread.sleep(random.nextInt(5) * 1000);
                        instagram.sendRequest(new InstagramUnfollowRequest(user));
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        publishProgress();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                adapter.removeItem(0);
                bUnfollowAll.setText(getString(R.string.bUnfollowAllCount,
                        adapter.getItemCount()));
            }
        }.execute(adapter.getUnfollowAllList());
    }

    public void onClickUnfollow(View view) {
        showUnfollowDialog();
    }

    @SuppressLint("StaticFieldLeak")
    void loadData() {
        new AsyncTask<Void, Void, Void>() {
            Vector<InstagramUserSummary> followers = new Vector<>();
            Vector<InstagramUserSummary> following = new Vector<>();

            @Override
            protected Void doInBackground(Void... voids) {
                InstagramGetUserFollowersResult result;
                final long userId = instagram.getUserId();
                try {
                    result = instagram.sendRequest(new InstagramGetUserFollowersRequest(userId));
                    followers.addAll(result.getUsers());
                    for(;;) {
                        if (result.getNext_max_id() == null)
                            break;
                        result = instagram.sendRequest(new InstagramGetUserFollowersRequest(userId, result.getNext_max_id()));
                        followers.addAll(result.getUsers());
                    }
                    result = instagram.sendRequest(new InstagramGetUserFollowingRequest(userId));
                    following.addAll(result.getUsers());
                    for(;;) {
                        if (result.getNext_max_id() == null)
                            break;
                        result = instagram.sendRequest(new InstagramGetUserFollowingRequest(userId, result.getNext_max_id()));
                        following.addAll(result.getUsers());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Vector<InstagramUserSummary> unfollowers = new Vector<>(following);
                for (InstagramUserSummary i : following) {
                    final String s = i.getUsername();
                    for (InstagramUserSummary j : followers) {
                        if (s.equals(j.getUsername())) {
                            unfollowers.remove(i);
                            break;
                        }
                    }
                }
                adapter.setUsers(unfollowers);
                bUnfollowAll.setText(getString(R.string.bUnfollowAllCount, unfollowers.size()));
                spinner.cancel();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) login();
        spinner.show(getSupportFragmentManager(), "login");
        instagram = Instagram4Android.builder()
                .username(data.getStringExtra("username"))
                .password(data.getStringExtra("password"))
                .build();
        instagram.setup();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    instagram.login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!instagram.isLoggedIn()) {
                    spinner.cancel();
                    Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_LONG).show();
                    login();
                } else {
                    loadData();
                }
            }
        }.execute();
    }

    void showUnfollowDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this)
                .setTitle(R.string.attention)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        unfollowAll();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                })
                .setMessage(R.string.dialogUnfollowAll);
        adb.show();
    }
}
