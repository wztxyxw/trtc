package com.chuangdu.library.widget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.chuangdu.library.R;

import java.util.ArrayList;

public class MenuDialog extends DialogFragment {
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";
    private final static String EXTRA_MENUS = "menus";
    private final static String CHOSE = "chose";
    private final static String EXTRA_TITLE = "title";

    private Listener mListener;

    public interface Listener {

        void onMenuItemSelected(String item);
        void onMenuItemSelected(int position);
    }

    public static MenuDialog newInstance(ArrayList<String> menus, String chose, Listener listener) {
        MenuDialog f = new MenuDialog();
        Bundle b = new Bundle();
        b.putStringArrayList(EXTRA_MENUS, menus);
        b.putString(CHOSE, chose);
        f.setArguments(b);
        f.mListener = listener;
        return f;
    }
    public static MenuDialog newInstance(String title, ArrayList<String> menus, String chose, Listener listener) {
        MenuDialog f = new MenuDialog();
        Bundle b = new Bundle();
        b.putStringArrayList(EXTRA_MENUS, menus);
        b.putString(EXTRA_TITLE, title);
        b.putString(CHOSE, chose);
        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showBottom true 在底部显示 false 在中间显示
     */
    public MenuDialog setShowBottom(boolean showBottom) {
        getArguments().putBoolean(EXTRA_SHOW_BOTTOM, showBottom);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean showBottom = getArguments().getBoolean(EXTRA_SHOW_BOTTOM, false);
        Window window = getDialog().getWindow();
        if (showBottom) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.5f;
            window.setGravity(Gravity.BOTTOM);
            window.setAttributes(layoutParams);
        }
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final ArrayList<String> menus = getArguments().getStringArrayList(EXTRA_MENUS);
        assert menus != null;
        View view = inflater.inflate(R.layout.dialog_menu_layout,
                ((ViewGroup) window.findViewById(android.R.id.content)), false);
        if (showBottom) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }


        TextView tvTitle = view.findViewById(R.id.title);

        ListView list = (ListView) view.findViewById(R.id.dialog_menu_list);

        String title = getArguments().getString(EXTRA_TITLE);

        if (TextUtils.isEmpty(title)) {
            view.findViewById(R.id.title_layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        String chose = getArguments().getString(CHOSE);



        MyAdapter myAdapter = new MyAdapter(menus);
        myAdapter.setShowBottom(showBottom);
        myAdapter.setChose(chose);
        list.setAdapter(myAdapter);
        return view;
    }

    private class MyAdapter extends BaseAdapter {

        final ArrayList<String> menus;
        String chose = null;
        boolean showBottom = false;
        public MyAdapter(ArrayList<String> menus) {
            this.menus = menus;
        }

        public void setChose(String c) {
            chose = c;
        }

        public void setShowBottom(boolean showBottom) {
            this.showBottom = showBottom;
        }

        @Override
        public int getCount() {
            return (menus == null) ? 0 : menus.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            if (menus != null) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    convertView = inflater.inflate(R.layout.dialog_menu_item_layout, null);
                }
                TextView title = (TextView) convertView.findViewById(R.id.dialog_menu_list_txt);
                if (showBottom) {
                    title.setBackgroundResource(R.drawable.listview_bg_middle);
                } else {
                    if (getCount() == 1) {
                        title.setBackgroundResource(R.drawable.listview_bg);
                    } else if (position == 0) {
                        title.setBackgroundResource(R.drawable.listview_bg_top);
                    } else if (position == getCount() - 1) {
                        title.setBackgroundResource(R.drawable.listview_bg_bottom);
                    } else {
                        title.setBackgroundResource(R.drawable.listview_bg_middle);
                    }
                }
                title.setText(menus.get(position));

                if (position == getCount() - 1) {
                    if (showBottom) {
                        title.setTextColor(getResources().getColor(R.color.cancel_color));
                    }
                } else if (chose != null && chose.equals(menus.get(position))) {
                    title.setTextColor(getResources().getColor(R.color.important_operation_color));
                } else {
                    title.setTextColor(getResources().getColor(R.color.text_color_property));
                }
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MenuDialog.this.dismiss();
                        mListener.onMenuItemSelected(menus.get(position));
                        mListener.onMenuItemSelected(position);
                    }
                });
            }
            view = convertView;
            return view;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            if (menus != null && position < menus.size()) {
                return menus.get(position);
            }
            return null;
        }
    }

}
