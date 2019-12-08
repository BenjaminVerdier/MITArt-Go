package com.example.mitartgo

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


fun getBitmapFromURL(src: String?): Bitmap? {
    return try {
        val url = URL(src)
        val connection: HttpURLConnection = url
            .openConnection() as HttpURLConnection
        connection.setDoInput(true)
        connection.connect()
        val input: InputStream = connection.getInputStream()
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}



class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback

    private lateinit var markers: MutableCollection<Marker>
    //temp


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        var userCol : MutableList<MutableMap<String,String>> = mutableListOf()
        private lateinit var artPieces : MutableList<MutableMap<String,String>>
        fun addArtPiece(title: String) {
            for (artpiece in artPieces) {
                if (artpiece["title"] == title) {
                    userCol.add(artpiece)
                    return
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        buttoncollection.setOnClickListener{
            val intent = Intent(this, CollectionActivity::class.java)
            startActivity(intent)
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.maplayout));
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }

        mMap.uiSettings.setMapToolbarEnabled(false);
        mMap.uiSettings.isMyLocationButtonEnabled = true;
        mMap.uiSettings.isZoomControlsEnabled = true
        //mMap.uiSettings.isScrollGesturesEnabled = false
        gatherArtPieces()
        mMap.setOnMarkerClickListener(this);
    }

    private fun GetMapOfArtPieces() : MutableList<MutableMap<String,String>>{

        //temporary VERY dirty code
        val map = mutableListOf<MutableMap<String,String>>()

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Bakalar-TVMan-1.jpg", "title" to "TV Man or Five Piece Cube with Strange Hole", "artist" to "David Bakalar", "date" to "1993", "medium" to "Mountain rose granite", "size" to "82 in. x 57 in. x 105 in. (208.28 cm x 144.78 cm x 266.7 cm)", "credit" to "Gift of the Artist", "lat" to "42.355659", "long" to "-71.097839", "location" to "Hayden Memorial Library, 160 Memorial Dr, Cambridge, MA 02142", "description" to "Created from both conventional and innovative materials such as bronze, granite, marble, aluminum and stainless steel, David Bakalar’s abstract, organic forms show the influence of Constructivism as well as Surrealism as well as the artist’s affinity for biology and science.Their harmoniously arranged geometric forms suggest natural systems and manifest an underlying potential and kinetic energy. MIT’s TV Man or Five Piece Cube with Strange Hole incorporates the negative space surrounding the work as a primary component. A gift to the Institute in 1993, Bakalar’s composition of five pieces of mountain granite appears uniquely anthropomorphic in form." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Bertoia_Altarpiece%20for%20MIT%20Chapel3.jpg", "title" to "Altarpiece for MIT Chapel", "artist" to "Harry Bertoia", "date" to "1955", "medium" to "Brazed steel", "size" to "240 in. (609.6 cm)", "credit" to "Commissioned for Eero Saarinen Chapel, MIT", "lat" to "42.358421", "long" to "-71.093681", "location" to "MIT Chapel, 48 Massachusetts Ave, Cambridge, MA 02139", "description" to "Bertoia’s altarpiece screen, or reredos, was commissioned for Eero Saarinen’s early modernist, non-denominational MIT Chapel in 1955. Suspended over the main altar, his cascading, open fret screen of slim metal rods and crossplates scatters light throughout the chapel. Described as one of Bertoia’s most striking works, it is an integral part of the altar. Here, Bertoia has liberated sculpture from its base to usher in the contemporary era of spatial sculpture." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Boyce_Through%20the%20Layers%20a%20copy.jpg", "title" to "Through Layers and Leaves (Closer and Closer)", "artist" to "Martin Boyce", "date" to "2011", "medium" to "Galvanized mild steel, epoxy paint, and acid etched brass", "size" to "117.6 in. x 1186.2 in. (298.7 cm x 3012.95 cm)", "credit" to "Commissioned with MIT Percent-for-Art Funds and a generous gift from the Robert D. Sanders (‘64) and Sara-Ann Sanders Family", "lat" to "42.362652", "long" to "-71.089783", "location" to "Koch Institute For Integrative Cancer Research at MIT, 500 Main St, Cambridge, MA 02142", "description" to "Martin Boyce’s works allude to the ideals of modernist design and architecture, a legacy inhabited by the dream of a better world. Drawing clues from the visual language and fabrication of iconic modernist designs, Boyce’s work investigates how modernism’s utopian promises have vanished while leaving behind a style of forms. At the same time his work gestures towards an alternative life these forms might lead if separated from their modernist context. Boyce’s installations often stage the outdoors within the gallery space, evoking an entire landscape through a few carefully chosen details such as neon lights, wire fencing, and ventilation grills. The installation Through Layers and Leaves (Closer and Closer) continues Martin Boyce’s sculptural investigations of modernist design history, its visual language, and imagined alternative narratives. A photograph of four concrete trees found in a book on French Modernist gardens has emerged as a particularly important reference for Boyce. These concrete trees created by cubist sculptors Jan and Joël Martel were commissioned for the 1925 Exposition des arts Décoratifs et Industriels Modernes in Paris. From the shape of these trees Boyce has constructed a pattern of quadrilateral forms captured in a galvanized stainless steel wall frame. The steel frame is installed on the wall as if it were emerging from the original structure of the building. A number of perforated, painted steel panels set at angles in the frame serve as leaf-like forms. The color palette for the panels is elicited from the distinctive colors in Art Deco designs. Boyce deploys colors in the palette into two hues, one brighter and the other paler, thereby bringing a specific moment of the modernist past into the present. The quadrilateral forms of the wall frame are repeated on three brass wall grills placed low near the floor that contain text that reads \"closer\" \"and\" \"closer.\" The entire work spans the wall of the lobby of MIT’s Koch Institute for Integrative Cancer Research and operates in the interstices between art, architecture, and design, between the space of a public sculpture, and the privacy of a domestic realm. Boyce relates the new work specifically to the research practice of the building, as he has spoken of it in relation to the significance of recognizing patterns and relative scale in research." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Calder%3A09complete1%20copy.jpg", "title" to "La Grande Voile (The Big Sail)", "artist" to "Alexander Calder", "date" to "1965", "medium" to "Painted steel", "size" to "480 in. (1219.2 cm)", "credit" to "Gift of Mr. and Mrs. Eugene McDermott", "lat" to "42.373020", "long" to "-71.081207", "location" to "McDermott Court Cambridge, MA 02142", "description" to "Calder is best known for his development of sculptures in motion, known as \"mobiles.\" A second major mode in his work was the \"stabile,\" a stable sculpture that rests on the ground. The sculptor Jean Arp, whose biomorphic vocabulary resonated with Calder’s, own, coined the term stabile in order to contrast them to Calder’s mobiles. Calder’s first large stabiles date from the mid-1930s but it was not until the late 1950s and 1960s that they began to attain the colossal scale of MIT’s La Grande Voile (The Big Sail). In such works, Calder wanted to achieve a massiveness of form and scale without sacrificing the lightness and sense of motion of his mobiles. The stabiles establish as few points of contact with the ground as possible. The subtle interplays between straight and sinuous edges and between flat and curved planes and the appendages jutting out from the framework communicate an epic quality and a soaring dynamism. The large stabiles provide changing experiences of space as the spectator moves around and through them. Sudden angles and planes slice, divide, and mold the space into varied shapes. Calder wrote: \"When I use two or more sheets of metal cut into shapes and mounted at angles to each other, I feel that there is a solid form, perhaps concave, perhaps convex, filling the dihedral angles between them. I do not have a definite idea of what this would be like, I merely sense it and occupy myself with the shapes one actually sees.\" Although Calder’s forms are purely abstract, their presence is strongly organic. Such stabiles as La Grande Voile suggest primeval beings, giant insects or birds raised up on their legs and spreading their wings in an impressive array of spars, blades, bolts, and rivets. The spirit of innovation is powerful in everything Calder made, as is a sense of gestural and dynamic energy. Calder’s working method involved making a small model that was then mathematically enlarged and fabricated in large scale under his supervision at an ironworks. Calder worked closely with the fabricators both in Tours, France, where La Grande Voile was made, and in Waterbury Connecticut, at the Segre Ironworks. The MIT sculpture weighs thirty-three tons and was installed in 1966. Smaller in scale, the intermediate model of La Grande Voile, given by Mr. and Mrs. Julius Stratton, belongs to the MIT Permanent Collection, and is sited at the MIT List Visual Arts Center’s  first floor atrium  lobby in the Weisner Building." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/AKclock5_2.jpg", "title" to "Against the Run", "artist" to "Alicja Kwade", "date" to "2019", "medium" to "Sculpture", "size" to "", "credit" to "MIT Collection Commissioned with MIT Percent-for-Art funds", "lat" to "42.360210", "long" to "-71.089980", "location" to "21 Ames St, Cambridge, MA 02142", "description" to "At first glance Against the Run, a freestanding clock with a restrained modern design sited on a patch of lawn adjacent to Richard Fleischner’s Upper Courtyard, figures seamlessly into the plaza’s built environment. The clock’s unusual feature becomes evident on closer inspection; although its minute and hour hands tell the correct time, the second-hand ticks counterclockwise one beat and then returns to the twelve o’clock position, while the clock’s face rotates to the left, one fraction every second. This jerky punctuation appears to jostle the entire dial counter-clockwise with each movement of the second hand—quite literally against the run of time. As a human-made system of measurement, units of time like days, hours, and minutes, are an inescapable aspect of contemporary life, and govern the pace of human activity. In both Against the Run and a thematically related series of sculptures, Zeitzonen (Time Zones), Alicja Kwade questions the basis of metrics that index and quantify time, addressing the peculiar bureaucracies that maintain their global standardization. Time zones, for instance, are strategically hewn along national borders and economic zones to advantage industrial production. Adherence to \"the time\" similarly regulates the schedules of urban and industrial workers. While the way we measure time is linear, Against the Run suspends the notion of its ever-forward movement, proposing alternate systems of timekeeping." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Moore%3ARecline2%20copy.jpg", "title" to "Reclining Figure (Working Model for Lincoln Center Sculpture)", "artist" to "Henry Moore", "date" to "1963", "medium" to "Bronze Sculpture", "size" to "96 in. x 138 in. x 68 in. (243.84 cm x 350.52 cm x 172.72 cm)", "credit" to "Gift of Vera Glaser List in memory of Samuel Glaser, Class of 1925", "lat" to "42.360649", "long" to "-71.087440", "location" to "20 Ames St, Cambridge, MA 02142", "description" to "Reclining Figure is a smaller-scale working model for the sculpture located at Lincoln Center in New York City (1963-65). (A third version is installed on the grounds of Henry Moore’s estate in Hertfordshire, England.) While the New York version is set within a reflecting pool, the MIT sculpture stands on a granite base, fully exposing the monumental legs. At the time of its commission, Reclining Figure was the largest work Moore had yet undertaken. In order to produce this sculpture in conditions that would best approximate outdoor light, Moore built an open-air garden studio and covered it with transparent plastic sheeting. He later used this studio for all his large sculptures intended for outdoor sites. Less gentle and rounded than MIT’s three-part figure, these severe and heavy elements indicate Moore’s abiding interest in natural forms, from bone fragments to ancient geological formations." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Disuvero.jpg", "title" to "Aesop’s Fables, II", "artist" to "Mark di Suvero", "date" to "2005", "medium" to "Painted steel", "size" to "142 in. x 420 in. x166 in. (360.68 cm x 1066.8 cm x 421.64 cm)", "credit" to "Made possible through the generosity of the artist, gifts from Vera G. List and the Family of Robert S. Sanders, MIT ‘64, and by MIT Percent-for-Art Funds for the Northeast Sector Landscape", "lat" to "42.359840", "long" to "-71.091250", "location" to "Ray and Maria Stata Center, 32 Vassar St, Cambridge, MA 02139", "description" to "Mark di Suvero is best known for his architecturally-scaled, abstract sculptures fabricated from industrial building materials. Created from steel plates and I-beams and painted red, di Suvero and his crew assembled Aesop’s Fables II on site using plasma cutters, welding torches, and cranes. Di Suvero does not conceal the bolted joints or welded areas of his sculptures, opting instead to emphasize his materials and methods of fabrication. Connected by a single I-beam, the two main components of Aesop’s Fables II are spatially and materially connected but they also are formally distinct. One component is composed of five interlocking I-beams, joined to form dynamic angles that seem to recall the gestural compulsions of abstract expressionism. The second component, which is built with curved steel plates, seems grounded in hard-edge geometric abstraction. Negative space and implied movement feature prominently in both components and the sprawling ground that they occupy. In Aesop’s Fables II, different shapes and spaces emerge from each viewing angle and distance, compelling viewers to consider their own position and size in relation to the surrounding architectural environment. A founding member of the New York’s Park Place Group and Socrates Sculpture Park, di Suvero has made enduring contributions to modern sculpture and has been a pioneering advocate for community-oriented and accessible public art. Aesop’s Fables II is installed on Hockfield Court, a site immediately adjacent to Frank Gehry’s Stata Center, which is vital to MIT and the surrounding community. The prominent location and striking form have rendered Aesop’s Fables II a Cambridge landmark." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Gehry_Ray%20and%20Maria%20Strata%20copy.jpg", "title" to "Ray and Maria Stata Center", "artist" to "", "date" to "2004", "medium" to "Architecture", "size" to "", "credit" to "", "lat" to "42.359840", "long" to "-71.091250", "location" to "32 Vassar St Cambridge, MA 02139", "description" to "Frank Gehry was born Frank Goldberg in Toronto, Canada in 1929. In 1954, he received his undergraduate degree in architecture from the University of Southern California, and later attended the Harvard Graduate School of Design, where he studied city planning. He worked in architectural firms including Victor Gruen Associates and Pereira and Luckman Associates in California and also for a year in the office of Andre Remondet in Paris. He opened his first practice in Santa Monica, California in 1962. In 1979 this practice was succeeded by the firm Gehry & Krueger Inc., and by Gehry Partners, LLP, in 2002. Gehry’s dramatic and individual style, often referred to as Post-Modern and deconstructed, evolved from domestic and commercial buildings to large institutional structures, functioning between architecture and sculpture. A few highlights of his many works are Venice Beach House, California; Gehry House, Santa Monica, California; Guggenheim Museum, Bilbao, Spain; Walt Disney Concert Hall, Los Angeles; Fishdance Restaurant, Kobe, Japan; Chiat/Day Office building, Venice, California; Dancing House, Prague; DG Bank Building, Berlin; and the Experience Music Project, Seattle, Washington. As a teacher, Gehry has held the Charlotte Davenport Professorship in Architecture at Yale University, the Eliot Noyes Chair at Harvard University, and was a visiting scholar at the Federal Institute of Technology in Zürich, Switzerland. He has been widely recognized and honored, receiving the Arnold W. Brunner Memorial Prize in Architecture; the Pritzker Architecture Prize; the Praemium Imperiale Award by the Japan Art Association; the National Medal of Arts; and the Gold Medal from the American Institute of Architects. He has been named a Fellow of the American Academy of Arts and Letters; trustee of the American Academy in Rome; a Fellow of the American Academy of Arts and Sciences; Academician by the National Academy of Design; Honorary Academician by the Royal Academy of Arts, London; and has been a member of the College of Fellows of the American Institute of Architects since 1974. Frank Gehry lives in Santa Monica and works from his offices in Los Angeles, California." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Vanderwarker%20pdv_7602%20%281%29_WEB.jpg", "title" to "Chord", "artist" to "Antony Gormley", "date" to "2015", "medium" to "905 stainless steel elements of varying section sizes and 541 stainless steel balls", "size" to "Overall size: 1706 x 321 x 341 cm", "credit" to "Commissioned with MIT Percent-for-Art Funds", "lat" to "42.359840", "long" to "-71.127550", "location" to "182 Memorial Dr, Cambridge, MA 02138", "description" to "Antony Gormley’s Chord (2015) comprises thirty-three polyhedrons welded together and reaching vertically from the floor of Building 2 to the skylight, four stories above. Light passes through the open spaces of the sculpture, and also reflects off the slender rods and nodes that form each unit. Yet, the seemingly delicate and airy form is, in fact, fashioned from 1700 pounds of polished stainless steel. The sculpture was installed on the occasion of the 100th anniversary of the building, which was designed by architect William Welles Bosworth and combines stylistic elements referencing both ancient Greek structures and twentieth-century industrial steel constructions. Chord is situated at the busy intersection of MIT’s Mathematics and Chemistry Departments and fills the open well of Bosworth’s austere staircase. Gormley’s irregular and dynamic polyhedral column counters the regular and even intervals of the stairs through which it ascends. In 2008, the artist began creating what he refers to as \"cellular polyhedral sculptures,\" works that reside within the tension between the organic and inorganic. Polyhedrons are abstract and geometric forms that occur in nature—manifesting, for example, as the segments of a turtle shell, or the hexagonal honeycombs of beehives. Polyhedral forms are also important in nanotechnology, serving as the architectural frames used to assemble synthetic DNA sequences. Chord, with its interplay of light and shadow, celebrates the crystalline and organic cellular structures that comprise the geometry of life. Chord was realized as a percent-for-art project in association with the renovations by Ann Beha Architects that were undertaken on Building 2, which comprises a part of MIT’s Main Group complex." ))

        map.add(mutableMapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Cai-Guo-Qiang-Ring-Stone-04_0.jpg", "title" to "Ring Stone", "artist" to "Cai Guo-Qiang", "date" to "2010", "medium" to "Granite, seven Japanese Black Pine trees", "size" to "95 in. x 422.5 in. x 95 in. (241.3 cm x 1073.15 cm x 241.3 cm)", "credit" to "Commissioned with MIT Percent-for-Art Funds and made possible by generous donations from the Annie Wong Art Foundation and the Robert D. (‘64) and Sara-Ann Sanders family", "lat" to "42.361160", "long" to "-71.082190", "location" to "100 Main St, Cambridge, MA 02142", "description" to "Ring Stone is composed of twelve individual but indivisible links cut from a 39 and one-half-foot-long single block of white granite weighing approximately 14 metric tons. The massive stone block was quarried from the caves of Zhangbanzhen, Hui An County, near the artist’s hometown of Quanzhou in southern China’s Fujian Province, which also is where artisan stonemasons carved the work. Five graceful Japanese Black Pine trees, reminiscent of images found in traditional Chinese landscape painting, are planted inside  the rings and another two pines reside nearby. MIT’s Ring Stone, which celebrates the Sloan School of Management’s educational and cultural ties with China, is both firmly fixed and ever changing. The interlocking, inseparable granite links form a chain, representing the individual’s relationship to society. The rings are simultaneously symbolic of both wholeness and emptiness; and while the stone timelessly grounds the work, the seven Japanese Black Pines will slowly grow over time and change with each season. The solid granite contrasts with the elegant branching of the pines, suggesting the enduring power of nature in a modern urban architectural space. The twelve inextricably linked rings refer to the twelve months in the Chinese lunar calendar as well as the twelve animals in the Chinese zodiac, each of which is associated with one of eight Buddhist patron deities. The pine trees, which retain their green foliage even in a severe winter, represent longevity and endurance in the face of adversity. Cai has placed Ring Stone in its location on the Sloan School lawn according to the exacting principles of feng shui, the Chinese understanding of how qi (energy) flows throughout the universe. The artist is a serious student of this complex belief system, which has been practiced in China since 1100 B.C. Based on the concept that man and nature must exist in harmony,feng shui incorporates the concept of yin and yang of balanced forces in every aspect of existence. Whenever anything in nature becomes too yin or too yang, it moves to become the opposite. Cai has studied extensively the use of feng shui in Chinese military history and architecture. He also has worked with officials in Mito, Japan, to analyze the city’s feng shui to develop solutions to unblock energy within the city’s circulation systems. In siting Ring Stone, Cai has used feng shui to bestow beneficial qi on the Sloan School by blocking the inauspicious energy created by traffic converging from Broadway and Main Street. Cai has stated that the MIT Percent-for-Art provided him a perfect platform to bring his first public work to a university campus, \" I have a close relationship with MIT. I was in residency at the List Visual Arts Center in 2003–04, so I am glad to have a chance to work with MIT for my new creation.\"" ))


        return map
    }

    private fun GetIconResourceFromTitle(title: String): Int{
        when (title) {
            "TV Man or Five Piece Cube with Strange Hole" -> return R.drawable.tvrock
            "Altarpiece for MIT Chapel" -> return R.drawable.chruch
            "Through Layers and Leaves (Closer and Closer)" -> return R.drawable.colorwalls
            "La Grande Voile (The Big Sail)" -> return R.drawable.metalsails
            "Against the Run" -> return R.drawable.clock
            "Reclining Figure (Working Model for Lincoln Center Sculpture)" -> return R.drawable.rockcurve
            "Aesop’s Fables, II" -> return R.drawable.redmetalthing
            "Ray and Maria Stata Center" -> return R.drawable.weirdbuilding
            "Chord" -> return R.drawable.whitestring
            "Ring Stone" -> return R.drawable.chains
            else -> {
                return 0
            }
        }
    }

    private fun gatherArtPieces(){

        artPieces = GetMapOfArtPieces()

        for (artPiece in artPieces) {
            Log.d("dbg", artPiece["title"]!!)
            var bMap = BitmapFactory.decodeResource(resources,GetIconResourceFromTitle(artPiece["title"]!!))
            while (bMap == null) {
                //bMap = getBitmapFromURL(artPiece.get("image"))
                bMap = BitmapFactory.decodeResource(resources,R.drawable.tvrock)
            }

            val b = Bitmap.createScaledBitmap(bMap!!,150,150,false)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(artPiece["lat"]!!.toDouble(),artPiece["long"]!!.toDouble()))
                    .title(artPiece.get("title"))
                    .icon(BitmapDescriptorFactory.fromBitmap(b))
            )
        }

    }

    override fun onResume() {
        super.onResume()

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }

        Log.d("dbg", userCol.toString())
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("dbg","User clicked " + p0.title)
        val intent = Intent(this, ArtDetailActivity::class.java)
        for (artPiece in artPieces) {
            if (artPiece["title"] == p0.title) {
                intent.putExtra("title", artPiece["title"])
                intent.putExtra("image", artPiece["image"])
                intent.putExtra("description", artPiece["description"])
                intent.putExtra("artist", artPiece["artist"])
                intent.putExtra("date", artPiece["date"])
                break
            }
        }
        startActivity(intent)
        return false
    }
}
