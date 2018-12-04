package of.media.bq.bean;

import android.graphics.Bitmap;

/**
 * Created by MR.XIE on 2018/10/25.
 */
public class Gallery {
    private Bitmap thumbnail;//缩略图
    private  String galleryAddress;//图片的路径（测试用的是资源文件)

    public Gallery() {
    }

    public Gallery(String galleryAddress) {
        this.galleryAddress = galleryAddress;
    }

    public Gallery(Bitmap thumbnail, String galleryAddress) {
        this.thumbnail = thumbnail;
        this.galleryAddress = galleryAddress;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getGalleryAddress() {
        return galleryAddress;
    }

    public void setGalleryAddress(String galleryAddress) {
        this.galleryAddress = galleryAddress;
    }
}
