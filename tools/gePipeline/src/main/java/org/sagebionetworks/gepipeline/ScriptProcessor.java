package org.sagebionetworks.gepipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.sagebionetworks.utils.ExternalProcessHelper;
import org.sagebionetworks.utils.ExternalProcessHelper.ExternalProcessResult;
import org.sagebionetworks.workflow.UnrecoverableException;

public class ScriptProcessor {
	private static final Logger log = Logger.getLogger(ScriptProcessor.class
			.getName());

	private static final String R_SCRIPT_REGEXP = ".*\\.[rR]$";
	private static final String R_ARGS_DELIMITER = "--args";

	private static final String SYNAPSE_USERNAME_KEY = "--username";
	private static final String SYNAPSE_SECRETKEY_KEY = "--secretKey";
	private static final String AUTH_ENDPOINT_KEY = "--authEndpoint";
	private static final String REPO_ENDPOINT_KEY = "--repoEndpoint";

	private static final String PROJECT_ID_PARAMETER_KEY = "--projectId";

//	private static final String INPUT_LAYER_PARAMETER_KEY = "--layerId";

	/**
	 * Run a particular script
	 * 
	 * @param script
	 * @param scriptParams -- attr, value pairs of params, e.g. INPUT_DATASET_PARAMETER_KEY, datasetId.toString()
	 * @return the ScriptResult
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnrecoverableException
	 * @throws JSONException
	 */
	public static ScriptResult doProcess(String script, List<String> scriptParams) throws IOException, InterruptedException,
			UnrecoverableException, JSONException {

		String argsDelimiter = "";
		String rScriptPath = "";
		if (script.matches(R_SCRIPT_REGEXP)) {
			 argsDelimiter = R_ARGS_DELIMITER;
			 rScriptPath = ConfigHelper.getRScriptPath();
		}
		
		String scriptInput[] = new String[] {rScriptPath, script, argsDelimiter,
				SYNAPSE_USERNAME_KEY, ConfigHelper.getSynapseUsername(),
				SYNAPSE_SECRETKEY_KEY, ConfigHelper.getSynapseSecretKey(),
				AUTH_ENDPOINT_KEY, ConfigHelper.getAuthenticationServicePublicEndpoint(),
				REPO_ENDPOINT_KEY, ConfigHelper.getRepositoryServiceEndpoint(),
				PROJECT_ID_PARAMETER_KEY, ConfigHelper.getProjectId()
				};
		
		// now append the parameters onto the end
		List<String> cumInput = new ArrayList<String>(Arrays.asList(scriptInput));
		cumInput.addAll(scriptParams);
		//scriptInput = (String[])cumInput.toArray();
		// now copy everything back
		scriptInput = new String[cumInput.size()];
		for (int i=0; i<cumInput.size(); i++) scriptInput[i]=cumInput.get(i);

		log.debug("About to run: " + StringUtils.join(scriptInput, " "));
		ExternalProcessResult result = ExternalProcessHelper.runExternalProcess(scriptInput);

		if (0 != result.getReturnCode()) {
			throw new UnrecoverableException("Activity failed(" + result.getReturnCode()
					+ ") for " + StringUtils.join(scriptInput, " ")
					+ " stderr: " + result.getStderr());
		}
		log.debug("Finished running: " + StringUtils.join(scriptInput, " "));

		ScriptResult scriptResult = new ScriptResult(result);
		
		return scriptResult;
	}
}