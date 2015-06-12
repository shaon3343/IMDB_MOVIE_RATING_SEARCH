package imdbsearching;
//import java.net.*;
//import java.sql.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.io.*;
//import org.json.JSONArray;
import java.util.Iterator;
import java.util.List;

/************ Class for Runnig and testing test ******************/
/*
class running_test {
	public void Run_test() throws Exception
	{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		   Calendar cal = Calendar.getInstance();
		   System.out.println(dateFormat.format(cal.getTime()));
		InputStreamReader sysin=new InputStreamReader(System.in);
		
		BufferedReader inputsys=new BufferedReader(sysin);
		
		String movie_title="";
		
		System.out.println("enter movie title: ");
		long milliseconds1= cal.getTimeInMillis(); 
		
 		movie_title=inputsys.readLine();
 		
 		Imdb testimdb=new Imdb();
		testimdb.imdb_search_print(movie_title);
 		
		inputsys.close();	

		
		cal = Calendar.getInstance();
		long milliseconds2 = cal.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		System.out.println("total time=>"+diff);
	}

}  */



public class Imdb {
		public String[] imdb_search_print(String movie_title)throws Exception{
			
			InetSocketAddress address;
			Getsystemproxy gtproxy=new Getsystemproxy();
			address=gtproxy.getsysproxy();
			
			if(address!=null)
			{
				System.setProperty("java.net.useSystemProxies", "true");				//System.out.println("proxy hostname : " + address.getHostName());
                System.setProperty("http.proxyHost", address.getHostName());
              //  System.out.println("proxy port : " + address.getPort());
                System.setProperty("http.proxyPort", Integer.toString(address.getPort()));

			}
			//address=gtproxy.getsysproxy();
			URL imdb = new URL("http://www.omdbapi.com/?t="+ movie_title); // + "&r=json"
			System.out.println("### SEARCH API: "+imdb);
			URLConnection OC = imdb.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(OC.getInputStream(), Charset.forName("UTF-8")));
			String inputLine;
			int k;
			String strinout[]=new String [3000];
			String movie[]=new String[10];
			inputLine = in.readLine();
			strinout=inputLine.split("\"");
		 
		/***********niche in.close er por strinout array ta Return kora hoiche , kintu
		 ekhon mone hocche string array ta kat-chat kore pathale valo hoebe ******************/
		
	//			System.out.println(inputLine);
		k=0;
		for(int i=0;i<strinout.length;i++)
		{
			if(strinout[i].contains("Title"))
			{
				movie[k]=strinout[i]+strinout[i+1]+strinout[i+2]+"\n";
				k++;
				
			}
			if(strinout[i].contains("Year"))
			{
				movie[k]=strinout[i]+strinout[i+1]+strinout[i+2]+"\n";
				k++;
			}
			if(strinout[i].contains("Rating"))
			{
				movie[k]=strinout[i]+strinout[i+1]+strinout[i+2]+"\n";
				k++;
			}
			if(strinout[i].contains("Director"))
				{
				movie[k]=strinout[i]+strinout[i+1]+strinout[i+2]+"\n";
				k++;
				
				}
			if(strinout[i].contains("Actor"))
				{
				movie[k]=strinout[i]+strinout[i+1]+strinout[i+2]+"\n";
				k++;
				}
			if(strinout[i].contains("Genre"))
			{
				movie[k]=strinout[i]+strinout[i+1]+strinout[i+2]+"\n";
				k++;
			}
		}
		in.close();

		return movie;
		

	}
		/******** Run the test from command line ==>  ***************/
	/*	
	public static void main(String[] args) throws Exception
	{
		running_test testing=new  running_test();
		testing.Run_test();
	}  */
}
		



