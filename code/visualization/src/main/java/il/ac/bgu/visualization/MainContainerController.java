package il.ac.bgu.visualization;

//import com.github.ansell.shp.UTM;
import com.google.gson.JsonSyntaxException;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.*;
import il.ac.bgu.fusion.objects.*;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import il.ac.bgu.visualization.objects.AddEllipseBox;
import il.ac.bgu.visualization.objects.AddEllipseBox2;
import il.ac.bgu.visualization.objects.AlertWindow;
import il.ac.bgu.visualization.objects.VizualEllipse;
import il.ac.bgu.visualization.tree.HierarchyData;
import il.ac.bgu.visualization.tree.TreeItemContainer;
import il.ac.bgu.visualization.tree.TreeViewWithItems;
import il.ac.bgu.visualization.util.EllipseBuilder;
import il.ac.bgu.visualization.util.EllipseRepresentationTranslation;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import org.jscience.geography.coordinates.UTM;

import javax.measure.unit.SI;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

import static il.ac.bgu.fusion.algorithms.InitialClustering.initialClustering;
import static javax.measure.unit.NonSI.DEGREE_ANGLE;
import static org.jscience.geography.coordinates.UTM.utmToLatLong;
import static org.jscience.geography.coordinates.crs.ReferenceEllipsoid.WGS84;


/**
 * FXML Controller class for main_container.fxml
 */
public class MainContainerController implements Initializable, MapComponentInitializedListener{
  private GoogleMap map;

  private ArrayList<PointInTime> pointInTimeArray = null;
  private int pointInTimeArrayIndex = -1;
  private ArrayList<Sensor> sensors = null;
  private ArrayList<VizualEllipse> clickedEllipses = new ArrayList<VizualEllipse>();
  private HashMap<Long, String> colorByTrackIdTable = new HashMap<Long, String>();
  private ArrayList<VizualEllipse> fusEllipseList = new ArrayList<VizualEllipse>();
  private ArrayList<com.lynden.gmapsfx.shapes.Polyline> polylineArray = new ArrayList<com.lynden.gmapsfx.shapes.Polyline>();
  final private double rawEllFillOpacityUnClicked = 0;//0.1;
  final private double rawEllFillOpacityClicked = 0;//0.3;

  final private double fusEllFillOpacityUnClicked = 0;//0.25;
  final private double fusEllFillOpacityClicked = 0;//0.45;

  final private double ellStrokeWidthUnClicked = 4.65;
  final private double ellStrokeWidthClicked = 6.15;
  private boolean dbClick=false;

  @FXML
  private Button forwardButton;
  @FXML
  private Button backwardButton;
  @FXML
  private Button resetButton;
  @FXML
  private Slider slider;
  @FXML
  private TextField textFieldTimeCount;
  @FXML
  private TextField textFieldTimeStamp;
  @FXML
  protected GoogleMapView mapView;
  @FXML
  private ToggleButton showHideRawButton;


  /* Table related declarations start  */
  @FXML
  private TreeTableView dataTable;                         //get data table from fxml
  @FXML
  private TreeTableColumn<Map, String> dataTableNameCol;   //get left column from fxml
  @FXML
  private TreeTableColumn<Map, String> dataTableValueCol;  //get right column from fxml
  TreeItem<String> dataRoot;
  TreeItem<String> metadataRoot;
  final String nameColKey = "A";
  final String valueColKey = "B";
  /* Table related declarations end  */

  /* Tree related declarations start  */
  @FXML
  private AnchorPane treeArea;                      //get place for tree from fxml
  private TreeItem<HierarchyData> root;             //root for tree
  private TreeViewWithItems tree;                   //tree
  private ObservableList<HierarchyData> treeItems;  //data source for tree
  private ContextMenu treeMenu;                     //context menu for tree (empty space)
  private TreeItemContainer selectedItemContainer;  //currently selected tree item container
  /* Tree related declarations end  */



  @Override
  public void initialize(URL url, ResourceBundle rb) {
    mapView.addMapInializedListener(this);

    forwardButton.getStyleClass().add("forward-button-class");
    backwardButton.getStyleClass().add("backward-button-class");
    resetButton.getStyleClass().add("reset-button-class");
    textFieldTimeStamp.setDisable(true);
    textFieldTimeCount.setDisable(true);

    treeInit();
    tableInit();

  }//initialize

  @Override
  public void mapInitialized() {
    double centerLat = 31.264801;
    double centerLong = 34.760233;
    MapOptions options = new MapOptions();
    options.center(new LatLong(centerLat, centerLong))
            .zoomControl(true)
            .zoom(17)
            .overviewMapControl(true)
            .mapType(MapTypeIdEnum.ROADMAP);
    map = mapView.createMap(options);
/*
    map.addMouseEventHandler(UIEventType.dblclick, (GMapMouseEvent event) -> {
      LatLong latLong = event.getLatLong();
      //VizualEllipse newEllipse=new VizualEllipse();
      MVCArray polyluneArray = EllipseBuilder.buildEllipsePoints(latLong, 500, 300, 20);
      PolylineOptions polylineOptions=new PolylineOptions().path(polyluneArray).strokeColor("blue").clickable(true);
      com.lynden.gmapsfx.shapes.Polyline polyline = new com.lynden.gmapsfx.shapes.Polyline(polylineOptions);
      map.addMapShape(polyline);
      System.out.println("Latitude: " + latLong.getLatitude());
      System.out.println("Longitude: " + latLong.getLongitude());
    });*/

    map.addMouseEventHandler(UIEventType.rightclick ,(GMapMouseEvent rclickevent) -> {
      // convert GMaps LatLong to jscience LatLong, then to UTM:
      LatLong lLong = rclickevent.getLatLong();
      org.jscience.geography.coordinates.LatLong lLong2= org.jscience.geography.coordinates.LatLong.valueOf(lLong.getLongitude(), lLong.getLatitude(), DEGREE_ANGLE);
      org.jscience.geography.coordinates.UTM utm = UTM.latLongToUtm(lLong2, WGS84);

      // get ellipse from user (display LatLong coordinates), then set UTM coordinates:
      VizualEllipse newEllipse = AddEllipseBox.display(lLong.getLatitude(), lLong.getLongitude());
      newEllipse.setCentreX(utm.getCoordinates()[0]);
      newEllipse.setCentreY(utm.getCoordinates()[1]);

      // get polyline from ellipse and draw it:
      Polyline polyl = newEllipse.ellipseToDraw(colorGenerator() ,2);
      map.addMapShape(polyl);
      polylineArray.add(polyl);

      // add onClick handlers:
      ellipseSetOnClick(newEllipse);
    });
  }



  /*
    Action functions for GUI components:
   */

  public void loadFileAction() {
    String filePath = null;
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Json File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("json", "*.json"));
    File file = fileChooser.showOpenDialog(mapView.getScene().getWindow());

    if (file != null) {
      filePath = file.getAbsolutePath();
    }
    if (filePath != null) {
      try {


        pointInTimeArray = JsonReaderWriter.jsonToObject(filePath);
        int numOfPoints = pointInTimeArray.size();
        sliderInit(numOfPoints); //initializes slider with number of points in time
        colorByTrackIdTable.clear();
        //treeItems.clear();
        fillTreeItemsFromJson(pointInTimeArray, treeItems);  // this is also where each Track gets his color
        resetAction();
        // System.out.println(pointInTimeArray.get(0).getClass().getSimpleName());
      } catch (JsonSyntaxException e) {
        AlertWindow.display("Json Error", e.getMessage());
      }
    }
  }
  public void loadSensorsFileAction() {
    String filePath = null;
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Json File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("json", "*.json"));
    File file = fileChooser.showOpenDialog(mapView.getScene().getWindow());

    if (file != null) {
      filePath = file.getAbsolutePath();
    }
    if (filePath != null) {
      try {

        sensors = JsonReaderWriter.sensorToFile(filePath);
        if(sensors != null)
          showSensors(sensors);

      } catch (JsonSyntaxException e) {
        AlertWindow.display("Json Error", e.getMessage());
      }
    }
  }
  public void forwardAction(int forward) {
    pointInTimeArrayIndex+=forward;
    slider.setValue(pointInTimeArrayIndex+1);
    textFieldTimeCount.setText((pointInTimeArrayIndex+1)+" / "+pointInTimeArray.size());
    textFieldTimeStamp.setText(String.valueOf(pointInTimeArray.get(pointInTimeArrayIndex).getTimeStamp()));
    PointInTime pointInTime = pointInTimeArray.get(pointInTimeArrayIndex);
    clearScreen();
    showPointInTime(pointInTime);
    if (pointInTimeArrayIndex >= 0) {
      backwardButton.setDisable(false);
      resetButton.setDisable(false);
    } if (pointInTimeArrayIndex == pointInTimeArray.size() - 1) {
      forwardButton.setDisable(true);
    }
  }

  public void backwardAction(int backward) {
    pointInTimeArrayIndex-=backward;
    slider.setValue(pointInTimeArrayIndex+1);
    clearScreen();
    if (pointInTimeArrayIndex == -1) {
      backwardButton.setDisable(true);
      resetButton.setDisable(true);
      textFieldTimeStamp.clear();
    } else {

      textFieldTimeCount.setText((pointInTimeArrayIndex+1) +" / "+pointInTimeArray.size());
      textFieldTimeStamp.setText(String.valueOf(pointInTimeArray.get(pointInTimeArrayIndex).getTimeStamp()));
      PointInTime pointInTime = pointInTimeArray.get(pointInTimeArrayIndex);
      showPointInTime(pointInTime);
      if (pointInTimeArrayIndex == pointInTimeArray.size() - 2) {
        forwardButton.setDisable(false);
      }
    }
  }
  public void forwardAction() { forwardAction(1); }
  public void backwardAction() {
    backwardAction(1);
  }

  public void addEllipseAction() {
    VizualEllipse newEllipse = AddEllipseBox.display();
    String newColor = colorGenerator();
    newEllipse.ellipseToDraw(newColor,newEllipse.getStroke());
    showEllipse(newEllipse, newColor, newEllipse.getStroke());
    ellipseSetOnClick(newEllipse);

  }

  public void addEllipseOnClickAction(double x, double y) {
    VizualEllipse newEllipse = AddEllipseBox.display(x, y);
    String newColor = colorGenerator();

    showEllipse(newEllipse, newColor ,newEllipse.getStroke());
    ellipseSetOnClick(newEllipse);
  }

  public void clearAction() {
    clearScreen();
    pointInTimeArrayIndex = -1;
    pointInTimeArray = null;
    forwardButton.setDisable(true);
    backwardButton.setDisable(true);
    resetButton.setDisable(true);
    colorByTrackIdTable.clear();
    treeItems.clear();
    sliderInit(0);
  }

  public void resetAction() {
    clearScreen();
    pointInTimeArrayIndex = -1;
    forwardButton.setDisable(false);
    backwardButton.setDisable(true);
    resetButton.setDisable(true);
  }

  public void closeAction() {
    GUI.window.close();
  }

  public void showHideRawAction() {
    boolean show=showHideRawButton.isSelected();

    for (VizualEllipse fusEllipse : fusEllipseList) {
      for (VizualEllipse rawEllipse : fusEllipse.getRawList()) {
        if (rawEllipse.isVisibleRaw() && !show) {
          map.removeMapShape(rawEllipse.getPolylineObject());
          rawEllipse.setVisibleRaw(false);
          polylineArray.remove(rawEllipse.getPolylineObject());
        } else if(!rawEllipse.isVisibleRaw() && show){
          showEllipse(rawEllipse, rawEllipse.getEllipseColor(), rawEllipse.getStroke());
          ellipseSetOnClick(rawEllipse);
          rawEllipse.setVisibleRaw(true);
        }
      }
    }
  }

  public void selectionInitialClusteringAction(){
    List<CovarianceEllipse> clickedEllipsesCov= new ArrayList<CovarianceEllipse>();
    for(VizualEllipse clickedEllipse: clickedEllipses)
      clickedEllipsesCov.add(EllipseRepresentationTranslation.fromVizualToCovariance(clickedEllipse));
    List<State> clusters= initialClustering(clickedEllipsesCov);

    if (clusters.size() < clickedEllipsesCov.size()){ // if some actual clustering happened
      //for(VizualEllipse clickedEllipse: clickedEllipses)
      //clickedEllipse.setVisible(false);
      clickedEllipses.clear();
      String color= colorGenerator();
      for(State cluster: clusters)
        showState(cluster, color);///change
    }
  }

  /*
    Misc functions:
   */

  public void ellipseSetOnClick(VizualEllipse ellipse) {
    com.lynden.gmapsfx.shapes.Polyline polyline= ellipse.getPolylineObject();

    // single left click:
    map.addUIEventHandler(polyline, UIEventType.click, jsObject -> {
      generateDataForTable(EllipseRepresentationTranslation.fromVizualToCovariance(ellipse));
      double newStroke=0;
      if(ellipse.isClicked())
        newStroke=ellipse.getStroke()/2;
      else
        newStroke=ellipse.getStroke()*2;

      map.removeMapShape(polyline);
      ellipse.ellipseToDraw(ellipse.getEllipseColor(), newStroke);
      showEllipse(ellipse, ellipse.getEllipseColor(), newStroke);
      ellipseSetOnClick(ellipse);///check
      ellipse.setClicked(!ellipse.isClicked());
    });

    // hover:
    InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
    infoWindowOptions.content("<h2>MetaData</h2>"
            + "Current Location:"+ellipse.getLatLong() +"<br>"
            + "*********" );
    InfoWindow polylineInfoWindow = new InfoWindow(infoWindowOptions);
    polylineInfoWindow.setPosition(ellipse.getLatLong());
    map.addUIEventHandler(polyline, UIEventType.mouseover, jsObject -> {
      polylineInfoWindow.open(map);
    });
    map.addUIEventHandler(polyline, UIEventType.mouseout, jsObject -> {
      polylineInfoWindow.close();
    });

    //right click:
    map.addUIEventHandler(polyline, UIEventType.rightclick, jsObject -> {
      if(ellipse.isFusionEllipse()) {
        for (VizualEllipse ellipseInRaw : ellipse.getRawList()) {
          if (!ellipseInRaw.isVisibleRaw()) {

            map.addMapShape(ellipseInRaw.getPolylineObject());
            ellipseInRaw.setVisibleRaw(true);
          } else {
            map.removeMapShape(ellipseInRaw.getPolylineObject());
            ellipseInRaw.setVisibleRaw(false);
          }
        }
      }
    });


    /*ellipse.setOnMousePressed(event -> {
      switch (event.getClickCount()) {
        case 1:
          if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (ellipse.getStrokeWidth() < ellStrokeWidthClicked - 0.1) {                  //was not clicked/chosen
              generateDataForTable(EllipseRepresentationTranslation.fromVizualToCovariance(ellipse));
              ellipseSetClicked(ellipse);
            }
            else{                                          //was clicked/chosen
              dataTable.setVisible(false);
              ellipseSetUnclicked(ellipse);
            }
          }
          break;
        case 2:
          System.out.println("Two clicks");
          if (ellipse.getIsFusionEllipse()) {
            Iterator<Ellipse> itr = ellipse.getRaw().iterator();
            while (itr.hasNext()) {
              Ellipse temp = itr.next();
              if (temp.isVisible()) {
                temp.setVisible(false);
              } else {
                temp.setVisible(true);
              }
            }
          }
          break;
      }
    });*/


//
  }//ellipseSetOnClick

  public void ellipseSetClicked(VizualEllipse ellipse) {
    double currFill = rawEllFillOpacityClicked;
    /*
    if (ellipse.getIsFusionEllipse()) {
      currFill = fusEllFillOpacityClicked;
    }

    Color clF = (Color) ellipse.getFill();
    if (ellipse.getStrokeWidth() < ellStrokeWidthClicked - 0.1) {
      clickedEllipses.add(ellipse);
      ellipse.setFill(new Color(clF.getRed(), clF.getGreen(), clF.getBlue(), currFill));
      ellipse.setStrokeWidth(ellStrokeWidthClicked);
    }*/
  }

  public void ellipseSetUnclicked(VizualEllipse ellipse) {
    /*
    double currFill = rawEllFillOpacityUnClicked;
    if (ellipse.getIsFusionEllipse()) {
      currFill = fusEllFillOpacityUnClicked;
    }

    Color clF = (Color) ellipse.getFill();
    if (ellipse.getStrokeWidth() >= ellStrokeWidthClicked - 0.1) {
      clickedEllipses.remove(ellipse);
      ellipse.setFill(new Color(clF.getRed(), clF.getGreen(), clF.getBlue(), currFill));
      ellipse.setStrokeWidth(ellStrokeWidthUnClicked);
    }*/
  }


  public void clearScreen() { /* TODO Add lists to clear*/
    for (com.lynden.gmapsfx.shapes.Polyline polyline : polylineArray){
      map.removeMapShape(polyline);
    }
    for (VizualEllipse fusEllipse : fusEllipseList) {
      fusEllipse.setVisibleRaw(false);
    }
    clickedEllipses.clear();
    dataTable.setVisible(false);
  }

  public void showPointInTime(PointInTime p) {
    //map.setZoom(map.getZoom()+3);
    //map.setCenter();
    //VizualEllipse ell= p.getTrackList().get(0).getStateList().get(0).getFusionEllipse();
    Iterator<Track> trackIterator = p.getTrackList().iterator();
    while (trackIterator.hasNext()) {
      Track currTrack = trackIterator.next(); //colorByTrackIdTable
      Iterator<State> stateIterator = currTrack.getStateList().iterator();
      if (!colorByTrackIdTable.containsKey(currTrack.getId()))
        colorByTrackIdTable.put(currTrack.getId(), colorGenerator());
      while (stateIterator.hasNext()) {
        showState(stateIterator.next(), colorByTrackIdTable.get(currTrack.getId()));
      }
    }
  }
  public void showSensors(ArrayList<Sensor> sensors){
    for(Sensor sensor : sensors){
      org.jscience.geography.coordinates.UTM c= org.jscience.geography.coordinates.UTM.valueOf(36, 'N', sensor.getxCoordinate(), sensor.getyCoordinate(), SI.METRE);
      org.jscience.geography.coordinates.LatLong centerPTemp= utmToLatLong(c, WGS84);

      LatLong centerP= new LatLong(centerPTemp.getCoordinates()[1], centerPTemp.getCoordinates()[0]);
      MVCArray polylineArray = EllipseBuilder.buildEllipsePoints(centerP, 30, 30, 0,3);

      PolylineOptions polylineOptions=new PolylineOptions().path(polylineArray).strokeColor("#BA55D3").clickable(true).strokeWeight(3);
      com.lynden.gmapsfx.shapes.Polyline polyline = new com.lynden.gmapsfx.shapes.Polyline(polylineOptions);

      map.addMapShape(polyline);
    }
  }

  public void showState(State state, String color) {
    CovarianceEllipse fusEll = state.getFusionEllipse();
    VizualEllipse tempFusEllipse= null;
    if (fusEll != null) {
      tempFusEllipse = EllipseRepresentationTranslation.fromCovarianceToVizual(fusEll);
      tempFusEllipse.setFusionEllipse(true);
      fusEllipseList.add(tempFusEllipse);
    }
    List<CovarianceEllipse> CovarianceEllipseArray = state.getEllipseList();
    Iterator<CovarianceEllipse> covEllItr = CovarianceEllipseArray.iterator();
    while (covEllItr.hasNext()) {
      CovarianceEllipse tempCovEllipse = covEllItr.next();
      VizualEllipse tempEllipse = EllipseRepresentationTranslation.fromCovarianceToVizual(tempCovEllipse);
      tempEllipse.ellipseToDraw(color,tempEllipse.getStroke());
      if(showHideRawButton.isSelected()){
        showEllipse(tempEllipse, color,tempEllipse.getStroke());
        ellipseSetOnClick(tempEllipse);
      }

      if (tempFusEllipse != null){
        tempFusEllipse.ellipseToDraw(color,tempFusEllipse.getStroke());
        tempFusEllipse.addToRaw(tempEllipse);
        showEllipse(tempFusEllipse, color, tempFusEllipse.getStroke());
        ellipseSetOnClick(tempFusEllipse);
      }
    }
  }

  public void showEllipse(VizualEllipse ellipse, String color, double stroke) {
    ellipse.setVisible(true);
    com.lynden.gmapsfx.shapes.Polyline polyline= ellipse.getPolylineObject();
    map.addMapShape(polyline);
    polylineArray.add(polyline);
    clickedEllipses.add(ellipse); // TEMPORARY

    /*
    Tooltip tooltip= new Tooltip("Bla\nBla");
    Tooltip.install(ellipse, tooltip);

    ellipse.getStrokeDashArray().addAll(5d, 12d);
    if (ellipse.getIsFusionEllipse()) {
      color = new Color(color.getRed(), color.getGreen(), color.getBlue(), fusEllFillOpacityUnClicked);
      ellipse.getStrokeDashArray().clear();
    }
    ellipse.setFill(color);
    ellipse.setStroke(new Color(color.getRed(), color.getGreen(), color.getBlue(), ellipse.getStroke));
    ellipse.setStrokeWidth(ellStrokeWidthUnClicked);
    viewArea.getChildren().addAll(ellipse);*/
  }

  public static String colorToRGBCode(Color color){
    return String.format( "#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
  }

  public String colorGenerator() {
    Random rand = new Random();
    float r = rand.nextFloat();
    float g = rand.nextFloat();
    float b = rand.nextFloat();
    Color color= new Color(r, g, b, rawEllFillOpacityUnClicked);
    return colorToRGBCode(color);
  }

  /**
   Slider initialization:
   initializes the slider with the current number of points in time.
   If param max is zero then initialize to empty slider
   */
  private void sliderInit(int max) {
    slider.setMin(0);
    slider.setMax(max);
    String numOfPoints = String.valueOf(max);
    textFieldTimeCount.setDisable(false);
    textFieldTimeCount.setText("0"+" / "+numOfPoints);
    slider.setShowTickMarks(true);
    slider.setShowTickLabels(false);
    slider.setValue(0);
    if (max == 0)
      slider.setShowTickLabels(false);
    else
      slider.setShowTickLabels(false);

    Label label = new Label();
    Popup popup = new Popup();
    popup.getContent().add(label);
    double offset = 1 ;

    /*
       mouse hover option
    */
    slider.setOnMouseMoved(e -> {
      NumberAxis axis = (NumberAxis) slider.lookup(".axis");
      Point2D locationInAxis = axis.sceneToLocal(e.getSceneX(), e.getSceneY());
      double mouseX = locationInAxis.getX() ;
      double value = axis.getValueForDisplay(mouseX).doubleValue() ;
      int roundValue = (int) Math.round(value);
      if (value >= slider.getMin() && value <= slider.getMax()) {
        label.setText("    TimeStamp: "+ pointInTimeArray.get(roundValue-1).getTimeStamp());
      } else {
        label.setText("    TimeStamp: ---");
      }
      popup.setAnchorX(e.getScreenX());
      popup.setAnchorY(e.getScreenY() + offset);
    });

    slider.setOnMouseEntered(e -> popup.show(slider, e.getScreenX(), e.getScreenY() + offset));
    slider.setOnMouseExited(e -> popup.hide());

    /*
       change in the textField option
    */
    textFieldTimeCount.setOnKeyPressed(event -> {
      //the user changed the text and pressed ENTER
      if(event.getCode() == KeyCode.ENTER){
        String enteredText = textFieldTimeCount.getText();
        //check if the entered text is legal (in bounds)
        if(Integer.parseInt(enteredText) <= max && Integer.parseInt(enteredText) >=0 ) {
          int currentPoint = Integer.parseInt(enteredText);
          textFieldTimeCount.setText(enteredText+" / "+numOfPoints);
          forwardOrBackward(currentPoint,(pointInTimeArrayIndex+1));
        }
      }
    });

    /*
       change the slider point by mouse click option
    */
    slider.setOnMouseClicked(event -> {
      int currentPoint = ((int) slider.getValue());
      if(currentPoint <= max && currentPoint >=0 )
        forwardOrBackward(currentPoint,(pointInTimeArrayIndex+1));
    });

    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      int roundValue =(int) Math.round(slider.getValue());
      slider.setValue(roundValue);
      String timeCount = String.valueOf(roundValue);
      textFieldTimeCount.setText(timeCount+" / "+numOfPoints);
    });
  }

  /*
     check if we need to go backward of forward
   */
  private void forwardOrBackward(int currentPoint, int lastPoint){
    if (currentPoint > lastPoint) {
      int diff = currentPoint - lastPoint;
      forwardAction(diff);
    }
    else if (currentPoint < lastPoint) {
      int diff = lastPoint - currentPoint;
      backwardAction(diff);
    }
  }

  /*
    Table related code start
  */
  private void tableInit(){
    dataRoot = new TreeItem<>("Data:");
    dataRoot.setExpanded(true);

    metadataRoot = new TreeItem<>("Metadata:");
    metadataRoot.setExpanded(true);

    TreeItem<String> root = new TreeItem<>("Root");
    root.setExpanded(true);
    root.getChildren().setAll(dataRoot, metadataRoot);

    dataTable.setRoot(root);
    tableHideHeader(dataTable);

    dataTableNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Map, String> p) -> {
      TreeItem treeItem = p.getValue();
      Object treeValue = treeItem.getValue();
      String value = null;
      if (treeValue instanceof Map){
        Map map = (Map)treeValue;
        value = (String)map.get(nameColKey);
      }
      else
        value = treeValue.toString();
      return new ReadOnlyStringWrapper(value);
    });

    dataTableValueCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Map, String> p) -> {
      TreeItem treeItem = p.getValue();
      Object treeValue = treeItem.getValue();
      String value = null;
      if (treeValue instanceof Map){
        Map map = (Map)treeValue;
        value = (String)map.get(valueColKey);
      }
      return new ReadOnlyStringWrapper(value);
    });
  }//tablesInit

  private void tableHideHeader(TreeTableView table){
    table.widthProperty().addListener((source, oldWidth, newWidth) -> {
      Pane header = (Pane) table.lookup("TableHeaderRow");
      if (header.isVisible()){
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
      }
    });
  }


  private void generateEllipseDataForTable(CovarianceEllipse ellipse) {
    // Data Part:
    Map<String, String> ellipseId = new HashMap<>();
    ellipseId.put(nameColKey, "ID:");
    ellipseId.put(valueColKey, Long.toString(ellipse.getId()));

    Map<String, String> ellipseTime = new HashMap<>();
    ellipseTime.put(nameColKey, "Timestamp:");
    ellipseTime.put(valueColKey, Long.toString(ellipse.getTimeStamp()));

    Map<String, String> ellipseX = new HashMap<>();
    ellipseX.put(nameColKey, "X Axis:");
    ellipseX.put(valueColKey, Double.toString(ellipse.getCentreX()));

    Map<String, String> ellipseY = new HashMap<>();
    ellipseY.put(nameColKey, "Y Axis:");
    ellipseY.put(valueColKey, Double.toString(ellipse.getCentreY()));

    dataRoot.setValue("Ellipse Data:");
    dataRoot.getChildren().setAll(new TreeItem(ellipseId), new TreeItem(ellipseX), new TreeItem(ellipseY));

    // Metadata Part:
    Map<String, String> ellipseColor = new HashMap<>();
    ellipseColor.put(nameColKey, "Is Fused:");
    ellipseColor.put(valueColKey, Boolean.toString(ellipse.getIsFusionEllipse()));

    VizualEllipse taggedEllipse= EllipseRepresentationTranslation.fromCovarianceToVizual(ellipse);
    Map<String, String> ellipseRadX = new HashMap<>();
    ellipseRadX.put(nameColKey, "Radius X:");
    ellipseRadX.put(valueColKey, Double.toString(taggedEllipse.getRadiusX()));

    Map<String, String> ellipseRadY = new HashMap<>();
    ellipseRadY.put(nameColKey, "Radius Y:");
    ellipseRadY.put(valueColKey, Double.toString(taggedEllipse.getRadiusY()));

    Map<String, String> ellipseAngle = new HashMap<>();
    ellipseAngle.put(nameColKey, "Angle:");
    ellipseAngle.put(valueColKey, Double.toString(taggedEllipse.getAngle()));

    metadataRoot.setValue("Ellipse Metadata:");//111
    metadataRoot.getChildren().setAll(new TreeItem(ellipseColor), new TreeItem(ellipseRadX), new TreeItem(ellipseRadY),
            new TreeItem(ellipseAngle));
  }

  private void generateDataForTable(Object item){
    dataTable.setVisible(false);

    if (item instanceof CovarianceEllipse){
      generateEllipseDataForTable((CovarianceEllipse)item);
      dataTable.setVisible(true);
    }

  }
  /*
    Table related code end
  */




  /*
    tree related code start
   */

  /**
   * On-click settings of the tree cells are here
   */
  private void treeInit() {
    //init root (for tree):
   /* root = new TreeItem<>(new TreeItemContainer());
    root.setExpanded(true);

    //init tree:
    tree = new TreeViewWithItems(root);
    tree.setShowRoot(false);

    //set tree and add tree to GUI
    tree.prefWidthProperty().bind(treeArea.widthProperty());    //bound tree width to parent width
    tree.prefHeightProperty().bind(treeArea.heightProperty());  //bound tree height to parent height
    tree.setEditable(true);
    treeArea.getChildren().add(tree);

    //set cell factory for tree
    this.tree.setCellFactory(new Callback<TreeView<HierarchyData>, TreeCell<HierarchyData>>() {
      @Override
      public TreeCell<HierarchyData> call(TreeView<HierarchyData> p) {
        return new NameIconAddCell();
      }
    });

    //set actions for selecting a node on tree
    this.tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            TreeItem<HierarchyData> selectedTreeNode = (TreeItem<HierarchyData>) newValue;
            TreeItemContainer newItemContainer = (TreeItemContainer) selectedTreeNode.getValue();
            selectedItemContainer = newItemContainer;
            Object newItem= newItemContainer.getContainedItem();

            unclickAll();
            recursiveTreeClick(newItemContainer);
            generateDataForTable(newItem);
          }
        }//new ChangeListener(){
    );//addListener(
*/
    treeItems = FXCollections.observableList(new ArrayList<HierarchyData>()); //init data source for tree
    //tree.setItems(treeItems);                                                //bind tree with data source (items)

  }//treeInit

  /**
   * Modify TreeCell class for cell-factory of the tree
   *
   * On-create settings of the tree cells are here
   */
  private final class NameIconAddCell extends TreeCell<HierarchyData> {
    private ContextMenu defaultTreeContextMenu = new ContextMenu();

    public NameIconAddCell() {
      //make default context menu for tree nodes:
      MenuItem defaultTreeContextMenuShowItem = new MenuItem("Show");
      MenuItem defaultTreeContextMenuHideItem = new MenuItem("Hide");
      defaultTreeContextMenuHideItem.setDisable(true);
      defaultTreeContextMenu.getItems().addAll(defaultTreeContextMenuShowItem, defaultTreeContextMenuHideItem);
      //set actions for default context menu:
      defaultTreeContextMenuShowItem.setOnAction(e -> {
        defaultTreeContextMenuShowItem.setDisable(true);
        defaultTreeContextMenuHideItem.setDisable(false);
        recursiveShowItemContainer(selectedItemContainer);
      });
      defaultTreeContextMenuHideItem.setOnAction(e -> {
        defaultTreeContextMenuShowItem.setDisable(false);
        defaultTreeContextMenuHideItem.setDisable(true);
        recursiveHideItemContainer(selectedItemContainer);
      });
    }//NameIconAddCell

    @Override
    public void updateItem(HierarchyData item, boolean empty) {
      super.updateItem(item, empty);

      if (empty) { //cell is empty and not being rendered (for example collapsed parent):
        setText(null);
        setContextMenu(null);
        //setGraphic(null);
      } else { //cell is filled, specific settings depends on the class of the item represented by the cell:
        //setGraphic(getItem().getNode());   //if we use CSS, not needed
        setText(getItem().toString());
        setContextMenu(defaultTreeContextMenu);

        TreeItemContainer itemTmp = (TreeItemContainer) item;
        Object containedItem = itemTmp.getContainedItem();
        if (containedItem instanceof CovarianceEllipse) {
          CovarianceEllipse ell = (CovarianceEllipse) containedItem;
          if (ell.getIsFusionEllipse()) {
            setId("fusell-cell");
          } else {
            setId("rawell-cell");
          }
        } else if (containedItem instanceof State) {
          setId("state-cell");
        } else if (containedItem instanceof Track) {
          setId("track-cell");
        } else if (containedItem instanceof PointInTime) {
          setId("point-cell");
        } else {
          setId("def-cell");
        }


      }//else (cell is filled)
    }//updateItem

  }//NameIconAddCell

  private void fillTreeItemsFromJson(ArrayList<PointInTime> listOfPoints, ObservableList<HierarchyData> treeData) {
    Iterator<PointInTime> pointIterator = listOfPoints.iterator();
    while (pointIterator.hasNext()) {
      PointInTime currPoint = pointIterator.next();
      TreeItemContainer currPointItem = new TreeItemContainer(currPoint);  /*1*/
      Iterator<Track> trackIterator = currPoint.getTrackList().iterator();
      while (trackIterator.hasNext()) {
        Track currTrack = trackIterator.next();
        if (!colorByTrackIdTable.containsKey(currTrack.getId())) {
          colorByTrackIdTable.put(currTrack.getId(), colorGenerator());
        }
        TreeItemContainer currTrackItem = new TreeItemContainer(currTrack);  /*2*/
        currTrackItem.setColorId(currTrack.getId());
        Iterator<State> stateIterator = currTrack.getStateList().iterator();
        while (stateIterator.hasNext()) {
          State currState = stateIterator.next();
          TreeItemContainer currStateItem = new TreeItemContainer(currState);  /*3*/
          currStateItem.setColorId(currTrack.getId());
          CovarianceEllipse currFusEl = currState.getFusionEllipse();
          if (currFusEl != null){
            currFusEl.setIsFusionEllipse(true);
            TreeItemContainer currFusElItem = new TreeItemContainer(currFusEl);  /*4.0*/
            currFusElItem.setColorId(currTrack.getId());
            currStateItem.getChildren().add(currFusElItem);                      /*4.0*/
          }
          Iterator<CovarianceEllipse> ellipseIterator = currState.getEllipseList().iterator();
          while (ellipseIterator.hasNext()) {
            CovarianceEllipse currEllipse = ellipseIterator.next();
            TreeItemContainer currEllipseItem = new TreeItemContainer(currEllipse); /*4.1*/
            currEllipseItem.setColorId(currTrack.getId());
            currStateItem.getChildren().add(currEllipseItem);                       /*4.1*/
          }//no more ellipses in current state
          currTrackItem.getChildren().add(currStateItem); /*3*/
        }//no more states in current track
        currPointItem.getChildren().add(currTrackItem);/*2*/
      }//no more tracks in current point
      treeData.add(currPointItem); /*1*/
    }//no more points in list
  }//fillTreeItemsFromJson


  private void recursiveShowItemContainer(TreeItemContainer itemContainer) {
    Object item = itemContainer.getContainedItem();

    if (item instanceof CovarianceEllipse) {
      showEllipseContainer(itemContainer);
    }

    ObservableList<HierarchyData> childrenList = itemContainer.getChildren();
    if (childrenList.size() > 0) {
      Iterator<HierarchyData> childrenListIterator = childrenList.iterator();
      while (childrenListIterator.hasNext()) {
        recursiveShowItemContainer((TreeItemContainer) childrenListIterator.next());
      }
    }
  }

  private void showEllipseContainer(TreeItemContainer ellipseContainer) {
    CovarianceEllipse covEllipse = (CovarianceEllipse) ellipseContainer.getContainedItem();
    if (ellipseContainer.getContainedGraphicItem() == null) {
      VizualEllipse ellipse = EllipseRepresentationTranslation.fromCovarianceToVizual(covEllipse);

      //showEllipse(ellipse, colorByTrackIdTable.get(ellipseContainer.getColorId()));
      showEllipse(ellipse, "blue",ellipse.getStroke());  ////change
      ellipseSetOnClick(ellipse);
      //ellipseSetClicked(ellipse, covEllipse);
      ellipseContainer.setContainedGraphicItem(ellipse);
    }
  }


  private void recursiveHideItemContainer(TreeItemContainer itemContainer) {
    Object item = itemContainer.getContainedItem();

    if (item instanceof CovarianceEllipse) {
      hideEllipseContainer(itemContainer);
    }

    ObservableList<HierarchyData> childrenList = itemContainer.getChildren();
    if (childrenList.size() > 0) {
      Iterator<HierarchyData> childrenListIterator = childrenList.iterator();
      while (childrenListIterator.hasNext()) {
        recursiveHideItemContainer((TreeItemContainer) childrenListIterator.next());
      }
    }
  }

  private void hideEllipseContainer(TreeItemContainer ellipseContainer) {
    VizualEllipse ellipse = (VizualEllipse) ellipseContainer.getContainedGraphicItem();
    if (ellipse != null) {
      //viewArea.getChildren().remove(ellipse);
      clickedEllipses.remove(ellipse);
      ellipseContainer.setContainedGraphicItem(null);
    }
  }


  private void recursiveTreeClick(TreeItemContainer itemContainer) {
    Object item = itemContainer.getContainedItem();
    Object graphicItem = itemContainer.getContainedGraphicItem();
    ObservableList<HierarchyData> childrenList = itemContainer.getChildren();

    if (item instanceof CovarianceEllipse) {
      if (graphicItem != null) {
        VizualEllipse ellipse = (VizualEllipse) graphicItem;
        ellipseSetClicked(ellipse);
      }
    }

    boolean tmoBool = true;
    item = selectedItemContainer.getContainedItem();
    if (item instanceof CovarianceEllipse) {
      CovarianceEllipse covEllipse = (CovarianceEllipse) item;
      if (covEllipse.getIsFusionEllipse()) {
        tmoBool = false;
      }
    }

    if (childrenList.size() > 0 && tmoBool) {
      Iterator<HierarchyData> childrenListIterator = childrenList.iterator();
      while (childrenListIterator.hasNext()) {
        recursiveTreeClick((TreeItemContainer) childrenListIterator.next());
      }
    }
  }

  private void unclickAll() {
    ArrayList<VizualEllipse> toUnclickList = new ArrayList<VizualEllipse>();

    Iterator<VizualEllipse> clickedEllipseIterator = clickedEllipses.iterator();
    while (clickedEllipseIterator.hasNext()) {
      VizualEllipse toUnclick = clickedEllipseIterator.next();
      if (clickedEllipses.contains(toUnclick)) {
        toUnclickList.add(toUnclick);
      }
    }

    Iterator<VizualEllipse> toUnclickIterator = toUnclickList.iterator();
    while (toUnclickIterator.hasNext()) {
      ellipseSetUnclicked(toUnclickIterator.next());
    }

    clickedEllipses.clear();
  }

  /*
    tree related code end
   */


}//MainContainerController