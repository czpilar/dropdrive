package net.czpilar.dropdrive.core.service;

import com.dropbox.core.DbxEntry;

/**
 * Directory service interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IDirectoryService {

    /**
     * Finds directory with give pathname where finding starts with root parent.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname
     * @return found directory or null if directory is not found
     */
    DbxEntry.Folder findDirectory(String pathname);

    /**
     * Finds directory with give pathname where finding starts with given parent.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname
     * @param parentDir
     * @return found directory or null if directory is not found
     */
    DbxEntry.Folder findDirectory(String pathname, DbxEntry.Folder parentDir);

    /**
     * Finds directory with given pathname where finding starts with root parent.
     * If directory does not exist creates one with give pathname.
     * Also creates all non-existing directories on pathname.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname
     * @return found or created directory
     */
    DbxEntry.Folder findOrCreateDirectory(String pathname);

    /**
     * Finds directory with given pathname where finding starts with given parent directory.
     * If directory does not exist creates one with give pathname.
     * Also creates all non-existing directories on pathname.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname
     * @param parentDir
     * @return found or created directory
     */
    DbxEntry.Folder findOrCreateDirectory(String pathname, DbxEntry.Folder parentDir);
}
