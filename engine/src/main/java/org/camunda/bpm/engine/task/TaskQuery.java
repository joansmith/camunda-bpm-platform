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
package org.camunda.bpm.engine.task;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.variable.type.ValueType;

/**
 * Allows programmatic querying of {@link Task}s;
 *
 * @author Joram Barrez
 * @author Falko Menge
 */
public interface TaskQuery extends Query<TaskQuery, Task>{

  /**
   * Only select tasks with the given task id (in practice, there will be
   * maximum one of this kind)
   */
  TaskQuery taskId(String taskId);

  /** Only select tasks with the given name */
  TaskQuery taskName(String name);

  /** Only select tasks with a name matching the parameter.
   *  The syntax is that of SQL: for example usage: nameLike(%activiti%)*/
  TaskQuery taskNameLike(String nameLike);

  /** Only select tasks with the given description. */
  TaskQuery taskDescription(String description);

  /** Only select tasks with a description matching the parameter .
   *  The syntax is that of SQL: for example usage: descriptionLike(%activiti%)*/
  TaskQuery taskDescriptionLike(String descriptionLike);

  /** Only select tasks with the given priority. */
  TaskQuery taskPriority(Integer priority);

  /** Only select tasks with the given priority or higher. */
  TaskQuery taskMinPriority(Integer minPriority);

  /** Only select tasks with the given priority or lower. */
  TaskQuery taskMaxPriority(Integer maxPriority);

  /** Only select tasks which are assigned to the given user. */
  TaskQuery taskAssignee(String assignee);

  /**
   *  <p>Only select tasks which are assigned to the user described by the given expression.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery taskAssigneeExpression(String assigneeExpression);

  /** Only select tasks which are matching the given user.
   *  The syntax is that of SQL: for example usage: nameLike(%activiti%)*/
  TaskQuery taskAssigneeLike(String assignee);

  /**
   * <p>Only select tasks which are assigned to the user described by the given expression.
   *  The syntax is that of SQL: for example usage: taskAssigneeLikeExpression("${'%test%'}")</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery taskAssigneeLikeExpression(String assigneeLikeExpression);

  /** Only select tasks for which the given user is the owner. */
  TaskQuery taskOwner(String owner);

  /**
   * <p>Only select tasks for which the described user by the given expression is the owner.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery taskOwnerExpression(String ownerExpression);

  /** Only select tasks which don't have an assignee. */
  TaskQuery taskUnassigned();

  /** @see {@link #taskUnassigned} */
  @Deprecated
  TaskQuery taskUnnassigned();

  /** Only select tasks with the given {@link DelegationState}. */
  TaskQuery taskDelegationState(DelegationState delegationState);

  /**
   * Only select tasks for which the given user is a candidate.
   *
   * <p>
   * Per default it only selects tasks which are not already assigned
   * to a user. To also include assigned task in the result specify
   * {@link #includeAssignedTasks()} in your query.
   * </p>
   *
   * @throws ProcessEngineException
   *   <ul><li>When query is executed and {@link #taskCandidateGroup(String)} or
   *     {@link #taskCandidateGroupIn(List)} has been executed on the query instance.
   *   <li>When passed user is <code>null</code>.
   *   </ul>
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   *
   */
  TaskQuery taskCandidateUser(String candidateUser);

  /**
   * Only select tasks for which the described user by the given expression is a candidate.
   *
   * <p>
   * Per default it only selects tasks which are not already assigned
   * to a user. To also include assigned task in the result specify
   * {@link #includeAssignedTasks()} in your query.
   * </p>
   *
   * @throws ProcessEngineException
   *   <ul><li>When query is executed and {@link #taskCandidateGroup(String)} or
   *     {@link #taskCandidateGroupIn(List)} has been executed on the query instance.
   *   <li>When passed user is <code>null</code>.
   *   </ul>
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery taskCandidateUserExpression(String candidateUserExpression);

  /** Only select tasks for which there exist an {@link IdentityLink} with the given user */
  TaskQuery taskInvolvedUser(String involvedUser);

  /**
   * <p>Only select tasks for which there exist an {@link IdentityLink} with the
   * described user by the given expression</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery taskInvolvedUserExpression(String involvedUserExpression);

  /**
   *  Only select tasks for which users in the given group are candidates.
   *
   * <p>
   * Per default it only selects tasks which are not already assigned
   * to a user. To also include assigned task in the result specify
   * {@link #includeAssignedTasks()} in your query.
   * </p>
   *
   * @throws ProcessEngineException
   *   When query is executed and {@link #taskCandidateUser(String)} or
   *     {@link #taskCandidateGroupIn(List)} has been executed on the query instance.
   *   When passed group is <code>null</code>.
   */
  TaskQuery taskCandidateGroup(String candidateGroup);

  /**
   * Only select tasks for which users in the described group by the given expression are candidates.
   *
   * <p>
   * Per default it only selects tasks which are not already assigned
   * to a user. To also include assigned task in the result specify
   * {@link #includeAssignedTasks()} in your query.
   * </p>
   *
   * @throws ProcessEngineException
   *   <ul><li>When query is executed and {@link #taskCandidateUser(String)} or
   *     {@link #taskCandidateGroupIn(List)} has been executed on the query instance.
   *   <li>When passed group is <code>null</code>.
   *   </ul>
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery taskCandidateGroupExpression(String candidateGroupExpression);

  /**
   * Only select tasks for which the 'candidateGroup' is one of the given groups.
   *
   * <p>
   * Per default it only selects tasks which are not already assigned
   * to a user. To also include assigned task in the result specify
   * {@link #includeAssignedTasks()} in your query.
   * </p>
   *
   * @throws ProcessEngineException
   *   When query is executed and {@link #taskCandidateGroup(String)} or
   *     {@link #taskCandidateUser(String)} has been executed on the query instance.
   *   When passed group list is empty or <code>null</code>.
   */
  TaskQuery taskCandidateGroupIn(List<String> candidateGroups);

  /**
   * Only select tasks for which the 'candidateGroup' is one of the described groups of the given expression.
   *
   * <p>
   * Per default it only selects tasks which are not already assigned
   * to a user. To also include assigned task in the result specify
   * {@link #includeAssignedTasks()} in your query.
   * </p>
   *
   * @throws ProcessEngineException
   *   <ul><li>When query is executed and {@link #taskCandidateGroup(String)} or
   *     {@link #taskCandidateUser(String)} has been executed on the query instance.
   *   <li>When passed group list is empty or <code>null</code>.</ul>
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery taskCandidateGroupInExpression(String candidateGroupsExpression);

  /**
   * Select both assigned and not assigned tasks for candidate user or group queries.
   * <p>
   * By default {@link #taskCandidateUser(String)}, {@link #taskCandidateGroup(String)}
   * and {@link #taskCandidateGroupIn(List)} queries only select not assigned tasks.
   * </p>
   *
   * @throws ProcessEngineException
   *    When no candidate user or group(s) are specified beforehand
   */
  TaskQuery includeAssignedTasks();

  /** Only select tasks for the given process instance id. */
  TaskQuery processInstanceId(String processInstanceId);

  /** Only select tasks for the given process instance business key */
  TaskQuery processInstanceBusinessKey(String processInstanceBusinessKey);

  /**
   * Only select tasks for any of the given the given process instance business keys.
   */
  TaskQuery processInstanceBusinessKeyIn(String... processInstanceBusinessKeys);

  /** Only select tasks matching the given process instance business key.
   *  The syntax is that of SQL: for example usage: nameLike(%activiti%)*/
  TaskQuery processInstanceBusinessKeyLike(String processInstanceBusinessKey);

  /** Only select tasks for the given execution. */
  TaskQuery executionId(String executionId);

  /** Only select task which have one of the activity instance ids. **/
  TaskQuery activityInstanceIdIn(String... activityInstanceIds);

  /** Only select tasks that are created on the given date. **/
  TaskQuery taskCreatedOn(Date createTime);

  /** Only select tasks that are created on the described date by the given expression. **/
  TaskQuery taskCreatedOnExpression(String createTimeExpression);

  /** Only select tasks that are created before the given date. **/
  TaskQuery taskCreatedBefore(Date before);

  /** Only select tasks that are created before the described date by the given expression. **/
  TaskQuery taskCreatedBeforeExpression(String beforeExpression);

  /** Only select tasks that are created after the given date. **/
  TaskQuery taskCreatedAfter(Date after);

  /** Only select tasks that are created after the described date by the given expression. **/
  TaskQuery taskCreatedAfterExpression(String afterExpression);

  /** Only select tasks that have no parent (i.e. do not select subtasks). **/
  TaskQuery excludeSubtasks();

  /**
   * Only select tasks with the given taskDefinitionKey.
   * The task definition key is the id of the userTask:
   * &lt;userTask id="xxx" .../&gt;
   **/
  TaskQuery taskDefinitionKey(String key);

  /**
   * Only select tasks with a taskDefinitionKey that match the given parameter.
   *  The syntax is that of SQL: for example usage: taskDefinitionKeyLike("%activiti%").
   * The task definition key is the id of the userTask:
   * &lt;userTask id="xxx" .../&gt;
   **/
  TaskQuery taskDefinitionKeyLike(String keyLike);

  /** Only select tasks which have one of the taskDefinitionKeys. **/
  TaskQuery taskDefinitionKeyIn(String... taskDefinitionKeys);

  /**
   * Select the tasks which are sub tasks of the given parent task.
   */
  TaskQuery taskParentTaskId(String parentTaskId);

  /** Only select tasks for the given case instance id. */
  TaskQuery caseInstanceId(String caseInstanceId);

  /** Only select tasks for the given case instance business key */
  TaskQuery caseInstanceBusinessKey(String caseInstanceBusinessKey);

  /** Only select tasks matching the given case instance business key.
   *  The syntax is that of SQL: for example usage: nameLike(%aBusinessKey%)*/
  TaskQuery caseInstanceBusinessKeyLike(String caseInstanceBusinessKeyLike);

  /** Only select tasks for the given case execution. */
  TaskQuery caseExecutionId(String caseExecutionId);

  /**
   * Only select tasks which are part of a case instance which has the given
   * case definition key.
   */
  TaskQuery caseDefinitionKey(String caseDefinitionKey);

  /**
   * Only select tasks which are part of a case instance which has the given
   * case definition id.
   */
  TaskQuery caseDefinitionId(String caseDefinitionId);

  /**
   * Only select tasks which are part of a case instance which has the given
   * case definition name.
   */
  TaskQuery caseDefinitionName(String caseDefinitionName);

  /**
   * Only select tasks which are part of a case instance which case definition
   * name is like the given parameter.
   * The syntax is that of SQL: for example usage: nameLike(%processDefinitionName%)*/
  TaskQuery caseDefinitionNameLike(String caseDefinitionNameLike);

  /**
   * Only select tasks which have a local task variable with the given name
   * set to the given value.
   */
  TaskQuery taskVariableValueEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which have a local task variable with the given name, but
   * with a different value than the passed value.
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   */
  TaskQuery taskVariableValueNotEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which have a local task variable with the given name
   * matching the given value.
   * The syntax is that of SQL: for example usage: valueLike(%value%)
   */
  TaskQuery taskVariableValueLike(String variableName, String variableValue);

  /**
   * Only select tasks which have a local task variable with the given name
   * and a value greater than the given one.
   */
  TaskQuery taskVariableValueGreaterThan(String variableName, Object variableValue);

  /**
   * Only select tasks which have a local task variable with the given name
   * and a value greater than or equal to the given one.
   */
  TaskQuery taskVariableValueGreaterThanOrEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which have a local task variable with the given name
   * and a value less than the given one.
   */
  TaskQuery taskVariableValueLessThan(String variableName, Object variableValue);

  /**
   * Only select tasks which have a local task variable with the given name
   * and a value less than or equal to the given one.
   */
  TaskQuery taskVariableValueLessThanOrEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which have are part of a process that have a variable
   * with the given name set to the given value.
   */
  TaskQuery processVariableValueEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which have a variable with the given name, but
   * with a different value than the passed value.
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   */
  TaskQuery processVariableValueNotEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a process that have a variable
   * with the given name and matching the given value.
   * The syntax is that of SQL: for example usage: valueLike(%value%)*/
  TaskQuery processVariableValueLike(String variableName, String variableValue);

  /**
   * Only select tasks which are part of a process that have a variable
   * with the given name and a value greater than the given one.
   */
  TaskQuery processVariableValueGreaterThan(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a process that have a variable
   * with the given name and a value greater than or equal to the given one.
   */
  TaskQuery processVariableValueGreaterThanOrEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a process that have a variable
   * with the given name and a value less than the given one.
   */
  TaskQuery processVariableValueLessThan(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a process that have a variable
   * with the given name and a value greater than or equal to the given one.
   */
  TaskQuery processVariableValueLessThanOrEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a case instance that have a variable
   * with the given name set to the given value. The type of variable is determined based
   * on the value, using types configured in {@link ProcessEngineConfiguration#getVariableSerializers()}.
   *
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   *
   * @param name name of the variable, cannot be null.
   */
  TaskQuery caseInstanceVariableValueEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a case instance that have a variable
   * with the given name, but with a different value than the passed value. The
   * type of variable is determined based on the value, using types configured
   * in {@link ProcessEngineConfiguration#getVariableSerializers()}.
   *
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   *
   * @param name name of the variable, cannot be null.
   */
  TaskQuery caseInstanceVariableValueNotEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a case instance that have a variable value
   * like the given value.
   *
   * This be used on string variables only.
   *
   * @param name variable name, cannot be null.
   *
   * @param value variable value. The string can include the
   * wildcard character '%' to express like-strategy:
   * starts with (string%), ends with (%string) or contains (%string%).
   */
  TaskQuery caseInstanceVariableValueLike(String variableName, String variableValue);


  /**
   * Only select tasks which are part of a case instance that have a variable
   * with the given name and a variable value greater than the passed value.
   *
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   *
   * @param name variable name, cannot be null.
   */
  TaskQuery caseInstanceVariableValueGreaterThan(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a case instance that have a
   * variable value greater than or equal to the passed value.
   *
   * Booleans, Byte-arrays and {@link Serializable} objects (which
   * are not primitive type wrappers) are not supported.
   *
   * @param name variable name, cannot be null.
   */
  TaskQuery caseInstanceVariableValueGreaterThanOrEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a case instance that have a variable
   * value less than the passed value.
   *
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   *
   * @param name variable name, cannot be null.
   */
  TaskQuery caseInstanceVariableValueLessThan(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a case instance that have a variable
   * value less than or equal to the passed value.
   *
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   *
   * @param name variable name, cannot be null.
   */
  TaskQuery caseInstanceVariableValueLessThanOrEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a process instance which has the given
   * process definition key.
   */
  TaskQuery processDefinitionKey(String processDefinitionKey);

  /**
   * Only select tasks which are part of a process instance which has one of the
   * given process definition keys.
   */
  TaskQuery processDefinitionKeyIn(String... processDefinitionKeys);

  /**
   * Only select tasks which are part of a process instance which has the given
   * process definition id.
   */
  TaskQuery processDefinitionId(String processDefinitionId);

  /**
   * Only select tasks which are part of a process instance which has the given
   * process definition name.
   */
  TaskQuery processDefinitionName(String processDefinitionName);

  /**
   * Only select tasks which are part of a process instance which process definition
   * name  is like the given parameter.
   * The syntax is that of SQL: for example usage: nameLike(%processDefinitionName%)*/
  TaskQuery processDefinitionNameLike(String processDefinitionName);

  /**
   * Only select tasks with the given due date.
   */
  TaskQuery dueDate(Date dueDate);

  /**
   * <p>Only select tasks with the described due date by the given expression.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery dueDateExpression(String dueDateExpression);

  /**
   * Only select tasks which have a due date before the given date.
   */
  TaskQuery dueBefore(Date dueDate);

  /**
   * <p>Only select tasks which have a due date before the described date by the given expression.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery dueBeforeExpression(String dueDateExpression);

  /**
   * Only select tasks which have a due date after the given date.
   */
  TaskQuery dueAfter(Date dueDate);

  /**
   * <p>Only select tasks which have a due date after the described date by the given expression.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery dueAfterExpression(String dueDateExpression);

  /**
   * Only select tasks with the given follow-up date.
   */
  TaskQuery followUpDate(Date followUpDate);

  /**
   * <p>Only select tasks with the described follow-up date by the given expression.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery followUpDateExpression(String followUpDateExpression);

  /**
   * Only select tasks which have a follow-up date before the given date.
   */
  TaskQuery followUpBefore(Date followUpDate);

  /**
   * <p>Only select tasks which have a follow-up date before the described date by the given expression.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery followUpBeforeExpression(String followUpDateExpression);

  /**
   * Only select tasks which have no follow-up date or a follow-up date before the given date.
   * Serves the typical use case "give me all tasks without follow-up or follow-up date which is already due"
   */
  TaskQuery followUpBeforeOrNotExistent(Date followUpDate);

  /**
   * <p>Only select tasks which have no follow-up date or a follow-up date before the described date by the given expression.
   * Serves the typical use case "give me all tasks without follow-up or follow-up date which is already due"</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery followUpBeforeOrNotExistentExpression(String followUpDateExpression);

  /**
   * Only select tasks which have a follow-up date after the given date.
   */
  TaskQuery followUpAfter(Date followUpDate);

  /**
   * <p>Only select tasks which have a follow-up date after the described date by the given expression.</p>
   *
   * @throws BadUserRequestException
   *   <ul><li>When the query is executed and expressions are disabled for adhoc queries
   *  (in case the query is executed via {@link #list()}, {@link #listPage(int, int)}, {@link #singleResult()}, or {@link #count()})
   *  or stored queries (in case the query is stored along with a filter).
   *  Expression evaluation can be activated by setting the process engine configuration properties
   *  <code>enableExpressionsInAdhocQueries</code> (default <code>false</code>) and
   *  <code>enableExpressionsInStoredQueries</code> (default <code>true</code>) to <code>true</code>.
   */
  TaskQuery followUpAfterExpression(String followUpDateExpression);

  /**
   * Only selects tasks which are suspended, because its process instance was suspended.
   */
  TaskQuery suspended();

  /**
   * Only selects tasks which are active (ie. not suspended)
   */
  TaskQuery active();

  /**
   * If called, the form keys of the fetched tasks are initialized and
   * {@link Task#getFormKey()} will return a value (in case the task has a form key).
   *
   * @return the query itself
   */
  TaskQuery initializeFormKeys();

  /** Only select tasks definitions with one of the given tenant ids. */
  TaskQuery tenantIdIn(String... tenantIds);

  // ordering ////////////////////////////////////////////////////////////

  /** Order by task id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByTaskId();

  /** Order by task name (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByTaskName();

  /** Order by task name case insensitive (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByTaskNameCaseInsensitive();

  /** Order by description (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByTaskDescription();

  /** Order by priority (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByTaskPriority();

  /** Order by assignee (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByTaskAssignee();

  /** Order by the time on which the tasks were created (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByTaskCreateTime();

  /** Order by process instance id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByProcessInstanceId();

  /** Order by case instance id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByCaseInstanceId();

  /** Order by execution id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByExecutionId();

  /** Order by case execution id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByCaseExecutionId();

  /** Order by due date (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByDueDate();

  /** Order by follow-up date (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQuery orderByFollowUpDate();

  /**
   * Order by a process instance variable value of a certain type. Calling this method multiple times
   * specifies secondary, tertiary orderings, etc. The ordering of variables with <code>null</code>
   * values is database-specific.
   */
  TaskQuery orderByProcessVariable(String variableName, ValueType valueType);

  /**
   * Order by an execution variable value of a certain type. Calling this method multiple times
   * specifies secondary, tertiary orderings, etc. The ordering of variables with <code>null</code>
   * values is database-specific.
   */
  TaskQuery orderByExecutionVariable(String variableName, ValueType valueType);

  /**
   * Order by a task variable value of a certain type. Calling this method multiple times
   * specifies secondary, tertiary orderings, etc. The ordering of variables with <code>null</code>
   * values is database-specific.
   */
  TaskQuery orderByTaskVariable(String variableName, ValueType valueType);

  /**
   * Order by a task variable value of a certain type. Calling this method multiple times
   * specifies secondary, tertiary orderings, etc. The ordering of variables with <code>null</code>
   * values is database-specific.
   */
  TaskQuery orderByCaseExecutionVariable(String variableName, ValueType valueType);

  /**
   * Order by a task variable value of a certain type. Calling this method multiple times
   * specifies secondary, tertiary orderings, etc. The ordering of variables with <code>null</code>
   * values is database-specific.
   */
  TaskQuery orderByCaseInstanceVariable(String variableName, ValueType valueType);

  /** Order by tenant id (needs to be followed by {@link #asc()} or {@link #desc()}).
   * Note that the ordering of process instances without tenant id is database-specific. */
  TaskQuery orderByTenantId();
}
