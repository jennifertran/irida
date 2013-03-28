/*
 * Copyright 2013 Franklin Bristow <franklin.bristow@phac-aspc.gc.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.corefacility.bioinformatics.irida.model;

import java.util.Collection;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A biological sample. Each sample may correspond to many files.
 *
 * @author Franklin Bristow <franklin.bristow@phac-aspc.gc.ca>
 */
public class Sample {

    private Identifier id;
    @NotNull
    private Audit audit;
    @NotNull
    @Size(min = 3)
    private String sampleName;
    @NotNull
    private Project project;
    private Collection<SampleFile> files;

    public Sample() {
        audit = new Audit();
    }

    public Sample(Identifier id) {
        this();
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Sample) {
            Sample sample = (Sample) other;
            return Objects.equals(sampleName, sample.sampleName)
                    && Objects.equals(project, sample.project)
                    && Objects.equals(files, sample.files);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleName, project, files);
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Collection<SampleFile> getFiles() {
        return files;
    }

    public void setFiles(Collection<SampleFile> files) {
        this.files = files;
    }
}
