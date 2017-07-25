package com.powernote.project.powernote;

import java.util.Date;

/**
 * Created by Maks on 25.07.2017.
 */

public class Note {
    private String name;
    private Date date;
    private String text;

    public Note(String text){
        this.text = text;
        this.date = new Date();
        this.name = text.substring(0, 5);
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.name = text.substring(0, 5);
    }

}
