package existv2.com.model.AllTicketInfoa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("reply_id")
    @Expose
    private String replyId;
    @SerializedName("ticket_id")
    @Expose
    private String ticketId;
    @SerializedName("ticket_number")
    @Expose
    private String ticketNumber;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("deparments")
    @Expose
    private String deparments;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("attachments")
    @Expose
    private String attachments;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("reply_name")
    @Expose
    private String replyName;
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("ticket")
    @Expose
    private String ticket;

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
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

    public String getDeparments() {
        return deparments;
    }

    public void setDeparments(String deparments) {
        this.deparments = deparments;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

}