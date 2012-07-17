/**
 * Copyright 2012 Markus Scheidgen
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
 */
package de.hub.emffrag.testmodels.cdo.DOM;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Prefix Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.hub.emffrag.testmodels.cdo.DOM.PrefixExpression#getOperand <em>Operand</em>}</li>
 *   <li>{@link de.hub.emffrag.testmodels.cdo.DOM.PrefixExpression#getOperator <em>Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.hub.emffrag.testmodels.cdo.DOM.DOMPackage#getPrefixExpression()
 * @model
 * @generated
 */
public interface PrefixExpression extends Expression {
	/**
	 * Returns the value of the '<em><b>Operand</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operand</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operand</em>' containment reference.
	 * @see #setOperand(Expression)
	 * @see de.hub.emffrag.testmodels.cdo.DOM.DOMPackage#getPrefixExpression_Operand()
	 * @model containment="true" required="true" ordered="false"
	 * @generated
	 */
	Expression getOperand();

	/**
	 * Sets the value of the '{@link de.hub.emffrag.testmodels.cdo.DOM.PrefixExpression#getOperand <em>Operand</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operand</em>' containment reference.
	 * @see #getOperand()
	 * @generated
	 */
	void setOperand(Expression value);

	/**
	 * Returns the value of the '<em><b>Operator</b></em>' attribute.
	 * The literals are from the enumeration {@link de.hub.emffrag.testmodels.cdo.DOM.PrefixExpressionOperatorKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operator</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operator</em>' attribute.
	 * @see de.hub.emffrag.testmodels.cdo.DOM.PrefixExpressionOperatorKind
	 * @see #setOperator(PrefixExpressionOperatorKind)
	 * @see de.hub.emffrag.testmodels.cdo.DOM.DOMPackage#getPrefixExpression_Operator()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
	PrefixExpressionOperatorKind getOperator();

	/**
	 * Sets the value of the '{@link de.hub.emffrag.testmodels.cdo.DOM.PrefixExpression#getOperator <em>Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operator</em>' attribute.
	 * @see de.hub.emffrag.testmodels.cdo.DOM.PrefixExpressionOperatorKind
	 * @see #getOperator()
	 * @generated
	 */
	void setOperator(PrefixExpressionOperatorKind value);

} // PrefixExpression