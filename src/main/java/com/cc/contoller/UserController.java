package com.cc.contoller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.cc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Map;

//import com.vcc.velocity.HelloVeloctiy;

@RestController
@RequestMapping("/queryUser")
public class UserController {

    @Autowired
    private UserService userService;

    private static AlipayClient alipayClient;
    static {
        String serverUrl = "https://openapi.alipaydev.com/gateway.do";
        String appId = "2016102400753616";
        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCc/MfmALJMNgJKgEvJjPvYIJCmkN/7CyT+o6m6CggXWL7IrEbwPKfP0eXChMrAp2KbdZiyhDk3WJXilevqAXaDHDmPLZFpwiSYyO+S0hL3LECb7RKvexRgYkVrd8r8KmoIfQdWI8BfWy3WbqEJX9SNFERxFcBRbWWUhgvnw/3OsyLo+UYImu5bPblIAXnozS/9WRLD1u2FKYGmdWXLjEEmfmc4LdDWj+MQjnAce+MwWGiwFdV/U5lAb3ApMIcxczyNnM7xgb3WH1LOwTQOeqQifynpSfc8JxP09jTYKV2NHB5S6OpUOBNDoZNLHNW/Ikj702OrKP4ebZj5X9udaAgtAgMBAAECggEASi2rawPTO+2dQYTDytXCPoqMrxfvrdjDP0soLM8Gqbl2VK4KywNuK+W7UiGbCFNPZbO4VkniG0PRdwJuOeE9FZClpgDbIATrdAImKdL3wTF4yfTIioB2zWBB+xjcojCMb7xcVhiIksYrr5IQp7RPY+Y6vNCRUiiGAhsgzv+uExeWooMGNewJIoj4UnePgjo10RaLSvz/ZU1v7NhYSDtFykVQQnmuOFEIOqTxsuaL7Gu9bEqQtXdpKFhSoM7OIyhh7PU84QxjFUIGzAYGb/ru9WYqkwFaJmc+uMGgTORU56xDWB7hOUep0ET6XWwagJE28WafnguWR8uEHgW41GP+JQKBgQDLmPzdhCOctDPL71CKnAu24hqYDxukhWtTfyUAmoD7AA1eeiT0oYpS9P/JLAe0LPHKqPbFOamjYsGzNA+8StoHkDnJcw/Vy7b3kozd/JBg+ZXloBiIqTmI65RnCLgISrZpg1dSzv7QqFTaX2Hj5jpOBmaABxixUMfiGcaSoUpWRwKBgQDFZKfsV2BnGaytlroHTN+yRPKJaIiSshO7cLJfhAfPXgsqW22NmPdOa/vv7b+g5duce03QC+1Yw7NAmRbqBH7TpQd5vX5+6fN2ZDN0w8pHQeahPLPVCRU7Njf5H9lqH/kBUUYjKbf7cBDS2+MW2B+HiBmCbN9VKo1Rznq7Y5gD6wKBgQCdl4XjUOzf1vxg1pTZNYA7xzgei1MyPbOdpcTWesFrh9bRUmpYkNGo9MsoxFQSOtfNrouUjdWpCkA0ng+wB6BMSm7wZ4fSTSUK7PzstxpqdcZ5Pzq+TX6jZPYx6qXIhUw8z9U/28mRaTqE7V+wL/zheELOxq9uUMDnWlxCE2ae8QKBgQC1I0UlkJkag2j+cs6LXHP6t2KJza9mTpvk6eoF1LcGCBre57+ErNZk1JsA9X9gOw5obo5dWFSZnBymLtXkMfPgbBSMFuQ5up58Cj9d6z14XnUxiO7Nk+QzzfFXX264AFcUamyeX+5BgfkuRjICCJoGWVJvaVEYak/8vinKQ/5/uQKBgQC+gRswRfair+wFL/W+HTWq7imkYKkNyy3Hpv9PkNOGWFPM8rHeD/u1R1eFAzC4a88FH96bF45sJ8pM+PuyP/je90qk3lBytDOPQWiA8/O+mFy09gcRrdMc6NprSHrMobRpzjK/6kWdRsEFphszS8eOivTMHlYm4lp81dQ+peLVDQ==";
        String format = "json";
        String charset = "utf-8";
        String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7vFZJzu36vMxRxq/tlQPnQk7sJJ+u8ejDemcX8W/xh59DHpvAHtWg+tvEFlxteaQ+0KCB9+GRsMGzaJV6q4drm+A8l8AzP3JDOIgYAv4olf+z26T+Kwu7E3V7klAOdWB80XBfY1vFp0X2cl4HBMNvHaqbkrXhyj5gz9Nti3dyxYqLkNKhjVX4/FqlwSsUJSjehOGsyD07fdygSuAuptLy6HLjAMgso8Vv4KwsFGvfnlf4rdUUHuP/I04+VurTQ3nrDurDmvzLmSKEmOYIoqn1A3TzmUX7rKEjmZ2yqTTS5yp2lTF/fd975afxWR5wtq5Yx/iAldMfa2FaKz76toSsQIDAQAB";
        String signType = "RSA2";
        //TODO alipayClient用单例模式后期实际应用修改
        alipayClient  =  new DefaultAlipayClient(serverUrl,appId,privateKey,format,charset,alipayPublicKey,signType);
    }

    @ResponseBody
    @RequestMapping(value="/user",method = RequestMethod.POST)
    public List<Map> QueryUser(@RequestBody String params, @Context HttpServletRequest request){
        JSONObject jsonObject = JSONObject.parseObject(params);
        System.out.println(jsonObject);
        System.out.println(userService);
        List<Map> username = userService.selectByCategories("SELECT * FROM user where id =1");
        return username;
    }

    @ResponseBody
    @RequestMapping(value="/getAlipay",method = RequestMethod.POST)
    public String getAlipay(@RequestBody String params, @Context HttpServletRequest httpServletRequest){
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        JSONObject jsonObject = new JSONObject();
        request.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101002\"," +//商户订单号
                "    \"total_amount\":\"88.88\"," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"store_id\":\"NJ_001\"," +
                "    \"timeout_express\":\"90m\"}");//订单允许的最晚付款时间
        jsonObject.put("out_trade_no","20150320010101002");
        jsonObject.put("total_amount","88.88");
        jsonObject.put("subject","Iphone6 16G");
        jsonObject.put("store_id","NJ_001");
        jsonObject.put("timeout_express","90m");
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.print(response.getBody());
        return response.getBody();
    }

    @ResponseBody
    @RequestMapping(value="/user1",method = RequestMethod.POST)
    public void test(@RequestBody String params, @Context HttpServletRequest request){
//        HelloVeloctiy t = new HelloVeloctiy("src//com/cc/velocity/example.vm");
//        JSONObject jsonObject = JSONObject.parseObject(params);
//        System.out.println(jsonObject);
//        System.out.println(userService);
//        List<Map> username = userService.selectByCategories("SELECT * FROM user where id =1");
//        return username;
    }

}
