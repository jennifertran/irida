package ca.corefacility.bioinformatics.irida.web.controller.api.sequencingrun;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.corefacility.bioinformatics.irida.model.run.MiseqRun;
import ca.corefacility.bioinformatics.irida.model.run.SequencingRun;
import ca.corefacility.bioinformatics.irida.model.sequenceFile.SequenceFile;
import ca.corefacility.bioinformatics.irida.model.sequenceFile.SequencingObject;
import ca.corefacility.bioinformatics.irida.service.SequencingObjectService;
import ca.corefacility.bioinformatics.irida.service.SequencingRunService;
import ca.corefacility.bioinformatics.irida.web.controller.api.RESTGenericController;

import com.google.common.net.HttpHeaders;

/**
 *
 */
@Controller
public class RESTSequencingRunSequenceFilesController {

	private SequencingRunService miseqRunService;
	private SequencingObjectService sequencingObjectService;
	/**
	 * key used in map when adding sequencefile to miseqrun.
	 */
	public static final String SEQUENCEFILE_ID_KEY = "sequenceFileId";

	@Autowired
	public RESTSequencingRunSequenceFilesController(SequencingRunService service,
			SequencingObjectService sequencingObjectService) {
		this.miseqRunService = service;
		this.sequencingObjectService = sequencingObjectService;
	}

	/**
	 * Add a relationship between a {@link MiseqRun} and a {@link SequenceFile}.
	 *
	 * @param sequencingrunId
	 *            the id of the run to add sequence file to.
	 * @param representation
	 *            the JSON key-value pair that contains the identifier for the
	 *            sequenceFile
	 * @param response
	 *            a reference to the response.
	 * @return a response indicating that the collection was modified.
	 */
	@RequestMapping(value = "/api/sequencingrun/{sequencingrunId}/sequenceFiles", method = RequestMethod.POST)
	public ModelMap addSequenceFileToMiseqRun(@PathVariable Long sequencingrunId,
			@RequestBody Map<String, String> representation, HttpServletResponse response) {
		ModelMap modelMap = new ModelMap();
		String stringId = representation.get(SEQUENCEFILE_ID_KEY);
		long seqId = Long.parseLong(stringId);
		// first, get the SequenceFile
		SequencingObject sequencingObject = sequencingObjectService.read(seqId);
		// then, get the miseq run
		SequencingRun run = miseqRunService.read(sequencingrunId);
		// then add the user to the project with the specified role.
		miseqRunService.addSequencingObjectToSequencingRun(run, sequencingObject);

		MiseqRun miseqRun;
		if (run instanceof MiseqRun) {
			miseqRun = (MiseqRun) run;
		} else {
			throw new IllegalArgumentException("The sequencing run ID must correspond to a a valid MiSeq sequence");
		}
		Link seqFileLocation = linkTo(RESTSequencingRunController.class).slash(sequencingrunId).slash("sequenceFiles")
				.slash(seqId).withSelfRel();
		miseqRun.add(seqFileLocation);
		modelMap.addAttribute(RESTGenericController.RESOURCE_NAME, miseqRun);
		response.addHeader(HttpHeaders.LOCATION, seqFileLocation.getHref());
		response.setStatus(HttpStatus.CREATED.value());

		return modelMap;
	}

}
