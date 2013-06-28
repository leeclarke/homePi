/**
 *
 *
 * Copyright (c) 2012-2013 Lee Clarke
 *
 * LICENSE:
 *
 * This file is part of HomePi (http://homepi.org).
 *
 * HomePi is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any
 * later version.
 *
 * HomePi is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with HomePi.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 *
 * @author Lee Clarke <lees2bytes[at]gmail[dot]com>
 * @license http://www.gnu.org/licenses/gpl.html
 * @copyright 2012-2013 Lee Clarke
 */
package com.meadowhawk.homepi.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.service.business.DeviceManagementService;
import com.meadowhawk.homepi.service.business.HomePiUserService;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.model.GoogleInfo;
import com.meadowhawk.homepi.util.model.PublicRESTDoc;
import com.meadowhawk.homepi.util.model.PublicRESTDocMethod;
import com.meadowhawk.homepi.util.service.AppConfigService;
import com.meadowhawk.homepi.util.service.ServiceUtils;
import com.sun.jersey.api.view.Viewable;

@Path("/user")
@Component
@PublicRESTDoc(serviceName = "UserRESTService", description = "User focused management services.")
public class UserRESTService {
	private static Logger log = Logger.getLogger( UserRESTService.class );
	
	@Context UriInfo uriInfo;
	
	@Autowired
	AppConfigService appConfigService;
	
	@Autowired
	HomePiUserService userService;
	
	@Autowired
	DeviceManagementService managementService;
	
	private static final String ACCESS_TOKEN = "access_token"; //First pass authorization token used to verify on second auth check. see google oAuth docs for more info
	private static final String GOOGLE_USER_INFO_LINK = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=";
	private static final String GRANT_TYPE_AUTH = "authorization_code";
	private static final String AUTH_CALLBACK_URI = "https://accounts.google.com/o/oauth2/token";
	
	private String firstAuthUri;
	
	@Context UriInfo ui;
	
	//The following is set from the System Properties you know  -Dgoogle_auth_client_id
	@Value("#{systemProperties['google_auth_client_id']}")
	String google_auth_client_id;
	
	@Value("#{systemProperties['google_auth_client_secret']}")
	String google_auth_client_secret;
	
	@GET
	@Path("/profile/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName = "User Profile", description = "Retrieve user profile. Include access_token in head to gain owner view.", sampleLinks = { "/user/profile/test_user" })
	public Response getUser(@PathParam("user_id") String userId, @HeaderParam(ACCESS_TOKEN) String authToken){
		if(!StringUtil.isNullOrEmpty(userId)){
			//get authfrom request or set to null
			HomePiUser hUser = userService.getUserData(userId, authToken);
			return Response.ok(hUser).build(); 
		} else {
			throw new HomePiAppException(Status.NOT_FOUND,"Invalid user ID");
		}
		
	}
	
	@POST
	@Path("/profile/{user_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName = "Update User Profile", description = "Update profile. Include access_token in head or will return error.", sampleLinks = { "/user/profile/test_user" })
	public Response updateUserInfo(HomePiUser updateUser, @PathParam("user_id") String userName, @HeaderParam(ACCESS_TOKEN) String authToken){
		HomePiUser hUser = userService.updateUserData(userName, authToken, updateUser);
		return Response.ok(hUser).build();
	}
	
	@GET
	@Path("/googleauth")
	@PublicRESTDocMethod(endPointName = "Google Auth Request", description = "Initiates the OAuth requests for authentication with google doing redirect to google account accept page.", sampleLinks = { "/user/googleauth" })
	public Response makeGoogleAuthRequest(@Context HttpServletRequest request) {

		URI location;
		try {
			request.getHeaderNames();
			location = new URI(getFirstAuthReqURIui());

			return Response.temporaryRedirect(location).build();
		} catch (URISyntaxException e) {
			throw new HomePiAppException(Status.BAD_REQUEST, "Unable to authenticate.", e);
		}
	}


	@GET
	@Path("/gauthcallback")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName = "Google Auth Callback", description = "Google auth request, provides a callback for google oAuth and shouldnt be called directly elsewhere.", sampleLinks = { "/user/goocallback" })
	public Response googleAuthCallback(@QueryParam("state") String state, @QueryParam("code") String code, @QueryParam("error") String error) {
		GoogleInfo user = null;
		if (error != null) {			
			throw new HomePiAppException(Status.UNAUTHORIZED);
		} else {
			Map<String, String> auth = requestSecondStageRequest(state, code);
			user = getUserInfo(auth);
		}
		HomePiUser hUser = null;
		if(user != null && !StringUtil.isNullOrEmpty(user.getEmail())){
			 hUser = userService.getUserFromGoogleAuth(user);
		} else {
			throw new HomePiAppException(Status.UNAUTHORIZED,"Auth was unauthorized or incomplete.");
		}
		
		try {
			Map<String,String> params = new HashMap<String, String>();
			params.put("auth_token", hUser.getGoogleAuthToken());
			params.put("user_id", ""+hUser.getUserId());
			URI dashboard = ServiceUtils.getUIResource(uriInfo, "dashboard.html", params);

			return Response.seeOther(dashboard).build();
		} catch (UriBuilderException e) {
			log.error("Failed to create URI to html page! " , e);
			throw new HomePiAppException(Status.BAD_REQUEST, "Unable to authenticate.", e);
			//TODO Add redirect to 404 page
			
		} catch (URISyntaxException e) {
			log.error("Can't build URI to web resource.",e);
			throw new HomePiAppException(Status.INTERNAL_SERVER_ERROR, "Unable to create redirect.", e);
		}
		
	}

	
	/**
	 * Retrieves G+ userInfo so we can figure out how they are. 
	 * @param auth - initial auth response.
	 * @return
	 */
	protected GoogleInfo getUserInfo(Map<String, String> auth) {
		if(auth.containsKey(ACCESS_TOKEN)){
			GoogleInfo userInfo = null;
			String accessToken = auth.get(ACCESS_TOKEN);
			ClientResource cr = new ClientResource(GOOGLE_USER_INFO_LINK+accessToken);
			Representation response = null;
			try {
				response = cr.get();
				if(cr.getStatus().isSuccess()){
					if(response != null){
						userInfo = GoogleInfo.buildGoogleInfo(response.getText());
						userInfo.setAuth_token(accessToken);
					}
				} else{
					//TODO: Something has gone wrong or need to reauth? Not sure we ever get here.
					log.debug("Secondary auth request failed: " + cr.getStatus());
				}				
			} catch (ResourceException re) {
				log.warn("GAuth Error - Request error:",re);
				throw new HomePiAppException(Status.BAD_REQUEST, "Unable to retrieve user info from auth service.");
			} catch (IOException e) {
				log.warn("GAuth Error",e);
				throw new HomePiAppException(Status.BAD_REQUEST, "Unable to retrieve user info from auth service.");
			}
			return userInfo;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> requestSecondStageRequest(String state, String code) {
		if (StringUtil.isNullOrEmpty(state) || StringUtil.isNullOrEmpty(code)) {
			throw new HomePiAppException(Status.PRECONDITION_FAILED,
					"Invalid response from google auth.");
		}
		
			Form form = new Form();
			form.set("code", code);
			form.set("client_id", google_auth_client_id);
			form.set("client_secret", google_auth_client_secret);
			form.set("redirect_uri", getGoogleRedirectURL(ui));
			form.set("grant_type", GRANT_TYPE_AUTH);

			ClientResource cr = new ClientResource(AUTH_CALLBACK_URI);
			
			Representation response = null;
			try {
				response = cr.post(form.getWebRepresentation()); 
			} catch (ResourceException re) {
				log.warn("GAuth Error - Request error:",re);
				throw new HomePiAppException(Status.BAD_REQUEST, "Unable to authorize with Google account.", re);
			}
			if (cr.getStatus().isSuccess()) {
				try {
					ObjectMapper om = new ObjectMapper();
					Map<String,String> resp = om.readValue(response.getText(), HashMap.class);
					return resp;
				} catch (IOException e) {
					log.warn("GAuth Error:",e);
					throw new HomePiAppException(Status.PRECONDITION_FAILED, "Unknown response from auth service.", e);
				}
			}
			return new HashMap<String, String>();
	}


	private String getGoogleRedirectURL(UriInfo ui){
		UriBuilder ub = ui.getBaseUriBuilder().path(UserRESTService.class);
		URI gauthCallbackURI = ub.path(UserRESTService.class, "googleAuthCallback").build();
		
		return gauthCallbackURI.toString();
	}
	
	@SuppressWarnings("deprecation")
	private String getGoogleRedirectURLEncoded(UriInfo ui) {
		String callback = getGoogleRedirectURL(ui);
		return URLEncoder.encode(callback);
	}
	
	private String getFirstAuthReqURIui() {
		if(this.firstAuthUri == null){ 
			//TODO:  Break this out so scope can be set depending on if user enables drive usage, calender scheduling etc..
				this.firstAuthUri = "https://accounts.google.com/o/oauth2/auth?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&state=%2Fprofile&redirect_uri="+getGoogleRedirectURLEncoded(ui) +"&response_type=code&client_id="+google_auth_client_id;
		}
		return this.firstAuthUri;
	}
}
