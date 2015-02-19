package PhoneStation.beans;

import java.util.Date;


/**
 * A POJO describing the "Bill" DB Entity
 * @author Oleksandr Malishevskyi
*/
public class Bill {
    private int id;
    private int usersId;
    private Date dateIssued;
    private Date payMonth;
    private double amount;
    private boolean isPayed;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public Date getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(Date payMonth) {
        this.payMonth = payMonth;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isIsPayed() {
        return isPayed;
    }

    public void setIsPayed(boolean isPayed) {
        this.isPayed = isPayed;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    
}
