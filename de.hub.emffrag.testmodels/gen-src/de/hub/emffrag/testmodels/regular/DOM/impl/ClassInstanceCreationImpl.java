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
package de.hub.emffrag.testmodels.regular.DOM.impl;

import de.hub.emffrag.testmodels.regular.DOM.AnonymousClassDeclaration;
import de.hub.emffrag.testmodels.regular.DOM.ClassInstanceCreation;
import de.hub.emffrag.testmodels.regular.DOM.DOMPackage;
import de.hub.emffrag.testmodels.regular.DOM.Expression;
import de.hub.emffrag.testmodels.regular.DOM.Type;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Class Instance Creation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.hub.emffrag.testmodels.regular.DOM.impl.ClassInstanceCreationImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link de.hub.emffrag.testmodels.regular.DOM.impl.ClassInstanceCreationImpl#getAnonymousClassDeclaration <em>Anonymous Class Declaration</em>}</li>
 *   <li>{@link de.hub.emffrag.testmodels.regular.DOM.impl.ClassInstanceCreationImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link de.hub.emffrag.testmodels.regular.DOM.impl.ClassInstanceCreationImpl#getType <em>Type</em>}</li>
 *   <li>{@link de.hub.emffrag.testmodels.regular.DOM.impl.ClassInstanceCreationImpl#getTypeArguments <em>Type Arguments</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ClassInstanceCreationImpl extends ExpressionImpl implements ClassInstanceCreation {
	/**
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<Expression> arguments;

	/**
	 * The cached value of the '{@link #getAnonymousClassDeclaration() <em>Anonymous Class Declaration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnonymousClassDeclaration()
	 * @generated
	 * @ordered
	 */
	protected AnonymousClassDeclaration anonymousClassDeclaration;

	/**
	 * The cached value of the '{@link #getExpression() <em>Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpression()
	 * @generated
	 * @ordered
	 */
	protected Expression expression;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Type type;

	/**
	 * The cached value of the '{@link #getTypeArguments() <em>Type Arguments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<Type> typeArguments;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ClassInstanceCreationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DOMPackage.Literals.CLASS_INSTANCE_CREATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Expression> getArguments() {
		if (arguments == null) {
			arguments = new EObjectContainmentEList.Resolving<Expression>(Expression.class, this, DOMPackage.CLASS_INSTANCE_CREATION__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnonymousClassDeclaration getAnonymousClassDeclaration() {
		if (anonymousClassDeclaration != null && anonymousClassDeclaration.eIsProxy()) {
			InternalEObject oldAnonymousClassDeclaration = (InternalEObject)anonymousClassDeclaration;
			anonymousClassDeclaration = (AnonymousClassDeclaration)eResolveProxy(oldAnonymousClassDeclaration);
			if (anonymousClassDeclaration != oldAnonymousClassDeclaration) {
				InternalEObject newAnonymousClassDeclaration = (InternalEObject)anonymousClassDeclaration;
				NotificationChain msgs = oldAnonymousClassDeclaration.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION, null, null);
				if (newAnonymousClassDeclaration.eInternalContainer() == null) {
					msgs = newAnonymousClassDeclaration.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION, oldAnonymousClassDeclaration, anonymousClassDeclaration));
			}
		}
		return anonymousClassDeclaration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnonymousClassDeclaration basicGetAnonymousClassDeclaration() {
		return anonymousClassDeclaration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnonymousClassDeclaration(AnonymousClassDeclaration newAnonymousClassDeclaration, NotificationChain msgs) {
		AnonymousClassDeclaration oldAnonymousClassDeclaration = anonymousClassDeclaration;
		anonymousClassDeclaration = newAnonymousClassDeclaration;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION, oldAnonymousClassDeclaration, newAnonymousClassDeclaration);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnonymousClassDeclaration(AnonymousClassDeclaration newAnonymousClassDeclaration) {
		if (newAnonymousClassDeclaration != anonymousClassDeclaration) {
			NotificationChain msgs = null;
			if (anonymousClassDeclaration != null)
				msgs = ((InternalEObject)anonymousClassDeclaration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION, null, msgs);
			if (newAnonymousClassDeclaration != null)
				msgs = ((InternalEObject)newAnonymousClassDeclaration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION, null, msgs);
			msgs = basicSetAnonymousClassDeclaration(newAnonymousClassDeclaration, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION, newAnonymousClassDeclaration, newAnonymousClassDeclaration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Expression getExpression() {
		if (expression != null && expression.eIsProxy()) {
			InternalEObject oldExpression = (InternalEObject)expression;
			expression = (Expression)eResolveProxy(oldExpression);
			if (expression != oldExpression) {
				InternalEObject newExpression = (InternalEObject)expression;
				NotificationChain msgs = oldExpression.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION, null, null);
				if (newExpression.eInternalContainer() == null) {
					msgs = newExpression.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION, oldExpression, expression));
			}
		}
		return expression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Expression basicGetExpression() {
		return expression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExpression(Expression newExpression, NotificationChain msgs) {
		Expression oldExpression = expression;
		expression = newExpression;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION, oldExpression, newExpression);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpression(Expression newExpression) {
		if (newExpression != expression) {
			NotificationChain msgs = null;
			if (expression != null)
				msgs = ((InternalEObject)expression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION, null, msgs);
			if (newExpression != null)
				msgs = ((InternalEObject)newExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION, null, msgs);
			msgs = basicSetExpression(newExpression, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION, newExpression, newExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type getType() {
		if (type != null && type.eIsProxy()) {
			InternalEObject oldType = (InternalEObject)type;
			type = (Type)eResolveProxy(oldType);
			if (type != oldType) {
				InternalEObject newType = (InternalEObject)type;
				NotificationChain msgs = oldType.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__TYPE, null, null);
				if (newType.eInternalContainer() == null) {
					msgs = newType.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__TYPE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DOMPackage.CLASS_INSTANCE_CREATION__TYPE, oldType, type));
			}
		}
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type basicGetType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetType(Type newType, NotificationChain msgs) {
		Type oldType = type;
		type = newType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DOMPackage.CLASS_INSTANCE_CREATION__TYPE, oldType, newType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(Type newType) {
		if (newType != type) {
			NotificationChain msgs = null;
			if (type != null)
				msgs = ((InternalEObject)type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__TYPE, null, msgs);
			if (newType != null)
				msgs = ((InternalEObject)newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DOMPackage.CLASS_INSTANCE_CREATION__TYPE, null, msgs);
			msgs = basicSetType(newType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DOMPackage.CLASS_INSTANCE_CREATION__TYPE, newType, newType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Type> getTypeArguments() {
		if (typeArguments == null) {
			typeArguments = new EObjectContainmentEList.Resolving<Type>(Type.class, this, DOMPackage.CLASS_INSTANCE_CREATION__TYPE_ARGUMENTS);
		}
		return typeArguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DOMPackage.CLASS_INSTANCE_CREATION__ARGUMENTS:
				return ((InternalEList<?>)getArguments()).basicRemove(otherEnd, msgs);
			case DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION:
				return basicSetAnonymousClassDeclaration(null, msgs);
			case DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION:
				return basicSetExpression(null, msgs);
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE:
				return basicSetType(null, msgs);
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE_ARGUMENTS:
				return ((InternalEList<?>)getTypeArguments()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DOMPackage.CLASS_INSTANCE_CREATION__ARGUMENTS:
				return getArguments();
			case DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION:
				if (resolve) return getAnonymousClassDeclaration();
				return basicGetAnonymousClassDeclaration();
			case DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION:
				if (resolve) return getExpression();
				return basicGetExpression();
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE_ARGUMENTS:
				return getTypeArguments();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DOMPackage.CLASS_INSTANCE_CREATION__ARGUMENTS:
				getArguments().clear();
				getArguments().addAll((Collection<? extends Expression>)newValue);
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION:
				setAnonymousClassDeclaration((AnonymousClassDeclaration)newValue);
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION:
				setExpression((Expression)newValue);
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE:
				setType((Type)newValue);
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE_ARGUMENTS:
				getTypeArguments().clear();
				getTypeArguments().addAll((Collection<? extends Type>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DOMPackage.CLASS_INSTANCE_CREATION__ARGUMENTS:
				getArguments().clear();
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION:
				setAnonymousClassDeclaration((AnonymousClassDeclaration)null);
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION:
				setExpression((Expression)null);
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE:
				setType((Type)null);
				return;
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE_ARGUMENTS:
				getTypeArguments().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DOMPackage.CLASS_INSTANCE_CREATION__ARGUMENTS:
				return arguments != null && !arguments.isEmpty();
			case DOMPackage.CLASS_INSTANCE_CREATION__ANONYMOUS_CLASS_DECLARATION:
				return anonymousClassDeclaration != null;
			case DOMPackage.CLASS_INSTANCE_CREATION__EXPRESSION:
				return expression != null;
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE:
				return type != null;
			case DOMPackage.CLASS_INSTANCE_CREATION__TYPE_ARGUMENTS:
				return typeArguments != null && !typeArguments.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ClassInstanceCreationImpl