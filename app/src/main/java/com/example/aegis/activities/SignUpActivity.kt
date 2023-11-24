package com.example.aegis.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aegis.R
import com.example.aegis.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var image: String
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        ArrayAdapter.createFromResource(
            this,
            R.array.gender_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genderSpinner.adapter = adapter
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding.profileImage.setOnClickListener {
            openImagePicker()
        }

        binding.signUp.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.signUp.visibility = View.GONE

            val name = binding.name.text.toString()
            val passport = binding.passport.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val password = binding.password.text.toString()
            val selectedGender = binding.genderSpinner.selectedItem.toString()
            val address = binding.address.text.toString()

            if (selectedImageUri != null) {
                uploadImageAndCreateAccount(name, passport, email, phone, password, selectedGender, address)
            } else {
                // If no image is selected, create an account without uploading an image
                image = "https://firebasestorage.googleapis.com/v0/b/aegis-17642.appspot.com/o/default.jpg?alt=media&token=60e1c165-8227-4c62-bb09-b7cace0e1510"
                createAccount(name, passport, email, phone, password, selectedGender, address)
            }
        }
    }

    private fun uploadImageAndCreateAccount(
        name: String, passport: String, email: String,
        phone: String, password: String, selectedGender: String, address: String
    ) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageRef.putFile(selectedImageUri!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                image = downloadUri.toString()
                createAccount(name, passport, email, phone, password, selectedGender, address)
            } else {
                handleUploadImageError()
            }
        }
    }

    private fun createAccount(
        name: String, passport: String, email: String,
        phone: String, password: String, selectedGender: String, address: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { authTask ->
                if (authTask.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid
                    if (uid != null) {
                        val userRef = database.reference.child("users").child(uid)
                        val userData = mapOf(
                            "name" to name,
                            "passport" to passport,
                            "email" to email,
                            "password" to password,
                            "imageUrl" to image,
                            "address" to address,
                            "gender" to selectedGender,
                            "phone" to phone
                        )

                        userRef.setValue(userData)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    sendEmailVerification()
                                } else {
                                    handleSaveUserDataError()
                                }
                            }
                    }
                } else {
                    handleCreateUserError(authTask.exception?.message)
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            } else {
                handleSendEmailVerificationError(task.exception?.message)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    private fun handleUploadImageError() {
        binding.progressBar.visibility = View.GONE
        binding.signUp.visibility = View.VISIBLE
        Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
    }

    private fun handleCreateUserError(errorMessage: String?) {
        binding.progressBar.visibility = View.GONE
        binding.signUp.visibility = View.VISIBLE
        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    private fun handleSaveUserDataError() {
        binding.progressBar.visibility = View.GONE
        binding.signUp.visibility = View.VISIBLE
        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
    }

    private fun handleSendEmailVerificationError(errorMessage: String?) {
        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                Glide.with(this@SignUpActivity).load(selectedImageUri).into(binding.profileImage)
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1
    }
}
