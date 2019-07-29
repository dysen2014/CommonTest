package com.dysen.im_demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dysen.common_library.adapter.recycler.SuperRecyclerAdapter;
import com.dysen.common_library.adapter.recycler.SuperRecyclerHolder;
import com.dysen.common_library.utils.DateUtils;
import com.dysen.common_library.utils.ImgResUtils;
import com.dysen.common_library.utils.Tools;
import com.dysen.dao.MsgDao;
import com.dysen.im_demo.entry.Bean;
import com.dysen.im_demo.entry.Msg;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ECChatActivity extends AppCompatActivity implements EMMessageListener {

    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private CheckBox mMoreMenu;

    // 显示内容的 TextView
    private TextView mContentText;
    private RecyclerView rclData, rclMenu;
    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String mChatId;
    // 当前会话对象
    private EMConversation mConversation;
    private long lastTime;
    private List<Integer> mTimeIndexs = new ArrayList<>();

    List<Msg> datas = new ArrayList<>();
    List<Bean.Msg> dataList = new ArrayList<>();
    List<Bean.Menu> menuList = new ArrayList<>();
    private SuperRecyclerAdapter<Bean.Msg> mAdapter;
    private SuperRecyclerAdapter<Bean.Menu> mMenuAdapter;
    private Context mContext;
    private String TAG = "sendy";
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_id");
        mMessageListener = this;

        initView();
        initData();
        initConversation();
    }

    private void initData() {
        mRealm = AppContext.getRealm();

        for (int i = 0; i < 3; i++) {

            menuList.add(new Bean.Menu("系统提示" + i, ImgResUtils.ImageUrl.imageUrls().get(i)));
        }
//        datas = MsgDao.getInstance().getMsg();
        mAdapter = new SuperRecyclerAdapter<Bean.Msg>(mContext, dataList) {
            @Override
            public void convert(final SuperRecyclerHolder holder, Bean.Msg msg, int layoutType, int position) {

//                datas.add(position, new Msg(position+"", msg.getMsg(), msg.getName(), msg.getImgUrl(), msg.getTime(), msg.getMsg(),msg.getType()));
                TextView tvTime = (TextView) holder.getViewById(R.id.tv_time);
                LinearLayout llSendMsg = (LinearLayout) holder.getViewById(R.id.ll_send_msg);
                LinearLayout llReceiveMsg = (LinearLayout) holder.getViewById(R.id.ll_receive_msg);
                LinearLayout llStateMsg = (LinearLayout) holder.getViewById(R.id.ll_state_msg);
                ImageView ivSendMsg = (ImageView) holder.getViewById(R.id.img_send);
                ImageView ivReceiveMsg = (ImageView) holder.getViewById(R.id.img_receive);
                TextView tvSysMsg = (TextView) holder.getViewById(R.id.tv_sys_msg);

//                Tools.setGone(llSendMsg);
//                Tools.setGone(llReceiveMsg);
//                Tools.setGone(llStateMsg);
//                Tools.setGone(tvTime);
//                Tools.setGone(tvSysMsg);
                System.out.println((DateUtils.getOtherMinute(lastTime, msg.getTime()) >= 10) + "====time======" + DateUtils.getOtherMinute(lastTime, msg.getTime()));
                if (DateUtils.getOtherMinute(lastTime, msg.getTime()) > 10 || (mTimeIndexs.size() > position ? mTimeIndexs.get(position) == position : false)) {//判断与上条消息间隔超过10分钟显示
                    mTimeIndexs.add(position, position);
                    Tools.setVisible(tvTime);
                    holder.setIsRecyclable(false);
                    tvTime.setText(Tools.getString(R.string.time, DateUtils.getDateString(msg.getTime()), ""));
                }
                lastTime = msg.getTime();
                System.out.println(position + "========msg=" + msg.toString());
                if (msg.getType() == Bean.Msg.MsgType.SYSTEM_MSG) {
                    Tools.setVisible(tvSysMsg);
                    tvSysMsg.setText(Tools.getString(R.string.sys_msg, DateUtils.getDateString(msg.getTime()), msg.getMsg()));
                } else if (msg.getType() == Bean.Msg.MsgType.SEND_MSG || msg.getType() == Bean.Msg.MsgType.RECEIVE_MSG) {

                    Tools.setGone(msg.getType() == Bean.Msg.MsgType.SEND_MSG ? llReceiveMsg : llSendMsg);
                    Tools.setVisible(msg.getType() == Bean.Msg.MsgType.SEND_MSG ? llSendMsg : llReceiveMsg);
                    holder.setText(msg.getType() == Bean.Msg.MsgType.SEND_MSG ? R.id.tv_send_msg : R.id.tv_receive_msg, msg.getMsg());
                    Glide.with(mContext).load(msg.getImgUrl()).apply(new RequestOptions().circleCrop().error(R.mipmap.ic_error_load_failed))
                            .into(msg.getType() == Bean.Msg.MsgType.SEND_MSG ? ivSendMsg : ivReceiveMsg);
                } else if (msg.getType() == Bean.Msg.MsgType.NOMAL_MSG) {
//                    Tools.toast("最后一条聊天记录："+msg.getMsg());
                } else if (msg.getType() == Bean.Msg.MsgType.TRADE_STATE_MSG) {
//                    Tools.toast("订单状态："+msg.getMsg());
                    Tools.setVisible(llStateMsg);
                }
            }

            @Override
            public int getLayoutAsViewType(Bean.Msg msg, int position) {
                return R.layout.layout_msg_item;
            }
        };
        rclData.setLayoutManager(Tools.setManager1(mContext, LinearLayoutManager.VERTICAL));
        rclData.setAdapter(mAdapter);

        mMenuAdapter = new SuperRecyclerAdapter<Bean.Menu>(mContext, menuList) {

            @Override
            public void convert(SuperRecyclerHolder holder, Bean.Menu menu, int layoutType, int position) {
                Glide.with(mContext).load(menu.getImgUrl()).into((ImageView) holder.getViewById(R.id.iv_img));
                holder.setText(R.id.tv_name, menu.getName());

                holder.setOnItemClickListenner(v -> {
                    switch (position) {
                        case 0:
                            sendMsg(Bean.Msg.MsgType.SYSTEM_MSG, "sendy 已经放款！ 请您查收。。。");
                            break;
                        case 1:
                            sendMsg(Bean.Msg.MsgType.TRADE_STATE_MSG, "sendy 您的订单已付款！ 请您放币。。。");
                            break;
                    }
                });
            }

            @Override
            public int getLayoutAsViewType(Bean.Menu menu, int position) {
                return R.layout.layout_common_menu;
            }
        };
        rclMenu.setLayoutManager(Tools.setManager3(3, LinearLayoutManager.VERTICAL));
        rclMenu.setAdapter(mMenuAdapter);
    }

    private void sendMsg(Bean.Msg.MsgType type, String msg) {

        // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
        EMMessage message = EMMessage.createTxtSendMessage(msg, mChatId);
        mAdapter.setObject(new Bean.Msg(type, message));

        // 调用发送消息的方法
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mInputEdit = (EditText) findViewById(R.id.ec_edit_message_input);
        mMoreMenu = (CheckBox) findViewById(R.id.chb_more_menu);
        mContentText = (TextView) findViewById(R.id.ec_text_content);
        // 设置textview可滚动，需配合xml布局设置
        mContentText.setMovementMethod(new ScrollingMovementMethod());

        rclData = findViewById(R.id.rcl_data);
        rclMenu = findViewById(R.id.rcl_menu);
        mMoreMenu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Tools.setVisible(findViewById(R.id.ll_menu));
            else
                Tools.setGone(findViewById(R.id.ll_menu));
        });

        mInputEdit.setOnClickListener(v -> {
            /** 使其列表滚动到最底部  */
            rclData.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        });
        // 设置发送按钮的点击事件
        mInputEdit.setOnEditorActionListener((v, actionId, event) -> {

            String content = mInputEdit.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                mInputEdit.setText("");
                // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
                EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);
                mAdapter.setObject(new Bean.Msg(Bean.Msg.MsgType.SEND_MSG, ImgResUtils.ImageUrl.imageList().get(1), message));
                // 将新的消息内容和时间加入到下边
                mContentText.setText(mContentText.getText()
                        + "\n发送："
                        + content
                        + " - time: "
                        + DateUtils.getDateString(message.getMsgTime()));
                // 调用发送消息的方法
                EMClient.getInstance().chatManager().sendMessage(message);
                // 为消息设置回调
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // 消息发送成功，打印下日志，正常操作应该去刷新ui
                        Log.i("lzan13", "send message on success");
                    }

                    @Override
                    public void onError(int i, String s) {
                        // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                        Log.i("lzan13", "send message on error " + i + " - " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                    }
                });
            }
            return false;
        });
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatId, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            EMMessage messge = mConversation.getLastMessage();
            EMTextMessageBody body = (EMTextMessageBody) messge.getBody();

//            mAdapter.setObject(new Bean.Msg(Bean.Msg.MsgType.NOMAL_MSG, ImgResUtils.ImageUrl.imageList().get(0), messge));
            // 将消息内容和时间显示出来
            mContentText.setText(
                    "聊天记录：" + mChatId + "\t\t" + body.getMessage() + " - time: " + DateUtils.getDateString(mConversation.getLastMessage()
                            .getMsgTime()));
        }
    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();

                    mAdapter.setObject(new Bean.Msg(Bean.Msg.MsgType.RECEIVE_MSG, ImgResUtils.ImageUrl.imageList().get(2), message));
                    // 将新的消息内容和时间加入到下边
                    mContentText.setText(mContentText.getText()
                            + "\n接收：" + message.getFrom()
                            + body.getMessage()
                            + " - time: "
                            + DateUtils.getDateString(message.getMsgTime()));
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
    /**
     * --------------------------------- Message Listener -------------------------------------
     * 环信消息监听主要方法
     */
    /**
     * 收到新消息
     *
     * @param list 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 循环遍历当前收到的消息
        for (EMMessage message : list) {
            Log.i("lzan13", "收到新消息:" + message);
            if (message.getFrom().equals(mChatId)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());

                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // TODO 如果消息不是当前会话的消息发送通知栏通知
            }
        }
    }

    /**
     * 收到新的 CMD 消息
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.i("lzan13", "收到 CMD 透传消息" + body.action());
        }
    }

    /**
     * 收到新的已读回执
     *
     * @param list 收到消息已读回执
     */
    @Override
    public void onMessageRead(List<EMMessage> list) {
    }

    /**
     * 收到新的发送回执
     * TODO 无效 暂时有bug
     *
     * @param list 收到发送回执的消息集合
     */
    @Override
    public void onMessageDelivered(List<EMMessage> list) {
    }

    /**
     * 消息撤回回调
     *
     * @param list 撤回的消息列表
     */
    @Override
    public void onMessageRecalled(List<EMMessage> list) {
    }

    /**
     * 消息的状态改变
     *
     * @param message 发生改变的消息
     * @param object  包含改变的消息
     */
    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }
}
