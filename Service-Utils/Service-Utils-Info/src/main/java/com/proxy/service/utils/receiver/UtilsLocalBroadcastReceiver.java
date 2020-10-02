package com.proxy.service.utils.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author : cangHX
 * on 2020/09/30  11:34 PM
 */
public final class UtilsLocalBroadcastReceiver {

    /**
     * 接收消息回调
     */
    public interface ReceiverListener {
        /**
         * 接收到消息
         *
         * @param context: 上下文环境
         * @param intent   : 意图
         * @version: 1.0
         * @author: cangHX
         * @date: 2020-06-24 18:04
         */
        void onLocalReceive(Context context, Intent intent);
    }

    private static final class ReceiverRecord {
        final IntentFilter filter;
        final ReceiverListener receiver;
        boolean broadcasting;
        boolean dead;

        ReceiverRecord(IntentFilter filter, ReceiverListener receiver) {
            this.filter = filter;
            this.receiver = receiver;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(128);
            builder.append("Receiver{");
            builder.append(receiver);
            builder.append(" filter=");
            builder.append(filter);
            if (dead) {
                builder.append(" DEAD");
            }
            builder.append("}");
            return builder.toString();
        }
    }

    private static final class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent, ArrayList<ReceiverRecord> receivers) {
            this.intent = intent;
            this.receivers = receivers;
        }
    }

    private final Context mAppContext;

    private final HashMap<ReceiverListener, ArrayList<ReceiverRecord>> mReceivers = new HashMap<>();
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap<>();

    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList<>();

    static final int MSG_EXEC_PENDING_BROADCASTS = 1;

    private final Handler mHandler;

    private static final Object M_LOCK = new Object();
    private static UtilsLocalBroadcastReceiver mInstance;

    @NonNull
    public static UtilsLocalBroadcastReceiver getInstance(@NonNull Context context) {
        synchronized (M_LOCK) {
            if (mInstance == null) {
                mInstance = new UtilsLocalBroadcastReceiver(context);
            }
            return mInstance;
        }
    }

    private UtilsLocalBroadcastReceiver(Context context) {
        mAppContext = context;
        mHandler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == MSG_EXEC_PENDING_BROADCASTS) {
                    executePendingBroadcasts();
                } else {
                    super.handleMessage(msg);
                }
            }
        };
    }

    public void registerReceiver(@NonNull ReceiverListener receiver, @NonNull IntentFilter filter) {
        synchronized (mReceivers) {
            ReceiverRecord entry = new ReceiverRecord(filter, receiver);
            ArrayList<ReceiverRecord> filters = mReceivers.get(receiver);
            if (filters == null) {
                filters = new ArrayList<>(1);
                mReceivers.put(receiver, filters);
            }
            filters.add(entry);
            for (int i = 0; i < filter.countActions(); i++) {
                String action = filter.getAction(i);
                ArrayList<ReceiverRecord> entries = mActions.get(action);
                if (entries == null) {
                    entries = new ArrayList<>(1);
                    mActions.put(action, entries);
                }
                entries.add(entry);
            }
        }
    }

    public void unregisterReceiver(@NonNull ReceiverListener receiver) {
        synchronized (mReceivers) {
            final ArrayList<ReceiverRecord> filters = mReceivers.remove(receiver);
            if (filters == null) {
                return;
            }
            for (int i = filters.size() - 1; i >= 0; i--) {
                final ReceiverRecord filter = filters.get(i);
                filter.dead = true;
                for (int j = 0; j < filter.filter.countActions(); j++) {
                    final String action = filter.filter.getAction(j);
                    final ArrayList<ReceiverRecord> receivers = mActions.get(action);
                    if (receivers != null) {
                        for (int k = receivers.size() - 1; k >= 0; k--) {
                            final ReceiverRecord rec = receivers.get(k);
                            if (rec.receiver == receiver) {
                                rec.dead = true;
                                receivers.remove(k);
                            }
                        }
                        if (receivers.size() <= 0) {
                            mActions.remove(action);
                        }
                    }
                }
            }
        }
    }

    public boolean sendBroadcast(@NonNull Intent intent) {
        synchronized (mReceivers) {
            final String action = intent.getAction();
            final String type = intent.resolveTypeIfNeeded(mAppContext.getContentResolver());
            final Uri data = intent.getData();
            final String scheme = intent.getScheme();
            final Set<String> categories = intent.getCategories();

            ArrayList<ReceiverRecord> entries = mActions.get(intent.getAction());
            if (entries != null) {
                ArrayList<ReceiverRecord> receivers = null;
                for (int i = 0; i < entries.size(); i++) {
                    ReceiverRecord receiver = entries.get(i);
                    if (receiver.broadcasting) {
                        continue;
                    }

                    int match = receiver.filter.match(action, type, scheme, data, categories, "UtilsLocalBroadcastReceiver");
                    if (match >= 0) {
                        if (receivers == null) {
                            receivers = new ArrayList<>();
                        }
                        receivers.add(receiver);
                        receiver.broadcasting = true;
                    }
                }

                if (receivers != null) {
                    for (int i = 0; i < receivers.size(); i++) {
                        receivers.get(i).broadcasting = false;
                    }
                    mPendingBroadcasts.add(new BroadcastRecord(intent, receivers));
                    if (!mHandler.hasMessages(MSG_EXEC_PENDING_BROADCASTS)) {
                        mHandler.sendEmptyMessage(MSG_EXEC_PENDING_BROADCASTS);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void executePendingBroadcasts() {
        while (true) {
            final BroadcastRecord[] brs;
            synchronized (mReceivers) {
                final int n = mPendingBroadcasts.size();
                if (n <= 0) {
                    return;
                }
                brs = new BroadcastRecord[n];
                mPendingBroadcasts.toArray(brs);
                mPendingBroadcasts.clear();
            }
            for (BroadcastRecord br : brs) {
                int nbr = br.receivers.size();
                for (int j = 0; j < nbr; j++) {
                    final ReceiverRecord rec = br.receivers.get(j);
                    if (!rec.dead) {
                        rec.receiver.onLocalReceive(mAppContext, br.intent);
                    }
                }
            }
        }
    }
}
