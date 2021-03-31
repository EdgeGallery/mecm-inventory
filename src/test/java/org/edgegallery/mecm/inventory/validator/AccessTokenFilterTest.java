package org.edgegallery.mecm.inventory.validator;

import org.edgegallery.mecm.inventory.apihandler.filter.AccessTokenFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import sun.net.www.http.HttpClient;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AccessTokenFilterTest {

    public static final String HEALTH_URI = "/inventory/v1/health";

    @Mock
    private HttpClient defaultHttpClient;

    AccessTokenFilter filter;
    HttpServletRequest mockReq;
    HttpServletResponse mockResp;
    FilterChain mockFilterChain;
    FilterConfig mockFilterConfig;
    OAuth2AccessToken oAuth2AccessToken;

    @Before
    public void onStartup() {
        filter = new AccessTokenFilter();
        mockFilterChain = Mockito.mock(FilterChain.class);
        mockFilterConfig = Mockito.mock(FilterConfig.class);
        oAuth2AccessToken = mock(OAuth2AccessToken.class);
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        mockReq = Mockito.mock(HttpServletRequest.class);
        mockResp = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockReq.getRequestURI()).thenReturn(HEALTH_URI);
        BufferedReader br = new BufferedReader(new StringReader("test"));
        Mockito.when(mockReq.getReader()).thenReturn(br);

        // filter.init(mockFilterConfig);
        filter.doFilter(mockReq, mockResp, mockFilterChain);
        //filter.destroy();
    }

    @Test(expected = Exception.class)
    public void testDoFilterException() throws IOException, ServletException {
        mockReq = Mockito.mock(HttpServletRequest.class);
        mockResp = Mockito.mock(HttpServletResponse.class);
        filter.doFilter(mockReq, mockResp, mockFilterChain);
        //  filter.destroy();
        doThrow(new Exception("Error occurred because of : Access token is empty."))
                .when(filter);
    }

}