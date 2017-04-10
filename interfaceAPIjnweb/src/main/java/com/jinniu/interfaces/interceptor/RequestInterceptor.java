package com.jinniu.interfaces.interceptor;

import com.jinniu.commonjn.util.CryptUtil;
import com.jinniu.commonjn.util.RSAUtils;
import com.jinniu.commonjn.util.SignEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(RequestInterceptor.class);

    private SignEntity signEntity;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestMethod = request.getMethod();  //获取请求方式
        //  System.out.println(requestMethod);
        //LOG.info(String.format("requestMethod:%s", requestMethod));
        //  if(requestMethod.equals("GET")) return false;


        //获取请求头信息
        String token = request.getHeader("token");
        String userId = request.getHeader("userId"); //userCode
        String systemType = request.getHeader("systemType"); //系统类型
        String models = request.getHeader("models");    //机型
        String systemVersion = request.getHeader("systemVersion"); //机型系统版本
        String versioncode = request.getHeader("versioncode"); //机型版本
        String modulecoding = request.getHeader("modulecoding"); //模块编号，0-未知，1-首页，2-设置......

        LOG.info(String.format("userId:%s,systemType:%s,models:%s,systemVersion:%s,versioncode:%s,modulecoding:%s", userId, systemType, models, systemVersion, versioncode, modulecoding));

        //boolean signBoo = invalidSign(request);
        boolean signBoo = true;
        if (signBoo) {  //sign success

            //boolean tokenBoo = invalidToken(request);
            boolean tokenBoo = true;
            if (tokenBoo) {    //token invalid success

                return true;

            } else {
                return false;
            }


        } else {
            return false;
        }

    }


    /**
     * @throws Exception
     * @Title: SignInterceptor.java
     * @Package lander.interfaces.interceptor
     * @Description: 签名验证
     * @author chenwenhao
     * @date 2016-12-2 下午6:34:24
     */
    public boolean invalidSign(HttpServletRequest request) throws Exception {

        String android_client_secret = signEntity.getAndroid_client_secret();
        String ios_client_secret = signEntity.getIos_client_secret();
        String projectName = signEntity.getProjectName();

        String systemType = request.getHeader("systemType"); //系统类型
        String client_secret = ios_client_secret;
        if (null != systemType && systemType.equals("2")) {
            client_secret = android_client_secret;
        }

        Map<String, String[]> params = request.getParameterMap();
        StringBuffer queryBuffer = new StringBuffer("");
        Set<String> set = new TreeSet<String>(params.keySet());
        for (String key : set) { // 遍历所有key
            if ("sign".equalsIgnoreCase(key) || "files".equalsIgnoreCase(key)) { // 如果是sign或者是文件流files就跳出本次循环
                continue;
            }
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++) { //
                String value = values[i];
                queryBuffer.append(key).append("=").append(value).append("&");
            }
        }
        queryBuffer.append("client_secret=").append(client_secret);

        LOG.info("sign parms : {}", queryBuffer.toString());
        String sign = request.getParameter("sign");
        if (StringUtils.isNotBlank(queryBuffer.toString()) && StringUtils.isNotBlank(sign)) {
            String toBeEncrypt = queryBuffer.toString();
//			sign =UTF-8(转小写(MD5(参数按字母顺序排序)+公司名))
            String expectSign = CryptUtil.md5(toBeEncrypt);
            expectSign = new String((expectSign + projectName).toLowerCase().getBytes(), "utf-8");

            LOG.info(String.format("[sign] uri:%s ,toBeEncrypt:%s,expectSign:%s,sign:%s", request.getRequestURI(), toBeEncrypt, expectSign, sign));
            if (StringUtils.equalsIgnoreCase(sign, expectSign)) {
                //签名验证成功
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean invalidToken(HttpServletRequest request) {
        // 增加token的校验
        String token = StringUtils.trim(request.getHeader("token"));
        LOG.info("[token] token is {}", token);

        String msg = RSAUtils.decrypt(token, true);
        if (msg == null) {
            LOG.warn("[sign] token is invalid msg is null");
            return false;
        }
        String[] arr = msg.split(";");
        if (arr.length < 3) {
            LOG.info("[sign] token is invalid msg length is smaller than 3 ;{}", msg);
            return false;
        }
        Long userId = NumberUtils.toLong(arr[0]);
        Long timestamp = NumberUtils.toLong(arr[1]);
        if (userId <= 0 || timestamp <= 0) {
            LOG.info("[sign] token is invalid,userId:{},timestamp:{}", userId, timestamp);
            return false;
        }

        // 检查时效，不超过两小时 2*60*60*1000 = 7200000
        if (System.currentTimeMillis() - timestamp > 7200000) {
            LOG.info("[sign] token is expired,userId:{},timestamp:{}", userId, new Date(timestamp));
            return false;
        }

        request.setAttribute("tokenUserId", userId);
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
