package net.czpilar.dropdrive.core.service;

import net.czpilar.dropdrive.core.credential.Credential;

/**
 * Authorization service interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IAuthorizationService {

    /**
     * Returns authorization URL to authorize application.
     *
     * @return
     */
    String getAuthorizationURL();

    /**
     * Authorize application and returns credential.
     *
     * @param authorizationCode
     * @return
     */
    Credential authorize(String authorizationCode);

}
