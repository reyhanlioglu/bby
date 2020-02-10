package com.emrereyhanlioglu.bby.students.homepage.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.SharedPreferencesHelper
import com.emrereyhanlioglu.bby.info.AboutActivity
import com.emrereyhanlioglu.bby.login.view.LoginActivity
import com.emrereyhanlioglu.bby.students.examResults.viewmodel.ExamResultsViewModel
import com.emrereyhanlioglu.bby.students.messageBoard.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_message_board.view.*
import java.text.SimpleDateFormat
import java.util.*


class HomepageActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var auth: FirebaseAuth? = null
    private lateinit var prefHelper: SharedPreferencesHelper
    private var className: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        prefHelper = SharedPreferencesHelper(applicationContext)

        navController = Navigation.findNavController(this, R.id.fragmentHomepage)
        navController.setGraph(R.navigation.homepage_navigation)
        NavigationUI.setupActionBarWithNavController(this, navController)

        // UPDATE -> GET IT FROM INTENT
        saveStudentsClassToSharedPref()
        saveStudentsFullnameToSharedPref()

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_sign_out -> {

                AlertDialog.Builder(this)
                    .setTitle("Çıkış Yap")
                    .setMessage("Hesabınızdan çıkış yapılsın mı?")
                    .setPositiveButton("Evet") { _, _ ->
                        FirebaseAuth.getInstance().signOut()
                        prefHelper.saveFullname("")
                        prefHelper.saveClassName("")
                        prefHelper.saveUserType("")

                        // CLEAR DATABASE
                        val viewModel = ViewModelProviders.of(this).get(ExamResultsViewModel::class.java)
                        viewModel.deleteAllResultsFromDatabase()

                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    .setNegativeButton("Hayır") { _, _ ->

                    }
                    .show()
            }

            R.id.menu_info -> {
                val intent = Intent(applicationContext, AboutActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }




    private fun saveStudentsClassToSharedPref(){
        className = prefHelper.getClassName()

        if(className != null && !className.equals("")){
            return
        }

        val currentUserUid =FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/Students")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {classes->
                    val classNameFirebase = classes.child("User-$currentUserUid/class").getValue(String::class.java)
                    if (classNameFirebase != null) {
                        prefHelper.saveClassName(classNameFirebase)
                        className = classNameFirebase
                        return
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                System.err.println("ERROR: User type could not retrieved from Firebase")
            }
        })
    }



    private fun saveStudentsFullnameToSharedPref(){
        val name = prefHelper.getFullname()
        className = prefHelper.getClassName()

        if(name != null && !name.equals("")){
            return
        }

        val currentUserUid =FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/Students/$className/User-$currentUserUid/fullname")

     /*   val ref = FirebaseFirestore.getInstance().collection("/Users/Students/$className/User-$currentUserUid/fullname")
        ref.get().addOnSuccessListener { result ->
            for(p0 in result){
                val fullname = p0.getValue(String::class.java)
                fullname?.let {
                    prefHelper.saveFullname(it)
                    println("TEST FULLNAME "+it)
                }
            }
        }
        */
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val fullname = p0.getValue(String::class.java)
                fullname?.let {
                    prefHelper.saveFullname(it)
                    println("TEST FULLNAME "+it)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                System.err.println("ERROR: User type could not retrieved from Firebase")
            }
        })
    }





    fun onClickExamResults(v: View) {
        val action = HomepageFragmentDirections.actionExamResults()
        Navigation.findNavController(v).navigate(action)
    }

    fun onClickRemainingTime(v: View) {
        val action = HomepageFragmentDirections.actionRemainingTime()
        Navigation.findNavController(v).navigate(action)
    }

    fun onClickAnnouncements(v: View) {
        val action = HomepageFragmentDirections.actionAnnouncements()
        Navigation.findNavController(v).navigate(action)
    }

    fun onClickMessageBoard(v: View) {
        val action = HomepageFragmentDirections.actionMessageBoard()
        Navigation.findNavController(v).navigate(action)
    }

    fun onClickStatistics(v: View) {
        val action = HomepageFragmentDirections.actionStatistics()
        Navigation.findNavController(v).navigate(action)
    }

    fun onClickAttendance(v: View) {
        val action = HomepageFragmentDirections.actionAttendance()
        Navigation.findNavController(v).navigate(action)
    }

    fun onClickSendMessageToBoard(v: View) {
        className = prefHelper.getClassName()
        val username = prefHelper.getFullname()
        val root = v.rootView
        val messageText = root.messageBoardEditText.text.toString()
        val date = SimpleDateFormat("HH:mm - dd.MM.yyyy").format(Date())

        //println("Message: $messageText , className: $className , fullname: $username")
        if( !messageText.equals("") && !className.equals("") && !username.equals("")){
            root.messageBoardEditText.text.clear()
            val ref = FirebaseDatabase.getInstance().getReference("/Users/Messages/$className").child(System.currentTimeMillis().toString())
            // PUSH ALLOWS US TO ADD UNIQUE RECORDS
            val message = Message(username!!, messageText, date )
            ref.setValue(message).addOnSuccessListener {

            } .addOnFailureListener {
            }
        }
    }

}
