import org.json.*;

String srcCluster = "http://staging.londondroids.com/fom/index.php/cluster";

int width = 800;
int height = 600;


void setup()
{
  size( width, height );
  background( #FFFFFF );
  
  int cluster_num = getCluster();
  int w_slice = width / ( cluster_num + 1 );
  int h_slice = height / 2;
  
  Cluster test = new Cluster( 100, 100, 20, 20 );
  test.draw();
  
  for( int i = 0; i < cluster_num; i++ ) {
    int x = w_slice + i * w_slice;
    int y = h_slice;
    float gray = 255 * ((float)i / (float)cluster_num);
    fill( gray );
    noStroke();
    ellipse( x, y, 20, 20 );
  }
};

void draw()
{
};

int getCluster()
{
  String request = srcCluster + "/read/1/json";
  JSONArray result = new JSONArray();
  try {
    result = new JSONArray(join( loadStrings( request ), ""));
    for( int i = 0; i < result.length(); i++ ) {
      JSONObject obj = result.getJSONObject( i );
      println( "id = " + obj.get( "id_cluster" ) );
    }
  } catch(Exception e) {
    println("Error while loading cluster objects");
  }
  return result.length();
};

class Cluster {
  private int x;
  private int y;
  private float sx;
  private float sy;
  
  Cluster( int x, int y, float sx, float sy) {
    this.x = x;
    this.y = y;
    this.sx = sx;
    this.sy = sy;
  }

  void draw() {
    noStroke();
    fill( #ff0000, 100 );
    ellipse( x, y, sx, sy );
  }

  void setX( int x ) {
    this.x = x;
  }
  int getX() {
    return x;
  }

  void setY( int y ) {
    this.y = y;
  }
  int getY() {
    return y;
  }

  void setSx( float sx ) {
    this.sx = sx;
  }
  float getSx() {
    return sx;
  }

  void setSy( float sy ) {
    this.sy = sy;
  }
  float getSy() {
    return sy;
  }
}

