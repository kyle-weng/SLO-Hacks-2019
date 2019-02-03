package com.example.navtest1;

import android.net.Uri;
import android.net.

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;

public class FileHandler {

    private StorageReference mStorageRef;
    private Uri file;

    public FileHandler() {
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://slo-hacks-2019-230507.appspot.com/");
    }

    public File downloadFile(String filepath) {
        file = Uri.fromFile(new File(filepath));
    }

}
