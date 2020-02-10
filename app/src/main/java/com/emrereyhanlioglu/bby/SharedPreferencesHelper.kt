package com.emrereyhanlioglu.bby

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class SharedPreferencesHelper {

    companion object {
        private const val PREF_CLASS = "Pref class"
        private const val PREF_FULLNAME = "Pref fullname"
        private const val PREF_USERTYPE = "Pref user type"
        private const val PREF_ADMIN_STATUS = "Admin status"
        private const val PREF_PERMISSION_SHARE_EXAM_RESULT = "Permission share exam result"
        private const val PREF_PERMISSION_ATTENDANCE = "Permission attendance"
        private const val PREF_PERMISSION_ANNOUNCEMENT = "Permission announcement"
        private var prefs: SharedPreferences? = null

        @Volatile private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance ?: synchronized(LOCK) {
            instance ?: buildHelper(context).also{
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    // PERMISSIONS
    fun savePermissionForSharingExamResults(permission: String){
        prefs?.edit(commit = true) {
            putString(PREF_PERMISSION_SHARE_EXAM_RESULT, permission)
        }
    }

    fun getPermissionForSharingExamResults() = prefs?.getString(PREF_PERMISSION_SHARE_EXAM_RESULT, "")


    fun savePermissionForAttendance(permission: String){
        prefs?.edit(commit = true) {
            putString(PREF_PERMISSION_ATTENDANCE, permission)
        }
    }

    fun getPermissionForAttendance() = prefs?.getString(PREF_PERMISSION_ATTENDANCE, "")


    fun savePermissionForAnnouncements(permission: String){
        prefs?.edit(commit = true) {
            putString(PREF_PERMISSION_ANNOUNCEMENT, permission)
        }
    }

    fun getPermissionForAnnouncements() = prefs?.getString(PREF_PERMISSION_ANNOUNCEMENT, "")


    fun saveClassName(classname: String) {
        prefs?.edit(commit = true) {
            putString(PREF_CLASS, classname)
        }
    }

    fun getClassName() = prefs?.getString(PREF_CLASS, "")



    fun saveFullname(fullname: String) {
        prefs?.edit(commit = true) {
            putString(PREF_FULLNAME, fullname)
        }
    }


    fun getFullname() = prefs?.getString(PREF_FULLNAME, "")


    fun saveUserType(userType: String) {
        prefs?.edit(commit = true) {
            putString(PREF_USERTYPE, userType)
        }
    }

    fun getUserType() = prefs?.getString(PREF_USERTYPE, "")


    fun saveAdminStatus(status: String) {
        prefs?.edit(commit = true) {
            putString(PREF_ADMIN_STATUS, status)
        }
    }

    fun getAdminStatus() = prefs?.getString(PREF_ADMIN_STATUS, "")

}