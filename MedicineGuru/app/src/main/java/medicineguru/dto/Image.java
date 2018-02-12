package medicineguru.dto;

import java.util.UUID;

/**
 * Created by Brinder Saini on 10/02/2018.
 */

public class Image {

    public String path;

    public Image() {
    }

    public Image(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
