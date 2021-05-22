
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by varjunan on 8/24/17.
 */
@Service
public class PartnerAPIClient {

    private final MetricRegistry metricRegistry;
    private final RestClient restClient;
    private final EnvironmentConfig environmentConfig;
    private static final Logger LOGGER = LogManager.getLogger(PartnerAPIClient.class);
    private final Timer timerAccessToken;
    private final Timer timerAccessToken2;
    private final Timer timerRefreshToken;
    private final Timer timerRequestArtifact;
    private static Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();


    public static Map<String, CircuitBreaker> getCircuitBreakerMap() {
        return circuitBreakerMap;
    }

    @Autowired
    public PartnerAPIClient( EnvironmentConfig environmentConfig, RestClient restClient,
                             final MetricRegistry metricRegistry,@Value("${roku.env:Undefined}") final String rokuEnv){
        this.metricRegistry = metricRegistry;
        this.environmentConfig = environmentConfig;
        this.restClient = restClient;
        this.timerAccessToken2 = this.metricRegistry.timer("apps." +  rokuEnv + ".authsvc.partnerAccessToken2");
        this.timerAccessToken = this.metricRegistry.timer("apps."  +  rokuEnv + ".authsvc.partnerAccessToken");
        this.timerRefreshToken = this.metricRegistry.timer("apps."+  rokuEnv + ".authsvc.refreshToken");
        this.timerRequestArtifact = this.metricRegistry.timer("apps."+  rokuEnv + ".authsvc.requestArtifact");


    }

    public PartnerRefreshTokenResponse getPartnerAccessToken2(String partnerAPIEndpoint, PartnerAccessTokenRequest partnerAccessTokenRequest) throws HttpGatewayException, HttpBadRequestException, HttpRequestTimeout {

        final Timer.Context context = timerAccessToken2.time();
        PartnerRefreshTokenResponse response = null;
        CircuitBreaker cb = circuitBreakerMap.get(partnerAPIEndpoint);
        if ( cb == null ) {
            cb = new CircuitBreaker();
            circuitBreakerMap.put(partnerAPIEndpoint,cb);
        }
        CircuitBreaker.State state = cb.getState() ;
        if ( state == CircuitBreaker.State.OPEN) {
            LOGGER.error("Circuit Open - Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " +
                " Partner - " + partnerAPIEndpoint + "request = " +partnerAccessTokenRequest );
            throw new HttpRequestTimeout("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse "  +
                " Partner - " + partnerAPIEndpoint + "request = " +partnerAccessTokenRequest );
        }
        boolean lockAcquired = false;
        if ( state == CircuitBreaker.State.HALF_OPEN ) {
            lockAcquired = cb.lock.tryLock();
            if ( !lockAcquired) {  // some one has acquired lock and he will try to 'close' the circuit they succeed
                LOGGER.error("Circuit Open - Exception occurred while reading External Partner API - PartnerRefreshTokenResponse2 " +
                    " Partner - " + partnerAPIEndpoint + "request = " +partnerAccessTokenRequest );
                throw new HttpRequestTimeout("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse "  +
                    " Partner - " + partnerAPIEndpoint + "request = " +partnerAccessTokenRequest );
            }
        }
        try{
            LOGGER.debug("PartnerAccessTokenRequest " + partnerAccessTokenRequest);
            response = restClient.postJson(partnerAPIEndpoint, partnerAccessTokenRequest, PartnerRefreshTokenResponse.class);
            LOGGER.debug("Got response from Partner endpoint" + response);
            if (lockAcquired ) {
                // we are successful, so 'close' the circuit
                cb.closeCircuit();
            }

        } catch ( HttpClientErrorException e) {
            LOGGER.debug("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
            throw new HttpBadRequestException("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + "request = " +partnerAccessTokenRequest );
        } catch (Exception e){
            if ( lockAcquired) {
                cb.openCircuit();
            } else  {
                cb.updateErrorThreshold();
            }
            LOGGER.error("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage()
                + " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest  );
            throw new HttpGatewayException("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage()
                + " Partner - " + partnerAPIEndpoint + "request = " +partnerAccessTokenRequest  );
        } finally {
            context.stop();
            if ( lockAcquired) {
                cb.lock.unlock();
            }
        }
        if ( response == null ) {
            throw new HttpGatewayException("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
        }
        return response;
    }

    public PartnerAccessTokenResponse getPartnerAccessToken(String partnerAPIEndpoint, PartnerAccessTokenRequest partnerAccessTokenRequest) throws HttpBadRequestException, HttpGatewayException, HttpRequestTimeout {
        PartnerAccessTokenResponse response = null;

        CircuitBreaker cb = circuitBreakerMap.get(partnerAPIEndpoint);
        if ( cb == null ) {
            cb = new CircuitBreaker();
            circuitBreakerMap.put(partnerAPIEndpoint,cb);
        }
        CircuitBreaker.State state = cb.getState() ;
        if ( state == CircuitBreaker.State.OPEN) {
            LOGGER.error("Circuit Open - Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " +
                " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
            throw new HttpRequestTimeout("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse "  +
                " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
        }
        boolean lockAcquired = false;
        if ( state == CircuitBreaker.State.HALF_OPEN ) {
            lockAcquired = cb.lock.tryLock();
            if ( !lockAcquired) {  // some one has acquired lock and he will try to 'close' the circuit they succeed
                LOGGER.error("Circuit Open - Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " +
                    " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
                throw new HttpRequestTimeout("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse "  +
                    " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
            }
        }

        final Timer.Context context = timerAccessToken.time();
        try{
            LOGGER.debug("PartnerAccessTokenRequest " + partnerAccessTokenRequest);
            response = restClient.postJson(partnerAPIEndpoint, partnerAccessTokenRequest, PartnerAccessTokenResponse.class);
            LOGGER.debug("Got response from Partner endpoint" + response);
            if (lockAcquired ) {
                // we are successful, so 'close' the circuit
                cb.closeCircuit();
            }

        } catch ( HttpClientErrorException e) {
            LOGGER.debug("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
            throw new HttpBadRequestException("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
        } catch (Exception e){
            if ( lockAcquired) {
                cb.openCircuit();
            } else  {
                cb.updateErrorThreshold();
            }
            LOGGER.error("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
            throw new HttpGatewayException("Exception occurred while reading External Partner API - PartnerRefreshTokenResponse " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );


        } finally {
            context.stop();
            if ( lockAcquired) {
                cb.lock.unlock();
            }
        }
        if ( response == null ) {
            throw new HttpGatewayException("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerAccessTokenRequest );
        }
        return response;
    }

    public PartnerRefreshTokenResponse refreshToken(String partnerAPIEndpoint, PartnerRefreshTokenRequest refreshTokenRequest) throws HttpGatewayException, HttpBadRequestException, HttpRequestTimeout {
        PartnerRefreshTokenResponse response = new PartnerRefreshTokenResponse();
        CircuitBreaker cb = circuitBreakerMap.get(partnerAPIEndpoint);
        if ( cb == null ) {
            cb = new CircuitBreaker();
            circuitBreakerMap.put(partnerAPIEndpoint,cb);
        }
        CircuitBreaker.State state = cb.getState() ;
        if ( state == CircuitBreaker.State.OPEN) {
            LOGGER.error("Circuit Open - Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest );
            throw new HttpRequestTimeout("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest );
        }
        boolean lockAcquired = false;
        if ( state == CircuitBreaker.State.HALF_OPEN) {
            lockAcquired = cb.lock.tryLock();
            if ( !lockAcquired) {  // some one has acquired lock and he will try to 'close' the circuit when they succeed
                LOGGER.error("Circuit Open - Error reading from Partner API refreshToken API " + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest );
                throw new HttpRequestTimeout("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest );
            }
        }
        final Timer.Context context = timerRefreshToken.time();
        try{
            LOGGER.debug("PartnerRefreshTokenRequest " + refreshTokenRequest);

            final List<String> formEncodedRefreshPartners = Arrays.asList(environmentConfig.getFormEncodedRefreshPartners().split(","));
            if (formEncodedRefreshPartners.stream().parallel().anyMatch(p -> !StringUtils.isEmpty(p) && partnerAPIEndpoint.toLowerCase().contains(p))) {
                // APPSUSER-4786: Pandora does not recognize application/json which is normally being used here.
                // Because the oauth protocol really expects "application/x-www-form-urlencoded", adding this hack
                // so as to not break existing partners while still supporting the right format for Pandora.
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("client_id", refreshTokenRequest.client_id);
                map.add("client_secret", refreshTokenRequest.client_secret);
                map.add("refresh_token", refreshTokenRequest.refresh_token);
                map.add("grant_type", refreshTokenRequest.grant_type);

                response = restClient.postForm(partnerAPIEndpoint, map, PartnerRefreshTokenResponse.class);
            } else {
                response = restClient.postJson(partnerAPIEndpoint, refreshTokenRequest, PartnerRefreshTokenResponse.class);
            }
            LOGGER.debug("Got response from Partner endpoint" + response);
            if (lockAcquired ) {
                // we are successful, so close the circuit
                cb.closeCircuit();
            }

        } catch ( HttpClientErrorException e) {
            LOGGER.debug("Exception occurred while reading External Partner API - refreshToken " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest );
            throw new HttpBadRequestException("Exception occurred while reading External Partner API - refreshToken " + e.getMessage() +
                " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest );
        } catch (Exception e){
            if ( lockAcquired) {
                cb.openCircuit();
            } else  {
                cb.updateErrorThreshold();
            }
            LOGGER.error("Exception occurred while reading External Partner API  - refreshToken " + e.getMessage()  + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest);
            throw new HttpGatewayException("Exception occurred while reading External Partner API  - refreshToken " + e.getMessage()  + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest);
        } finally {
            context.stop();
            if ( lockAcquired) {
                cb.lock.unlock();
            }
        }
        if ( response == null ) {
            LOGGER.error("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest);
            throw new HttpGatewayException("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +refreshTokenRequest);
        }
        return response;

    }

    public String requestArtifact(String partnerAPIEndpoint, PartnerArtifactRequest partnerArtifactRequest) throws
        HttpGatewayException, HttpBadRequestException, HttpNotFoundException, HttpRequestTimeout {

        String response  = null ;
        CircuitBreaker cb = circuitBreakerMap.get(partnerAPIEndpoint);
        if ( cb == null ) {
            cb = new CircuitBreaker();
            circuitBreakerMap.put(partnerAPIEndpoint,cb);
        }

        CircuitBreaker.State state = cb.getState() ;
        if ( state == CircuitBreaker.State.OPEN) {
            LOGGER.error("Circuit Open - Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest );
            throw new HttpRequestTimeout("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest );
        }
        boolean lockAcquired = false;
        if ( state == CircuitBreaker.State.HALF_OPEN) {
            lockAcquired = cb.lock.tryLock();
            if ( !lockAcquired) {  // some one has acquired lock and he will try to 'close' the circuit when they succeed
                LOGGER.error("Circuit Open - Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest );
                throw new HttpRequestTimeout("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest );
            }
        }
        final Timer.Context context = timerRequestArtifact.time();
        try{
            LOGGER.debug("PartnerArtifactRequest " + partnerArtifactRequest);
            response = restClient.postJson(partnerAPIEndpoint, partnerArtifactRequest, String.class);
            LOGGER.debug("Got response from Partner endpoint" + response);
            if (lockAcquired ) {
                // we are successful, so close the circuit
                cb.closeCircuit();
            }
        } catch ( HttpClientErrorException e){ // this will catch 4xx as per Spring documentation
            // hulu is returning lot of 400 bad requests which hulu said is normal
            LOGGER.debug("Exception occurred while reading External Partner API  - requestArtifact " + e.getMessage()  + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest);
            if (HttpStatus.SC_BAD_REQUEST == e.getStatusCode().value()) {
                throw new HttpBadRequestException("Exception occurred while reading External Partner API  - requestArtifact " + e.getMessage() + " Partner - " + partnerAPIEndpoint + ", request = " + partnerArtifactRequest);
            } else { //else through 404
                throw new HttpNotFoundException("Exception occurred while reading External Partner API  - requestArtifact " + e.getMessage() + " Partner - " + partnerAPIEndpoint + ", request = " + partnerArtifactRequest);
            }
        } catch ( Exception e){  // all 4xx will be caught above, rest 5xx will be thrown by this exception
            if ( lockAcquired) {
                cb.openCircuit();
            } else  {
                cb.updateErrorThreshold();
            }
            LOGGER.error("Exception occurred while reading External Partner API  - requestArtifact " + e.getMessage()  + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest);
            throw new HttpGatewayException("Exception occurred while reading External Partner API  - requestArtifact " + e.getMessage()  + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest);
        } finally {
            context.stop();
            if ( lockAcquired) {
                cb.lock.unlock();
            }
        }
        if ( response == null ) {
            LOGGER.error("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest );
            throw new HttpGatewayException("Error reading from Partner API " + " Partner - " + partnerAPIEndpoint + ", request = " +partnerArtifactRequest );
        }
        return response;

    }

}


