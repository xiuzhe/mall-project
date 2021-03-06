package com.ishare.mall.api.restful.oauth;

import com.ishare.mall.api.service.oauth.OAuthService;
import com.ishare.mall.common.base.constant.uri.OpenApiURIConstant;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

import static com.ishare.mall.common.base.constant.ResourceConstant.OAUTH.INVALID_CLIENT_DESCRIPTION;
import static com.ishare.mall.common.base.constant.ResourceConstant.OAUTH.INVALID_CODE_DESCRIPTION;
/**
 * Created by YinLin on 2015/8/11.
 * Description : accessToken获取
 * Version 1.0
 */
@RestController
@Deprecated
public class AccessTokenController {

    private static final Logger log = LoggerFactory.getLogger(AccessTokenController.class);

    @Autowired
    private OAuthService oAuthService;

    @RequestMapping(value = OpenApiURIConstant.Oauth.ACCESS_TOKEN, method = RequestMethod.POST)
    public HttpEntity token(HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException {
        log.debug("哈哈哈哈哈");
        try {
            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            //检查提交的客户端id是否正确
            if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                log.debug("59 : " + response.getBody());
                return new ResponseEntity(
                        response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
              //  throw new ApiLogicException(OAuthError.TokenResponse.INVALID_CLIENT, HttpStatus.BAD_REQUEST);
            }

            // 检查客户端安全KEY是否正确
            if (!oAuthService.checkClientSecret(oauthRequest.getClientId(), oauthRequest.getClientSecret())) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                log.debug("72 : " + response.getBody());
                return new ResponseEntity(
                        response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
                //throw new ApiLogicException(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, HttpStatus.UNAUTHORIZED);
            }

            String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
                    GrantType.AUTHORIZATION_CODE.toString())) {
                if (!oAuthService.checkAuthCode(authCode, oauthRequest.getClientId())) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription(INVALID_CODE_DESCRIPTION)
                            .buildJSONMessage();
                    log.debug("88 : " + response.getBody());
                    return new ResponseEntity(
                            response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
                    //throw new ApiLogicException(OAuthError.TokenResponse.INVALID_GRANT, HttpStatus.BAD_REQUEST);
                }
            }

            //生成Access Token
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();
            oAuthService.addAccessToken(accessToken,
                    oAuthService.getAccountByAuthCode(authCode), oauthRequest.getClientId());

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request,
                            HttpServletResponse.SC_FOUND);
            //设置授权码
            //builder.setCode(accessToken);
            //得到到客户端重定向地址
            //String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            builder.setAccessToken(accessToken);
            builder.setExpiresIn(oAuthService.getExpireIn());
            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(oAuthService.getExpireIn()))
                    .buildJSONMessage();

//            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
//            HttpHeaders headers = new HttpHeaders();
//            String uri = response.getLocationUri();
//            log.debug(uri);
//            headers.setLocation(new URI(uri));
//            Response<OAuthDTO> response = new Response<>();
//            response.setCode(200);
//            OAuthDTO oAuthDTO = new OAuthDTO();
//            oAuthDTO.setToken(accessToken);
//            oAuthDTO.setExpiresIn(oAuthService.getExpireIn());
//            response.setData(oAuthDTO);
            log.debug(accessToken);
            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(
                    response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            //构建错误响应
            OAuthResponse res = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return new ResponseEntity(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));

           // throw new ApiLogicException(OAuthError.TokenResponse.INVALID_CLIENT, HttpStatus.BAD_REQUEST);
        }
    }

    public static Logger getLog() {
        return log;
    }
}
