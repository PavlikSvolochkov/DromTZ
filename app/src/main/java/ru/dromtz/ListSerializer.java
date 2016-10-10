package ru.dromtz;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ListSerializer {

    private File savedLinks;

    public ListSerializer(Context context, String fileName) throws IOException {
        savedLinks = new File(context.getFilesDir().getAbsolutePath() + fileName);
        savedLinks.createNewFile();
    }

    public void saveLinks(List<String> links) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedLinks));
            oos.writeObject(links);
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object loadLinks() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedLinks));
            Object object = ois.readObject();
            return object;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}
