package ssthouse.com.simplereader.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.base.BaseFragment;
import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.event.ChangeToArticleEvent;
import ssthouse.com.simplereader.bean.event.UpdateBookListEvent;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class BookListFragment extends BaseFragment {

    @Bind(R.id.id_lv_books)
    ListView mListViw;

    private List<BookBean> mBookList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.fragment_book_list;
    }

    @Override
    public void init() {
        mListViw.setAdapter(mAdapter);
        mListViw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new ChangeToArticleEvent(mBookList.get(position)));
            }
        });
        //刷新书库列表
        EventBus.getDefault().post(new UpdateBookListEvent());
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mBookList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBookList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.item_book, parent, false);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.id_tv_book_name);
                viewHolder.tvBrief = (TextView) convertView.findViewById(R.id.id_tv_brief);
                convertView.setTag(viewHolder);
            }
            viewHolder.tvName.setText(mBookList.get(position).bookName);
            viewHolder.tvBrief.setText(mBookList.get(position).brief);
            return convertView;
        }
    };

    class ViewHolder {
        TextView tvName;
        TextView tvBrief;
    }

    /**
     * UI操作
     */
    public void loadBookList(List<BookBean> bookList) {
        if (bookList == null || bookList.size() == 0) {
            return;
        }
        mBookList = bookList;
        mAdapter.notifyDataSetChanged();
    }
}
