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
 * @author Masoumeh
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
	//  runError("geo:isSimple<gml:LinearRing><gml:coordinates>1,1 55,99 2,1</gml:coordinates></gml:LinearRing>",
	//		  new QNm("GEO0002"));
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
  static void runError(final String query, final QNm err) {
	final String q = "import module namespace geo='http://basex.org/geo/GeoModule'; " +
	        "declare namespace gml='http://www.opengis.net/gml';" + query;
	  
    final QueryProcessor qp = new QueryProcessor(q, context);
    qp.ctx.sc.baseURI(".");
    try {
      final String res = qp.execute().toString().replaceAll("(\\r|\\n) *", "");
      fail("Query did not fail:\n" + query + "\n[E] " +
          err + "...\n[F] " + res);
    } catch(final QueryException ex) {
      check(ex, err);
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
