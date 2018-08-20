/**
 * 
 */
package io.github.agentsoz.ees;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.core.utils.misc.CRCChecksum;
import org.matsim.testcases.MatsimTestUtils;

import io.github.agentsoz.util.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dsingh
 *
 */
@Ignore // Should remove this altogether, not added any value to existing tests [DS Aug/18]
public class MainCampbellsCreek50Test {
	// have tests in separate classes so that they run, at least under maven, in separate JVMs.  kai, nov'17

	private static final Logger log = LoggerFactory.getLogger(MainCampbellsCreek50Test.class ) ;

	@Rule public MatsimTestUtils utils = new MatsimTestUtils() ;

	@Test
	public void testCampbellsCreek50() {

		String [] args = {
				"--config",  "scenarios/mount-alexander-shire/campbells-creek-50/scenario_main.xml",
				"--logfile", "scenarios/mount-alexander-shire/campbells-creek-50/scenario.log",
				"--loglevel", "INFO",
				//	                "--plan-selection-policy", "FIRST", // ensures it is deterministic, as default is RANDOM
				"--seed", "12345",
				"--safeline-output-file-pattern", "scenarios/mount-alexander-shire/campbells-creek-50/safeline.%d%.out",
				"--matsim-output-directory", utils.getOutputDirectory(),
				"--jillconfig", "--config={"+
						"agents:[{classname:io.github.agentsoz.ees.agents.Resident, args:null, count:50}],"+
						"logLevel: WARN,"+
						"logFile: \"scenarios/mount-alexander-shire/campbells-creek-50/jill.log\","+
						"programOutputFile: \"scenarios/mount-alexander-shire/campbells-creek-50/jill.out\","+
						"randomSeed: 12345"+ // jill random seed
						//"numThreads: 1"+ // run jill in single-threaded mode so logs are deterministic
		"}"};

		Main.main(args);

		final String actualEventsFilename = utils.getOutputDirectory() + "/output_events.xml.gz";
		long actualEventsCRC = CRCChecksum.getCRCFromFile( actualEventsFilename ) ;
		System.err.println("actual(events)="+actualEventsCRC) ;

		long actualPlansCRC = CRCChecksum.getCRCFromFile( utils.getOutputDirectory() + "/output_plans.xml.gz" ) ;
		System.err.println("actual(plans)="+actualPlansCRC) ;
		
		// ---
		
		final String primaryExpectedEventsFilename = utils.getInputDirectory() + "/output_events.xml.gz";

		// ---
		
		TestUtils.comparingDepartures(primaryExpectedEventsFilename,actualEventsFilename,1.);
		TestUtils.comparingArrivals(primaryExpectedEventsFilename,actualEventsFilename,1.);
		TestUtils.comparingActivityStarts(primaryExpectedEventsFilename,actualEventsFilename, 1.);
		TestUtils.compareFullEvents(primaryExpectedEventsFilename,actualEventsFilename, true);

		// ---
//		{
//			long[] expectedEventsCRCs = new long[]{
//					CRCChecksum.getCRCFromFile(primaryExpectedEventsFilename)
//			};
//			TestUtils.checkSeveral(expectedEventsCRCs, actualEventsCRC);
//		}
		{
			long[] expectedPlansCRCs = new long[]{
					CRCChecksum.getCRCFromFile(utils.getInputDirectory() + "/output_plans.xml.gz")
			};
			TestUtils.checkSeveral(expectedPlansCRCs, actualPlansCRC);
		}

		//		{
		//			long expectedCRC = CRCChecksum.getCRCFromFile( utils.getInputDirectory() + "/jill.out" ) ;
		//			long actualCRC = CRCChecksum.getCRCFromFile( "scenarios/campbells-creek/jill.out" ) ;
		//			Assert.assertEquals (expectedCRC, actualCRC); 
		//		}
	}


}
