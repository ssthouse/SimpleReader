package ssthouse.com.simplereader.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ssthouse on 2016/10/1.
 */
@Table(name = "Words")
public class WordBean extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "Level")
    public int level;

    public WordBean(){
        super();
    }

    public WordBean(String name, int level) {
        super();
        this.name = name;
        this.level = level;
    }
}
