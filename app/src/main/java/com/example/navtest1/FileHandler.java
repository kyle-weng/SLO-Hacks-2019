package com.example.navtest1;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FileHandler {

    private Context testContext;
    private FileHandler handler = new FileHandler();
    private PointOfInterest poi;
    private StorageReference mStorageRef;
    private Uri identifier;

    public FileHandler() {
        handler = new FileHandler();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://slo-hacks-2019-230507.appspot.com/");
        identifier = null;
        poi = null;
    }

    public void setIdentifier(String filepath) {
        identifier = Uri.fromFile(new File(filepath));
    }

    /*
     * write to file! i think
     */
    public void serializePOI(PointOfInterest in) {
        File directory = testContext.getFilesDir();
        FileOutputStream fOut = null;
        ObjectOutputStream oos = null;

        try {
            fOut = new FileOutputStream(directory);
            oos = new ObjectOutputStream(fOut);
            oos.writeObject(in);

            System.out.println("it done!");
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
