/**
 * (C) Copyright IBM Corp. 2010, 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.ibm.bi.dml.test.integration.applications;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** Group together the tests in this package/related subpackages into a single suite so that the Maven build
 *  won't run two of them at once. Since the DML and PyDML equivalent tests currently share the same directories,
 *  they should not be run in parallel. */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	
  // .applications.dml package
  com.ibm.bi.dml.test.integration.applications.dml.GNMFDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.dml.HITSDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.dml.LinearRegressionDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.dml.PageRankDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.dml.WelchTDMLTest.class,

  // .applications.pydml package
  com.ibm.bi.dml.test.integration.applications.pydml.GNMFPyDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.pydml.HITSPyDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.pydml.LinearRegressionPyDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.pydml.PageRankPyDMLTest.class,
  com.ibm.bi.dml.test.integration.applications.pydml.WelchTPyDMLTest.class,
  
  // .applications package
  ApplyTransformTest.class,
  ArimaTest.class,
  CsplineCGTest.class,
  CsplineDSTest.class,
  GLMTest.class,
  ID3Test.class,  
  L2SVMTest.class,
  LinearLogRegTest.class,
  MDABivariateStatsTest.class,
  MultiClassSVMTest.class,
  NaiveBayesTest.class
})


/** This class is just a holder for the above JUnit annotations. */
public class ZPackageSuite {

}