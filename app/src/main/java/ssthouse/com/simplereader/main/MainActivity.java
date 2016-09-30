package ssthouse.com.simplereader.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.utils.PreferUtil;

public class MainActivity extends BaseActivity implements IMainView {

    @Bind(R.id.id_tb)
    Toolbar toolbar;

    @Bind(R.id.id_fab_add_book)
    FloatingActionButton fabAddBook;

    @Bind(R.id.id_lv_books)
    ListView lvBooks;

    private List<BookBean> bookList = new ArrayList<>();

    private MainPresenter mPresenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("SimpleReader");

        //初始话presenter
        mPresenter = new MainPresenter(this, new MainModel());

        lvBooks.setAdapter(lvAdapter);

        //TODO  第一次进入逻辑有待商榷
        if (PreferUtil.getInstance().isFistIn(this)) {
            //模拟点击添加书籍按钮事件
            fabAddBook.performClick();
            PreferUtil.getInstance().setIsFistIn(this, false);
        }
    }

    private BaseAdapter lvAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return bookList.size();
        }

        @Override
        public Object getItem(int position) {
            return bookList.get(position);
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
            } else {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.item_book, parent, false);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.id_tv_book_name);
                viewHolder.tvBrief = (TextView) convertView.findViewById(R.id.id_tv_brief);
                convertView.setTag(viewHolder);
            }
            viewHolder.tvName.setText(bookList.get(position).getBookName());
            viewHolder.tvBrief.setText(bookList.get(position).getBrief());
            return convertView;
        }
    };

    @Override
    public void reloadBooks(List<BookBean> bookList) {
        this.bookList = bookList;
        lvAdapter.notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvName;
        TextView tvBrief;
    }

    @OnClick(R.id.id_fab_add_book)
    public void onAddBookClicked() {
        //TODO 模拟添加书籍
        //BookListActivity.start(this);
        mPresenter.reloadBookList();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }
}
