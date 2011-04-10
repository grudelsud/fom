import org.json.*;

String clusterUrl = "http://localhost/";

int width = 800;
int height = 600;


void setup()
{
  size( width, height );
  background( #FFFFFF );
  
  int cluster_num = getTerms();
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

ArrayList getTerms()
{
  String request = clusterUrl;
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

class Term {
  private float lat_m;
  private float lon_m;
  private String term;
  
  Term( float lat_m, float lon_m, String term ) {
    this.lat_m = lat_m;
    this.lon_m = lon_m;
    this.term = term;
  }
  void draw() {
  }
}

