package ssthouse.com.simplereader.bean.event;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class AddNewBookEvent {

    public String filePath;

    public AddNewBookEvent(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
