package ex.java.studentorder.domain;

public class University
{
    private Long universutyId;
    private String universutyName;

    public University(Long universutyId, String universutyName) {
        this.universutyId = universutyId;
        this.universutyName = universutyName;
    }

    public University() {
    }

    public Long getUniversutyId() {
        return universutyId;
    }

    public void setUniversutyId(Long universutyId) {
        this.universutyId = universutyId;
    }

    public String getUniversutyName() {
        return universutyName;
    }

    public void setUniversutyName(String universutyName) {
        this.universutyName = universutyName;
    }
}
