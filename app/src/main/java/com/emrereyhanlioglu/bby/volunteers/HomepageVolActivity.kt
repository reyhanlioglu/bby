package com.emrereyhanlioglu.bby.volunteers

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.SharedPreferencesHelper
import com.emrereyhanlioglu.bby.info.AboutActivity
import com.emrereyhanlioglu.bby.login.view.LoginActivity
import com.emrereyhanlioglu.bby.students.announcements.model.Announcement
import com.emrereyhanlioglu.bby.students.attendance.model.AttendanceDetail
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResult
import com.emrereyhanlioglu.bby.students.messageBoard.model.Message
import com.emrereyhanlioglu.bby.volunteers.homepage.HomepageVolFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_announcement_share.*
import kotlinx.android.synthetic.main.fragment_announcement_share.view.*
import kotlinx.android.synthetic.main.fragment_attendance_share.view.*
import kotlinx.android.synthetic.main.fragment_exam_result_share.*
import kotlinx.android.synthetic.main.fragment_exam_result_share.view.*
import kotlinx.android.synthetic.main.fragment_homepage_vol.*
import kotlinx.android.synthetic.main.fragment_message_board.view.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import java.text.SimpleDateFormat
import java.util.*


class HomepageVolActivity : AppCompatActivity() {
    private lateinit var prefHelper: SharedPreferencesHelper
    private lateinit var navController: NavController
    private var examName: String? = null
    private var matD: String? = null
    private var matY: String? = null
    private var turD: String? = null
    private var turY: String? = null
    private var fenD: String? = null
    private var fenY: String? = null
    private var sosD: String? = null
    private var sosY: String? = null

    private var username:String? = ""

    val refNew = FirebaseFirestore.getInstance()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage_vol)

        prefHelper = SharedPreferencesHelper(applicationContext)

        navController = Navigation.findNavController(this, R.id.fragmentHomepageVol)
        navController.setGraph(R.navigation.homepage_vol_navigation)
        NavigationUI.setupActionBarWithNavController(this, navController)


        //saveVolunteerFullnameToSharedPref()
        //saveVolunteerClassToSharedPref()
        //checkPermissionForAccessManagementPanel()

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
                        prefHelper.saveAdminStatus("")
                        prefHelper.savePermissionForSharingExamResults("")
                        prefHelper.savePermissionForAnnouncements("")
                        prefHelper.savePermissionForAttendance("")

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



    fun onClickRemainingTime(v: View) {
        val action = HomepageVolFragmentDirections.actionRemainingTimeVol()
        Navigation.findNavController(v).navigate(action)
    }

    fun onClickExamResultShare(v: View) {
        if(prefHelper.getPermissionForSharingExamResults().equals("GRANTED")){
            val action = HomepageVolFragmentDirections.actionSelectClass()
            action.destinationPage = "exam result"
            Navigation.findNavController(v).navigate(action)
        }
        else{
            Toast.makeText(applicationContext, "Sınav sonucu paylaşma yetkiniz bulunmamaktadır.\n(Yetkiniz varsa hesabınızdan çıkış yapıp tekrar giriş yapınız)", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickAttendanceShare(v: View) {
        if(prefHelper.getPermissionForAttendance().equals("GRANTED")){
            val action = HomepageVolFragmentDirections.actionSelectClass()
            action.destinationPage = "attendance"
            Navigation.findNavController(v).navigate(action)
        }
        else {
            Toast.makeText(applicationContext, "Devamsızlık paylaşma yetkiniz bulunmamaktadır.\n(Yetkiniz varsa hesabınızdan çıkış yapıp tekrar giriş yapınız)", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickAnnouncementShare(v: View) {
        if(prefHelper.getPermissionForAnnouncements().equals("GRANTED")){
            val action = HomepageVolFragmentDirections.actionAnnouncementShare()
            action.fullname = prefHelper.getFullname()!!
            Navigation.findNavController(v).navigate(action)
        }
        else {
            Toast.makeText(applicationContext, "Duyuru yapma yetkiniz bulunmamaktadır.\n(Yetkiniz varsa hesabınızdan çıkış yapıp tekrar giriş yapınız)", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickMessageBoard(v: View) {
        if(!prefHelper.getClassName().equals("Sınıfı Yok")){
            val action = HomepageVolFragmentDirections.actionMessageBoardVol()
            Navigation.findNavController(v).navigate(action)
        }
        else{
            Toast.makeText(applicationContext, "Herhangi bir sınıfa ait değilsiniz", Toast.LENGTH_SHORT).show()
        }
    }

   /* fun onClickManagementPanel(v: View) {
        val action = HomepageVolFragmentDirections.actionAdminSignup()
        Navigation.findNavController(v).navigate(action)
    }
    */
    fun onClickStudentResults(v: View) {
        val action = HomepageVolFragmentDirections.actionSelectClass()
        action.destinationPage = "show exam results"
        Navigation.findNavController(v).navigate(action)
    }


    fun shareAttendance(v: View) {
        val root = v.rootView
        val attendanceWriter = root.attendanceShareWriter?.text.toString()
        val attendanceMonth = root.spinnerMonths.selectedItem.toString()
        val attendanceType = root.spinnerAttendanceType.selectedItem.toString()
        val attendanceDate = root.editTextAttendanceDate?.text.toString()
        val attendanceHours = root.editTextAttendanceHours?.text.toString()

        val studentName = root.attendanceShareStudentName.text.toString()

        var refMonth = ""
        when(attendanceMonth) {
            "Eylül" -> {refMonth = "01Eylul"}
            "Ekim" -> {refMonth = "02Ekim"}
            "Kasım" -> {refMonth = "03Kasim"}
            "Aralık" -> {refMonth = "04Aralik"}
            "Ocak" -> {refMonth = "05Ocak"}
            "Şubat" -> {refMonth = "06Subat"}
            "Mart" -> {refMonth = "07Mart"}
            "Nisan" -> {refMonth = "08Nisan"}
            "Mayıs" -> {refMonth = "09Mayis"}
        }

        println("Writer $attendanceWriter \nMonth $attendanceMonth\nType $attendanceType\nDate $attendanceDate\nHours $attendanceHours" )

        if ( !attendanceWriter.equals("") && !attendanceMonth.equals("Bir ay seçiniz") && !attendanceType.equals("") && !attendanceDate.equals("") && !attendanceHours.equals("")   )
        {

            AlertDialog.Builder(this)
                .setTitle("Devamsızlık Kaydı Paylaşılsın mı?")
                .setMessage("$studentName isimli öğrencinin $attendanceDate tarihli $attendanceHours saatlik $attendanceMonth ayı devamsızlık kaydı paylaşılsın mı?")
                .setPositiveButton("Evet") { _, _ ->

                    root.attendanceShareButton.isEnabled = false
                    val studentUid = root.attendanceShareStudentUid.text
                    val studentClass = root.attendanceShareStudentClass.text

                    // Write data to Firestore


                    val currentTime = System.currentTimeMillis().toString()
                    refNew.collection("Users")

                    val ref = FirebaseDatabase.getInstance().getReference("/Users/Students/$studentClass/User-$studentUid/attendance").child(refMonth).child(System.currentTimeMillis().toString())
                    // PUSH ALLOW US TO ADD UNIQUE RECORDS

                    val attendance = AttendanceDetail(attendanceType, attendanceDate, attendanceHours.toInt(), attendanceWriter, attendanceMonth )

                    // New write operation
                    refNew.document("Attendance/$studentClass/User-$studentUid/attendance/$refMonth/$currentTime")
                        .set(attendance).addOnSuccessListener {
                            println("Data has been added to FIRESTORE")
                    }/* // TODO : OPEN IT LATER
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "Devamsızlık kaydı başarıyla paylaşıldı.", Toast.LENGTH_LONG).show()
                            onBackPressed()
                        } .addOnFailureListener {
                            Toast.makeText(applicationContext, "Devamsızlık kaydı eklenemedi. Lütfen internet bağlantınızı kontrol ediniz.", Toast.LENGTH_LONG).show()
                            root.attendanceShareButton.isEnabled = true
                        }
                        */

                    // TODO : REMOVE THESE LATER
                    ref.setValue(attendance).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Devamsızlık kaydı başarıyla paylaşıldı.", Toast.LENGTH_LONG).show()
                        onBackPressed()
                    } .addOnFailureListener {
                        Toast.makeText(applicationContext, "Devamsızlık kaydı eklenemedi. Lütfen internet bağlantınızı kontrol ediniz.", Toast.LENGTH_LONG).show()
                        root.attendanceShareButton.isEnabled = true
                    }

                }
                .setNegativeButton("Hayır") { _, _ ->

                }
                .show()

        }

    }


    fun shareAnnouncement(v: View) {
        val root = v.rootView
        val sdf = SimpleDateFormat("HH:mm - dd.MM.yyyy")
        val currentDateandTime = sdf.format(Date())

        username = prefHelper.getFullname()

        val isValid =  !TextUtils.isEmpty(root.announcementShareHeader.text.toString())
                        && !TextUtils.isEmpty(root.announcementShareText.text.toString()) && !username.equals("")

        if(isValid) {

            AlertDialog.Builder(this)
                .setTitle("Duyuruyu Paylaş")
                .setMessage("Duyuruyu bütün sınıflarla paylaşılacak, devam edilsin mi?")
                .setPositiveButton("Evet") { _, _ ->

                    announcementShareButton.isEnabled = false
                    val header = root.announcementShareHeader.text.toString()
                    val message = root.announcementShareText.text.toString()

                    val ref = FirebaseDatabase.getInstance().getReference("/Users/Announcements").child(System.currentTimeMillis().toString())
                    val info = "$username tarafından $currentDateandTime tarihinde paylaşıldı."
                    val announcement = Announcement(header, message, currentDateandTime, info)

                    ref.setValue(announcement).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Duyuru başarıyla paylaşıldı.", Toast.LENGTH_LONG).show()

                        onBackPressed()
                    } .addOnFailureListener {
                        Toast.makeText(applicationContext, "Duyuru paylaşılamadı. Lütfen internet bağlantınızı kontrol ediniz.", Toast.LENGTH_LONG).show()
                        announcementShareButton.isEnabled = true
                    }

                }
                .setNegativeButton("Hayır") { _, _ ->

                }
                .show()
        }
        else {
            Toast.makeText(applicationContext, "Lütfen bütün alanları eksiksiz bir şekilde doldurunuz.", Toast.LENGTH_LONG).show()
        }

    }



    fun shareExamResult(v: View) {
        val root = v.rootView
        examName =root.examResultExamName?.text.toString()
        matD = root.examResultMatD?.text.toString()
        matY = root.examResultMatY?.text.toString()
        turD = root.examResultTurD?.text.toString()
        turY = root.examResultTurY?.text.toString()
        fenD = root.examResultFenD?.text.toString()
        fenY = root.examResultFenY?.text.toString()
        sosD = root.examResultSosD?.text.toString()
        sosY = root.examResultSosY?.text.toString()


        if (!validateTYTForm()) {
            return
        }
        else {
            AlertDialog.Builder(this)
                .setTitle("Sınav Sonucu Paylaşılsın mı?")
                .setMessage("${examResultStudentName.text} isimli öğrencinin sınav sonucu paylaşılacak. Sonuçları doğru bir şekilde girdiğinizden emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->

                    root.examResultShareButton.isEnabled = false
                    val studentUid = root.examResultStudentUid.text
                    val studentClass = root.examResultStudentClass.text

                    val ref = FirebaseDatabase.getInstance().getReference("/Users/Students/$studentClass/User-$studentUid/exam results").child(System.currentTimeMillis().toString())
                    // PUSH ALLOW US TO ADD UNIQUE RECORDS

                    val examresult = ExamResult("TYT", examName!!, turD!!.toInt(), turY!!.toInt(), 40, matD!!.toInt(), matY!!.toInt(), 40,
                        fenD!!.toInt(), fenY!!.toInt(), 20, sosD!!.toInt(), sosY!!.toInt(), 20)


                 /*   ref.setValue(examresult).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Sınav sonucu başarıyla paylaşıldı.", Toast.LENGTH_LONG).show()
                        onBackPressed()
                    } .addOnFailureListener {
                        Toast.makeText(applicationContext, "Sınav sonucu eklenemedi. Lütfen internet bağlantınızı kontrol ediniz.", Toast.LENGTH_LONG).show()
                        root.examResultShareButton.isEnabled = true
                    }
                  */

                    val currentTime = System.currentTimeMillis().toString()

                    refNew.collection("Users/Students/$studentClass/User-$studentUid/exam results").document(currentTime)
                        .set(examresult).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Sınav sonucu başarıyla paylaşıldı.", Toast.LENGTH_LONG).show()
                        onBackPressed()
                    } .addOnFailureListener {
                        Toast.makeText(applicationContext, "Sınav sonucu eklenemedi. Lütfen internet bağlantınızı kontrol ediniz.", Toast.LENGTH_LONG).show()
                        root.examResultShareButton.isEnabled = true
                    }

                }
                .setNegativeButton("Hayır") { _, _ ->

                }
                .show()

        }
    }

    private fun validateTYTForm(): Boolean {

        val isValid =  !TextUtils.isEmpty(examName)
                && !TextUtils.isEmpty(matD) && !TextUtils.isEmpty(matY)
                && !TextUtils.isEmpty(turD) && !TextUtils.isEmpty(turY)
                && !TextUtils.isEmpty(fenD) && !TextUtils.isEmpty(fenY)
                && !TextUtils.isEmpty(sosD) && !TextUtils.isEmpty(sosY)

        if(isValid) {
            if(matD!!.toInt() > 40 || matY!!.toInt()> 40 || (matD!!.toInt() + matY!!.toInt() > 40) ||
                turD!!.toInt() > 40 || turY!!.toInt()> 40 || (turD!!.toInt() + turY!!.toInt() > 40) ||
                fenD!!.toInt() > 20 || fenY!!.toInt()> 20 || (fenD!!.toInt() + fenY!!.toInt() > 20) ||
                sosD!!.toInt() > 20 || sosY!!.toInt()> 20 || (sosD!!.toInt() + sosY!!.toInt() > 20) ) {
                Toast.makeText(applicationContext, "Girdiğiniz değerleri kontrol ediniz!", Toast.LENGTH_LONG).show()
                return false
            }
            else {
                return true
            }

        }
        else {
            Toast.makeText(applicationContext, "Lütfen bütün alanları eksiksiz bir şekilde doldurunuz.", Toast.LENGTH_LONG).show()
        }

        return false

    }



    private fun saveVolunteerFullnameToSharedPref(){
        val name = prefHelper.getFullname()

        if(name != null && !name.equals("")){
            return
        }

        val currentUserUid =FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/Volunteers/User-$currentUserUid/fullname")
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


    private fun saveVolunteerClassToSharedPref(){
        val name = prefHelper.getClassName()

        if(name != null && !name.equals("")){
            return
        }

        val currentUserUid =FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/Volunteers/User-$currentUserUid/class")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val classname = p0.getValue(String::class.java)
                classname?.let {
                    prefHelper.saveClassName(it)
                    println("TEST CLASSNAME "+it)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                System.err.println("ERROR: User type could not retrieved from Firebase")
            }
        })
    }


    fun onClickSendMessageToBoard(v: View) {
        val className = prefHelper.getClassName()
        val username = prefHelper.getFullname()
        val root = v.rootView
        val messageText = root.messageBoardEditText.text.toString()
        val date = SimpleDateFormat("HH:mm - dd.MM.yyyy").format(Date())

        //println("Message: $messageText , className: $className , fullname: $username")
        if( !messageText.equals("") && !className.equals("") && !username.equals("")){
            root.messageBoardEditText.text.clear()
            val ref = FirebaseDatabase.getInstance().getReference("/Users/Messages/$className").child(System.currentTimeMillis().toString())
            // PUSH ALLOW US TO ADD UNIQUE RECORDS
            val message = Message(username!!, messageText, date )
            ref.setValue(message).addOnSuccessListener {

            } .addOnFailureListener {
            }
        }
    }


    private fun checkPermissionForAccessManagementPanel() {
        val isAdmin = prefHelper.getAdminStatus()

        if(isAdmin != null && !isAdmin.equals("")){
            return
        }

        val currentUserUid =FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/Volunteers/User-$currentUserUid/admin")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val adminStatus = p0.getValue(String::class.java)
                adminStatus?.let {
                    prefHelper.saveAdminStatus(it)
                    if(it.equals("true")){
                        managementPanel?.visibility = View.VISIBLE
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                System.err.println("ERROR: Admin access could not checked from Firebase")
            }
        })
    }



    fun signup(v: View) {
        val root = v.rootView
        val firstName = root.registerName?.text.toString()
        val surName = root.registerSurname?.text.toString()
        val email = root.registerEmail?.text.toString()
        val password = root.registerPassword?.text.toString()

        val userTypeText = root.spinnerUserType?.selectedItem.toString()
        var userType = ""
        when(userTypeText) {
            "Öğrenci" ->  { userType = "Students" }
            "Gönüllü" ->  { userType = "Volunteers" }
        }
        var userClass = ""
        if(userType.equals("Students")) {
            userClass = root.spinnerUserClass?.selectedItem.toString()
        }

        val auth = FirebaseAuth.getInstance()

        // CHECK INPUTS
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(surName)
            || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
            || TextUtils.isEmpty(userTypeText) || (userType.equals("Students") && userClass.equals("Sınıfı Yok")) ) {
            Toast.makeText(applicationContext, "Enter all details", Toast.LENGTH_SHORT).show()
            return
        }
        else {

            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val databaseReference = FirebaseDatabase.getInstance().reference.child("Users/$userType/$userClass")
                        val currentUserUid = auth.currentUser?.uid

                        userClass = root.spinnerUserClass?.selectedItem.toString()

                        databaseReference.child("User-$currentUserUid").child("name").setValue(firstName)
                        databaseReference.child("User-$currentUserUid").child("surname").setValue(surName)
                        databaseReference.child("User-$currentUserUid").child("email").setValue(email)
                        databaseReference.child("User-$currentUserUid").child("password").setValue(password)
                        databaseReference.child("User-$currentUserUid").child("user type").setValue(userType)
                        databaseReference.child("User-$currentUserUid").child("class").setValue(userClass)
                        databaseReference.child("User-$currentUserUid").child("fullname").setValue(firstName+" "+surName)
                        databaseReference.child("User-$currentUserUid").child("uid").setValue(currentUserUid)

                        Toast.makeText(baseContext, "Kullanıcı başarıyla oluşturuldu", Toast.LENGTH_SHORT).show()

                        onBackPressed()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Kullanıcı oluşturulamadı",
                            Toast.LENGTH_SHORT).show()
                    }


                }
        }


    }

}
