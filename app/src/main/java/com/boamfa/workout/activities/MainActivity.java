package com.boamfa.workout.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boamfa.workout.R;
import com.boamfa.workout.classes.User;
import com.boamfa.workout.classes.UserLocalStore;

import java.io.IOException;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawerLayout.addView(contentView, 0);

//        UserLocalStore userLocalStore = new UserLocalStore(this);
//        User currentUser = userLocalStore.getLoggedInUser();

//        System.out.println("ACCOUNTS ======");
//        System.out.println(availableAccounts);
//        System.out.println(availableAccounts.length);

//        System.out.println(availableAccounts[0]);
//        Account acc = availableAccounts[0];
//        final AccountManagerFuture<Bundle> future = am.getAuthToken(acc, "Full access", null, this, null, null);
//        System.out.println(future);

//        try {
//            System.out.println(future.getResult());
//        } catch (OperationCanceledException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (AuthenticatorException e) {
//            e.printStackTrace();
//        }


//        final AccountManagerFuture<Bundle> future = am.getAuthToken(account, authTokenType, null, this, null, null);

        final String accountType = getString(R.string.accountType);
        final String authTokenType = getString(R.string.authTokenType);
        Account availableAccounts[] = accountManager.getAccountsByType(accountType);

        if (availableAccounts.length == 0) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
            intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
            intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
            startActivity(intent);
        } else {
            currentAccount = availableAccounts[0];
            final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(currentAccount, authTokenType, null, this, null, null);
            try {
                authToken = future.getResult().getString("authtoken");
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(MainActivity.this, TracksActivity.class);
            startActivity(i);
        }
    }
}