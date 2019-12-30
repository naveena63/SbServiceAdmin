package com.ashesha.sbservicesadmin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static final String PREF_NAME = "SbServices";
    private static final String ADMIN_EMAIL_ID = "email_id";
    public static final String ADMIN_ID = "adminId";
    private static final String ADMIN_NAME = "name";
    private static final String ADMIN_PHONE_NUMBER = "phone_number";
    private static final String CUSTMOR_ID = "user_id";
    private static final String ORDER_ID = "order_id";
    private static final String PASSWORD = "password";
    private static final String Reson_id = "id";

    private static final String EMPLOYEE_ID = "emp_id";
    private static final String EMPLOYEE_NAME = "emp_name";
    private static final String EMPLOYEE_FATHER_NAME = "fname";
    private static final String EMPLOYEE_COMPANY_NUM = "emp_cnum";
    private static final String EMPLOYEE_PERSONAL_NUM = "emp_pnum";
    private static final String EMPLOYEE_EMAIL = "emp_email";
    private static final String EMPLOYEE_ADDRESS = "address";
    private static final String EMPLOYEE_ADHAR = "aadhar";
    private static final String EMPLOYEE_PANCARD = "pancard";
    private static final String EMPLOYEE_BANK = "bank";
    private static final String DEVICE_ID = "deviceid";
    private static final String TOKEN = "token";
    private static final String Spinner = "spinner";
    private static final String ADMIN_EMP_ID = "adminEmpId";
    private static final String Spinner_Emp_id = "adminEmpId";


    private static final String LOGIN_TYPE = "logi_type";


    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }

    public void storeValue(String key, Object object) {

        if (object instanceof String) {
            editor.putString(key, object.toString());


        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        }
        editor.apply();

    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }
    public String getAdminId() {
        return pref.getString(ADMIN_ID, "");
    }

    public void setAdminId(String adminId) {
        editor.putString(ADMIN_ID, adminId);
        editor.commit();
    }
    public String getAdminEmpId() {
        return pref.getString(ADMIN_EMP_ID, "");
    }

    public void setAdminEmpId(String adminEmpId) {
        editor.putString(ADMIN_EMP_ID, adminEmpId);
        editor.commit();
    }
    public String getSpinner_Emp_id() {
        return pref.getString(Spinner_Emp_id, "");
    }

    public void setSpinner_Emp_id(String spinner_emp_id) {
        editor.putString(Spinner_Emp_id, spinner_emp_id);
        editor.commit();
    }
    public String getOrderId() {
        return pref.getString(ORDER_ID, "");
    }

    public void setOrderId(String orderId) {
        editor.putString(ORDER_ID, orderId);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN, "");
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }
    public String getReson_id() {
        return pref.getString(Reson_id, "");
    }

    public void setReson_id(String reson_id) {
        editor.putString(Reson_id, reson_id);
        editor.commit();
    }

    public String getDeviceId() {
        return pref.getString(DEVICE_ID, "");
    }

    public void setDeviceId(String deviceId) {
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }
    public String getSpinner() {
        return pref.getString(Spinner, "");
    }

    public void setSpinner(String spinner) {
        editor.putString(Spinner, spinner);
        editor.commit();
    }

    public String getLoginType() {
        return pref.getString(LOGIN_TYPE, "");
    }

    public void setLoginType(String loginType) {
        editor.putString(LOGIN_TYPE, loginType);
        editor.commit();
    }

    public String getAdminEmailId() {
        return pref.getString(ADMIN_EMAIL_ID, "");
    }

    public void setAdminEmailId(String adminEmailId) {
        editor.putString(ADMIN_EMAIL_ID, adminEmailId);
        editor.commit();
    }

    public String getAdminPhoneNumber() {
        return pref.getString(ADMIN_PHONE_NUMBER, "");
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        editor.putString(ADMIN_PHONE_NUMBER, adminPhoneNumber);
        editor.commit();
    }

    public String getAdminName() {
        return pref.getString(ADMIN_NAME, "");
    }

    public void setAdminName(String adminName) {
        editor.putString(ADMIN_NAME, adminName);
        editor.commit();
    }

    public String getEmployeeId() {
        return pref.getString(EMPLOYEE_ID, "");
    }

    public void setEmployeeId(String employeeId) {
        editor.putString(EMPLOYEE_ID, employeeId);
        editor.commit();
    }

    public static String getEmployeeName() {
        return EMPLOYEE_NAME;
    }

    public void setEmployeeName(String employeeName) {
        editor.putString(EMPLOYEE_NAME, employeeName);
        editor.commit();
    }

    public static String getEmployeeFatherName() {
        return EMPLOYEE_FATHER_NAME;
    }

    public void setEmployeeFatherName(String employeeFatherName) {
        editor.putString(EMPLOYEE_FATHER_NAME, employeeFatherName);
        editor.commit();
    }

    public static String getEmployeeCompanyNum() {
        return EMPLOYEE_COMPANY_NUM;
    }

    public void setEmployeeCompanyNum(String employeeCompanyNum) {
        editor.putString(EMPLOYEE_COMPANY_NUM, employeeCompanyNum);
        editor.commit();
    }

    public static String getEmployeePersonalNum() {
        return EMPLOYEE_PERSONAL_NUM;
    }

    public void setEmployeePersonalNum(String employeePersonalNum) {
        editor.putString(EMPLOYEE_PERSONAL_NUM, employeePersonalNum);
        editor.commit();
    }

    public static String getEmployeeEmail() {
        return EMPLOYEE_EMAIL;
    }

    public void setEmployeeEmail(String employeeEmail) {
        editor.putString(EMPLOYEE_EMAIL, employeeEmail);
        editor.commit();
    }

    public static String getEmployeeAddress() {
        return EMPLOYEE_ADDRESS;
    }

    public void setEmployeeAddress(String employeeAddress) {
        editor.putString(EMPLOYEE_ADDRESS, employeeAddress);
        editor.commit();
    }

    public static String getEmployeeAdhar() {
        return EMPLOYEE_ADHAR;
    }

    public void setEmployeeAdhar(String employeeAdhar) {
        editor.putString(EMPLOYEE_ADHAR, employeeAdhar);
        editor.commit();
    }

    public static String getEmployeePancard() {
        return EMPLOYEE_PANCARD;
    }

    public void setEmployeePancard(String employeePancard) {
        editor.putString(EMPLOYEE_PANCARD, employeePancard);
        editor.commit();
    }

    public static String getEmployeeBank() {
        return EMPLOYEE_BANK;
    }

    public void setEmployeeBank(String employeeBank) {
        editor.putString(EMPLOYEE_BANK, employeeBank);
        editor.commit();
    }

    public void logout() {
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}










