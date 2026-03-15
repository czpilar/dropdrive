package net.czpilar.dropdrive.core.service;

/**
 * Authorization service interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IAuthorizationService {

    /**
     * Returns authorization URL to authorize application.
     *
     * @return authorization url
     */
    String getAuthorizationURL();

    /**
     * Authorize application.
     *
     * @param authorizationCode authorization code
     */
    void authorize(String authorizationCode);

}
