package com.ashesha.sbservicesadmin.BalanceSheet;

public class BalanceSheetModel {


    public  String id;
    public  String emp_id;
    public  String previous_balance;
    public  String credit;
    public  String debit;
    public String totalbalance;
    public String creditBy;
    public String debitedFor;
    public String type;
    public String order_id;
    public String reason;


    public String getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getPrevious_balance() {
        return previous_balance;
    }

    public void setPrevious_balance(String previous_balance) {
        this.previous_balance = previous_balance;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getTotalbalance() {
        return totalbalance;
    }

    public void setTotalbalance(String totalbalance) {
        this.totalbalance = totalbalance;
    }

    public String getCreditBy() {
        return creditBy;
    }

    public void setCreditBy(String creditBy) {
        this.creditBy = creditBy;
    }

    public String getDebitedFor() {
        return debitedFor;
    }

    public void setDebitedFor(String debitedFor) {
        this.debitedFor = debitedFor;
    }
}


