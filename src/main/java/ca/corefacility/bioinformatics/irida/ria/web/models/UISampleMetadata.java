package ca.corefacility.bioinformatics.irida.ria.web.models;

import java.util.HashMap;
import java.util.Map;

import ca.corefacility.bioinformatics.irida.model.sample.MetadataTemplateField;
import ca.corefacility.bioinformatics.irida.model.sample.Sample;
import ca.corefacility.bioinformatics.irida.model.sample.metadata.MetadataEntry;

public class UISampleMetadata {
	private Long id;
	private String label;
	private Map<String, String> metadata;

	public UISampleMetadata(Sample sample) {
		this.id = sample.getId();
		this.label = sample.getLabel();
		this.metadata = getMetadataForSample(sample);
	}

	private Map<String, String> getMetadataForSample(Sample sample) {
		Map<String, String> entries = new HashMap<>();
		Map<MetadataTemplateField, MetadataEntry> sampleMetadata = sample.getMetadata();
		for (MetadataTemplateField field : sampleMetadata.keySet()) {
			MetadataEntry entry = sampleMetadata.getOrDefault(field, new MetadataEntry());
			entries.put(field.getLabel(), entry.getValue());
		}
		return entries;
	}

	public Long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}
}
