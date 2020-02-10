package com.emrereyhanlioglu.bby.login.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.emrereyhanlioglu.bby.SharedPreferencesHelper
import com.emrereyhanlioglu.bby.students.homepage.view.HomepageActivity
import com.emrereyhanlioglu.bby.volunteers.HomepageVolActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.android.synthetic.main.fragment_signup_or_login.*




class LoginActivity : AppCompatActivity() {
    private lateinit var prefHelper: SharedPreferencesHelper
    private lateinit var navController: NavController


    private var database: FirebaseDatabase? = null
    private var auth: FirebaseAuth? = null

    private var studentVerificationCode: String? = null
    private var volunteerVerificationCode: String? = null

    val refNew = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.emrereyhanlioglu.bby.R.layout.activity_login)

        //SETUP NAVIGATION CONTROLLER WITH THIS ACTIVITY AND NAVIGATION GRAPH
        navController = Navigation.findNavController(this, com.emrereyhanlioglu.bby.R.id.fragmentLogin)
        navController.setGraph(com.emrereyhanlioglu.bby.R.navigation.login_navigation)

        prefHelper = SharedPreferencesHelper(applicationContext)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth?.currentUser
        if(currentUser == null) {
            getAndSaveUserInformationsLocally()
            getVerificationCodes()
        }
        else{
            navigateUserToHomepage(prefHelper.getUserType()!!)
        }
    }

    override fun onResume() {
        super.onResume()
        val actionBar = this.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setHomeButtonEnabled(false)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    fun getVerificationCodes() {
        refNew.collection("Users").document("VerificationCodes").get().addOnSuccessListener {
            studentVerificationCode = it["student"]?.toString()
            volunteerVerificationCode = it["volunteer"]?.toString()

            //println("Student code: $studentVerificationCode , volunteer code: $volunteerVerificationCode")
        }

    }


    fun getAndSaveUserInformationsLocally() {
        val currentUserUid =FirebaseAuth.getInstance().currentUser?.uid


        val newRef = FirebaseFirestore.getInstance()
        newRef.collection("User-Class").document(currentUserUid.toString()).get().addOnSuccessListener { user->
            println("User's class is ${user["class"]}")

            val className = user["class"]
            val userType = user["user type"]
            val fullname = user["fullname"]
            val permissionExamShare = user["permissionExamShare"]
            val permissionAttendance = user["permissionAttendance"]
            val permissionAnnouncement = user["permissionAnnouncement"]


            permissionAnnouncement?.let {
                prefHelper.savePermissionForAnnouncements(it.toString())
            }
            permissionAttendance?.let {
                prefHelper.savePermissionForAttendance(it.toString())
            }
            permissionExamShare?.let {
                prefHelper.savePermissionForSharingExamResults(it.toString())
            }
            fullname?.let {
                prefHelper.saveFullname(it.toString())
            }
            className?.let {
                prefHelper.saveClassName(it.toString())
            }
            userType?.let {
                prefHelper.saveUserType(it.toString())

                // If there is a user, then navigate to the homepage
                navigateUserToHomepage(it.toString())
            }

        }

    }

    fun navigateUserToHomepage(userType: String) {

        if(userType.equals("Students")) {
            val intent = Intent(applicationContext, HomepageActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(userType.equals("Volunteers")){

            val intent = Intent(applicationContext, HomepageVolActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    fun openSignupPage(v: View){
        val action = SignupOrLoginFragmentDirections.actionSignup()
        Navigation.findNavController(v).navigate(action)
    }



    fun signin(v: View) {
        val email1 = loginEmail.text.toString()
        val password1 = loginPassword.text.toString()

        auth?.signInWithEmailAndPassword(email1, password1)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Log.d("LOGIN", "signInWithEmail:success")

                    if(!prefHelper.getClassName().equals("")){
                        navigateUserToHomepage(prefHelper.getUserType()!!)
                    }
                    else {
                        getAndSaveUserInformationsLocally()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOGIN", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    // navigateUserToHomepage(null)
                }

            }

    }


    fun signup(v: View) {
        val root = v.rootView
        val firstName = root.registerName?.text.toString()
        val surName = root.registerSurname?.text.toString()
        val email = root.registerEmail?.text.toString()
        val password = root.registerPassword?.text.toString()
        val verificationCode = root.verificationCode?.text.toString()


        val userTypeText = root.spinnerUserType?.selectedItem.toString()
        var userType = ""
        when(userTypeText) {
            "Öğrenci" ->  { userType = "Students" }
            "Gönüllü" ->  { userType = "Volunteers" }
        }
        var userClass = ""
        if(userType.equals("Students")){
            userClass = root.spinnerUserClass?.selectedItem.toString()
        }


        val auth = FirebaseAuth.getInstance()

        // CHECK WHETHER ANY FIELD IS EMPTY OR NOT
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(surName)
            || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
            || TextUtils.isEmpty(userTypeText) || (userType.equals("Students") && userClass.equals("Sınıfı Yok"))   ) {
            Toast.makeText(applicationContext, "Lütfen bütün alanları doldurunuz", Toast.LENGTH_SHORT).show()
            return
        }
        // CHECK PASSWORD LENGHT
        if(password.length<6){
            Toast.makeText(applicationContext, "Şifreniz en az 6 haneli olmalıdır", Toast.LENGTH_SHORT).show()
            return
        }
        // CHECK VERIFICATION CODE
        if( (userType.equals("Students") && verificationCode != studentVerificationCode) ||
            (userType.equals("Volunteers") && verificationCode != volunteerVerificationCode)){
            Toast.makeText(applicationContext, "Doğrulama anahtarınız geçersiz", Toast.LENGTH_SHORT).show()
            println("Std: $studentVerificationCode, vol: $volunteerVerificationCode")
            return
        }
        else {

            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val currentUserUid = auth.currentUser?.uid

                        val data = HashMap<String, Any>()
                        val spinnerUserClass = root.spinnerUserClass?.selectedItem.toString()
                        data["name"] = firstName
                        data["surname"] = surName
                        data["email"] = email
                        data["password"] = password
                        data["user type"] = userType
                        data["class"] = spinnerUserClass
                        data["fullname"] = firstName + " " + surName
                        data["uid"] = currentUserUid.toString()

                        prefHelper.saveClassName(spinnerUserClass)
                        prefHelper.saveFullname(firstName + " " + surName)
                        prefHelper.saveUserType(userType)

                        // Add user to the User-Class Table
                        refNew.collection("User-Class").document(currentUserUid.toString()).set(data).addOnSuccessListener {

                            // Add user informations into the student or volunteer table

                            // userClass will be empty if the user is a volunteer
                            if(userType.equals("Students")){
                                refNew.collection("Users/$userType/$userClass")
                                    .document("User-${currentUserUid.toString()}").set(data)
                                    .addOnSuccessListener {
                                        Toast.makeText(applicationContext, "Hesabınız başarıyla oluşturuldu", Toast.LENGTH_SHORT).show()
                                        navigateUserToHomepage(prefHelper.getUserType()!!)
                                    }
                            }
                            else if(userType.equals("Volunteers")){
                                refNew.collection("Users").document(userType)
                                    .collection("User-${currentUserUid.toString()}").document(currentUserUid.toString())
                                    .set(data).addOnSuccessListener {
                                        Toast.makeText(applicationContext, "Hesabınız başarıyla oluşturuldu", Toast.LENGTH_SHORT).show()
                                        navigateUserToHomepage(prefHelper.getUserType()!!)
                                    }
                            }
                        }




                        /*  val databaseReference = FirebaseDatabase.getInstance().reference.child("Users/$userType/$userClass")


                            databaseReference.child("User-$currentUserUid").child("name").setValue(firstName)
                            databaseReference.child("User-$currentUserUid").child("surname").setValue(surName)
                            databaseReference.child("User-$currentUserUid").child("email").setValue(email)
                            databaseReference.child("User-$currentUserUid").child("password").setValue(password)
                            databaseReference.child("User-$currentUserUid").child("user type").setValue(userType)
                            databaseReference.child("User-$currentUserUid").child("class").setValue(userClass)
                            databaseReference.child("User-$currentUserUid").child("fullname").setValue(firstName+" "+surName)
                            databaseReference.child("User-$currentUserUid").child("uid").setValue(currentUserUid)

                            Toast.makeText(baseContext, "Hesabınız başarıyla oluşturuldu", Toast.LENGTH_SHORT).show()
                            onBackPressed()

                            */



                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Kullanıcı oluşturulamadı. Geçerli bir email adresi girdiğinizden emin olunuz",
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }

    }




}
