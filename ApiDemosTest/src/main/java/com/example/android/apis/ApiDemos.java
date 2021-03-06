package com.example.android.apis;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 6/4/13.
 */
public class ApiDemos extends ListActivity {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);//父类的方法

        Intent intent = getIntent();
        String path = intent.getStringExtra("com.example.android.apis.Path");
        if (path == null) {//如果没有传递的参数,那么path路径为空字符串
            path = "";
        }
        //为ListActivity设置适配器
        setListAdapter(new SimpleAdapter(this, getData(path),
                android.R.layout.simple_list_item_1, new String[]{"title"},
                new int[]{android.R.id.text1}));
        getListView().setTextFilterEnabled(true);//这个功能需要外置的键盘,可以通过直接输入字母来过滤item(只会显示符合的item)
    }

    /**
     * @param prefix 这个是什么呢?一个字符串
     * @return
     */
    protected List<Map<String, Object>> getData(String prefix) {

        List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();//Map保存每个ITEM的信息,这里保存所有的ITEM

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);//取得PackageManager中指定的信息

        if (null == list)
            return myData;

        String[] prefixPath;//前缀路经
        String prefixWithSlash = prefix;//前缀和斜线

        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }

        int len = list.size();

        Map<String, Boolean> entries = new HashMap<String, Boolean>();

        /*通过取得我们定义的指定的格式查找出所有对应的Activity*/
        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);//取得一个ResolveInfo
            CharSequence labelSeq = info.loadLabel(pm);//取得android:name对应的名称,去掉package标签部分
            String label = labelSeq != null//    {a/b ,a/c}   { b/a ,b/c}
                    ? labelSeq.toString()
                    : info.activityInfo.name;//如果label为空,则取得完整的activity名称

            //经过测试上面两句可以变成这样:
            //String label = info.ladLabel(pm).toString();因为info.ladbel(pm)默认就是如果为null,就返回当前所在activity的名字.
            //如果是第一次进,或者label是以Prefix+"/"开始
            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {
                String[] labelPath = label.split("/");
                //如果不是第一层,那就是label分割后第一层,如果
                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {

                    addItem(myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name
                    ));
                } else {
                    if (entries.get(nextLabel) == null) {//这是map是用来保证唯一,当label的某一字段相同时
                        addItem(myData, nextLabel,
                                browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }

        Collections.sort(myData, sDisplayNameComparator);//按指定comparator进行升序排序

        return myData;
    }

    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();//准备一个HashMap存放ITEM的字段信息
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    /**
     * @param path
     * @return
     */
    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, ApiDemos.class);
        result.putExtra("com.example.android.apis.Path", path);
        return result;
    }

    private final static Comparator<Map<String, Object>> sDisplayNameComparator =
            new Comparator<Map<String, Object>>() {
                private final Collator collator = Collator.getInstance();

                @Override
                public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
                    return collator.compare(lhs.get("title"), rhs.get("title"));
                }
            };

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg,componentName);
        return result;
    }

    @Override
    protected void onListItemClick(ListView l,View v,int position, long id){
        Map<String,Object> map = (Map<String,Object>)l.getItemAtPosition(position);

        Intent intent = (Intent)map.get("intent");
        startActivity(intent);
    }

}
