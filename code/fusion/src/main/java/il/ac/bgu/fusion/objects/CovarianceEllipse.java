package il.ac.bgu.fusion.objects;

/**
 * Created by Stas on 29/11/2016.
 */
public class CovarianceEllipse {

    private long timeStamp;
    private long id;
    private Sensor sensor;
    private double centreX;
    private double centreY;
    private double sx2;
    private double sy2;
    private double sxy;

    private double vx;
    private double vy;
    private double svxy;
    private double svx2;
    private double svy2;



    private boolean isFusionEllipse;



    public CovarianceEllipse(){
        this.isFusionEllipse= false;
    }

    public CovarianceEllipse(long timeStamp,
                             long id,
                             Sensor sensor,
                             double centreX,
                             double centreY,
                             double sx2,
                             double sy2,
                             double sxy,
                             double vx,
                             double vy,
                             double svxy,
                             double svx2,
                             double svy2) {
        this.timeStamp = timeStamp;
        this.id = id;
        this.sensor = sensor;
        this.centreX = centreX;
        this.centreY = centreY;
        this.sx2 = sx2;
        this.sy2 = sy2;
        this.sxy = sxy;
        this.vx = vx;
        this.vy = vy;
        this.svxy = svxy;
        this.svx2 = svx2;
        this.svy2 = svy2;
        this.isFusionEllipse = false;
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

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getVxy() {
        return svxy;
    }

    public void setVxy(double vxy) {
        this.svxy = vxy;
    }

    public double getSvxy() {
        return svxy;
    }

    public void setSvxy(double svxy) {
        this.svxy = svxy;
    }

    public double getSvx2() {
        return svx2;
    }

    public void setSvx2(double svx2) {
        this.svx2 = svx2;
    }

    public double getSvy2() {
        return svy2;
    }

    public void setSvy2(double svy2) {
        this.svy2 = svy2;
    }

    public boolean getIsFusionEllipse() {
        return this.isFusionEllipse;
    }

    public void setIsFusionEllipse(boolean isFusionEllipse) {
        this.isFusionEllipse= isFusionEllipse;
    }


    @Override
    public String toString() {
        return "CovarianceEllipse{" +
               "timeStamp=" + timeStamp +
               ", id=" + id +
               ", sensor=" + sensor +
               ", centreX=" + centreX +
               ", centreY=" + centreY +
               ", sx2=" + sx2 +
               ", sy2=" + sy2 +
               ", sxy=" + sxy +
               ", vx=" + vx +
               ", vy=" + vy +
               ", svxy=" + svxy +
               ", svx2=" + svx2 +
               ", svy2=" + svy2 +
               ", isFusionEllipse=" + isFusionEllipse +
               '}';
    }
}

