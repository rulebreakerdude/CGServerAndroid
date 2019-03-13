package dev.n.d.cgserverandroid;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "caller_table")
public class Caller {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "primary_key")
    private int primaryKey;

    @NonNull
    @ColumnInfo(name = "caller_number")
    private String callerNumber;

    @ColumnInfo(name = "call_datetime")
    private String callDatetime;

    @ColumnInfo(name = "api_datetime")
    private String apiDatetime;

    @ColumnInfo(name = "call_from_CGNet")
    private int callFromCGNet;

    @ColumnInfo(name = "response_CGNet")
    private String responseCGNet;

    @ColumnInfo(name = "call_from_IMI")
    private int callFromIMI;

    @ColumnInfo(name = "response_IMI")
    private String responseIMI;

    @ColumnInfo(name = "successful_callback_datetime")
    private String successfulCallbackDatetime;

    @NonNull
    public int getPrimaryKey() {
        return primaryKey;
    }

    @NonNull
    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(@NonNull String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getCallDatetime() {
        return callDatetime;
    }

    public void setCallDatetime(String callDatetime) {
        this.callDatetime = callDatetime;
    }

    public String getResponseCGNet() {
        return responseCGNet;
    }

    public void setResponseCGNet(String responseCGNet) {
        this.responseCGNet = responseCGNet;
    }

    public String getSuccessfulCallbackDatetime() {
        return successfulCallbackDatetime;
    }

    public void setSuccessfulCallbackDatetime(String successfulCallbackDatetime) {
        this.successfulCallbackDatetime = successfulCallbackDatetime;
    }

    public int getCallFromCGNet() {
        return callFromCGNet;
    }

    public void setCallFromCGNet(int callFromCGNet) {
        this.callFromCGNet = callFromCGNet;
    }

    public int getCallFromIMI() {
        return callFromIMI;
    }

    public void setCallFromIMI(int callFromIMI) {
        this.callFromIMI = callFromIMI;
    }

    public String getApiDatetime() {
        return apiDatetime;
    }

    public void setApiDatetime(String apiDatetime) {
        this.apiDatetime = apiDatetime;
    }

    public String getResponseIMI() {
        return responseIMI;
    }

    public void setResponseIMI(String responseIMI) {
        this.responseIMI = responseIMI;
    }
}
