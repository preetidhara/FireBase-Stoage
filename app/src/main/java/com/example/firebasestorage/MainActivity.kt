package com.example.firebasestorage

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var filepath:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        choose.setOnClickListener() {
            SelectPic()
        }

        upload.setOnClickListener() {
            UploadPic()
        }

    }

    private fun UploadPic() {

        if (filepath!=null){
            var pd=ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef=FirebaseStorage.getInstance().reference.child("images/pic.jpg")
            imageRef.putFile(filepath)

                .addOnSuccessListener { p0->
                    pd.dismiss()
                    Toast.makeText(applicationContext,"File Uploaded",Toast.LENGTH_LONG).show()


                }

                .addOnFailureListener{p0->
                    pd.dismiss()
                    Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()

                }

                .addOnProgressListener {p0->
                    var progress:Double=(100.0*p0.bytesTransferred)/p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")

                }
        }

    }

    private fun SelectPic() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_PICK)
        startActivityForResult(Intent.createChooser(intent, "Choose Pic"), 0)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            filepath = data.data!!
            var bitmap=MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            imageView.setImageBitmap(bitmap)

        }

    }
}
