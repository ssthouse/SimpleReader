package ssthouse.com.simplereader.main;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import butterknife.OnClick;
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.base.BaseFragment;
import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.event.BookBeanChangedEvent;
import ssthouse.com.simplereader.bean.event.ChangeToArticleEvent;
import ssthouse.com.simplereader.bean.event.LoadApkBookBeanEvent;
import timber.log.Timber;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class BookListFragment extends BaseFragment {

    @Bind(R.id.id_lv_books)
    ListView mListViw;

    @Bind(R.id.id_fab_add_book)
    FloatingActionButton mFabAddBook;

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
        EventBus.getDefault().post(new BookBeanChangedEvent());
    }

    @OnClick(R.id.id_fab_add_book)
    public void onClickAddBook() {
        //TODO 逻辑还需要思考
//        AddBookActivity.start(getActivity());

        //先尝试加载raw中的文件

        //显示加载内置书籍 dialog
        EventBus.getDefault().post(new LoadApkBookBeanEvent());

        //直接启动文件管理器  获取路径
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();
            String filePath = getRealFilePath(uri);
            Timber.e("文件路径:\t" + filePath);
        }
    }

    /**
     * TODO bug:只有通过系统文件浏览器才能拿到文件??
     *
     * @param uri
     * @return
     */
    public String getRealFilePath(Uri uri) {
        if (null == uri)
            return null;
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
            Timber.e("get path from scheme");
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
            Timber.e("get path from uti");
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Timber.e("get path from content resolver");
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
            Timber.e("cursor is null");
        }
        return data;
    }
}
