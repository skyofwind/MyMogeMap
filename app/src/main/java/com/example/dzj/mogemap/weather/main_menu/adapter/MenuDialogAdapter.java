package com.example.dzj.mogemap.weather.main_menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dzj.mogemap.R;
import com.example.dzj.mogemap.weather.main_menu.bean.MenuData;

import java.util.List;


/**
 * 要显示的adapter
 * Created by LaiYingtang on 2016/5/24.
 */
public class MenuDialogAdapter extends BaseAdapter {
    private Context mContext;//上下文
    private List<MenuData> menuDatas;//data组，里面包括name id flag
    private int selectedPos = -1;//
    private int mSelectedBackgroundResource;//选中item时的背景颜色
    private int mNormalBackgroundResource;//为选中的背景颜色
    private boolean hasDivider = true;

    public void setSelectedBackgroundResource(int mSelectedBackgroundResource) {//设置选中背景颜色
        this.mSelectedBackgroundResource = mSelectedBackgroundResource;
    }

    public void setNormalBackgroundResource(int mNormalBackgroundResource) {//设置正常背景颜色
        this.mNormalBackgroundResource = mNormalBackgroundResource;
    }

    public void setHasDivider(boolean hasDivider) {
        this.hasDivider = hasDivider;
    }

    public MenuDialogAdapter(Context mContext, List<MenuData> menuDatas) {
        this.mContext = mContext;
        this.menuDatas = menuDatas;
    }

    //选中的position,及时更新数据
    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        notifyDataSetChanged();
    }

    public void setData(List<MenuData> data) {
        this.menuDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (menuDatas == null) {
            return 0;
        }
        return menuDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if (menuDatas == null) {
            return null;
        }
        return menuDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_menu_item, null);
        }
        LinearLayout itemLayout = ViewHolder.get(convertView, R.id.menu_item_ly);
        TextView nameText = ViewHolder.get(convertView, R.id.menu_item_textview);
        TextView dividerTextView = ViewHolder.get(convertView, R.id.menu_item_divider);

        final MenuData menuData = menuDatas.get(position);
        nameText.setText(menuData.name);//设置标题

        convertView.setSelected(selectedPos == position);//设置选中时的view
        nameText.setSelected(selectedPos == position);

        //选中后的标题字体颜色
        nameText.setTextColor(selectedPos == position ? 0xFF00B4C9 : 0xFF333333);
        //选中与未选中的背景色
        if (mNormalBackgroundResource == 0)
            mNormalBackgroundResource = R.color.white;

        if (mSelectedBackgroundResource != 0)
            itemLayout.setBackgroundResource(selectedPos == position ? mSelectedBackgroundResource : mNormalBackgroundResource);

        //隐藏view
        dividerTextView.setVisibility(hasDivider ? View.VISIBLE : View.INVISIBLE);
        return convertView;
    }
}
