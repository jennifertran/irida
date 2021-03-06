package ca.corefacility.bioinformatics.irida.model.workflow.config;

import java.util.Set;

import ca.corefacility.bioinformatics.irida.model.workflow.IridaWorkflow;

/**
 * Wraps around a {@link Set} of {@link IridaWorkflow}s to allow it
 * to be handled as a spring managed bean.
 * 
 *
 */
public class IridaWorkflowSet {
	private Set<IridaWorkflow> iridaWorkflows;

	/**
	 * Builds a new {@link IridaWorkflowSet} of workflows.
	 * 
	 * @param iridaWorkflows
	 *            The set of workflows to build.
	 */
	public IridaWorkflowSet(Set<IridaWorkflow> iridaWorkflows) {
		this.iridaWorkflows = iridaWorkflows;
	}

	public Set<IridaWorkflow> getIridaWorkflows() {
		return iridaWorkflows;
	}
}
