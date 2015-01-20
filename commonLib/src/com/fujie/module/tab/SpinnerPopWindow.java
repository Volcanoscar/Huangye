package com.fujie.module.tab;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.fujie.module.horizontalListView.ViewBean;
import com.fujie.module.titlebar.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerPopWindow extends PopupWindow implements OnItemClickListener{
    /**
     * 弹出菜单中要展示的数据
     */
    private  ViewBean viewBean = new ViewBean();

    //接口IOnItemClickListenr
    private IOnItemSelectListener iOnItemSelectListener;
	private Context context ;
    private String tag;
    private View contentView;
    /**
     * 第一菜单索引
     */
    private int oneIndex;

    /**
     *
     * @param context
     */
	public SpinnerPopWindow(Context context) {
		super(context);
		this.context = context;
		init();
	}

    /**
     * @param context
     * @param viewBean   弹出菜单中要展示的数据
     */
    public SpinnerPopWindow(Context context, ViewBean viewBean) {
        super(context);
        this.context= context;
        this.viewBean = viewBean;
        init();

    }

	public void setItemSelectListener(IOnItemSelectListener iOnItemSelectListener)
	{
		this.iOnItemSelectListener = iOnItemSelectListener;
	}
	 
	public void init()
	{
        // -------初始化PopWindow -------------------
        contentView = LayoutInflater.from(context).inflate(R.layout.level_two_menu,null);
		setContentView(contentView);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		//设置下拉菜单背景颜色
		setBackgroundDrawable(context.getResources().getDrawable(android.R.color.white));
        setOutsideTouchable(false);
        // -------初始化PopWindow结束 -------------------
	}

    public  void initData( ViewBean tabViewBean) {
        this.viewBean = tabViewBean;
        final List<ViewBean> levelOneList = tabViewBean.getBizAreaList();
        final ListView level_two_list = (ListView) contentView.findViewById(R.id.level_two_list);
        final ListView level_one_list = (ListView) contentView.findViewById(R.id.level_one_list);
        LinearLayout districtLayout = (LinearLayout) contentView.findViewById(R.id.districtLayout);
        final SearchMainAdapter districtAdapter = new SearchMainAdapter(context,tabViewBean.getBizAreaList(), R.layout.shop_list1_item, false);
        level_one_list.setAdapter(districtAdapter);

        if (tabViewBean.isParent()) { //如果有二级菜单添加事件
            level_one_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    initDistrictAdapter(level_two_list,levelOneList.get(i).getBizAreaList());
                    oneIndex= i;
                    districtAdapter.setSelectItem(i);
                    districtAdapter.notifyDataSetChanged();
                }
            });
            level_two_list.setOnItemClickListener(this);
            initDistrictAdapter(level_two_list,levelOneList.get(0).getBizAreaList());
        } else {//如果是一级菜单点击事件
            level_one_list.setOnItemClickListener(this);
            level_two_list.setVisibility(View.GONE);
        }
        update();

    }

    private void initDistrictAdapter(ListView listView,List<ViewBean> viewBeans) {
        SearchMoreAdapter  bizAreaAdapter = new SearchMoreAdapter(context, viewBeans, R.layout.shop_list2_item);
        listView.setAdapter(bizAreaAdapter);
        bizAreaAdapter.setViewBeanList(viewBeans);
        bizAreaAdapter.notifyDataSetChanged();
    }
   
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	  dismiss();
	  if (iOnItemSelectListener!=null) {
          if (parent.getId() == R.id.level_two_list) {
              iOnItemSelectListener.onItemClick(tag,viewBean.getBizAreaList().get(oneIndex).getBizAreaList().get(position), position);

          }else{
              iOnItemSelectListener.onItemClick(tag, viewBean.getBizAreaList().get(position), position);
          }
      }
    }



    public void showAsDropDown(View anchor,String tag) {
        super.showAsDropDown(anchor);
        this.tag= tag;
    }


    /**
     * 弹出菜单项点击事件
     */
    public static interface IOnItemSelectListener
    {
        /**
         *
         * @param tag 此变量标记弹出菜单是由谁触发的。
         * @param viewBean 点击的对象
         * @param position
         */
        public void onItemClick(String tag,ViewBean viewBean,int position);
    }
}
