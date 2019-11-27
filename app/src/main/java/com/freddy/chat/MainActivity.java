package com.freddy.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.freddy.chat.bean.HostBean;
import com.freddy.chat.bean.SingleMessage;
import com.freddy.chat.event.CEvent;
import com.freddy.chat.event.CEventCenter;
import com.freddy.chat.event.Events;
import com.freddy.chat.event.I_CEventListener;
import com.freddy.chat.im.IMSClientBootstrap;
import com.freddy.chat.im.MessageProcessor;
import com.freddy.chat.im.MessageType;
import com.freddy.chat.utils.CThreadPoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements I_CEventListener {

    private EditText et_content;
    private EditText et_host;
    private EditText et_port;
    private EditText et_userId;
    private EditText et_toId;
    private TextView tv_msg;

    String userId = "100001";
    String token = "token_" + userId;
    String hosts = "[{\"host\":\"10.154.139.165\", \"port\":8855}]";

    private static final String[] EVENTS = {
            Events.CHAT_SINGLE_MESSAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_content = findViewById(R.id.et_content);
        et_host = findViewById(R.id.et_host);
        et_port = findViewById(R.id.et_port);
        et_userId = findViewById(R.id.et_userId);
        et_toId = findViewById(R.id.et_toId);
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = et_userId.getText().toString().trim();
                token = "token_" + userId;
                HostBean hostBean = new HostBean(et_host.getText().toString().trim(), et_port.getText().toString().trim());
                List<HostBean> hostBeans = new ArrayList<>(1);
                hostBeans.add(hostBean);
                hosts = JSON.toJSONString(hostBeans);
                IMSClientBootstrap.getInstance().init(userId, token, hosts, 1);
                CEventCenter.registerEventListener(MainActivity.this, EVENTS);
            }
        });

        tv_msg = findViewById(R.id.tv_msg);

    }

    public void sendMsg(View view) {
        SingleMessage message = new SingleMessage();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType(MessageType.SINGLE_CHAT.getMsgType());
        message.setMsgContentType(MessageType.MessageContentType.TEXT.getMsgContentType());
        message.setFromId(userId);
        message.setToId(et_toId.getText().toString().trim());
        message.setTimestamp(System.currentTimeMillis());
        message.setContent(et_content.getText().toString());

        MessageProcessor.getInstance().sendMsg(message);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CEventCenter.unregisterEventListener(this, EVENTS);
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.CHAT_SINGLE_MESSAGE: {
                final SingleMessage message = (SingleMessage) obj;
                CThreadPoolExecutor.runOnMainThread(new Runnable() {

                    @Override
                    public void run() {
                        tv_msg.setText(message.getContent());
                    }
                });
                break;
            }

            default:
                break;
        }
    }
}
