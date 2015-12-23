package com.boamfa.workout.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.boamfa.workout.R;
import com.boamfa.workout.utils.AppService;

/**
 * Created by bogdan on 21/12/15.
 */
public class AppSyncAdapter extends AbstractThreadedSyncAdapter {
    private static AccountManager accountManager;
    private static AppService service;

    public AppSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        accountManager = AccountManager.get(context);
        final String accountType = context.getString(R.string.accountType);
        final String authTokenType = context.getString(R.string.authTokenType);
        Account availableAccounts[] = accountManager.getAccountsByType(accountType);
        final Account currentAccount = availableAccounts[0];
//        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(currentAccount, authTokenType, null, context, null, null);
//        String authToken;
//        try {
//            authToken = future.getResult().getString("authtoken");
//        }
//        service = new AppService();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    }
}
