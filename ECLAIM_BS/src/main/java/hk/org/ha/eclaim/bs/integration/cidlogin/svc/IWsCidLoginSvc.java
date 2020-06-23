package hk.org.ha.eclaim.bs.integration.cidlogin.svc;

import hk.org.ha.cid_login_ws.cidloginfunctions.CheckLogin;
import hk.org.ha.cid_login_ws.cidloginfunctions.CheckLoginResponse;

public interface IWsCidLoginSvc {

	public CheckLoginResponse checkLogin(CheckLogin checkLogin) throws Exception;
}
