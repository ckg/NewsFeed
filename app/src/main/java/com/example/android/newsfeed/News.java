package com.example.android.newsfeed;

/**
 * Created by Christos on 20-Jun-17.
 */

public class News {
    /** Title of the News */
    private String mTitle;

    /** Published Date of the News */
    private String mPublishedDate;

    /** Section of the News */
    private String mSection;

    /** News Link */
    private String mUrl;





    /**
     * Constructs a new {@link News} object.
     *
     * @param title is the title of the News
     * @param publishedDate is the published date of the News
     * @param section is the section of the News
     * @param url is the web link about the News
     */
    public News(String title, String publishedDate, String section, String url) {
        mTitle = title;
        mPublishedDate = publishedDate;
        mSection= section;
        mUrl = url;
    }

    /**
     * Returns the title of the News.
     */
    public String getTitle() {
        return mTitle;
    }

     /**
     * Returns the published date of the News.
     */
    public String getPublishedDate() {
        return mPublishedDate;
    }

    /**
     * Returns the section of the News.
     */
    public String getSection() {
        return mSection;
    }


    /**
     * Returns the article link of the News.
     */
    public String getUrl() {
        return mUrl;
    }

}
