package com.quyuanjin.imseven.ui.fragments.detailfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.pojo.ChatParce;
import com.quyuanjin.imseven.ui.fragments.chatfragment.FragmentChat;

import me.yokeyword.fragmentation.SupportFragment;

public class FragmentDetail extends SupportFragment {
    private static final String MSG = "arg_msg";
    private ChatParce chatParce;

    public static SupportFragment newInstance(ChatParce msg) {
        Bundle argss = new Bundle();
        argss.putParcelable(MSG, msg);
        FragmentDetail fragment = new FragmentDetail();
        fragment.setArguments(argss);
        return fragment;
    }

    private Button btnSendMsg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatParce = getArguments().getParcelable(MSG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initView(view);
        Toast.makeText(_mActivity,chatParce.yourUserId,Toast.LENGTH_SHORT).show();

        return view;
    }

    private void initView(View view) {
        btnSendMsg = view.findViewById(R.id.send_msg);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        sendClick();
    }

    private void sendClick() {
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startWithPop(FragmentChat.newInstance(chatParce));
            }
        });
    }
}
