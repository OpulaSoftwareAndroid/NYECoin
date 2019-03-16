package existv2.com.model.TicketInfoa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("TicketStatus")
    @Expose
    private String ticketStatus;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("TicketNumber")
    @Expose
    private String ticketNumber;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("LastUpdate")
    @Expose
    private String lastUpdate;

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}