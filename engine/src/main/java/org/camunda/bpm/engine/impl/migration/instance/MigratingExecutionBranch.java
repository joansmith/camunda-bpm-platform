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

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;

/**
 * Keeps track of executions created in a branch of the execution tree from the process instance downwards
 *
 * @author Thorben Lindhauer
 */
public class MigratingExecutionBranch {
  protected Map<ScopeImpl, ExecutionEntity> createdScopeExecutions;

  public MigratingExecutionBranch() {
    this(new HashMap<ScopeImpl, ExecutionEntity>());
  }

  protected MigratingExecutionBranch(Map<ScopeImpl, ExecutionEntity> createdScopeExecutions) {
    this.createdScopeExecutions = createdScopeExecutions;
  }

  public MigratingExecutionBranch copy() {
    return new MigratingExecutionBranch(new HashMap<ScopeImpl, ExecutionEntity>(createdScopeExecutions));
  }

  public ExecutionEntity getExecution(ScopeImpl scope) {
    return createdScopeExecutions.get(scope);
  }

  public boolean hasExecution(ScopeImpl scope) {
    return createdScopeExecutions.containsKey(scope);
  }

  public void registerExecution(ScopeImpl scope, ExecutionEntity execution) {
    this.createdScopeExecutions.put(scope, execution);
  }
}
