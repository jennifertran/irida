package ca.corefacility.bioinformatics.irida.model.sample.metadata;

import ca.corefacility.bioinformatics.irida.model.workflow.submission.AnalysisSubmission;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * {@link MetadataEntry} that has been created by an analysis pipeline
 */
@Entity
@Audited
@Table(name = "pipeline_metadata_entry")
public class PipelineProvidedMetadataEntry extends MetadataEntry {
	@ManyToOne
	private AnalysisSubmission submission;

	//private constructor for hibernate
	private PipelineProvidedMetadataEntry() {
	}

	/**
	 * Build a {@link PipelineProvidedMetadataEntry} with the given value, type and {@link AnalysisSubmission}
	 *
	 * @param value      the value of the metadata entry
	 * @param type       the datatype of the metadata
	 * @param submission the {@link AnalysisSubmission} that created this metadata
	 */
	public PipelineProvidedMetadataEntry(String value, String type, AnalysisSubmission submission) {
		super(value, type);
		this.submission = submission;
	}

	/**
	 * Get the {@link AnalysisSubmission} that created this metadata
	 *
	 * @return
	 */
	public AnalysisSubmission getSubmission() {
		return submission;
	}
}
