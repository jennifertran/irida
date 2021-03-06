package ca.corefacility.bioinformatics.irida.model.workflow.execution.galaxy;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Defines the state of a workflow.
 * Based off of states defined within Galaxy.
 * @see <a href="https://bitbucket.org/galaxy/galaxy-dist/src/097bbb3b7d3246faaa5188a1fc2a79b01630025c/lib/galaxy/model/__init__.py#cl-1316">Galaxy Dataset Model</a>
 * @see <a href="https://bitbucket.org/galaxy/galaxy-dist/src/097bbb3b7d3246faaa5188a1fc2a79b01630025c/lib/galaxy/web/base/controller.py#cl-429">Galaxy API</a>
 * @see <a href="https://github.com/jmchilton/blend4j/blob/c5e3f157d402950a843d4e395e1daf889945d587/src/main/java/com/github/jmchilton/blend4j/galaxy/beans/HistoryDetails.java">HistoryDetails in blend4j</a>
 *
 */
public enum GalaxyWorkflowState {
	NEW("new"),
	UPLOAD("upload"),
	QUEUED("queued"),
	RUNNING("running"),
	
	/**
	 * The workflow will be in this state when everything is complete.
	 */
	OK("ok"),
	
	EMPTY("empty"),
	ERROR("error"),
	DISCARDED("discarded"),
	PAUSED("paused"),
	SETTING_METADATA("setting_metadata"),
	FAILED_METADATA("failed_metadata"),
	RESUBMITTED("resubmitted");
	
	private static Map<String, GalaxyWorkflowState> stateMap = new HashMap<>();
	private String stateString;
	
	/**
	 * Sets of a Map used to convert a string to a WorkflowState
	 */
	static {
		for (GalaxyWorkflowState state : GalaxyWorkflowState.values()) {
			stateMap.put(state.toString(), state);
		}
	}
	
	private GalaxyWorkflowState(String stateString){
		this.stateString = stateString;
	}
	
	/**
	 * Given a string defining a state, converts this to a {@link GalaxyWorkflowState}.
	 * @param stateString  The string defining the state.
	 * @return  A {@link GalaxyWorkflowState} for the corresponding state.
	 */
	public static GalaxyWorkflowState stringToState(String stateString) {
		checkNotNull(stateString, "stateString is null");
		
		GalaxyWorkflowState state = stateMap.get(stateString);
		checkNotNull(state, "Unknown state " + stateString);
		
		return state;
	}
	
	@Override
	public String toString() {
		return stateString;
	}
}