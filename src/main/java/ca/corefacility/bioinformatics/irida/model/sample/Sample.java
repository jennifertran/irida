package ca.corefacility.bioinformatics.irida.model.sample;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.corefacility.bioinformatics.irida.model.IridaResourceSupport;
import ca.corefacility.bioinformatics.irida.model.MutableIridaThing;
import ca.corefacility.bioinformatics.irida.model.assembly.GenomeAssembly;
import ca.corefacility.bioinformatics.irida.model.event.SampleAddedProjectEvent;
import ca.corefacility.bioinformatics.irida.model.irida.IridaSample;
import ca.corefacility.bioinformatics.irida.model.joins.impl.ProjectSampleJoin;
import ca.corefacility.bioinformatics.irida.model.remote.RemoteStatus;
import ca.corefacility.bioinformatics.irida.model.remote.RemoteSynchronizable;
import ca.corefacility.bioinformatics.irida.model.sample.metadata.MetadataEntry;

/**
 * A biological sample. Each sample may correspond to many files.
 * 
 * A {@link Sample} comprises of many attributes. The attributes assigned to a
 * {@link Sample} correspond to the NCBI Pathogen BioSample attributes. See
 * <a href=
 * "https://submit.ncbi.nlm.nih.gov/biosample/template/?package=Pathogen.cl.1.0&action=definition"
 * >BioSample Attributes: Package Pathogen</a> for more information.
 * 
 */
@Entity
@Table(name = "sample")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Sample extends IridaResourceSupport
		implements MutableIridaThing, IridaSample, Comparable<Sample>, RemoteSynchronizable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@CreatedDate
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private final Date createdDate;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	private String sampleName;

	@Lob
	private String description;

	/**
	 * The most descriptive organism name for this sample (to the species, if
	 * relevant).
	 */
	private String organism;

	/**
	 * identification or description of the specific individual from which this
	 * sample was obtained
	 */
	private String isolate;

	/**
	 * microbial or eukaryotic strain name
	 */
	private String strain;

	/**
	 * Name of the person who collected the sample.
	 */
	private String collectedBy;

	/**
	 * Date of sampling
	 */
	@Temporal(TemporalType.DATE)
	private Date collectionDate;

	/**
	 * Geographical origin of the sample (country derived from
	 * http://www.insdc.org/documents/country-qualifier-vocabulary).
	 */
	private String geographicLocationName;

	/**
	 * Describes the physical, environmental and/or local geographical source of
	 * the biological sample from which the sample was derived.
	 */
	@Lob
	private String isolationSource;

	/**
	 * lat_lon is marked as a *mandatory* attribute in NCBI BioSample, but in
	 * practice many of the fields are shown as "missing".
	 */
	private String latitude;

	private String longitude;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "sample")
	private List<ProjectSampleJoin> projects;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "sample")
	private List<SampleSequencingObjectJoin> sequenceFiles;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "sample")
	@NotAudited
	private List<SampleAddedProjectEvent> events;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "remote_status")
	private RemoteStatus remoteStatus;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapKeyColumn(name = "metadata_KEY")
	private Map<MetadataTemplateField, MetadataEntry> metadata;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "genome_assembly_id")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private GenomeAssembly genomeAssembly;

	public Sample() {
		createdDate = new Date();
		metadata = new HashMap<>();
	}

	/**
	 * Create a new {@link Sample} with the given name
	 * 
	 * @param sampleName
	 *            The name of the sample
	 */
	public Sample(String sampleName) {
		this();
		this.sampleName = sampleName;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Sample) {
			Sample sample = (Sample) other;
			return Objects.equals(id, sample.id) && Objects.equals(createdDate, sample.createdDate)
					&& Objects.equals(modifiedDate, sample.modifiedDate)
					&& Objects.equals(sampleName, sample.sampleName) && Objects.equals(description, sample.description)
					&& Objects.equals(organism, sample.organism) && Objects.equals(isolate, sample.isolate)
					&& Objects.equals(strain, sample.strain) && Objects.equals(collectedBy, sample.collectedBy)
					&& Objects.equals(collectionDate, sample.collectionDate)
					&& Objects.equals(geographicLocationName, sample.geographicLocationName)
					&& Objects.equals(isolationSource, sample.isolationSource)
					&& Objects.equals(latitude, sample.latitude) && Objects.equals(longitude, sample.longitude)
					&& Objects.equals(metadata, sample.metadata);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createdDate, modifiedDate, sampleName, description, organism, isolate, strain,
				collectedBy, collectionDate, geographicLocationName, isolationSource, latitude, longitude, metadata);
	}

	@Override
	public int compareTo(Sample other) {
		return modifiedDate.compareTo(other.modifiedDate);
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getLabel() {
		return sampleName;
	}

	@Override
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getStrain() {
		return strain;
	}

	public void setStrain(String strain) {
		this.strain = strain;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public String getCollectedBy() {
		return collectedBy;
	}

	public void setCollectedBy(String collectedBy) {
		this.collectedBy = collectedBy;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getIsolate() {
		return isolate;
	}

	public void setIsolate(String isolate) {
		this.isolate = isolate;
	}

	public String getGeographicLocationName() {
		return geographicLocationName;
	}

	public void setGeographicLocationName(String geographicLocationName) {
		this.geographicLocationName = geographicLocationName;
	}

	public String getIsolationSource() {
		return isolationSource;
	}

	public void setIsolationSource(String isolationSource) {
		this.isolationSource = isolationSource;
	}
	
	public GenomeAssembly getAssembly() {
		return genomeAssembly;
	}
	
	public void setGenomeAssembly(GenomeAssembly genomeAssembly) {
		this.genomeAssembly = genomeAssembly;
	}

	@Override
	public RemoteStatus getRemoteStatus() {
		return remoteStatus;
	}

	@Override
	public void setRemoteStatus(RemoteStatus status) {
		this.remoteStatus = status;
	}

	@JsonIgnore
	public Map<MetadataTemplateField, MetadataEntry> getMetadata() {
		return metadata;
	}

	
	/**
	 * Set the {@link MetadataEntry} collection for the sample. Note duplicate
	 * keys cannot be used (ignoring case)
	 * 
	 * @param inputMetadata
	 *            the collection of {@link MetadataEntry}s
	 */
	@JsonIgnore
	public void setMetadata(Map<MetadataTemplateField, MetadataEntry> inputMetadata) {
		this.metadata = inputMetadata;
	}
	
	/**
	 * Merge {@link MetadataEntry} into the sample's existing metadata collection. Duplicate
	 * keys will be overwritten.
	 * 
	 * @param inputMetadata
	 *            the metadata to merge into the sample
	 */
	public void mergeMetadata(Map<MetadataTemplateField, MetadataEntry> inputMetadata) {
		// loop through entry set and see if it already exists
		for (Entry<MetadataTemplateField, MetadataEntry> entry : inputMetadata.entrySet()) {

			// if the value isn't empty
			if (!entry.getValue().getValue().isEmpty()) {

				// if the key is found, replace the entry
				if (metadata.containsKey(entry.getKey())) {
					MetadataEntry metadataEntry = metadata.get(entry.getKey());
					metadataEntry.setValue(entry.getValue().getValue());
				} else {
					// otherwise add the new entry
					metadata.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
