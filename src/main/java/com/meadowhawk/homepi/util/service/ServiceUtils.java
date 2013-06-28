package com.meadowhawk.homepi.util.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * Helper methods for performing static tasks for the Service layer.
 * @author lee
 */
public class ServiceUtils {

	/**
	 * Returns the UI root path, used for redirecting to the UI when absolutely have to do it. This may onyl be one case but broke out the code just in case this is needed again.
	 * @return
	 * @throws URISyntaxException 
	 */
	public static URI getUIResource(UriInfo uriInfo, String resourceFileName) throws URISyntaxException{
		String newUri = "";
		if(uriInfo != null){
			return ServiceUtils.getUIResource(uriInfo.getRequestUri(), resourceFileName, null);
		}
		
		return new URI(newUri);
	}
	
	/**
	 * Returns the UI root path, used for redirecting to the UI when absolutely have to do it. This may onyl be one case but broke out the code just in case this is needed again.
	 * @return
	 * @throws URISyntaxException 
	 */
	public static URI getUIResource(UriInfo uriInfo, String resourceFileName, Map<String, String> queryParams) throws URISyntaxException{
		String newUri = "";
		if(uriInfo != null){
			return ServiceUtils.getUIResource(uriInfo.getRequestUri(), resourceFileName,queryParams);
		}
		
		return new URI(newUri);
	}
	
	/**
	 * @param uriBuilder
	 * @param resourceFileName
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI getUIResource(URI uri, String resourceFileName, Map<String, String> queryParams) throws URISyntaxException{
		String uriStr = "";
		if(uri != null){
			uriStr = uri.toASCIIString();
			uriStr = uriStr.substring(0, uriStr.indexOf(uri.getPath()));
		}
		UriBuilder uriB = UriBuilder.fromPath(uriStr);
		if(queryParams != null){
			for (String key : queryParams.keySet()) {
				uriB.queryParam(key, queryParams.get(key));
			}						
		}
		return uriB.path(resourceFileName).build();
	}
}

