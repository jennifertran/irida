package ca.corefacility.bioinformatics.irida.ria.integration.projects;

import ca.corefacility.bioinformatics.irida.ria.integration.AbstractIridaUIIT;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.projects.ProjectAddSamplePage;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Integration Test for creating a new sample.
 */
@DatabaseSetup("/ca/corefacility/bioinformatics/irida/ria/web/projects/ProjectsAddSampleView.xml")
public class ProjectAddSamplePageIT extends AbstractIridaUIIT {
	private String NAME_ERROR_TOO_SHORT = "a";
	private String NAME_WITH_INVALID_CHARACTERS = "My Sample Name";
	private String NAME_VALID = "Sample00010-11";

	@Override
	public WebDriver driverToUse() {
		return new ChromeDriver();
	}

	@Test
	public void testSampleCreation() {
		ProjectAddSamplePage page = ProjectAddSamplePage.gotoAsProjectManager(driver());
		assertFalse("Create button should be disabled", page.isCreateButtonEnabled());

		// Test sending a too short of name
		page.enterSampleName(NAME_ERROR_TOO_SHORT);
		assertFalse("Create button should be disabled", page.isCreateButtonEnabled());
		assertTrue("Minimum Length for name error should be displayed", page.isMinLengthNameErrorVisible());

		// Test clearing the name
		page.enterSampleName("");
		assertFalse("Create button should be disabled", page.isCreateButtonEnabled());
		assertTrue("Required name error should be visible", page.isRequiredNameErrorVisible());

		// Test invalid characters
		page.enterSampleName(NAME_WITH_INVALID_CHARACTERS);
		assertFalse("Create button should be disabled", page.isCreateButtonEnabled());
		assertTrue("Invalid Characters in name error should be visible", page.isInvalidCharactersInNameVisible());

		// Create a valid sample
		page.enterSampleName(NAME_VALID);
		assertTrue("Create button should be enabled", page.isCreateButtonEnabled());
		page.createSample();
		assertTrue("Should redirect to sample files page.", driver().getCurrentUrl().contains("/sequenceFiles"));
	}
}
