/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.camunda.bpm.engine.impl.cmmn.operation;

import org.camunda.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.camunda.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.camunda.bpm.engine.impl.pvm.PvmException;

import static org.camunda.bpm.engine.delegate.CaseExecutionListener.MANUAL_START;
import static org.camunda.bpm.engine.impl.cmmn.execution.CaseExecutionState.ACTIVE;

/**
 * @author Roman Smirnov
 *
 */
public class AtomicOperationCaseExecutionManualStart extends AbstractCmmnEventAtomicOperation {

  public String getCanonicalName() {
    return "case-execution-manual-start";
  }

  protected String getEventName() {
    return MANUAL_START;
  }

  protected CmmnExecution eventNotificationsStarted(CmmnExecution execution) {
    try {
      CmmnActivityBehavior behavior = getActivityBehavior(execution);
      behavior.onManualStart(execution);

      execution.setCurrentState(ACTIVE);
    } catch (RuntimeException e) {
      String id = execution.getId();
      throw new PvmException("Cannot start case execution '"+id+"' manually.", e);
    }

    return execution;
  }

  protected void eventNotificationsCompleted(CmmnExecution execution) {
    try {
      CmmnActivityBehavior behavior = getActivityBehavior(execution);
      behavior.started(execution);

    } catch (Exception e) {
      String id = execution.getId();
      throw new PvmException("During the execution of case execution '"+id+"' occurred an exception.", e);
    }

  }

}