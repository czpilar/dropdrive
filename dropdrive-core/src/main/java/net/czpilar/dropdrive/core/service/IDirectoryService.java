package net.czpilar.dropdrive.core.service;

import com.dropbox.core.v2.files.FolderMetadata;

/**
 * Directory service interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IDirectoryService {

    /**
     * Finds directory with give pathname where finding starts with the root parent.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname path name
     * @return found directory or null if the directory is not found
     */
    FolderMetadata findDirectory(String pathname);

    /**
     * Finds directory with give pathname where finding starts with a given parent.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname  path name
     * @param parentDir parent directory
     * @return found directory or null if the directory is not found
     */
    FolderMetadata findDirectory(String pathname, FolderMetadata parentDir);

    /**
     * Finds directory with given pathname where finding starts with the root parent.
     * If a directory does not exist, create one with a given pathname.
     * Also creates all non-existing directories on pathname.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname path name
     * @return found or created a directory
     */
    FolderMetadata findOrCreateDirectory(String pathname);

    /**
     * Finds directory with the given pathname where finding starts with the given parent directory.
     * If a directory does not exist, create one with a given pathname.
     * Also creates all non-existing directories on pathname.
     * Pathname supports directory separators "/" or "\".
     *
     * @param pathname  path name
     * @param parentDir parent directory
     * @return found or created a directory
     */
    FolderMetadata findOrCreateDirectory(String pathname, FolderMetadata parentDir);
}
