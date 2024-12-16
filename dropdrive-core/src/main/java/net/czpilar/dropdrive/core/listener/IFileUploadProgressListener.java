package net.czpilar.dropdrive.core.listener;

/**
 * Interface for file upload progress listener.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IFileUploadProgressListener {

    enum State {
        INITIATION, IN_PROGRESS, COMPLETE
    }

    /**
     * Method for executing progress change.
     *
     * @param state    uploaded state
     * @param uploaded bytes already uploaded
     */
    void progressChanged(State state, long uploaded);
}
