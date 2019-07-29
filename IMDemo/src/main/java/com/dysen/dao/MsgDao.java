package com.dysen.dao;

import com.dysen.im_demo.AppContext;
import com.dysen.im_demo.entry.Msg;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @package com.dysen
 * @email dy.sen@qq.com
 * created by dysen on 2019-07-29 - 17:44
 * @info
 */
public class MsgDao {

    private static MsgDao msgDao;

    public static synchronized MsgDao getInstance() {
        if (msgDao == null) {
            msgDao = new MsgDao();
        }
        return msgDao;
    }

    private Realm realm = AppContext.getRealm();

    public RealmResults<Msg> getMsg() {
        RealmResults<Msg> msgRealmResults = realm.where(Msg.class)
                .findAll();
        return msgRealmResults;
    }

    public Msg getMsg(String name) {
        Msg msg = realm.where(Msg.class).equalTo("name", name).findFirst();
        return msg;
    }

    public void addAllMsg(final List<Msg> dataList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dataList);
            }
        });

    }

}
