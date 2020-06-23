package hk.org.ha.eclaim.bs.integration.cidlogin.svc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;

import hk.org.ha.cid_login_ws.cidloginfunctions.CheckLogin;
import hk.org.ha.cid_login_ws.cidloginfunctions.CheckLoginResponse;
import hk.org.ha.eclaim.bs.integration.cidlogin.svc.IWsCidLoginSvc;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Service("wsCidLoginSvc")
public class WsCidLoginSvcImpl implements IWsCidLoginSvc {
	
	@Autowired
	private WebServiceTemplate cidLoginWebServiceTemplate;
	
	public CheckLoginResponse checkLogin(CheckLogin checkLogin) throws Exception {
		//boolean result = false;
		CheckLoginResponse response = (CheckLoginResponse) cidLoginWebServiceTemplate.marshalSendAndReceive(checkLogin, new WebServiceMessageCallback() {

	        public void doWithMessage(WebServiceMessage message) {
	            try {
	            	
	                SoapMessage soapMessage = (SoapMessage)message;
	                soapMessage.setSoapAction("http://ha.org.hk/CID_LOGIN_WS/CidLoginFunctions/checkLogin");
	                
	            } catch (Exception e) {
	            	EClaimLogger.error("checkLogin:" + e.getMessage(), e);
	            }
	        }
	        
	    });
		EClaimLogger.info("ID Man response result=" + response.getCheckLoginResult());
		
		return response;
	}

}
