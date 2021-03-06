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
package org.camunda.bpm.engine.impl.migration.instance;

import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.PvmActivity;

/**
 * @author Thorben Lindhauer
 *
 */
public class MigratingNonScopeActivityInstance extends MigratingActivityInstance {

  @Override
  public void detachState() {
    ExecutionEntity currentExecution = resolveRepresentativeExecution();

    currentExecution.setActivity(null);
    currentExecution.leaveActivityInstance();

    if (dependentInstances != null) {
      for (MigratingInstance dependentInstance : dependentInstances) {
        dependentInstance.detachState();
      }
    }

    if (!currentExecution.isScope()) {
      ExecutionEntity parent = currentExecution.getParent();
      currentExecution.remove();
      parent.tryPruneLastConcurrentChild();
    }

  }

  @Override
  public void attachState(ExecutionEntity newScopeExecution) {
    this.representativeExecution = newScopeExecution;
    newScopeExecution.setActivity((PvmActivity) sourceScope);
    newScopeExecution.setActivityInstanceId(activityInstance.getId());

    if (dependentInstances != null) {
      for (MigratingInstance dependentInstance : dependentInstances) {
        dependentInstance.attachState(newScopeExecution);
      }
    }
  }

  @Override
  public void migrateState() {

    ExecutionEntity currentExecution = resolveRepresentativeExecution();
    currentExecution.setProcessDefinition(targetScope.getProcessDefinition());
    currentExecution.setActivity((PvmActivity) targetScope);
  }

  @Override
  public ExecutionEntity getFlowScopeExecution() {

    ExecutionEntity currentScopeExecution = resolveRepresentativeExecution();

    if (!currentScopeExecution.isScope()) {
      return currentScopeExecution.getParent();
    }
    else {
      return currentScopeExecution;
    }
  }

}
