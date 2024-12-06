package helloworld.secured_rest_api.spring_boot.demo.common.dto;

/**
 * Token DTO
 */
public class TokenDTO {

    /** Token value */
    private String token;
    /** Token type */
    private String type;
    /** Number of seconds token is valid */
    private long expiresInSeconds;

    /**
     * Constructor
     * 
     * @param token token value
     * @param type token type
     * @param expiresInSeconds Number of seconds token is valid
     */
    public TokenDTO(String token, String type, long expiresInSeconds) {
        this.token = token;
        this.type = type;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpiresInSeconds() {
        return this.expiresInSeconds;
    }

    public void setExpiresInSeconds(long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + (int) (expiresInSeconds ^ (expiresInSeconds >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TokenDTO other = (TokenDTO) obj;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (expiresInSeconds != other.expiresInSeconds)
            return false;
        return true;
    }
}
