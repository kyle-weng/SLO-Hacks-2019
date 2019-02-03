package com.example.navtest1;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public class FileHandler {
    private Context testContext;
    //private FileHandler handler;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private Uri identifier;
    private ObjectOutput out;
    private static final String URL = "gs://slo-hacks-2019-60f66.appspot.com";
    private static final String TAG = "FileHandler";

    //private PointOfInterest poi;


    public FileHandler() {
        //handler = new FileHandler();
        this.storage = FirebaseStorage.getInstance();
        this.mStorageRef = storage.getReferenceFromUrl(URL);
        this.identifier = null;
        //poi = null;
        this.out = null;
    }

    public void setIdentifier(String filepath) {
        identifier = Uri.fromFile(new File(filepath));
    }

    public byte[] convertToByteArray(PointOfInterest in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //turn this thing into a byte array
        byte[] fileContents = null;
        try {
            this.out = new ObjectOutputStream(bos);
            this.out.writeObject(in);
            this.out.flush();
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
     * NOT ACTUALLY USED
     */
    public void serializePOI(PointOfInterest in) {
        byte[] fileContents = convertToByteArray(in);

        String filename = in.getName();
        FileOutputStream outputStream;

        try {
            outputStream = this.testContext.openFileOutput(filename + ".xf", Context.MODE_PRIVATE);
            outputStream.write(fileContents);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * takes a file that contains a byte array and returns a PointOfInterest
     * https://stackoverflow.com/questions/5837698/converting-any-object-to-a-byte-array-in-java
     * this is the sketchiest thing in the world pls test
     */
    public PointOfInterest deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return (PointOfInterest)o.readObject();
            }
        }
    }

    /*
     * upload file to cloud! i think
     * https://firebase.google.com/docs/storage/android/upload-files
     */
    public void upload(PointOfInterest in) {
        StorageReference ref = this.mStorageRef.child(in.getName() + ".xf");
        byte[] fileContents = convertToByteArray(in);

        UploadTask task = ref.putBytes(fileContents);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Upload failed");
                Log.d(TAG, "Upload failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d(TAG, "Upload successful!");
            }
        });
    }

    /*
     * download file from cloud! i think
     * filename parameter does not include .xf
     * https://firebase.google.com/docs/storage/android/download-files
     */
    public File download(String filename) {
        StorageReference gsReference = this.storage.getReferenceFromUrl(URL + "/" + filename + ".xf");
        File localFile = new File(filename + ".xf");

        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.d(TAG, "Successful download!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Lol it doesn't work");
                Log.d(TAG, "The file was not downloaded");
            }
        });
        return localFile;
    }
}
