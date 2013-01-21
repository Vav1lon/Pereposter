package com.pereposter.utils.usertype;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Example of usage<br/>
 * <br/>
 *<br/>
 *For enum like this one:<br/>
 *<br/>
 * public enum OrderPayType {<br/>
 CASH(1, "cash"),<br/>
 EUROSET(2, "euroset"),<br/>
 private int id;<br/>
 private String name;<br/>
 OrderPayType(int id, String name) {<br/>
 this.id = id;<br/>
 this.name = name;<br/>
 }<br/>
 public int getId() {<br/>
 return id;<br/>
 }<br/>
 public String getName() {<br/>
 return name;<br/>
 }
 <br/>
 public static final OrderPayType fromName(String name) {<br/>
 for (OrderPayType v : OrderPayType.values()) {<br/>
 if (StringUtils.equals(v.getName(), name)) {<br/>
 return v;<br/>
 }<br/>
 }<br/>
 throw new IllegalArgumentException("couldn't find payment type with id: " + name);<br/>
 }<br/>
 public static final OrderPayType fromInt(int payTypeId) {<br/>
 for (OrderPayType v : OrderPayType.values()) {<br/>
 if (v.getId() == payTypeId) {<br/>
 return v;<br/>
 }<br/>
 }<br/>
 throw new IllegalArgumentException("couldn't find payment type with id: " + payTypeId);<br/>
 }<br/>
 }<br/>
 * &#064Column(name = "pay_type", nullable = true)<br/>
 &#064Type(<br/>
 type = "ru.agent.persistence.model.userTypes.GenericEnumUserType",<br/>
 parameters = {<br/>
 &#064Parameter(<br/>
 name = "enumClass",<br/>
 value = "ru.agent.common.enums.OrderPayType"),<br/>
 &#064Parameter(<br/>
 name = "identifierMethod",<br/>
 value = "getId"),<br/>
 &#064Parameter(<br/>
 name = "valueOfMethod",<br/>
 value = "fromInt")<br/>
 }<br/>
 )<br/>
 *
 * Date: Nov 2, 2011
 * Time: 13:00:00
 *
 *
 */
public class GenericEnumUserType implements UserType, ParameterizedType {
    private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "name";
    private static final String DEFAULT_VALUE_OF_METHOD_NAME = "valueOf";

    private Class<? extends Enum> enumClass;
    private Class<?> identifierType;
    private Method identifierMethod;
    private Method valueOfMethod;
    private AbstractStandardBasicType<?> type;
    private int[] sqlTypes;

    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClass");
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException cfne) {
            throw new HibernateException("Enum class not found", cfne);
        }

        String identifierMethodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);

        try {
            identifierMethod = enumClass.getMethod(identifierMethodName, new Class[0]);
            identifierType = identifierMethod.getReturnType();
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain identifier method", e);
        }

        type =
                (AbstractSingleColumnStandardBasicType<? extends Object>)
                        new TypeResolver().heuristicType(identifierType.getName(), parameters);

        if (type == null)
            throw new HibernateException("Unsupported identifier type " + identifierType.getName());

        sqlTypes = new int[]{((AbstractSingleColumnStandardBasicType<?>) type).sqlType()};

        String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);

        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName, new Class[]{identifierType});
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain valueOf method", e);
        }
    }

    public Class returnedClass() {
        return enumClass;
    }

    public int[] sqlTypes() {
        return sqlTypes;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object identifier = type.get(rs, names[0], session);
        if (identifier == null) {
            return null;
        }

        try {
            return valueOfMethod.invoke(enumClass, identifier);
        } catch (Exception e) {
            throw new HibernateException("Exception while invoking valueOf method '" + valueOfMethod.getName() + "' of " +
                    "enumeration class '" + enumClass + "'", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        try {
            if (value == null) {
                st.setNull(index, ((AbstractSingleColumnStandardBasicType<?>) type).sqlType());
            } else {
                Object identifier = identifierMethod.invoke(value);
                type.nullSafeSet(st, identifier, index, session);
            }
        } catch (Exception e) {
            throw new HibernateException("Exception while invoking identifierMethod '" + identifierMethod.getName() + "' of " +
                    "enumeration class '" + enumClass + "'", e);
        }
    }

    public boolean isMutable() {
        return false;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}