package com.chuangdu.library.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.chuangdu.library.R;
import com.chuangdu.library.util.CharacterUtil;
import com.chuangdu.library.util.KeyBoardUtils;


public class EditNameDialog extends DialogFragment {

    private final static String EXTRA_HINT = "hint";
    private final static String EXTRA_TITLE = "title";
    private final static String INPUT_TYPE = "inputtype";
    private final static String EXTRA_MESSAGE = "message";
    private final static String EXTRA_POSITIVE_LABEL = "positive_label";
    private final static String EXTRA_NEGATIVE_LABEL = "negative_label";
    private Listener mListener;
    private InputFilter mInputFilter;
    private int minputType = -1;
    private int mMaxLength = -1; // 可输入最大长度
    private String mHint; // 输入长度过长时，提示语
    private EditText mMsgEt;

    //输入表情前的光标位置
    private int mCursorPos;
    //输入表情前EditText中的文本
    private String mInputAfterText;
    //是否重置了EditText的内容
    private boolean mResetText;
    //表情
    private boolean mEmojiallow;
    //是否可以空
    private boolean mEmpty = false;
    public interface Listener {
        void onDone(String result);
        void onCancel();
    }

    public static EditNameDialog newInstance(Context context,
                                             Listener listener) {
        EditNameDialog f = new EditNameDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, context.getString(R.string.input_text));
        b.putString(EXTRA_POSITIVE_LABEL, context.getString(android.R.string.yes));
        b.putString(EXTRA_NEGATIVE_LABEL, context.getString(R.string.cancel));

        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    public static EditNameDialog newInstance(Context context, String title, String hint,
                                             String yes, String no,
                                             Listener listener) {
        EditNameDialog f = new EditNameDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        b.putString(EXTRA_HINT, hint);
        b.putString(EXTRA_POSITIVE_LABEL, yes);
        b.putString(EXTRA_NEGATIVE_LABEL, no);

        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    public static EditNameDialog newInstance(Context context, String title, String hint,
                                             String msg,
                                             String yes, String no,
                                             Listener listener) {
        EditNameDialog f = new EditNameDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        b.putString(EXTRA_HINT, hint);
        b.putString(EXTRA_MESSAGE, msg);
        b.putString(EXTRA_POSITIVE_LABEL, yes);
        b.putString(EXTRA_NEGATIVE_LABEL, no);

        f.setArguments(b);
        f.mListener = listener;
        return f;
    }
    public static EditNameDialog newInstance(Context context, String title, int inputtype, String hint,
                                             String msg,
                                             String yes, String no,
                                             Listener listener) {
        EditNameDialog f = new EditNameDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        b.putString(EXTRA_HINT, hint);
        b.putString(EXTRA_MESSAGE, msg);
        b.putString(EXTRA_POSITIVE_LABEL, yes);
        b.putString(EXTRA_NEGATIVE_LABEL, no);
        b.putInt(INPUT_TYPE, inputtype);

        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    public void setEditNameInputFilter(InputFilter inputFilter){
        mInputFilter = inputFilter;
    }

    public void setMaxInputLength(int max) {
        mMaxLength = max;
    }

    public void setInputTooMuchHint(String hint) {
        mHint = hint;
    }


    public void setMsg(String msg){
        if(mMsgEt != null) {
            mMsgEt.setText(msg);
        }
    }

    public void setInputType(int inputType) {
        minputType = inputType;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_name_layout, null);
        if (v == null) {
            return null;
        }
        final TextView promptTv = (TextView)v.findViewById(R.id.prompt_edit_name);
        ImageView clearContent = (ImageView)v.findViewById(R.id.clear_content);
        clearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMsgEt != null) {
                    mMsgEt.setText("");
                }
                if (promptTv != null) {
                    promptTv.setVisibility(View.GONE);
                }
            }
        });
        TextView title = (TextView)v.findViewById(R.id.dialog_custom_title);
        title.setText(getArguments().getString(EXTRA_TITLE));
        mMsgEt = (EditText) v.findViewById(R.id.dialog_fence_details_name_edit);
        mMsgEt.setHint(getArguments().getString(EXTRA_HINT));
        mMsgEt.setInputType(getArguments().getInt(INPUT_TYPE) != 0 ? getArguments().getInt(INPUT_TYPE) : InputType.TYPE_CLASS_TEXT);
        final String msg = getArguments().getString(EXTRA_MESSAGE);
        if (!TextUtils.isEmpty(msg)) {
            mMsgEt.setText(msg);
            assert msg != null;
            mMsgEt.setSelection(msg.length());
        }
        if (mInputFilter != null) {
            mMsgEt.setFilters(new InputFilter[]{mInputFilter});
        }
        if (mMaxLength != -1) {
            mMsgEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        }
        if (minputType!=-1) {
            mMsgEt.setInputType(minputType);
        }

        mMsgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!mEmojiallow) {
                    if (!mResetText) {
                        mCursorPos = mMsgEt.getSelectionEnd();
                        // 这里用s.toString()而不直接用s是因为如果用s，
                        // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                        // inputAfterText也就改变了，那么表情过滤就失败了
                        mInputAfterText = s.toString();
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mEmojiallow) {
                    if (!mResetText) {
                        if (count >= 2) {//表情符号的字符长度最小为2
                            if (mCursorPos + count > s.length()) {
                                return;
                            }
                            CharSequence input = s.subSequence(mCursorPos, mCursorPos + count);
                            if (CharacterUtil.containsEmoji(input.toString())) {
                                mResetText = true;
                                //Toast.makeText(mContext, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                mMsgEt.setText(mInputAfterText);
                                CharSequence text = mMsgEt.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            }
                        }
                    } else {
                        mResetText = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(mHint)) {
                    if (content.length() >= mMaxLength) {
                        promptTv.setText(mHint);
                        promptTv.setVisibility(View.VISIBLE);
                    } else {
                        promptTv.setVisibility(View.GONE);
                    }
                }
            }
        });

        TextView no = (TextView)v.findViewById(R.id.dialog_custom_no);
        TextView mYesTextView = (TextView) v.findViewById(R.id.dialog_custom_yes);
        no.setText(getArguments().getString(EXTRA_NEGATIVE_LABEL));
        mYesTextView.setText(getArguments().getString(EXTRA_POSITIVE_LABEL));

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                KeyBoardUtils.closeKeybord(mMsgEt, getActivity());
                EditNameDialog.this.dismiss();
            }
        });

        mYesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEmpty) {
                    if (TextUtils.isEmpty(mMsgEt.getText().toString())) {
                        promptTv.setText(getString(R.string.msg_not_null));
                        promptTv.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                if (mMsgEt.getText().toString().equals(msg)) {
                    EditNameDialog.this.dismiss();
                    return;
                }
                mListener.onDone(mMsgEt.getText().toString());
                KeyBoardUtils.closeKeybord(mMsgEt, getActivity());
                EditNameDialog.this.dismiss();
            }
        });
        this.getDialog().setCanceledOnTouchOutside(false);
        setKeybord(true);
        return v;
    }


    public boolean isEmojiallow() {
        return mEmojiallow;
    }

    public void setEmojiallow(boolean mEmojiallow) {
        this.mEmojiallow = mEmojiallow;
    }

    public boolean isEmpty() {
        return mEmpty;
    }

    public void setEmpty(boolean mEmpty) {
        this.mEmpty = mEmpty;
    }

    public void setKeybord(boolean bo) {
        if (bo){
            mMsgEt.setEnabled(true);
            mMsgEt.setFocusable(true);
            mMsgEt.setFocusableInTouchMode(true);
            mMsgEt.requestFocus();
            KeyBoardUtils.openKeybord(mMsgEt, getActivity());
        } else {
            KeyBoardUtils.closeKeybord(mMsgEt, getActivity());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        /*Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_progress_width);
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_progress_height);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);*/
    }
}