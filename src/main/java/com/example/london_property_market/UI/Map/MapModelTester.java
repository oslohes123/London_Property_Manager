package com.example.london_property_market.UI.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * This class performs jUnit tests on the mapModel class.
 *
 * NOTE: The validity of the tests are derived from the boundaries of london boroughs according to
 * https://findthatpostcode.uk/. It was observed that there may exist some points that are without any boroughs or
 * overlaps, but they are rare and only visible at a level that is well beyond the users experiments. The tests may fail
 * for this reason as the boroughs in overlaps are decided by the order of the polygons. In this case, it was assumed that
 * files to be on the same order in any machine since the same files would normally have the same hash values.
 * @author Yousef Altaher, K20047484
 * @version 23-03-2022
 */
public class MapModelTester {

    // The map model - which to be tested
    private MapModel mapModel;
    // The boroughs' identifier, which have polygons that represents boroughs
    private HashMap<String, Graphic> identifier;

    /**
     * This method perform any required tasks before testing.
     */
    @Before
    public void initialize(){
        mapModel = new MapModel();
        identifier = new HashMap<>();
        // It is important to call this method as the identifier is required for testing. Please see mapModel class for
        // more information about the identifier.
        drawBoroughsBoundariesFromFolder();
    }

    /**
     * This method test the random hex generator color, that it generate a color with zero (No) opacity.
     */
    @Test
    public void testGenerateRandomHexWithOpacityWithZeroOpacity(){
        int randomHex = mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY);
        assertTrue(randomHex > 0xff000000);

    }

    /**
     * This method test the random hex generator color, that it generate a color with partial opacity. 0x33000000 exactly.
     */
    @Test
    public void testGenerateRandomHexWithOpacityWithDefaultFillOpacity(){
        int randomHex = mapModel.generateRandomHexWithOpacity(Opacity.DEFAULT_FILL_OPACITY);
        assertTrue(randomHex > 0x33000000);

    }

    /**
     * This method populate the identifier with boroughs information. This method is not a test method. But a method
     * that aids the tester to perform the tests.
     */
    private void drawBoroughsBoundariesFromFolder(){

        // Generate the identifier
        for (String fileName : mapModel.getAllGeoJsonResources()) {

            PolygonBuilder polygon = new PolygonBuilder(GeoJsonCoordinatesParser.getPointCollectionFromGeoJsonCoordinates(mapModel.getGEO_JSON_FOLDER_PATH() + fileName));
            Graphic polygonGraphic = new Graphic(polygon.toGeometry(), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));
            polygonGraphic.getAttributes().put("Name", GeoJsonCoordinatesParser.getBoroughNameFromFile(mapModel.getGEO_JSON_FOLDER_PATH()+fileName));
            identifier.put(mapModel.getGEO_JSON_FOLDER_PATH()+fileName, polygonGraphic);

        }
    }

    /**
     * This method tests random points around, but outside, london
     */
    @Test
    public void testOutsideOfLondonPoints(){

        ImmutablePair<String, String> expectedResults = new ImmutablePair<>(null, null);
        ImmutablePair<Double, Double>[] tests = new ImmutablePair[17];
        tests[0] = new ImmutablePair<>(-0.021242722719984324,51.325325060447895);
        tests[1] = new ImmutablePair<>(-0.25825335644539826,51.35605402822077);
        tests[2] = new ImmutablePair<>(-0.18372171061979636,51.33184504786934);
        tests[3] = new ImmutablePair<>(-0.4758857622561557,51.4471883534338);
        tests[4] = new ImmutablePair<>(-0.38793842018194546,51.64001495215065);
        tests[5] = new ImmutablePair<>(-0.26272525519493434,51.66498542926382);
        tests[6] = new ImmutablePair<>(0.30520588599615184,51.51588377203082);
        tests[7] = new ImmutablePair<>(0.030929429357937018,51.29923583960771);
        tests[8] = new ImmutablePair<>(0.07415778393678604,51.61965846560425);
        tests[9] = new ImmutablePair<>(-0.0793774064639538,51.697334094815304);
        tests[10] = new ImmutablePair<>(0.16657702476053246,51.63353887910083);
        tests[11] = new ImmutablePair<>(0.22322107558798984,51.45276210738439);
        tests[12] = new ImmutablePair<>(-0.532529813083613,51.55111959395063);
        tests[13] = new ImmutablePair<>(0.010060568526768428,51.66221161110956);
        tests[14] = new ImmutablePair<>(0.1606144930944842,51.41373153911993);
        tests[15] = new ImmutablePair<>(0.2843370251649834,51.602996363452476);
        tests[16] = new ImmutablePair<>(-0.38495715434892147,51.396063897745236);

        for (ImmutablePair<Double, Double> pair : tests)
            assertEquals(mapModel.getBoroughName(pair.getLeft(), pair.getRight(), identifier), expectedResults);

    }

    /**
     * This method tests random points that in river thames
     */
    @Test
    public void testRiverThamesPoints(){

        ImmutablePair<String, String> expectedResults = new ImmutablePair<>(null, null);
        ImmutablePair<Double, Double>[] tests = new ImmutablePair[16];
        tests[0] = new ImmutablePair<>(0.31452079461190335,51.46957912170134);
        tests[1] = new ImmutablePair<>(0.1850898167147779,51.48719913812648);
        tests[2] = new ImmutablePair<>(0.14296280871704553,51.5126762791545);
        tests[3] = new ImmutablePair<>(0.04621458079218623,51.497972447598);
        tests[4] = new ImmutablePair<>(-0.08333475706676217,51.507151191945404);
        tests[5] = new ImmutablePair<>(-0.003438475530058786,51.501344446064394);
        tests[6] = new ImmutablePair<>(-0.30634561990773185,51.456966624593036);
        tests[7] = new ImmutablePair<>(0.001827757716239915,51.50696388911178);
        tests[8] = new ImmutablePair<>(-0.22537276542038498,51.48270090295512);
        tests[9] = new ImmutablePair<>(0.09782366660591364,51.51258263919065);
        tests[10] = new ImmutablePair<>(-0.1758699983945825,51.479234579474365);
        tests[11] = new ImmutablePair<>(-0.05414477850156358,51.50284303196906);
        tests[12] = new ImmutablePair<>(-0.11944607075566761,51.50696388911178);
        tests[13] = new ImmutablePair<>(-0.32062279338097127,51.46534779110818);
        tests[14] = new ImmutablePair<>(-0.1317841029327103,51.48513783875995);
        tests[15] = new ImmutablePair<>(-0.03022103318266369,51.503873281192575);

        for (ImmutablePair<Double, Double> pair : tests)
            assertEquals(mapModel.getBoroughName(pair.getLeft(), pair.getRight(), identifier), expectedResults);

    }

    /**
     * This method tests random points that are near the boundaries of two or more boroughs
     */
    @Test
    public void testCloseToBoundariesPoints() {
        ImmutablePair<String, String>[] expectedResults = new ImmutablePair[14];
        ImmutablePair<Double, Double>[] tests = new ImmutablePair[14];

        expectedResults[0] = new ImmutablePair<>("/map/geoJson/E09000018.geojson.json","Hounslow");
        expectedResults[1] = new ImmutablePair<>("/map/geoJson/E09000018.geojson.json","Hounslow");
        expectedResults[2] = new ImmutablePair<>("/map/geoJson/E09000033.geojson.json","Westminster");
        expectedResults[3] = new ImmutablePair<>("/map/geoJson/E09000033.geojson.json","Westminster");
        expectedResults[4] = new ImmutablePair<>("/map/geoJson/E09000002.geojson.json","Barking and Dagenham");
        expectedResults[5] = new ImmutablePair<>("/map/geoJson/E09000002.geojson.json","Barking and Dagenham");
        expectedResults[6] = new ImmutablePair<>("/map/geoJson/E09000016.geojson.json","Havering");
        expectedResults[7] = new ImmutablePair<>("/map/geoJson/E09000028.geojson.json","Southwark");
        expectedResults[8] = new ImmutablePair<>("/map/geoJson/E09000023.geojson.json","Lewisham");
        expectedResults[9] = new ImmutablePair<>("/map/geoJson/E09000011.geojson.json","Greenwich");
        expectedResults[10] = new ImmutablePair<>("/map/geoJson/E09000011.geojson.json","Greenwich");
        expectedResults[11] = new ImmutablePair<>("/map/geoJson/E09000006.geojson.json","Bromley");
        expectedResults[12] = new ImmutablePair<>("/map/geoJson/E09000011.geojson.json","Greenwich");
        expectedResults[13] = new ImmutablePair<>("/map/geoJson/E09000004.geojson.json","Bexley");

        tests[0] = new ImmutablePair<>(-0.4348077132386635,51.45707322723987);
        tests[1] = new ImmutablePair<>(-0.43480754551781525,51.45707320633847);
        tests[2] = new ImmutablePair<>(-0.11232540722179932,51.51075701726333);
        tests[3] = new ImmutablePair<>(-0.11152767452197344,51.510951991975446);
        tests[4] = new ImmutablePair<>(0.14820587348764822,51.59896851172558);
        tests[5] = new ImmutablePair<>(0.1482058419106548,51.59896844727818);
        tests[6] = new ImmutablePair<>(0.14820593664163503,51.59896846689259);
        tests[7] = new ImmutablePair<>(-0.03251098827019066,51.49315480977984);
        tests[8] = new ImmutablePair<>(-0.032457874198369346,51.49302253257593);
        tests[9] = new ImmutablePair<>(-0.024703219712458608,51.48542697274404);
        tests[10] = new ImmutablePair<>(-0.02275570374567736,51.47473151437354);
        tests[11] = new ImmutablePair<>(0.07538136243906672,51.431932523507434);
        tests[12] = new ImmutablePair<>(0.07528656916194691,51.4319789578576);
        tests[13] = new ImmutablePair<>(0.07536556355954675,51.431995843064136);

        for (int i = 0; i < expectedResults.length; i++)
            assertEquals(mapModel.getBoroughName(tests[i].getLeft(), tests[i].getRight(), identifier), expectedResults[i]);

    }

    /**
     * This method tests random points within the boroughs.
     */
    @Test
    public void testRandomPointsWithinTheBoroughs() {
        ImmutablePair<String, String>[] expectedResults = new ImmutablePair[94];
        ImmutablePair<Double, Double>[] tests = new ImmutablePair[94];

        expectedResults[0] = new ImmutablePair<>("/map/geoJson/E09000008.geojson.json","Croydon");
        expectedResults[1] = new ImmutablePair<>("/map/geoJson/E09000008.geojson.json","Croydon");
        expectedResults[2] = new ImmutablePair<>("/map/geoJson/E09000008.geojson.json","Croydon");
        expectedResults[3] = new ImmutablePair<>("/map/geoJson/E09000008.geojson.json","Croydon");

        expectedResults[4] = new ImmutablePair<>("/map/geoJson/E09000029.geojson.json","Sutton");
        expectedResults[5] = new ImmutablePair<>("/map/geoJson/E09000029.geojson.json","Sutton");
        expectedResults[6] = new ImmutablePair<>("/map/geoJson/E09000029.geojson.json","Sutton");


        expectedResults[7] = new ImmutablePair<>("/map/geoJson/E09000024.geojson.json","Merton");
        expectedResults[8] = new ImmutablePair<>("/map/geoJson/E09000024.geojson.json","Merton");


        expectedResults[9] = new ImmutablePair<>("/map/geoJson/E09000021.geojson.json","Kingston upon Thames");

        expectedResults[10] = new ImmutablePair<>("/map/geoJson/E09000027.geojson.json","Richmond upon Thames");
        expectedResults[11] = new ImmutablePair<>("/map/geoJson/E09000027.geojson.json","Richmond upon Thames");

        expectedResults[12] = new ImmutablePair<>("/map/geoJson/E09000018.geojson.json","Hounslow");
        expectedResults[13] = new ImmutablePair<>("/map/geoJson/E09000018.geojson.json","Hounslow");
        expectedResults[14] = new ImmutablePair<>("/map/geoJson/E09000018.geojson.json","Hounslow");
        expectedResults[15] = new ImmutablePair<>("/map/geoJson/E09000018.geojson.json","Hounslow");


        expectedResults[16] = new ImmutablePair<>("/map/geoJson/E09000017.geojson.json","Hillingdon");
        expectedResults[17] = new ImmutablePair<>("/map/geoJson/E09000017.geojson.json","Hillingdon");
        expectedResults[18] = new ImmutablePair<>("/map/geoJson/E09000017.geojson.json","Hillingdon");
        expectedResults[19] = new ImmutablePair<>("/map/geoJson/E09000017.geojson.json","Hillingdon");

        expectedResults[20] = new ImmutablePair<>("/map/geoJson/E09000009.geojson.json","Ealing");
        expectedResults[21] = new ImmutablePair<>("/map/geoJson/E09000009.geojson.json","Ealing");

        expectedResults[22] = new ImmutablePair<>("/map/geoJson/E09000005.geojson.json","Brent");

        expectedResults[23] = new ImmutablePair<>("/map/geoJson/E09000015.geojson.json","Harrow");
        expectedResults[24] = new ImmutablePair<>("/map/geoJson/E09000015.geojson.json","Harrow");
        expectedResults[25] = new ImmutablePair<>("/map/geoJson/E09000015.geojson.json","Harrow");

        expectedResults[26] = new ImmutablePair<>("/map/geoJson/E09000003.geojson.json","Barnet");
        expectedResults[27] = new ImmutablePair<>("/map/geoJson/E09000003.geojson.json","Barnet");
        expectedResults[28] = new ImmutablePair<>("/map/geoJson/E09000003.geojson.json","Barnet");


        expectedResults[29] = new ImmutablePair<>("/map/geoJson/E09000007.geojson.json","Camden");
        expectedResults[30] = new ImmutablePair<>("/map/geoJson/E09000007.geojson.json","Camden");
        expectedResults[31] = new ImmutablePair<>("/map/geoJson/E09000007.geojson.json","Camden");

        expectedResults[32] = new ImmutablePair<>("/map/geoJson/E09000033.geojson.json","Westminster");
        expectedResults[33] = new ImmutablePair<>("/map/geoJson/E09000033.geojson.json","Westminster");
        expectedResults[34] = new ImmutablePair<>("/map/geoJson/E09000020.geojson.json","Kensington and Chelsea");

        expectedResults[35] = new ImmutablePair<>("/map/geoJson/E09000003.geojson.json","Barnet");
        expectedResults[36] = new ImmutablePair<>("/map/geoJson/E09000020.geojson.json","Kensington and Chelsea");
        expectedResults[37] = new ImmutablePair<>("/map/geoJson/E09000013.geojson.json","Hammersmith and Fulham");
        expectedResults[38] = new ImmutablePair<>("/map/geoJson/E09000019.geojson.json","Islington");
        expectedResults[39] = new ImmutablePair<>("/map/geoJson/E09000019.geojson.json","Islington");
        expectedResults[40] = new ImmutablePair<>("/map/geoJson/E09000019.geojson.json","Islington");

        expectedResults[41] = new ImmutablePair<>("/map/geoJson/E09000012.geojson.json","Hackney");
        expectedResults[42] = new ImmutablePair<>("/map/geoJson/E09000012.geojson.json","Hackney");
        expectedResults[43] = new ImmutablePair<>("/map/geoJson/E09000012.geojson.json","Hackney");
        expectedResults[44] = new ImmutablePair<>("/map/geoJson/E09000030.geojson.json","Tower Hamlets");

        expectedResults[45] = new ImmutablePair<>("/map/geoJson/E09000030.geojson.json","Tower Hamlets");
        expectedResults[46] = new ImmutablePair<>("/map/geoJson/E09000025.geojson.json","Newham");
        expectedResults[47] = new ImmutablePair<>("/map/geoJson/E09000025.geojson.json","Newham");
        expectedResults[48] = new ImmutablePair<>("/map/geoJson/E09000025.geojson.json","Newham");
        expectedResults[49] = new ImmutablePair<>("/map/geoJson/E09000025.geojson.json","Newham");

        expectedResults[50] = new ImmutablePair<>("/map/geoJson/E09000031.geojson.json","Waltham Forest");
        expectedResults[51] = new ImmutablePair<>("/map/geoJson/E09000031.geojson.json","Waltham Forest");
        expectedResults[52] = new ImmutablePair<>("/map/geoJson/E09000031.geojson.json","Waltham Forest");

        expectedResults[53] = new ImmutablePair<>("/map/geoJson/E09000010.geojson.json","Enfield");
        expectedResults[54] = new ImmutablePair<>("/map/geoJson/E09000010.geojson.json","Enfield");
        expectedResults[55] = new ImmutablePair<>("/map/geoJson/E09000010.geojson.json","Enfield");

        expectedResults[56] = new ImmutablePair<>("/map/geoJson/E09000026.geojson.json","Redbridge");
        expectedResults[57] = new ImmutablePair<>("/map/geoJson/E09000026.geojson.json","Redbridge");
        expectedResults[58] = new ImmutablePair<>("/map/geoJson/E09000026.geojson.json","Redbridge");

        expectedResults[59] = new ImmutablePair<>("/map/geoJson/E09000016.geojson.json","Havering");
        expectedResults[60] = new ImmutablePair<>("/map/geoJson/E09000016.geojson.json","Havering");
        expectedResults[61] = new ImmutablePair<>("/map/geoJson/E09000016.geojson.json","Havering");
        expectedResults[62] = new ImmutablePair<>("/map/geoJson/E09000016.geojson.json","Havering");

        expectedResults[63] = new ImmutablePair<>("/map/geoJson/E09000002.geojson.json","Barking and Dagenham");
        expectedResults[64] = new ImmutablePair<>("/map/geoJson/E09000002.geojson.json","Barking and Dagenham");

        expectedResults[65] = new ImmutablePair<>("/map/geoJson/E09000004.geojson.json","Bexley");
        expectedResults[66] = new ImmutablePair<>("/map/geoJson/E09000004.geojson.json","Bexley");
        expectedResults[67] = new ImmutablePair<>("/map/geoJson/E09000004.geojson.json","Bexley");

        expectedResults[68] = new ImmutablePair<>("/map/geoJson/E09000011.geojson.json","Greenwich");
        expectedResults[69] = new ImmutablePair<>("/map/geoJson/E09000011.geojson.json","Greenwich");
        expectedResults[70] = new ImmutablePair<>("/map/geoJson/E09000011.geojson.json","Greenwich");

        expectedResults[71] = new ImmutablePair<>("/map/geoJson/E09000023.geojson.json","Lewisham");
        expectedResults[72] = new ImmutablePair<>("/map/geoJson/E09000023.geojson.json","Lewisham");

        expectedResults[73] = new ImmutablePair<>("/map/geoJson/E09000028.geojson.json","Southwark");
        expectedResults[74] = new ImmutablePair<>("/map/geoJson/E09000028.geojson.json","Southwark");
        expectedResults[75] = new ImmutablePair<>("/map/geoJson/E09000028.geojson.json","Southwark");

        expectedResults[76] = new ImmutablePair<>("/map/geoJson/E09000022.geojson.json","Lambeth");
        expectedResults[77] = new ImmutablePair<>("/map/geoJson/E09000022.geojson.json","Lambeth");
        expectedResults[78] = new ImmutablePair<>("/map/geoJson/E09000022.geojson.json","Lambeth");


        expectedResults[79] = new ImmutablePair<>("/map/geoJson/E09000032.geojson.json","Wandsworth");
        expectedResults[80] = new ImmutablePair<>("/map/geoJson/E09000032.geojson.json","Wandsworth");
        expectedResults[81] = new ImmutablePair<>("/map/geoJson/E09000024.geojson.json","Merton");
        expectedResults[82] = new ImmutablePair<>("/map/geoJson/E09000029.geojson.json","Sutton");
        expectedResults[83] = new ImmutablePair<>("/map/geoJson/E09000008.geojson.json","Croydon");

        expectedResults[84] = new ImmutablePair<>("/map/geoJson/E09000006.geojson.json","Bromley");
        expectedResults[85] = new ImmutablePair<>("/map/geoJson/E09000006.geojson.json","Bromley");
        expectedResults[86] = new ImmutablePair<>("/map/geoJson/E09000006.geojson.json","Bromley");
        expectedResults[87] = new ImmutablePair<>("/map/geoJson/E09000008.geojson.json","Croydon");
        expectedResults[88] = new ImmutablePair<>("/map/geoJson/E09000029.geojson.json","Sutton");

        expectedResults[89] = new ImmutablePair<>("/map/geoJson/E09000021.geojson.json","Kingston upon Thames");
        expectedResults[90] = new ImmutablePair<>("/map/geoJson/E09000021.geojson.json","Kingston upon Thames");
        expectedResults[91] = new ImmutablePair<>("/map/geoJson/E09000021.geojson.json","Kingston upon Thames");
        expectedResults[92] = new ImmutablePair<>("/map/geoJson/E09000027.geojson.json","Richmond upon Thames");
        expectedResults[93] = new ImmutablePair<>("/map/geoJson/E09000027.geojson.json","Richmond upon Thames");


        tests[0] = new ImmutablePair<>(-0.12037563544859306,51.3241462621422);
        tests[1] = new ImmutablePair<>(-0.03318256423989469,51.355746811693955);
        tests[2] = new ImmutablePair<>(-0.0924206049847356,51.40393723400949);
        tests[3] = new ImmutablePair<>(-0.08776143324075932,51.356162463191446);

        tests[4] = new ImmutablePair<>(-0.17096092866890664,51.3640591251121);
        tests[5] = new ImmutablePair<>(-0.21888383803551953,51.376109300257525);
        tests[6] = new ImmutablePair<>(-0.15964579729067863,51.35075869955869);


        tests[7] = new ImmutablePair<>(-0.20490632280359075,51.414731575697346);
        tests[8] = new ImmutablePair<>(-0.17295771655918218,51.401030629682715);


        tests[9] = new ImmutablePair<>(-0.2754594949266597,51.39646273537045);
        tests[10] = new ImmutablePair<>(-0.3579933943913819,51.420127790947646);
        tests[11] = new ImmutablePair<>(-0.27479389896323453,51.44875857819183);

        tests[12] = new ImmutablePair<>(-0.3208374606643736,51.468374908333324);
        tests[13] = new ImmutablePair<>(-0.39426318722693704,51.45961507709429);
        tests[14] = new ImmutablePair<>(-0.41738734470241234,51.44200205027935);
        tests[15] = new ImmutablePair<>(-0.3874115850119815,51.482021830343975);

        tests[16] = new ImmutablePair<>(-0.4572122825768417,51.47882153933344);
        tests[17] = new ImmutablePair<>(-0.4452219787006695,51.502285125431094);
        tests[18] = new ImmutablePair<>(-0.4589251831305806,51.54757831596584);
        tests[19] = new ImmutablePair<>(-0.44008327703945277,51.59442263214924);

        tests[20] = new ImmutablePair<>(-0.35229712366033406,51.53026565114062);
        tests[21] = new ImmutablePair<>(-0.2966278556638196,51.5177431598402);

        tests[22] = new ImmutablePair<>(-0.27607304901895285,51.5547677946899);


        tests[23] = new ImmutablePair<>(-0.36000517615215916,51.598146812117186);
        tests[24] = new ImmutablePair<>(-0.3373092438151187,51.612774562903695);
        tests[25] = new ImmutablePair<>(-0.3565793750446813,51.57819228383187);

        tests[26] = new ImmutablePair<>(-0.19765824441741764,51.588166549815305);
        tests[27] = new ImmutablePair<>(-0.23928211366032576,51.6174212401153);
        tests[28] = new ImmutablePair<>(-0.20532579927795339,51.62422195532645);

        tests[29] = new ImmutablePair<>(-0.1767330334478486,51.55367442902507);
        tests[30] = new ImmutablePair<>(-0.15790531292602789,51.5611781196616);

        tests[31] = new ImmutablePair<>(-0.1434224509861658,51.53265750871625);


        tests[32] = new ImmutablePair<>(-0.16321569563731064,51.51914150518693);
        tests[33] = new ImmutablePair<>(-0.18107789202980723,51.52995462897488);
        tests[34] = new ImmutablePair<>(-0.19942285048696587,51.50231598625646);

        tests[35] = new ImmutablePair<>(-0.19765824441741764,51.588166549815305);
        tests[36] = new ImmutablePair<>(-0.20328494700426245,51.51463527907227);
        tests[37] = new ImmutablePair<>(-0.2187333330734487,51.49540370497571);

        tests[38] = new ImmutablePair<>(-0.10092439098202814,51.52965948557204);
        tests[39] = new ImmutablePair<>(-0.10816727606523467,51.54617800068535);
        tests[40] = new ImmutablePair<>(-0.11927303319281797,51.561789992630196);

        tests[41] = new ImmutablePair<>(-0.06615854258263693,51.56118963048104);

        tests[42] = new ImmutablePair<>(-0.05118991341067681,51.54707883818073);
        tests[43] = new ImmutablePair<>(-0.05650136247169492,51.53897065862299);
        tests[44] = new ImmutablePair<>(-0.039601297277546375,51.52515339546919);
        tests[45] = new ImmutablePair<>(-0.02318409108894498,51.517642254243874);
        tests[46] = new ImmutablePair<>(0.01641034736591725,51.53146179674503);

        tests[47] = new ImmutablePair<>(0.03379327156561288,51.53596726251628);
        tests[48] = new ImmutablePair<>(0.007718885266069436,51.54257447234463);
        tests[49] = new ImmutablePair<>(0.06179909388734471,51.51914458159714);

        tests[50] = new ImmutablePair<>(-0.003878717199098339,51.58070535016182);
        tests[51] = new ImmutablePair<>(-0.003878717199098339,51.60110789916875);
        tests[52] = new ImmutablePair<>(-0.003878717199098339,51.61880268692976);
        tests[53] = new ImmutablePair<>(-0.09370565269527276,51.62659820067251);
        tests[54] = new ImmutablePair<>(-0.12557972658101205,51.65956440259124);
        tests[55] = new ImmutablePair<>(-0.05555335213506965,51.65626886093432);

        tests[56] = new ImmutablePair<>(0.04634709619661211,51.59810809899673);
        tests[57] = new ImmutablePair<>(0.10961230345467043,51.59810809899673);
        tests[58] = new ImmutablePair<>(0.08643115881049634,51.58190575376729);

        tests[59] = new ImmutablePair<>(0.17887954689659188,51.60581750686789);
        tests[60] = new ImmutablePair<>(0.22662056524817806,51.597957676904876);
        tests[61] = new ImmutablePair<>(0.22834614422474142,51.55577718649507);
        tests[62] = new ImmutablePair<>(0.21454151241223457,51.532167635714735);


        tests[63] = new ImmutablePair<>(0.13113852854500574,51.544331661398935);
        tests[64] = new ImmutablePair<>(0.14264238838876148,51.56507455543847);

        tests[65] = new ImmutablePair<>(0.1466693490548476,51.48060430943084);
        tests[66] = new ImmutablePair<>(0.13458773539166316,51.4504976513126);
        tests[67] = new ImmutablePair<>(0.10352072882918864,51.452290270309284);

        tests[68] = new ImmutablePair<>(0.05519427417645053,51.47702122504103);
        tests[69] = new ImmutablePair<>(0.05692021898547689,51.44870496192848);
        tests[70] = new ImmutablePair<>(0.012045653950791484,51.47988771507168);

        tests[71] = new ImmutablePair<>(-0.02592513184778843,51.44619507853787);
        tests[72] = new ImmutablePair<>(-0.021897927293393588,51.466628688455984);


        tests[73] = new ImmutablePair<>(-0.07770347611857928,51.481679179858446);
        tests[74] = new ImmutablePair<>(-0.056992138410262946,51.49672470596424);
        tests[75] = new ImmutablePair<>(-0.0846072553546847,51.444760797514625);

        tests[76] = new ImmutablePair<>(-0.10761985280836954,51.50496181879889);
        tests[77] = new ImmutablePair<>(-0.11222237229910598,51.43400225383499);
        tests[78] = new ImmutablePair<>(-0.11279768723544809,51.48382884471445);


        tests[79] = new ImmutablePair<>(-0.1812601646601604,51.452648785662205);
        tests[80] = new ImmutablePair<>(-0.21175185628629276,51.45479981865566);
        tests[81] = new ImmutablePair<>(-0.21117654134995067,51.420012358267904);
        tests[82] = new ImmutablePair<>(-0.18988988870529222,51.36365072609207);
        tests[83] = new ImmutablePair<>(-0.10416796319031628,51.37801696918233);


        tests[84] = new ImmutablePair<>(0.020100063059581723,51.39525051117781);
        tests[85] = new ImmutablePair<>(0.07820687163013589,51.4214474164764);
        tests[86] = new ImmutablePair<>(0.08165876124818862,51.35646591411504);
        tests[87] = new ImmutablePair<>(-0.059868713091973044,51.33993656934742);
        tests[88] = new ImmutablePair<>(-0.16975386593331754,51.361136170096884);
        tests[89] = new ImmutablePair<>(-0.2796390187746625,51.38376220431993);
        tests[90] = new ImmutablePair<>(-0.310706025337137,51.36616514403271);
        tests[91] = new ImmutablePair<>(-0.2802143337110046,51.415348107899476);
        tests[92] = new ImmutablePair<>(-0.33371862279082176,51.4264697653746);
        tests[93] = new ImmutablePair<>(-0.2945972071195576,51.445836512505466);


        for (int i = 0; i < expectedResults.length; i++)
            assertEquals(mapModel.getBoroughName(tests[i].getLeft(), tests[i].getRight(), identifier), expectedResults[i]);


    }

}
