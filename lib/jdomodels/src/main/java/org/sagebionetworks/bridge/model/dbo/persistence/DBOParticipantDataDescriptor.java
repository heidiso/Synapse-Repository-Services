package org.sagebionetworks.bridge.model.dbo.persistence;

import java.util.List;

import org.sagebionetworks.repo.model.dbo.AutoTableMapping;
import org.sagebionetworks.repo.model.dbo.Field;
import org.sagebionetworks.repo.model.dbo.MigratableDatabaseObject;
import org.sagebionetworks.repo.model.dbo.Table;
import org.sagebionetworks.repo.model.dbo.TableMapping;
import org.sagebionetworks.repo.model.dbo.migration.MigratableTableTranslation;
import org.sagebionetworks.repo.model.migration.MigrationType;
import org.sagebionetworks.repo.model.query.jdo.SqlConstants;

/**
 * descriptor of what's in a participant data record
 */
@Table(name = SqlConstants.TABLE_PARTICIPANT_DATA_DESCRIPTOR, constraints={
	"unique key UNIQUE_DSM_NAME (" + SqlConstants.COL_PARTICIPANT_DATA_DESCRIPTOR_NAME + ")"
})
public class DBOParticipantDataDescriptor implements MigratableDatabaseObject<DBOParticipantDataDescriptor, DBOParticipantDataDescriptor> {

	@Field(name = SqlConstants.COL_PARTICIPANT_DATA_DESCRIPTOR_ID, backupId = true, primary = true)
	private Long id;

	@Field(name = SqlConstants.COL_PARTICIPANT_DATA_DESCRIPTOR_NAME, varchar = 64, nullable = false)
	private String name;

	@Field(name = SqlConstants.COL_PARTICIPANT_DATA_DESCRIPTOR_DESCRIPTION, type = "text")
	private String description;

	private static TableMapping<DBOParticipantDataDescriptor> tableMapping = AutoTableMapping.create(DBOParticipantDataDescriptor.class);

	@Override
	public TableMapping<DBOParticipantDataDescriptor> getTableMapping() {
		return tableMapping;
	}

	@Override
	public MigrationType getMigratableTableType() {
		return MigrationType.PARTICIPANT_DATA_DESCRIPTOR;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DBOParticipantDataDescriptor other = (DBOParticipantDataDescriptor) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DBOParticipantDataDescriptor [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

	@Override
	public MigratableTableTranslation<DBOParticipantDataDescriptor, DBOParticipantDataDescriptor> getTranslator() {
		// We do not currently have a backup for this object.
		return new MigratableTableTranslation<DBOParticipantDataDescriptor, DBOParticipantDataDescriptor>() {

			@Override
			public DBOParticipantDataDescriptor createDatabaseObjectFromBackup(DBOParticipantDataDescriptor backup) {
				return backup;
			}

			@Override
			public DBOParticipantDataDescriptor createBackupFromDatabaseObject(DBOParticipantDataDescriptor dbo) {
				return dbo;
			}
		};
	}

	@Override
	public Class<? extends DBOParticipantDataDescriptor> getBackupClass() {
		return DBOParticipantDataDescriptor.class;
	}

	@Override
	public Class<? extends DBOParticipantDataDescriptor> getDatabaseObjectClass() {
		return DBOParticipantDataDescriptor.class;
	}

	@Override
	public List<MigratableDatabaseObject> getSecondaryTypes() {
		return null;
	}
}
