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
package de.hub.emffrag.testmodels.reflective.DOM.impl;

import org.eclipse.emf.ecore.EClass;

import de.hub.emffrag.testmodels.reflective.DOM.DOMPackage;
import de.hub.emffrag.testmodels.reflective.DOM.Expression;
import de.hub.emffrag.testmodels.reflective.DOM.SingleMemberAnnotation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Single Member Annotation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.hub.emffrag.testmodels.reflective.DOM.impl.SingleMemberAnnotationImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SingleMemberAnnotationImpl extends AnnotationImpl implements SingleMemberAnnotation {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SingleMemberAnnotationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DOMPackage.Literals.SINGLE_MEMBER_ANNOTATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Expression getValue() {
		return (Expression)eGet(DOMPackage.Literals.SINGLE_MEMBER_ANNOTATION__VALUE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(Expression newValue) {
		eSet(DOMPackage.Literals.SINGLE_MEMBER_ANNOTATION__VALUE, newValue);
	}

} //SingleMemberAnnotationImpl
