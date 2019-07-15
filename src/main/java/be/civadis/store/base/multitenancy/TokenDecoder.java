package be.civadis.store.base.multitenancy;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.lang.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Permet de décoder un token JWT
 * Attention, pas de vérification de la signature
 */
public class TokenDecoder {

    public static final String TENANT_PATH_PREFIX = "realms";
    private static TokenDecoder instance;

    protected final Log logger = LogFactory.getLog(this.getClass());
    private ObjectMapper objectMapper = new ObjectMapper();
    private CompressionCodecResolver compressionCodecResolver = new DefaultCompressionCodecResolver();

    public TokenDecoder() {
    }

    public static TokenDecoder getInstance(){
        if (instance == null){
            instance = new TokenDecoder();
        }
        return instance;
    }

    public Claims getClaims(String jwt){
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");

        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;

        int delimiterCount = 0;

        StringBuilder sb = new StringBuilder(128);

        for (char c : jwt.toCharArray()) {

            if (c == '.') {

                CharSequence tokenSeq = Strings.clean(sb);
                String token = tokenSeq!=null?tokenSeq.toString():null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }

        // =============== Header =================
        Header header = null;

        CompressionCodec compressionCodec = null;

        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
            Map<String, Object> m = readValue(origValue);

            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }

            compressionCodec = compressionCodecResolver.resolveCompressionCodec(header);
        }

        // =============== Body =================
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }

        Claims claims = null;

        if (payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') { //likely to be json, parse it:
            Map<String, Object> claimsMap = readValue(payload);
            claims = new DefaultClaims(claimsMap);
        }

        return claims;
    }

    public String getTenant(Claims claims){
        logger.warn("Issuer : " + claims.getIssuer());
        if (claims != null && claims.getIssuer() != null){
            int idx = claims.getIssuer().lastIndexOf(TENANT_PATH_PREFIX);
            return claims.getIssuer().substring(idx + TENANT_PATH_PREFIX.length() + 1);
        } else {
            return null;
        }
    }

    public String getTenant(String token){

        if (token.toLowerCase().startsWith("bearer")){
            token = token.substring(7);
        }

        Claims claims = getClaims(token);
        if (claims != null){
            return getTenant(claims);
        }
        return null;
    }

    protected Map<String, Object> readValue(String val) {
        try {
            return objectMapper.readValue(val, Map.class);
        } catch (IOException e) {
            logger.error("Unable to read JSON value: " + val, e);
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }


}
