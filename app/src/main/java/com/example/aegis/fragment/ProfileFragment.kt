package com.example.aegis.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aegis.activities.LogInActivity
import com.example.aegis.databinding.FragmentProfileBinding
import com.example.aegis.helper.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userReference = database.reference.child("users").child(auth.currentUser!!.uid)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        retrieveUserData()

        binding.update.setOnClickListener {
            val updatedName = binding.name.text.toString()
            val updatedEmail = binding.email.text.toString()
            val updatedPassport = binding.passport.text.toString()
            val updatedPhone = binding.phone.text.toString()
            val updatedGender = binding.gender.text.toString()
            val updatedAddress = binding.address.text.toString()

            // Update user data in the database
            updateUserData(updatedName, updatedEmail, updatedPassport, updatedPhone, updatedGender, updatedAddress)

            // Check if a new image is selected
            selectedImageUri?.let { uri ->
                // Upload the image to Firebase Storage
                uploadProfileImage(uri)
            }
        }

        binding.logout.setOnClickListener {
            // Set loggedIn preference to false
            auth.signOut()
            Toast.makeText(requireContext(), "Logout successfully!!!", Toast.LENGTH_LONG).show()
            // Redirect to the login screen after logout
            val intent = Intent(requireContext(), LogInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        binding.profileImage.setOnClickListener { openImagePicker() }

        return binding.root
    }

    private fun openImagePicker() {
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_IMAGE_PICKER)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun loadProfileImage(uri: Uri?) {
        uri?.let { Glide.with(this).load(it).into(binding.profileImage) }
    }

    private fun retrieveUserData() {
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    userData?.let {
                        with(binding) {
                            name.setText(it.name)
                            email.setText(it.email)
                            passport.setText(it.passport)
                            phone.setText(it.phone)
                            gender.setText(it.gender)
                            address.setText(it.address)
                            loadProfileImage(Uri.parse(it.imageUrl))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileFragment", "Error retrieving user data: $error")
            }
        })
    }


    private fun updateUserData(name: String, email: String, passport: String, phone: String, gender: String, address: String) {
        with(userReference) {
            child("name").setValue(name)
            child("email").setValue(email)
            child("passport").setValue(passport)
            child("phone").setValue(phone)
            child("gender").setValue(gender)
            child("address").setValue(address)
        }
        showToast("Profile updated successfully")
    }

    private fun uploadProfileImage(imageUri: Uri) {
        val originalBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
        val compressedBitmap = Helper().compressBitmap(originalBitmap)

        // Create a byte array from the compressed bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        // Define the storage reference
        val imageRef = storageReference.child("profile_images/${auth.currentUser!!.uid}")

        // Upload the compressed image data to Firebase Storage
        imageRef.putBytes(data)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL for the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Update the user's profile image URL in the database
                    userReference.child("imageUrl").setValue(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                Log.e("ProfileFragment", "Error uploading profile image: $e")
            }
    }


    data class UserData(val name: String = "", val email: String = "", val passport: String = "", val phone: String = "", val gender: String = "", val address: String = "", val imageUrl: String = "")

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                Glide.with(this@ProfileFragment).load(selectedImageUri).into(binding.profileImage)
            }
        }
    }
}