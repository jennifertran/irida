package ca.corefacility.bioinformatics.irida.repositories.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import ca.corefacility.bioinformatics.irida.model.project.Project;
import ca.corefacility.bioinformatics.irida.model.user.group.UserGroup;
import ca.corefacility.bioinformatics.irida.repositories.IridaJpaRepository;

/**
 * A repository for {@link UserGroup}.
 *
 */
public interface UserGroupRepository extends IridaJpaRepository<UserGroup, Long> {

	/**
	 * Find a collection of {@link UserGroup} not already attached to the
	 * {@link Project}.
	 * 
	 * @param p
	 *            the project
	 * @return the user groups not on the project.
	 */
	@Query("from UserGroup ug where ug not in (select ugpj.userGroup from UserGroupProjectJoin ugpj where ugpj.project = ?1)")
	public List<UserGroup> findUserGroupsNotOnProject(final Project p);
}
