/*******************************************************************************
 * Project		:	itce600
 * Package		:	kr.ac.postech.itce
 * Programmer	:	GUNiNE (gunine@postech.ac.kr)
 * Description	:	
 * Create Date	:	2013. 9. 16.
 * Update Date	:	2013. 9. 16.
 * -----------------------------------------------------------------------------
 * TODO
 ******************************************************************************/
package kr.ac.postech.itce;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.postech.itce.model.Compact;
import kr.ac.postech.itce.model.Flow;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;


public class SimpleTopoMod
{
	public static final String HOST_PROT = "http";
	public static final String HOST_URL = "127.0.0.1";
	public static final Integer HOST_PORT = 8080;

	public static final String REST_FLOW_MOD_URL = "/wm/staticflowentrypusher/";
	public static final String REST_FLOW_LIST_URL = "/wm/staticflowentrypusher/list/all/";
	public static final String REST_FLOW_CLEAR_URL = "/wm/staticflowentrypusher/clear/all/";
	public static final String REST_FLOW_SUFFIX = "json";
	
	private String routeType = new String();
	
	private Log log = LogFactory.getLog( SimpleTopoMod.class );
	
	public static void main( String[] args ) throws Exception
	{
		if( args.length == 0 ) showErrorMsg();
		
		// in the case of initialize flow tables
		if( args[0].equals( "init" ) )
		{
			if( args.length != 2 ) showErrorMsg();
			
			String type = args[1]; // can be either route1 or route2
			
			SimpleTopoMod stm = new SimpleTopoMod();
			List< Compact > compacts = stm.genCompactInfos( type );
			List< Flow > flows = stm.genCustomFlows( compacts );
			
			// add all prefix flows into flow tables
			for ( Flow flow : flows )
			{
				stm.insertFlowToSwitch( createJsonString( flow ) );
			}
		}
		else if( args[0].equals( "clear" ) )
		{
			if( args.length > 1 ) showErrorMsg();
			
			SimpleTopoMod stm = new SimpleTopoMod();
			
			stm.removeAllFlows();
		}
		else showErrorMsg();
	}
	
	/**
	 * Show program usages...
	 */
	public static void showErrorMsg()
	{
		System.err.println( "Error: Wrong number of arguments is specified!" );
		System.err.println( "Usage: simpleTopoMod.sh command "
									 + "[typeOfRoute(e.g., route1, route2)]" );
		System.err.println( "Command can be either init or clear..." );
		System.exit( 1 );
	}
	
	public List< Compact > genCompactInfos( String type )
	{
		List< Compact > compacts = new ArrayList< Compact >();
		
		if( StringUtils.equals( type, "route1" ) )
		{
			Compact switch1_1 = new Compact( "1_1", "01", "2", 1, "2" );
			Compact switch1_2 = new Compact( "1_2", "01", "1", 1, "1" );
			
			Compact switch2_1 = new Compact( "2_1", "02", "2", 1, "2" );
			Compact switch2_2 = new Compact( "2_2", "02", "1", 1, "1" );
			
			Compact switch3_1 = new Compact( "3_1", "03", "2", 1, "2" );
			Compact switch3_2 = new Compact( "3_2", "03", "1", 1, "1" );
			
			Compact switch5_1 = new Compact( "4_1", "05", "3", 1, "2" );
			Compact switch5_2 = new Compact( "4_2", "05", "1", 1, "1" );
			
			compacts.add( switch1_1 );
			compacts.add( switch1_2 );
			compacts.add( switch2_1 );
			compacts.add( switch2_2 );
			compacts.add( switch3_1 );
			compacts.add( switch3_2 );
			compacts.add( switch5_1 );
			compacts.add( switch5_2 );
		}
		else if( StringUtils.equals( type, "route2" ) )
		{
			Compact switch1_1 = new Compact( "1_1", "01", "3", 1, "2" );
			Compact switch1_2 = new Compact( "1_2", "01", "1", 1, "1" );
			
			Compact switch4_1 = new Compact( "4_1", "04", "2", 1, "2" );
			Compact switch4_2 = new Compact( "4_2", "04", "1", 1, "1" );
			
			Compact switch5_1 = new Compact( "5_1", "05", "3", 1, "2" );
			Compact switch5_2 = new Compact( "5_2", "05", "2", 1, "1" );
			
			compacts.add( switch1_1 );
			compacts.add( switch1_2 );
			compacts.add( switch4_1 );
			compacts.add( switch4_2 );
			compacts.add( switch5_1 );
			compacts.add( switch5_2 );
		}
		return compacts;
	}
	
	public List< Flow > genCustomFlows( List< Compact > compacts )
	{
		List< Flow > flows = new ArrayList< Flow >();
		
		for( Compact c : compacts )
		{
			for( int i = 0; i < 2; i ++ )
			{
				String etherType = new String();
				if( i == 0 ) etherType = "0x0800";
				else etherType = "0x0806";

				Flow f = new Flow();
				f.setName( "flow_mod_" + c.getName() + "_" + i );
				f.setSwitch( "00:00:00:00:02:" + c.getMac() );
				f.setActions( "output=" + c.getActions() );
				f.setDst_ip( "10.0.1." + c.getDst_ip() );
				f.setPriority( c.getPriority() );
				f.setEther_type( etherType );
			
				flows.add( f );
			}
		}
		
		return flows;
	}
	
	/**
	 * Remove all flows stored in flow tables of all switches
	 * 
	 * @throws Exception
	 */
	public void removeAllFlows() throws Exception
	{
		HttpURLConnection conn = makeGetConnection( REST_FLOW_CLEAR_URL
														+ REST_FLOW_SUFFIX );
		conn.connect();
		String res = printResponse( conn );
		
		if( StringUtils.isEmpty( StringUtils.stripToEmpty( res ) ) )
		{
			res = "Successfully remove all flow entries!";
		}
		
		log.debug( res );
	}
	
	/**
	 * Insert a flow entry into flow table of corresponding switch
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void insertFlowToSwitch( String data ) throws Exception
	{
		HttpURLConnection conn = makePostConnection( REST_FLOW_MOD_URL
				+ REST_FLOW_SUFFIX );

		OutputStreamWriter writer = new OutputStreamWriter(
				conn.getOutputStream() );

		writer.write( data );
		writer.flush();
		writer.close();

		String res = printResponse( conn );
		
		log.debug( res );
	}
	
	/**
	 * Make HTTP GET Request
	 * 
	 * @param path url path
	 * @return HttpURLConnection
	 * @throws Exception
	 */
	public HttpURLConnection makeGetConnection( String path ) throws Exception
	{
		return makeConnection( path, "GET" );
	}

	/**
	 * Make HTTP POST Request
	 * 
	 * @param path url path
	 * @return HttpURLConnection
	 * @throws Exception
	 */
	public HttpURLConnection makePostConnection( String path ) throws Exception
	{
		return makeConnection( path, "POST" );
	}

	/**
	 * Make HTTP DELETE Request
	 * 
	 * @param path url path
	 * @return HttpURLConnection
	 * @throws Exception
	 */
	public HttpURLConnection makeDeleteConnection( String path )
			throws Exception
	{
		return makeConnection( path, "DELETE" );
	}

	/**
	 * Make regular HTTP connection
	 * 
	 * @param path url path
	 * @param type request method type
	 * @return HttpURLConnection
	 * @throws Exception
	 */
	private HttpURLConnection makeConnection( String path, String type )
			throws Exception
	{
		URL url = new URL( HOST_PROT, HOST_URL, HOST_PORT, path );
		HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
		httpcon.setDoOutput( true );
		httpcon.setRequestProperty( "Content-Type", "application/json" );
		httpcon.setRequestProperty( "Accept", "application/json" );
		httpcon.setRequestMethod( type );
		httpcon.connect();

		return httpcon;
	}
	
	/**
	 * Print out the Http response message
	 * 
	 * @param conn HttpURLConnection
	 * @return response output as string
	 * @throws Exception
	 */
	private String printResponse( HttpURLConnection conn ) throws Exception
	{
		BufferedReader reader = new BufferedReader( new InputStreamReader(
				new BufferedInputStream( conn.getInputStream() ) ) );
		
		StringBuilder res = new StringBuilder( 4096 );

		String read = reader.readLine();
		while ( read != null )
		{
			res.append( read );
			res.append( '\n' );
			read = reader.readLine();
		}

		reader.close();
		
		return res.toString();
	}

	/**
	 * Generate plain text in JSON format
	 * 
	 * @param object JSON object
	 * @return JSON formatted string
	 * @throws IOException
	 */
	public static String createJsonString( Object object ) throws IOException
	{
		Writer writer = new StringWriter();
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createJsonGenerator( writer );
		generator.setCodec( new ObjectMapper() );
		generator.writeObject( object );
		generator.close();

		// try to replace the '_' to '-'
		String jsonString = StringUtils.replace( writer.toString(), "_", "-" );

		System.out.println( jsonString );
		return jsonString;
	}
}
