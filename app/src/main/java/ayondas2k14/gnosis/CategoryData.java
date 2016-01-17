package ayondas2k14.gnosis;

//Class to store title,description and image resource ID for each category
public class CategoryData {
    private String title;
    private String description;
    private int imgResId;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }
}
