package com.example.navtest1;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class FileHandler {

    private Context testContext;
    //private FileHandler handler;
    private StorageReference mStorageRef;
    private Uri identifier;
    private ObjectOutput out;

    //private PointOfInterest poi;


    public FileHandler() {
        //handler = new FileHandler();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://slo-hacks-2019-60f66.appspot.com");
        identifier = null;
        //poi = null;
        out = null;
    }

    public void setIdentifier(String filepath) {
        identifier = Uri.fromFile(new File(filepath));
    }

    public byte[] convertToByteArray(PointOfInterest in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //turn this thing into a byte array
        byte[] fileContents = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(in);
            out.flush();
            fileContents = bos.toByteArray();
        } catch (Exception e) {
            System.out.println("That didn't work :(");
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                //Ignore close exception
            }
        }
        return fileContents;
    }

    /*
     * write to file! i think
     */
    public void serializePOI(PointOfInterest in) {
        byte[] fileContents = convertToByteArray(in);

        String filename = in.getName();
        FileOutputStream outputStream;

        try {
            outputStream = testContext.openFileOutput(filename + ".xf", Context.MODE_PRIVATE);
            outputStream.write(fileContents);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * upload file to cloud! i think
     * https://firebase.google.com/docs/storage/android/upload-files
     */
    public void upload(PointOfInterest in) {
        StorageReference ref = mStorageRef.child(in.getName() + ".xf");
        byte[] fileContents = convertToByteArray(in);

        UploadTask task = ref.putBytes(fileContents);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Upload failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            }
        });
    }

}
