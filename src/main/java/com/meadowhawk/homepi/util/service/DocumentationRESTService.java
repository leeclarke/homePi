package com.meadowhawk.homepi.util.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.service.UserRESTService;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.model.PublicRESTDoc;
import com.meadowhawk.homepi.util.model.PublicRESTDocMethod;
import com.meadowhawk.homepi.util.model.ServiceDocMethodTO;
import com.meadowhawk.homepi.util.model.ServiceDocTO;

/**
 * Generates Documentation on all provided REST services.
 * @author Lee Clarke
 */
@Path("/docs")
@PublicRESTDoc(serviceName = "DocumentationService", description = "Provides Documentation Services for all anotated REST EndPoints so the API can be easily documented from with in the codebase.")
@Component
public class DocumentationRESTService {
	
	private Log log = LogFactory.getLog(DocumentationRESTService.class);
	private static final String CONTENT = "#Content#";
	private static final String DOC_APP_NAME = "#DOC_APP_NAME#";
	private static final String STYLE_LINK = "<link rel=\"stylesheet\" type=\"text/css\" href=\"/services/docs/restDoc.css\">";
	private static final String NO_CACHE = "<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\"><META HTTP-EQUIV=\"Expires\" CONTENT=\"-0\">";
	private static final String INDEX = "<HTML><head>" + NO_CACHE + STYLE_LINK +"</head><body><h2>"+ DOC_APP_NAME +" Service Endpoint Index.</h2><table>"+CONTENT+"</table></body></HTML>";
	
	private static final String END_POINT = "<HTML><head>" + NO_CACHE + STYLE_LINK + "</head><body><h2 class=\"breadcrumb\"><a href=\"../index\">Endpoint Index</a> >> "+ DOC_APP_NAME +" Service Endpoint Details.</h2><table>"+CONTENT+"</table></body></HTML>";
	private static final String DEFAUT_STYLE = "a:link {text-decoration:none; color:#000} a:visited {text-decoration:none; color:#000} a:hover {text-decoration:underline;} TH a:link {color:#FFFFFF;} TH a:visited{text- decoration:none; color:#FFFFFF} TH a:hover {text-decoration:underline;} table.end-point{border-width: 1px; border-spacing: 0px; width: 100%} table.end-point th{vertical-align: top; text-align:left; width:120px} table.end-point td{text-align:left}";
	
	@Context UriInfo uriInfo;
	
	@Value("${apidocs.css.path:}")
	private String altCssLocation;
	
	@Autowired
	DocService docService;
	
	@Value("${apidocs.app_name}")
	private String docAppName = "";
	
	@GET
	@Path("/index")
	@Produces({MediaType.TEXT_HTML})
	@PublicRESTDocMethod(endPointName="Index", description="Generates an HTML page for browsing all supported EndPoints in the REST API.", sampleLinks={"./index"})
	public String getRestServiceDocs() {
		List<ServiceDocTO> serviceDocs = docService.getEndpointDocs();
		StringBuilder indexString = new StringBuilder();
		for (ServiceDocTO serviceDoc : serviceDocs) {
			
			indexString.append("<TR><TH colspan=2 align=\"left\" bgcolor='red'><a href=\"")
				.append(getLocalMethodPath(DocumentationRESTService.class,"getRestServiceEndPointDocs",serviceDoc.getServiceName())).append("\">")
				.append(serviceDoc.getServiceName())   
				.append("</a>")
				.append("</TH></TR>");
				
			indexString.append("<TR><TH align=\"left\">Root Path</TH><TD>")
				.append(serviceDoc.getServicePath())
				.append("</TD></TR>");
			
			indexString.append("<TR><TH align=\"left\">Description</TH><TD>")
				.append(serviceDoc.getServiceDescription())
				.append("</TD></TR>").append("<TR><TD colspan=2>&nbsp;</TD></TR>");
		}
		
		return INDEX.replaceFirst(DOC_APP_NAME, docAppName).replaceFirst(CONTENT, indexString.toString());
	}


	@GET
	@Path("/endpoint/{endPointName}")
	@Produces({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@PublicRESTDocMethod(endPointName="EndPoint Documentation", description="Generates an HTML page for viewing the EndPoints supported by the requested service.", sampleLinks={"./docs/endpoint/DocumentationService"}, errorCodes="404 - If bad service name is used.")
	public String getRestServiceEndPointDocs(@PathParam("endPointName") String endPointName) {
		StringBuilder indexString = new StringBuilder();
		ServiceDocTO serviceDoc = docService.getEndpointDocsByName(endPointName);
			
		indexString.append("<TR><TH colspan=2 align=\"left\" bgcolor='red'><a href=\"")
			.append(getLocalMethodPath(DocumentationRESTService.class,"getRestServiceEndPointDocs",serviceDoc.getServiceName())).append("\">")
			.append(serviceDoc.getServiceName())
			.append("</a>")
			.append("</TH></TR>");
		
		indexString.append("<TR><TH align=\"left\">Description</TH><TD>")
			.append(serviceDoc.getServiceDescription())
			.append("</TD></TR>");
		indexString.append("<TR><TH a20lign=\"left\">Root Path</TH><TD>")
			.append(serviceDoc.getServicePath())
			.append("</TD></TR>").append("<TR><TD colspan=2>&nbsp;</TD></TR>");
		
		//TODO: Output Endpoint Data.
		indexString.append("<TR><TH align=\"left\">&nbsp;</TH><TD>");

		for (ServiceDocMethodTO method : serviceDoc.getMethodDocs()) {
			indexString.append("<table class=\"end-point\"><TR><TH colspan=2 align=\"left\" bgcolor='red'><a href=\"#").append(method.getEndPointName()).append("\">")
				.append(method.getEndPointName()).append("</TH></TR>")
				.append("<TR><th>HTTP Method</th><td>").append(method.getEndPointRequestType()).append("</td></TR>")
				.append("<TR><th>Path</th><td>").append(getLocalMethodPattern(serviceDoc.getServiceClass(), method.getEndPointMethodName())).append("</td></TR>")
				.append("<TR><th>Consumes</th><td>").append(convertToStringList(method.getConsumes())).append("</td></TR>")
				.append("<TR><th>Provides</th><td>").append(convertToStringList(method.getEndPointProvides())).append("</td></TR>")
				.append("<TR><th>Sample Links</th><td>").append(convertToLinks(method.getSampleLinks())).append("</td></TR>") //TODO: Finish prepending correct URI
				.append("<TR><th align=\"top\">Description</th><td>").append(method.getEndPointDescription()).append("</td></TR>")
				.append("<TR><th align=\"top\">Error Codes</th><td>").append(convertToBulletedList(method.getErrors())).append("</td></TR>")
				.append("<TR><TD colspan=2>&nbsp;</TD></TR></table>");
		}
		
		return (END_POINT.replaceFirst(CONTENT, indexString.toString()).replaceFirst(DOC_APP_NAME, docAppName));
	}

	@GET
	@Path("/restDoc.css")
	@Produces("text/css")
	@PublicRESTDocMethod(endPointName="StyleSheet override", description="Delivers the stylesheet used by the API docs. This can be overridden by including a new .css file in the WebApp folder and then setting the optional value in restDocs.properties,  pidocs.css.path=/{fileName}.css", sampleLinks={"./docs/restDoc.css"}, errorCodes="")
	public Response getDefaultStyleSheet(){
		if(!StringUtil.isNullOrEmpty(this.altCssLocation)){
			try {
				return Response.temporaryRedirect(uriInfo.getRequestUriBuilder().replacePath(this.altCssLocation).build()).build();
			} catch (Exception e) {
				return Response.ok(DEFAUT_STYLE).build();
			}
		}
		return Response.ok(DEFAUT_STYLE).build();
	}
	
	/**
	 * @param values
	 * @return
	 */
	private String convertToBulletedList(String[] values) {
		StringBuilder sb = new StringBuilder("<ul>");
		if (values != null) {

			for (String string : values) {
				sb.append("<li>").append(string).append("</li>");
			}
		}
		return sb.substring(0, sb.length())+"</ul>";
	}

	/**
	 * Makes a list of links into HTML links. If the link starts with a './' or '/' then the current host will be prepended to the sample link.
	 * @param array of url strings.
	 * @return html string
	 */
	private Object convertToLinks(String[] sampleLinks) {
		StringBuilder sb = new StringBuilder();
		
		for (String string : sampleLinks) {
			if(string.startsWith("./") || string.startsWith("/")){
				//add host to URL so that the URL works correctly.
				String sampleurl = uriInfo.getBaseUri() + string.substring(string.indexOf("/")+1);
				
				sb.append("<a href=\"")
				.append(sampleurl)
				.append("\" class=\"plain\">")
				.append(sampleurl)
				.append("</a><br>");
			} else {
			sb.append("<a href=\"")
				.append(string)
				.append("\" class=\"plain\">")
				.append(string)
				.append("</a><br>");
			}
		}
		return sb.toString();
	}

	/**
	 * Makes an array of string into a comma separated list for display.
	 * @param values
	 * @return
	 */
	private String convertToStringList(String[] values) {
		return convertToStringList(values, ",");
	}
	
	/**
	 * Makes an array of string into a comma separated list for display allowing delimiter to be specified..
	 * @param values
	 * @param delem
	 * @return
	 */
	private String convertToStringList(String[] values, String delem) {
		StringBuilder sb = new StringBuilder();
		if (values != null) {

			for (String string : values) {
				sb.append(" ").append(string).append(delem);
			}
		}
		return sb.substring(0, sb.length());
	}
	
	/**
	 * Builds a link to the referenced link and replaces any paramater values with values provided. 
	 * @param restServiceclazz -methods class for uri references
	 * @param methodName - name of the method contained in the restServiceclazz 
	 * @param params - replacement values for replacing params noted as {}
	 * @return - uri
	 */
	private String getLocalMethodPath(Class<?> restServiceclazz, String methodName, Object... params){
		if(params == null){
			params = new Object[]{""};
		}
		UriBuilder ub = uriInfo.getBaseUriBuilder().path(restServiceclazz);
		URI methodURI = ub.path(restServiceclazz, methodName).build(params);
		
		return methodURI.toASCIIString();
	}
	
	/**
	 * Returns an unencoded pattern used for displaying the api path on the docs.
	 * @param restServiceclazz
	 * @param methodName
	 * @return
	 */
	private String getLocalMethodPattern(Class<?> restServiceclazz, String methodName){
		try{
			UriBuilder ub = uriInfo.getBaseUriBuilder().path(restServiceclazz);
			URI methodURI = ub.path(restServiceclazz, methodName).build();

			return methodURI.getPath();
		} catch(Exception e){
			log.debug("Trying to derive MethodPattern but failed, soemthing is probably wrong with the doc configuration. Method Name:" + methodName ,e);
			return "";
		}
	}
}
