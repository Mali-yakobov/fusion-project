package il.ac.bgu.fusion.classes;

/**
 * Created by Stas on 29/11/2016.
 */
public class Elipse {

    private long timeStamp;
    private long id;

    private double centreX;
    private double centreY;

    private double sx2;
    private double sy2;
    private double sxy;


    public Elipse(){
    }

    public Elipse(long timeStamp, long id, double centreX, double centreY, double sx2, double sy2, double sxy) {
        this.timeStamp = timeStamp;
        this.id = id;
        this.centreX = centreX;
        this.centreY = centreY;
        this.sx2 = sx2;
        this.sy2 = sy2;
        this.sxy = sxy;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCentreX() {
        return centreX;
    }

    public void setCentreX(double centreX) {
        this.centreX = centreX;
    }

    public double getCentreY() {
        return centreY;
    }

    public void setCentreY(double centreY) {
        this.centreY = centreY;
    }

    public double getSx2() {
        return sx2;
    }

    public void setSx2(double sx2) {
        this.sx2 = sx2;
    }

    public double getSy2() {
        return sy2;
    }

    public void setSy2(double sy2) {
        this.sy2 = sy2;
    }

    public double getSxy() {
        return sxy;
    }

    public void setSxy(double sxy) {
        this.sxy = sxy;
    }


    @Override
    public String toString() {
        return "Elipse{" +
                "timeStamp=" + timeStamp +
                ", id=" + id +
                ", centreX=" + centreX +
                ", centreY=" + centreY +
                ", sx2=" + sx2 +
                ", sy2=" + sy2 +
                ", sxy=" + sxy +
                '}';
    }
}

