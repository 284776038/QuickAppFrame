package cn.richinfo.frame.ui.widget;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.richinfo.frame.R;
import cn.richinfo.frame.util.StringUtils;


/**
 * Created by Suma on 2016/8/17.
 */
public class RationaleForPerissionDialog extends DialogFragment {

    public static final String TITLE = "title";
    public static final String MESSAGE_LEFT = "message_left";
    public static final String MESSAGE_RIGHT = "message_right";

    private  String mTitle;
    private String mStrLeft;
    private String mStrRight;
    private Button mBtnConfirm;
    private Button mBtnCancel;
    private WrapOnClickListener mConfirmOnClickListener = new WrapOnClickListener();
    private WrapOnClickListener mCancelOnClickListener = new WrapOnClickListener();

    public static RationaleForPerissionDialog getInstance(FragmentManager manager, String tag, Bundle data){
        RationaleForPerissionDialog dlg = new RationaleForPerissionDialog();
//        if ()
        dlg.setArguments(data);
        dlg.show(manager,tag);
        return dlg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mTitle = getValue(savedInstanceState,TITLE);
        mStrLeft = getValue(savedInstanceState,MESSAGE_LEFT);
        mStrRight = getValue(savedInstanceState,MESSAGE_RIGHT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View root = inflater.inflate(R.layout.dialog_rationale,container,false);
        TextView textRationale = (TextView) root.findViewById(R.id.rationale_txt);
        mBtnConfirm = (Button) root.findViewById(R.id.rationale_right_btn);
        mBtnCancel = (Button) root.findViewById(R.id.rationale_left_btn);
        if (!StringUtils.isNullOrEmpty(mTitle))
            textRationale.setText(mTitle);
        if (!StringUtils.isNullOrEmpty(mStrRight))
            mBtnConfirm.setText(mStrRight);
        if (!StringUtils.isNullOrEmpty(mStrLeft))
            mBtnCancel.setText(mStrLeft);
        mBtnConfirm.setOnClickListener(mConfirmOnClickListener);
        mBtnCancel.setOnClickListener(mCancelOnClickListener);

        mCancelOnClickListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return root;
    }

    public void setOnConfirmListener(View.OnClickListener listener){
        if(listener != null)
        mConfirmOnClickListener.setOnClickListener(listener);
    }

    public void setOnCancelListener(View.OnClickListener listener){
        if(listener != null)
            mCancelOnClickListener.setOnClickListener(listener);
    }

    private String getValue(Bundle data,String key){
        String value = null;
        if (data != null){
            value = data.getString(key);
        }
        if (StringUtils.isNullOrEmpty(value) && getArguments() != null)
            value = getArguments().getString(key);
        return value;
    }

    private static class WrapOnClickListener implements View.OnClickListener{
        private View.OnClickListener mOnClickListener;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View v) {
            if(mOnClickListener != null)
                mOnClickListener.onClick(v);
        }
    }
}
