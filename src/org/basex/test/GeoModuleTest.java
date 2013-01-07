package org.basex.test;

import static org.junit.Assert.fail;

import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.util.Err;
import org.basex.query.value.item.QNm;
import org.basex.test.query.AdvancedQueryTest;
import org.basex.util.Token;
import org.basex.util.Util;
import org.junit.Test;

/**
 * This class tests the XQuery conversions functions prefixed with "convert".
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Masoumeh Seydi
 */
public final class GeoModuleTest extends AdvancedQueryTest {

 /** Test method. */
  @Test
  public void dimension() {
	  runQuery("geo:dimension(<gml:Point><gml:coordinates>1,2</gml:coordinates></gml:Point>)", "0");

	  runError("geo:dimension(text {'a'})", new QNm("FORG0006"));
	  runError("geo:dimension(<gml:unknown/>)", new QNm("GEO0001"));
	  runError("geo:dimension(<gml:Point><gml:coordinates>1 2</gml:coordinates></gml:Point>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void geometryType() {
	  runQuery("geo:geometryType(<gml:MultiPoint>" +
	  		"<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>" +
	  		"<gml:Point><gml:coordinates>1,2</gml:coordinates></gml:Point>" +
	  		"</gml:MultiPoint>)", "gml:MultiPoint");

	  runError("geo:geometryType(text {'srsName'})", new QNm("FORG0006"));
	  runError("geo:geometryType(<gml:unknown/>)", new QNm("GEO0001"));
	  runError("geo:geometryType(<gml:Point><gml:coordinates>1 2</gml:coordinates></gml:Point>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void envelope() {
	  runQuery("geo:envelope(<gml:LinearRing><gml:coordinates>1,1 20,1 50,30 1,1</gml:coordinates></gml:LinearRing>)",
			  "<gml:Polygon srsName=\"0\"><gml:outerBoundaryIs><gml:LinearRing srsName=\"0\"><gml:coordinates>" +
			  "1.0,1.0 50.0,1.0 50.0,30.0 1.0,30.0 1.0,1.0</gml:coordinates>" +
			  "</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>");
	  
	  runError("geo:envelope(text {'a'})", new QNm("FORG0006"));
	  runError("geo:envelope(<gml:unknown/>)", new QNm("GEO0001"));
	  runError("geo:envelope(<gml:LinearRing><gml:pos>1,1 20,1 50,30 1,1</gml:pos></gml:LinearRing>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void asText() {
	  runQuery("geo:asText(<gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>)",
	  		"LINESTRING (1 1, 55 99, 2 1)");

	  runError("geo:asText(text {'a'})", new QNm("FORG0006"));
	  runError("geo:asText(<gml:unknown/>)", new QNm("GEO0001"));
	  runError("geo:asText(<gml:LineString><gml:coordinates>1,1</gml:coordinates></gml:LineString>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void asBinary() {
	  runQuery("geo:asBinary(<gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>)",
	  		"AAAAAAIAAAADP/AAAAAAAAA/8AAAAAAAAEBLgAAAAAAAQFjAAAAAAABAAAAAAAAAAD/wAAAAAAAA");

	  runError("geo:asBinary(text {'a'})", new QNm("FORG0006"));
	  runError("geo:asBinary(<gml:unknown/>)", new QNm("GEO0001"));
	  runError("geo:asBinary(<gml:LinearRing><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LinearRing>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void isEmpty() { ////////////////??????????????????????????????????????????????
	  runQuery("geo:isEmpty(<gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>)",
	  		"false");

	  runError("geo:isEmpty(text {'a'})", new QNm("FORG0006"));
	  runError("geo:isEmpty(<gml:unknown/>)", new QNm("GEO0001"));
	  runError("geo:isEmpty(<gml:LinearRing><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LinearRing>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void isSimple() {
	  runQuery("geo:isSimple(<gml:LineString><gml:coordinates>1,1 20,1 10,4 20,-10</gml:coordinates></gml:LineString>)",
	  		"false");

	  runError("geo:isSimple(text {'a'})", new QNm("FORG0006"));
	  runError("geo:isSimple(<gml:unknown/>)", new QNm("GEO0001"));
	  runError("geo:isSimple(<gml:LinearRing><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LinearRing>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void boundary() {
	  runQuery("geo:boundary(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>1,1 20,1 20,20 30,20 30,30 1,30 1,1</gml:coordinates>" +
	  		"</gml:LinearRing></gml:outerBoundaryIs><gml:innerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>2,2 3,2 3,3 2,3 2,2</gml:coordinates></gml:LinearRing>" +
	  		"</gml:innerBoundaryIs><gml:innerBoundaryIs><gml:LinearRing><gml:coordinates>" +
	  		"10,10 20,10 20,20 10,20 10,10</gml:coordinates></gml:LinearRing>" +
	  		"</gml:innerBoundaryIs></gml:Polygon>)", 
	  		"<gml:MultiLineString srsName=\"0\"><gml:lineStringMember>" +
	  		"<gml:LineString srsName=\"0\"><gml:coordinates>" +
	  		"1.0,1.0 20.0,1.0 20.0,20.0 30.0,20.0 30.0,30.0 1.0,30.0 1.0,1.0" +
	  		"</gml:coordinates></gml:LineString></gml:lineStringMember>" +
	  		"<gml:lineStringMember><gml:LineString srsName=\"0\"><gml:coordinates>" +
	  		"2.0,2.0 3.0,2.0 3.0,3.0 2.0,3.0 2.0,2.0</gml:coordinates></gml:LineString>" +
	  		"</gml:lineStringMember><gml:lineStringMember><gml:LineString srsName=\"0\">" +
	  		"<gml:coordinates>10.0,10.0 20.0,10.0 20.0,20.0 10.0,20.0 10.0,10.0" +
	  		"</gml:coordinates></gml:LineString></gml:lineStringMember></gml:MultiLineString>");

	  runError("geo:boundary(text {'a'})", new QNm("FORG0006"));
	  runError("geo:boundary(a)", new QNm("XPDY0002"));
	  runError("geo:boundary(<gml:geo/>)", new QNm("GEO0001"));
	  runError("geo:boundary(<gml:Point><gml:pos>1 2</gml:pos></gml:Point>)",
			  new QNm("GEO0002"));
  }

  /** Test method. */
  @Test
  public void isEqual() {
	  runQuery("geo:isEqual(<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates>" +
	  		"</gml:LinearRing>, <gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>" +
	  		"</gml:outerBoundaryIs></gml:Polygon>)",
	  		"false");

	  runError("geo:isEqual(text {'a'}, a)", new QNm("XPDY0002"));
	  runError("geo:isEqual(<gml:unknown/>, " +
	  		"<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>)",
	  		new QNm("GEO0001"));
	  runError("geo:isEqual(<gml:LinearRing><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LinearRing>," +
	  		"<gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>)",
			  new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void disjoint() {
	  runQuery("geo:disjoint(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>, " +
	  		"<gml:LineString><gml:coordinates>0,0 2,1 3,3</gml:coordinates></gml:LineString>)",
	  		"false");

	  runError("geo:disjoint(a, text {'a'})", new QNm("XPDY0002"));
	  runError("geo:disjoint(<gml:Ring/>, " +
	  		"<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>)",
	  		new QNm("GEO0001"));
	  runError("geo:disjoint(<gml:LineString><gml:coordinates></gml:coordinates></gml:LineString>)",
			  new QNm("XPST0017"));
  }
  
  /** Test method. */
  @Test
  public void intersects() {
	  runQuery("geo:intersects(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>, " +
	  		"<gml:LineString><gml:coordinates>0,0 2,1 3,3</gml:coordinates></gml:LineString>)",
	  		"true");

	  runError("geo:intersects(a, text {'a'})", new QNm("XPDY0002"));
	  runError("geo:intersects(<gml:Point><gml:coordinates>10,10 12,11</gml:coordinates></gml:Point>, " +
	  		"<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>)",
	  		new QNm("GEO0002"));
	  runError("geo:intersects(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>," +
	  		"<gml:Line><gml:coordinates>0,0 2,1 3,3</gml:coordinates></gml:Line>)",
			  new QNm("GEO0001"));
  }

  /** Test method. */
  @Test
  public void touches() {
	  runQuery("geo:touches(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>, " +
	  		"<gml:LineString><gml:coordinates>0,0 2,1 3,3</gml:coordinates></gml:LineString>)",
	  		"false");

	  runError("geo:touches(a, text {'a'})", new QNm("XPDY0002"));
	  runError("geo:touches(<gml:Point><gml:coordinates>10,10 12,11</gml:coordinates></gml:Point>, " +
	  		"<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>)",
	  		new QNm("GEO0002"));
	  runError("geo:touches(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>," +
	  		"<gml:Line><gml:coordinates>0,0 2,1 3,3</gml:coordinates></gml:Line>)",
			  new QNm("GEO0001"));
  }
  
  /** Test method. */
  @Test
  public void crosses() {
	  runQuery("geo:crosses(<gml:Point><gml:coordinates>" +
	  		"10,11</gml:coordinates></gml:Point>, " +
	  		"<gml:LineString><gml:coordinates>0,0 2,2</gml:coordinates></gml:LineString>)",
	  		"false");

	  runError("geo:crosses(a, text {'a'})", new QNm("XPDY0002"));
	  runError("geo:crosses(<gml:Point><gml:coordinates>10,10 12,11</gml:coordinates></gml:Point>, " +
	  		"<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>)",
	  		new QNm("GEO0002"));
	  runError("geo:crosses(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>)",
			  new QNm("XPST0017"));
  }
  
  /** Test method. */
  @Test
  public void within() {
	  runQuery("geo:within(<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>, " +
	  		"<gml:LinearRing><gml:coordinates>1,1 20,1 50,30 1,1</gml:coordinates></gml:LinearRing>)",
	  		"false");

	  runError("geo:within()", new QNm("XPST0017"));
	  runError("geo:within(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>, " +
	  		"<gml:LinearRing><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>)",
	  		new QNm("GEO0001"));
	  runError("geo:within(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>)",
			  new QNm("XPST0017"));
  }
  
  /** Test method. */
  @Test
  public void contains() {
	  runQuery("geo:contains(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>, " +
	  		"<gml:Point><gml:coordinates>1.00,1.00</gml:coordinates></gml:Point>)",
	  		"true");

	  runError("geo:contains()", new QNm("XPST0017"));
	  runError("geo:contains(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>, " +
	  		"<gml:Line><gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:Line>)",
	  		new QNm("GEO0001"));
	  runError("geo:contains(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>)",
			  new QNm("XPST0017"));
  }
  
  /** Test method. */
  @Test
  public void overlaps() {
	  runQuery("geo:overlaps(<gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>, " +
	  		"<gml:LineString><gml:coordinates>1,1 55,0</gml:coordinates></gml:LineString>)",
	  		"false");

	  runError("geo:overlaps()", new QNm("XPST0017"));
	  runError("geo:overlaps(<gml:LineString><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LineString>," +
	  		"<gml:LineString></gml:LineString>)", new QNm("GEO0002"));
	  runError("geo:overlaps(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>)",
			  new QNm("XPST0017"));
  }
  
  /** Test method. */
  @Test
  public void relate() {
	  runQuery("geo:relate(<gml:Point><gml:coordinates>18,11</gml:coordinates></gml:Point>, " +
	  		"<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>" +
	  		"10,10 20,10 30,40 20,40 10,10</gml:coordinates></gml:LinearRing>" +
	  		"</gml:outerBoundaryIs></gml:Polygon>, \"0********\")",
	  		"true");

	  runError("geo:relate(<gml:Point><gml:coordinates>18,11</gml:coordinates></gml:Point>" +
	  		", <gml:LineString><gml:coordinates>11,10 20,1 20,20</gml:coordinates></gml:LineString>" +
	  		", \"0******\")", new QNm("FORG0006"));
	  
	  runError("geo:relate(<gml:Point><gml:coordinates>18,11</gml:coordinates></gml:Point>" +
		  		", <gml:LineString><gml:coordinates>11,10 20,1 20,20</gml:coordinates></gml:LineString>" +
		  		", \"0*******12*F\")", new QNm("FORG0006"));
	  
	  runError("geo:relate()", new QNm("XPST0017"));
	  runError("geo:relate(<gml:Line><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:Line>," +
	  		"<gml:LineString></gml:LineString>, \"0********\")", new QNm("GEO0001"));
	  runError("geo:relate(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>, \"0********\")",
			  new QNm("XPST0017"));
  }
 
  /** Test method. */
  @Test
  public void distance() {
	  runQuery("geo:distance(<gml:LinearRing><gml:coordinates>10,400 20,200 30,100 " +
	  		"20,100 10,400</gml:coordinates></gml:LinearRing>, " +
	  		"<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>" +
	  		"10,10 20,10 30,40 20,40 10,10</gml:coordinates></gml:LinearRing>" +
	  		"</gml:outerBoundaryIs></gml:Polygon>)",
	  		"60");

	  runError("geo:distance()", new QNm("XPST0017"));
	  runError("geo:distance(<gml:LinearRing><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LinearRing>," +
	  		"<gml:LineString></gml:LineString>)", new QNm("GEO0002"));
	  runError("geo:distance(<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>)",
			  new QNm("XPST0017"));
  }
  
  /** Test method. */
  @Test
  public void buffer() {
	  runQuery("geo:buffer(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>10,10 20,10 30,40 20,40 10,10</gml:coordinates>" +
	  		"</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>," +
	  		"xs:double(0))",
	  		"<gml:Polygon srsName=\"0\"><gml:outerBoundaryIs><gml:LinearRing srsName=\"0\">" +
	  		"<gml:coordinates>10.0,10.0 20.0,40.0 30.0,40.0 20.0,10.0 10.0,10.0" +
	  		"</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>");

	  runQuery("geo:buffer(<gml:LineString><gml:coordinates>1,1 5,9 2,1</gml:coordinates></gml:LineString>," +
		  		"xs:double(0))", "<gml:MultiGeometry srsName=\"0\"/>");
	  runError("geo:buffer(<gml:LinearRing><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LinearRing>," +
	  		" xs:double(1))", new QNm("GEO0002"));
	  runError("geo:buffer(<gml:LinearRing><gml:coordinates>1,1 55,99 1,1</gml:coordinates></gml:LinearRing>," +
	  		" 1)", new QNm("FORG0006"));
	  runError("geo:buffer(xs:double(1))", new QNm("XPST0017"));
  }

  /** Test method. */
  @Test
  public void convexHull() {
	  runQuery("geo:convexHull(<gml:LinearRing><gml:coordinates>1,1 55,99 2,2 1,1" +
	  		"</gml:coordinates></gml:LinearRing>)",
	  		"<gml:Polygon srsName=\"0\"><gml:outerBoundaryIs><gml:LinearRing srsName=\"0\">" +
	  		"<gml:coordinates>1.0,1.0 55.0,99.0 2.0,2.0 1.0,1.0" +
	  		"</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>");

	  runError("geo:convexHull(<gml:LinearRing><gml:coordinates>1,1 55,99 1,1" +
	  		"</gml:coordinates></gml:LinearRing>)", new QNm("GEO0002"));
	  runError("geo:convexHull()", new QNm("XPST0017"));
	  runError("geo:convexHull(<gml:LinearRing/>)", new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void intersection() {
	  runQuery("geo:intersection(<gml:LinearRing><gml:coordinates>1,1 55,99 2,3 1,1" +
	  		"</gml:coordinates></gml:LinearRing>," +
	  		" <gml:Polygon><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>" +
	  		"10,10 20,10 30,40 10,10</gml:coordinates></gml:LinearRing>" +
	  		"</gml:outerBoundaryIs></gml:Polygon>)",
	  		"<gml:MultiGeometry srsName=\"0\"/>");

	  runQuery("geo:intersection(<gml:LinearRing><gml:coordinates>1,1 55,99 2,3 1,1" +
		  		"</gml:coordinates></gml:LinearRing>," +
		  		"<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>");
	  runError("geo:intersection(<gml:LinearRing><gml:coordinates></gml:coordinates>" +
	  		"</gml:LinearRing>)", new QNm("XPST0017"));
	  runError("geo:intersection(<gml:Geo><gml:coordinates>2,3</gml:coordinates></gml:Geo>," +
	  		"<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)", new QNm("GEO0001"));
	  runError("geo:intersection(<gml:LinearRing/>, <gml:Point/>)", new QNm("GEO0002"));
  }
  
  /** Test method. */
  @Test
  public void union() {
	  
	  runQuery("geo:union(<gml:Point><gml:coordinates>2</gml:coordinates></gml:Point>," +
		  		"<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>2.0,NaN</gml:coordinates></gml:Point>");
	  
	  runQuery("geo:union(<gml:Point><gml:coordinates>2</gml:coordinates></gml:Point>," +
		  		"<gml:Point><gml:coordinates>3</gml:coordinates></gml:Point>)",
		  		"<gml:MultiPoint srsName=\"0\"><gml:pointMember><gml:Point srsName=\"0\">" +
		  		"<gml:coordinates>2.0,NaN</gml:coordinates></gml:Point></gml:pointMember>" +
		  		"<gml:pointMember><gml:Point srsName=\"0\"><gml:coordinates>" +
		  		"3.0,NaN</gml:coordinates></gml:Point></gml:pointMember></gml:MultiPoint>");

	  runError("geo:union(<gml:Point><gml:coordinates></gml:coordinates></gml:Point>," +
		  		"<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)",
		  		new QNm("GEO0002"));
	  runError("geo:union(text {'a'}, <gml:Point><gml:coordinates>2,3" +
	  		"</gml:coordinates></gml:Point>)", new QNm("FORG0006"));
  }
  
  /** Test method. */
  @Test
  public void difference() {
	  
	  runQuery("geo:difference(<gml:Point><gml:coordinates>20,1</gml:coordinates></gml:Point>," +
		  		"<gml:LinearRing><gml:coordinates>0,0 20,20 20,30 0,20 0,0</gml:coordinates></gml:LinearRing>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>20.0,1.0</gml:coordinates></gml:Point>");
	  
	  runError("geo:difference(<gml:Point><gml:coordinates></gml:coordinates></gml:Point>," +
		  		"<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)",
		  		new QNm("GEO0002"));
	  runError("geo:difference(text {'a'}, <gml:Point><gml:coordinates>2,3" +
	  		"</gml:coordinates></gml:Point>)", new QNm("FORG0006"));
  }
  
  /** Test method. */
  @Test
  public void symDifference() {
	  
	  runQuery("geo:symDifference(<gml:Point><gml:coordinates>20,1</gml:coordinates></gml:Point>," +
		  		"<gml:LinearRing><gml:coordinates>0,0 20,20 20,30 0,20 0,0</gml:coordinates></gml:LinearRing>)",
		  		"<gml:MultiGeometry srsName=\"0\"><gml:geometryMember><gml:Point srsName=\"0\">" +
		  		"<gml:coordinates>20.0,1.0</gml:coordinates></gml:Point></gml:geometryMember>" +
		  		"<gml:geometryMember><gml:LineString srsName=\"0\"><gml:coordinates>" +
		  		"0.0,0.0 20.0,20.0 20.0,30.0 0.0,20.0 0.0,0.0</gml:coordinates>" +
		  		"</gml:LineString></gml:geometryMember></gml:MultiGeometry>");
	  
	  runError("geo:symDifference(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>," +
		  		"<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)",
		  		new QNm("GEO0001"));
	  runError("geo:symDifference(text {'a'}, <gml:Point><gml:coordinates>2,3" +
	  		"</gml:coordinates></gml:Point>)", new QNm("FORG0006"));
  }

  /** Test method. */
  @Test
  public void geometryN() {
	  
	  runQuery("geo:geometryN(<gml:Point><gml:coordinates>2,1</gml:coordinates></gml:Point>, 1)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>2.0,1.0</gml:coordinates></gml:Point>");
	  
	  runError("geo:geometryN(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>,1)",
		  		new QNm("GEO0001"));
	  runError("geo:geometryN(<gml:Point><gml:coordinates>2,1</gml:coordinates></gml:Point>, 0)",
		  		new QNm("GEO0006"));
	  runError("geo:geometryN(<gml:Point><gml:coordinates>2,1</gml:coordinates></gml:Point>, 2)",
		  		new QNm("GEO0006"));
	  runError("geo:geometryN(text {'a'}, <gml:Point><gml:coordinates>2,3" +
	  		"</gml:coordinates></gml:Point>)", new QNm("FORG0006"));
  }
  
  /** Test method. */
  @Test
  public void x() {
	  
	  runQuery("geo:x(<gml:Point><gml:coordinates>2,1</gml:coordinates></gml:Point>)",
		  		"2");
	  
	  runError("geo:x(<gml:MultiPoint><gml:Point><gml:coordinates>1,1" +
	  		"</gml:coordinates></gml:Point><gml:Point><gml:coordinates>1,2" +
	  		"</gml:coordinates></gml:Point></gml:MultiPoint>)",
		  		new QNm("GEO0005"));
	  
	  runError("geo:x(<gml:LinearRing><gml:coordinates>0,0 20,0 0,20 0,0</gml:coordinates></gml:LinearRing>)",
			  		new QNm("GEO0005"));
	  runError("geo:x(<gml:Point><gml:coordinates></gml:coordinates></gml:Point>)",
		  		new QNm("GEO0002"));
	  runError("geo:x(<gml:geo><gml:coordinates>2,1</gml:coordinates></gml:geo>)",
		  		new QNm("GEO0001"));
	  runError("geo:x(text {'a'})", new QNm("FORG0006"));
  }
  
  /** Test method. */
  @Test
  public void y() {
	  
	  runQuery("geo:y(<gml:Point><gml:coordinates>2,1</gml:coordinates></gml:Point>)",
		  		"1");
	  runQuery("geo:y(<gml:Point><gml:coordinates>2</gml:coordinates></gml:Point>)",
		  		"NaN");
	  
	  runError("geo:z(<gml:MultiPoint><gml:Point><gml:coordinates>1,1" +
	  		"</gml:coordinates></gml:Point><gml:Point><gml:coordinates>1,2" +
	  		"</gml:coordinates></gml:Point></gml:MultiPoint>)",
		  		new QNm("GEO0005"));
	  
	  runError("geo:y(<gml:LinearRing><gml:coordinates>0,0 20,0 0,20 0,0</gml:coordinates></gml:LinearRing>)",
			  		new QNm("GEO0005"));
	  runError("geo:y(<gml:Point><gml:coordinates></gml:coordinates></gml:Point>)",
		  		new QNm("GEO0002"));
	  runError("geo:y(<gml:geo><gml:coordinates>2,1</gml:coordinates></gml:geo>)",
		  		new QNm("GEO0001"));
	  runError("geo:y(a)", new QNm("XPDY0002"));
  }
  
  /** Test method. */
  @Test
  public void z() {
	  
	  runQuery("geo:z(<gml:Point><gml:coordinates>2,1,3</gml:coordinates></gml:Point>)",
		  		"3");
	  runQuery("geo:z(<gml:Point><gml:coordinates>2</gml:coordinates></gml:Point>)",
		  		"NaN");
	  
	  runError("geo:z(<gml:MultiPoint><gml:Point><gml:coordinates>1,1" +
	  		"</gml:coordinates></gml:Point><gml:Point><gml:coordinates>1,2" +
	  		"</gml:coordinates></gml:Point></gml:MultiPoint>)",
		  		new QNm("GEO0005"));
	  
	  runError("geo:z(<gml:LinearRing><gml:coordinates>0,0 20,0 0,20 0,0</gml:coordinates></gml:LinearRing>)",
			  		new QNm("GEO0005"));
	  runError("geo:z(<gml:Point><gml:coordinates></gml:coordinates></gml:Point>)",
		  		new QNm("GEO0002"));
	  runError("geo:z(<gml:geo><gml:coordinates>2,1</gml:coordinates></gml:geo>)",
		  		new QNm("GEO0001"));
	  runError("geo:z(a)", new QNm("XPDY0002"));
  }
  
  /** Test method. */
  @Test
  public void length() {
	  
	  runQuery("geo:length(<gml:Point><gml:coordinates>2,1,3</gml:coordinates></gml:Point>)",
		  		"0");
	  runQuery("geo:length(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>" +
	  		"</gml:outerBoundaryIs></gml:Polygon>)",
		  		"9.07768723046357");
	  
	  runQuery("geo:length(<gml:MultiPoint><gml:Point><gml:coordinates>1,1" +
	  		"</gml:coordinates></gml:Point><gml:Point><gml:coordinates>1,2" +
	  		"</gml:coordinates></gml:Point></gml:MultiPoint>)",
		  		"0");
	  
	  runError("geo:length(<gml:LinearRing><gml:coordinates>0,0 0,20 0,0</gml:coordinates></gml:LinearRing>)",
			  		new QNm("GEO0002"));
	  runError("geo:length(<gml:Point><gml:coordinates></gml:coordinates></gml:Point>)",
		  		new QNm("GEO0002"));
	  runError("geo:length(<gml:geo><gml:coordinates>2,1</gml:coordinates></gml:geo>)",
		  		new QNm("GEO0001"));
  }
  
  /** Test method. */
  @Test
  public void startPoint() {
	  
	  runQuery("geo:startPoint(<gml:LinearRing><gml:coordinates>1,1 20,1 20,20 1,1" +
	  		"</gml:coordinates></gml:LinearRing>)",
			  "<gml:Point srsName=\"0\"><gml:coordinates>1.0,1.0</gml:coordinates></gml:Point>");
	  runQuery("geo:startPoint(<gml:LineString><gml:coordinates>1,1 20,1 20,20 1,1" +
	  		"</gml:coordinates></gml:LineString>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>1.0,1.0</gml:coordinates></gml:Point>");
	  
	  runError("geo:startPoint(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>)",
	  		new QNm("GEO0004"));
	  
	  runError("geo:startPoint(<gml:LineString><gml:coordinates>1,1</gml:coordinates></gml:LineString>)",
			  		new QNm("GEO0002"));
	  runError("geo:startPoint()", new QNm("XPST0017"));
	  runError("geo:startPoint(text {'gml:Point'})", new QNm("FORG0006"));
	  runError("geo:startPoint(<gml:geo><gml:coordinates>2,1</gml:coordinates></gml:geo>)",
		  		new QNm("GEO0001"));
  }
  
  /** Test method. */
  @Test
  public void endPoint() {
	  
	  runQuery("geo:endPoint(<gml:LinearRing><gml:coordinates>2,3 20,1 20,20 2,3" +
	  		"</gml:coordinates></gml:LinearRing>)",
			  "<gml:Point srsName=\"0\"><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>");
	  runQuery("geo:endPoint(<gml:LineString><gml:coordinates>11,10 20,1 20,20 12,13" +
	  		"</gml:coordinates></gml:LineString>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>12.0,13.0</gml:coordinates></gml:Point>");
	  
	  runError("geo:endPoint(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>)",
	  		new QNm("GEO0004"));
	  
	  runError("geo:endPoint(<gml:LineString><gml:coordinates>1,1</gml:coordinates></gml:LineString>)",
			  		new QNm("GEO0002"));
	  runError("geo:endPoint()", new QNm("XPST0017"));
	  runError("geo:endPoint(text {'gml:Point'})", new QNm("FORG0006"));
	  runError("geo:endPoint(<gml:geo><gml:coordinates>2,1</gml:coordinates></gml:geo>)",
		  		new QNm("GEO0001"));
  }
  
  /** Test method. */
  @Test
  public void isClosed() {
	  
	  runQuery("geo:isClosed(<gml:LinearRing><gml:coordinates>2,3 20,1 20,20 2,3" +
	  		"</gml:coordinates></gml:LinearRing>)",
			  "true");
	  runQuery("geo:isClosed(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>)",
		  		"false");
	  
	  runQuery("geo:isClosed(<gml:LineString><gml:coordinates>11,10 20,1 20,20 12,13" +
		  		"</gml:coordinates></gml:LineString>)", "false");
	 
	  runError("geo:isClosed(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>1,1 2,1 5,3 1,1</gml:coordinates></gml:LinearRing>" +
	  		"</gml:outerBoundaryIs></gml:Polygon>)",
	  		new QNm("GEO0004"));
	  
	  runError("geo:isClosed(<gml:LineString><gml:coordinates>1,1</gml:coordinates></gml:LineString>)",
			  		new QNm("GEO0002"));
	  runError("geo:isClosed()", new QNm("XPST0017"));
	  runError("geo:isClosed(text {'gml:Point'})", new QNm("FORG0006"));
	  runError("geo:isClosed(<Point><gml:coordinates>2,1</gml:coordinates></Point>)",
		  		new QNm("GEO0001"));
  }
  
  /** Test method. */
  @Test
  public void isRing() {
	  
	  runQuery("geo:isRing(<gml:LinearRing><gml:coordinates>2,3 20,1 20,20 2,3" +
	  		"</gml:coordinates></gml:LinearRing>)",
			  "true");
	  
	  runQuery("geo:isRing(<gml:LineString><gml:coordinates>11,10 20,1 20,20 12,13" +
		  		"</gml:coordinates></gml:LineString>)", "false");
	 
	  runError("geo:isRing(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
		  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
		  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
		  		"</gml:MultiLineString>)",
		  		new QNm("GEO0004"));
		  
	  runError("geo:isClosed(<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)",
	  		new QNm("GEO0004"));
	  
	  runError("geo:isRing(<gml:LineString><gml:coordinates>1,1</gml:coordinates></gml:LineString>)",
			  		new QNm("GEO0002"));
	  runError("geo:isRing()", new QNm("XPST0017"));
	  runError("geo:isRing(text {'gml:Point'})", new QNm("FORG0006"));
	  runError("geo:isRing(<Point><gml:coordinates>2,1</gml:coordinates></Point>)",
		  		new QNm("GEO0001"));
  }
  
  /** Test method. */
  @Test
  public void numPoints() {
	  
	  runQuery("geo:numPoints(<gml:LinearRing><gml:coordinates>2,3 20,1 20,20 2,3" +
	  		"</gml:coordinates></gml:LinearRing>)",
			  "4");
	  
	  runQuery("geo:numPoints(<gml:LineString><gml:coordinates>11,10 20,1 20,20 12,13" +
		  		"</gml:coordinates></gml:LineString>)", "4");
	 
	  runQuery("geo:numPoints(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
		  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
		  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
		  		"</gml:MultiLineString>)",
		  		"6");
		  
	  runError("geo:numPoints(<gml:Point><gml:coordinates>2,3</gml:coordinates></gml:Point>)",
	  		new QNm("GEO0004"));
	  
	  runError("geo:numPoints(<gml:LineString><gml:coordinates>1,1</gml:coordinates></gml:LineString>)",
			  		new QNm("GEO0002"));
	  runError("geo:numPoints()", new QNm("XPST0017"));
	  runError("geo:numPoints(text {'gml:Point'})", new QNm("FORG0006"));
	  runError("geo:numPoints(<Point><gml:coordinates>2,1</gml:coordinates></Point>)",
		  		new QNm("GEO0001"));
  }
  
  /** Test method. */
  @Test
  public void pointN() {
	  
	  runQuery("geo:pointN(<gml:LinearRing><gml:coordinates>2,3 20,1 20,20 2,3" +
	  		"</gml:coordinates></gml:LinearRing>, 1)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>");
	  
	  runQuery("geo:pointN(<gml:LineString><gml:coordinates>11,10 20,1 20,20 12,13" +
		  		"</gml:coordinates></gml:LineString>, 4)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>12.0,13.0</gml:coordinates></gml:Point>");
	  
	  runError("geo:pointN(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
		  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
		  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
		  		"</gml:MultiLineString>, 4)",
		  		new QNm("GEO0004"));
	  
	  runError("geo:pointN(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>,1)",
		  		new QNm("GEO0001"));
	  runError("geo:pointN(<gml:LineString><gml:coordinates>11,10 20,1 20,20 12,13" +
		  		"</gml:coordinates></gml:LineString>, 5)",
		  		new QNm("GEO0006"));
	  runError("geo:pointN(<gml:LineString><gml:coordinates>11,10 20,1 20,20 12,13" +
		  		"</gml:coordinates></gml:LineString>, 0)",
		  		new QNm("GEO0006"));
	  runError("geo:pointN(text {'a'},3)", new QNm("FORG0006"));
  }
 
  /** Test method. */
  @Test
  public void area() {
	  
	  runQuery("geo:area(<gml:MultiPoint><gml:Point><gml:coordinates>1,1" +
	  		"</gml:coordinates></gml:Point><gml:Point><gml:coordinates>1,2" +
	  		"</gml:coordinates></gml:Point></gml:MultiPoint>)",
		  		"0");
	  runQuery("geo:area(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates></gml:LinearRing>" +
	  		"</gml:outerBoundaryIs></gml:Polygon>)",
		  		"49");
	  
	  runQuery("geo:area(<gml:LineString><gml:coordinates>" +
	  		"11,10 20,1 20,20</gml:coordinates></gml:LineString>)",
		  		"0");
	  
	  runError("geo:area(<gml:LinearRing><gml:coordinates>0,0 0,20 0,0</gml:coordinates></gml:LinearRing>)",
			  		new QNm("GEO0002"));
	  runError("geo:area()",
		  		new QNm("XPST0017"));
	  runError("geo:area(<gml:geo><gml:coordinates>2,1</gml:coordinates></gml:geo>)",
		  		new QNm("GEO0001"));
  }
 
  /** Test method. */
  @Test
  public void centroid() {
	  
	  runQuery("geo:centroid(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>14.5,14.5</gml:coordinates></gml:Point>");
	  
	  runQuery("geo:centroid(<gml:Point><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>)",
			  		"<gml:Point srsName=\"0\"><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>");
	  
	  runQuery("geo:centroid(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>" +
		  		"1.8468564716806986,1.540569415042095</gml:coordinates></gml:Point>");
	  
	  runError("geo:centroid(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>)",
		  		new QNm("GEO0001"));
	  
	  runError("geo:centroid()",
		  		new QNm("XPST0017"));
	  runError("geo:centroid(text {'a'})", new QNm("FORG0006"));
  }
 
  /** Test method. */
  @Test
  public void pointOnSurface() {
	  
	  runQuery("geo:pointOnSurface(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>14.5,14.5</gml:coordinates></gml:Point>");
	  
	  runQuery("geo:pointOnSurface(<gml:Point><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>)",
			  		"<gml:Point srsName=\"0\"><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>");
	  
	  runQuery("geo:pointOnSurface(<gml:MultiLineString><gml:LineString><gml:coordinates>" +
	  		"1,1 0,0 2,1</gml:coordinates></gml:LineString><gml:LineString>" +
	  		"<gml:coordinates>2,1 3,3 4,4</gml:coordinates></gml:LineString>" +
	  		"</gml:MultiLineString>)",
		  		"<gml:Point srsName=\"0\"><gml:coordinates>" +
		  		"3.0,3.0</gml:coordinates></gml:Point>");
	  
	  runError("geo:pointOnSurface(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>)",
		  		new QNm("GEO0001"));
	  
	  runError("geo:pointOnSurface()", new QNm("XPST0017"));
	  runError("geo:pointOnSurface(text {'a'})", new QNm("FORG0006"));
  }
 
  /** Test method. */
  @Test
  public void exteriorRing() {
	  
	  runQuery("geo:exteriorRing(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>)",
		  		"<gml:LineString srsName=\"0\"><gml:coordinates>" +
		  		"11.0,11.0 18.0,11.0 18.0,18.0 11.0,18.0 11.0,11.0</gml:coordinates></gml:LineString>");
	  
	  runError("geo:exteriorRing(<gml:Point><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>)",
			  new QNm("GEO0003"));
	  
	  runError("geo:exteriorRing(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>)",
		  		new QNm("GEO0001"));
	  
	  runError("geo:exteriorRing()", new QNm("XPST0017"));
	  runError("geo:exteriorRing(text {'a'})", new QNm("FORG0006"));
  }
 
  /** Test method. */
  @Test
  public void numInteriorRing() {
	  
	  runQuery("geo:numInteriorRing(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates>" +
	  		"</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>)",
		  		"0");
	  
	  runError("geo:numInteriorRing(<gml:Point><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>)",
			  new QNm("GEO0003"));
	  
	  runError("geo:numInteriorRing(<gml:unknown><gml:coordinates>1,1</gml:coordinates></gml:unknown>)",
		  		new QNm("GEO0001"));
	  
	  runError("geo:numInteriorRing()", new QNm("XPST0017"));
	  runError("geo:numInteriorRing(text {'a'})", new QNm("FORG0006"));
  }
 
  /** Test method. */
  @Test
  public void interiorRingN() {
	  
	  runQuery("geo:interiorRingN(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates>" +
	  		"</gml:LinearRing></gml:outerBoundaryIs><gml:innerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>2,2 3,2 3,3 2,3 2,2</gml:coordinates></gml:LinearRing>" +
	  		"</gml:innerBoundaryIs><gml:innerBoundaryIs><gml:LinearRing><gml:coordinates>" +
	  		"10,10 20,10 20,20 10,20 10,10</gml:coordinates></gml:LinearRing>" +
	  		"</gml:innerBoundaryIs></gml:Polygon>, 1)",
		  		"<gml:LineString srsName=\"0\"><gml:coordinates>" +
		  		"2.0,2.0 3.0,2.0 3.0,3.0 2.0,3.0 2.0,2.0</gml:coordinates>" +
		  		"</gml:LineString>");
	  
	  runError("geo:interiorRingN(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
		  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates>" +
		  		"</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>, 1)",
			  		new QNm("GEO0006"));
	  
	  runError("geo:interiorRingN(<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>" +
	  		"<gml:coordinates>11,11 18,11 18,18 11,18 11,11</gml:coordinates>" +
	  		"</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>, 0)",
		  		new QNm("GEO0006"));
	  
	  runError("geo:interiorRingN(<gml:Point><gml:coordinates>2.0,3.0</gml:coordinates></gml:Point>, 1)",
			  new QNm("GEO0003"));
	  
	  runError("geo:interiorRingN(text {'<gml:Polygon/'}, 1)", new QNm("FORG0006"));
	  runError("geo:interiorRingN()", new QNm("XPST0017"));
  }
 
  /**
   * Query.
   * @param query query
   * @param result result
   */
  private void runQuery(String query, String result) {
	  query("import module namespace geo='http://basex.org/geo/GeoModule'; " +
        "declare namespace gml='http://www.opengis.net/gml';" + query, result);
  }
  
  /**
   * Checks if a query yields the specified error code.
   * @param query query string
   * @param error expected error
   */
  static void runError(final String query, final QNm error) {
	final String q = "import module namespace geo='http://basex.org/geo/GeoModule'; " +
	        "declare namespace gml='http://www.opengis.net/gml';" + query;
	  
    final QueryProcessor qp = new QueryProcessor(q, context);
    qp.ctx.sc.baseURI(".");
    try {
      final String res = qp.execute().toString().replaceAll("(\\r|\\n) *", "");
      fail("Query did not fail:\n" + query + "\n[E] " +
          error + "...\n[F] " + res);
    } catch(final QueryException ex) {
      check(ex, error);
    } finally {
      qp.close();
    }
  }

  /**
   * Checks if an exception yields one of the specified error codes.
   * @param ex exception
   * @param error expected errors
   */
  static void check(final QueryException ex, final QNm error) {
    final byte[] msg = Token.token(ex.getMessage());
    if(!Token.contains(msg, error.local()))
    	fail('\'' + Token.string(error.string()) +
          "' not contained in '" + Token.string(msg) + "'.");
  }
}
