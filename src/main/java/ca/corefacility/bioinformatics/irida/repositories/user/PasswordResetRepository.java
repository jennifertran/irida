package ca.corefacility.bioinformatics.irida.repositories.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import ca.corefacility.bioinformatics.irida.model.user.PasswordReset;
import ca.corefacility.bioinformatics.irida.model.user.User;

/**
 * A repository to store password resets for a user.
 * 
 * @author Josh Adam <josh.adam@phac-aspc.gc.ca>
 */
public interface PasswordResetRepository extends PagingAndSortingRepository<PasswordReset, String> {

	/**
	 * Find any existing PasswordResets for the specified user.
	 *
	 * @param user The {@link User} to find existing PasswordReset for.
	 * @return
	 */
	public PasswordReset findByUser(User user);
}