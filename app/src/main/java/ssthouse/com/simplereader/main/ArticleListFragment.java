package ssthouse.com.simplereader.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.base.BaseFragment;
import ssthouse.com.simplereader.bean.ArticleBean;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class ArticleListFragment extends BaseFragment {

    @Bind(R.id.id_lv_articles)
    ListView mListView;

    private List<ArticleBean> mArticleBeanList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.fragment_article_list;
    }

    @Override
    public void init() {
        mListView.setAdapter(mAdapter);
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mArticleBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArticleBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            }else{
                viewHolder = new ViewHolder();
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.id_tv_article_name);
                viewHolder.tvContent  = (TextView) convertView.findViewById(R.id.id_tv_content);
                convertView.setTag(viewHolder);
            }
            viewHolder.tvName.setText(mArticleBeanList.get(position).getArticleName());
            viewHolder.tvContent.setText(mArticleBeanList.get(position).content);
            return convertView;
        }
    };

    class ViewHolder{
        TextView tvName;
        TextView tvContent;
    }

    /**
     * UI操作
     * @param articleBeanList
     */
    public void loadArticles(List<ArticleBean> articleBeanList) {
        mArticleBeanList = articleBeanList;
        mAdapter.notifyDataSetChanged();
    }
}
