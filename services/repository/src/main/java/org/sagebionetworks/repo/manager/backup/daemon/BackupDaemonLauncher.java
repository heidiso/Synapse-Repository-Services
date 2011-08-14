package org.sagebionetworks.repo.manager.backup.daemon;

import org.sagebionetworks.repo.model.BackupRestoreStatus;
import org.sagebionetworks.repo.model.DatastoreException;
import org.sagebionetworks.repo.model.UnauthorizedException;
import org.sagebionetworks.repo.model.UserInfo;
import org.sagebionetworks.repo.web.NotFoundException;

public interface BackupDaemonLauncher {
	
	/**
	 * The deamon should create a starting status, and then start the thread 
	 * that will do the backup.
	 * @return
	 * @throws UnauthorizedException 
	 * @throws DatastoreException 
	 */
	public BackupRestoreStatus startBackup(UserInfo username) throws UnauthorizedException, DatastoreException;
	
	/**
	 * Start a restore daemon that will populate the system using the given backup file.
	 * The backup file must reside in the the S3 Bucket that belongs to this stack.
	 * 
	 * @param username
	 * @param fileName
	 * @return
	 * @throws UnauthorizedException
	 * @throws DatastoreException
	 */
	public BackupRestoreStatus startRestore(UserInfo username, String fileName) throws UnauthorizedException, DatastoreException;
	

	/**
	 * Terminate an existing backup daemon.
	 * @param user
	 * @param id
	 * @throws UnauthorizedException
	 * @throws NotFoundException 
	 * @throws DatastoreException 
	 */
	public void terminate(UserInfo user, String id) throws UnauthorizedException, DatastoreException, NotFoundException;
	
	/**
	 * Get the status of a running daemon.
	 * @param username
	 * @param id
	 * @return
	 * @throws UnauthorizedException 
	 * @throws NotFoundException 
	 * @throws DatastoreException 
	 */
	public BackupRestoreStatus getStatus(UserInfo username, String id) throws UnauthorizedException, DatastoreException, NotFoundException;

}